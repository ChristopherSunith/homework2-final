package comp5216.sydney.edu.au.todolist;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity {
    //define variables
    GridView gridView;
    ListView listView;
    ArrayList<Item> items;
    ItemAdapter itemsAdapter;
    // EditText addItemEditText;
    Button addEditItem;
    Switch swh_status;
    LocationManager lm;
    double x, y;
    private TextView tv_show;
    LocationListener locationListener1;

    CallbackManager callbackManager;
    private LoginManager loginManager;

    //public final int EDIT_ITEM_REQUEST_CODE = 647;
    public final int ADD_ITEM_REQUEST_CODE = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialising Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //use "activity_main.xml" as the layout
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();
        this.loginManager = LoginManager.getInstance();

        //设置登录权限 此处如果不添加 "user_friends" 你是不能访问好友信息的
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(new String[]{"public_profile", "email", "user_friends"}));

        //登录按钮
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e("test", "success");
                //取得自己的信息
                Log.e("personProfile", "User ID: "
                        + loginResult.getAccessToken().getUserId()
                        + "\n" +
                        "Auth Token: "
                        + loginResult.getAccessToken().getToken());
                //取得你的好友信息，回调中就是你的好友信息
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
            /* handle the result */
                                //TextView info = (TextView)findViewById(R.id.info);

                                //info.setText("Members = " + response.toString());

                                Log.e("callback", "Members: " + response.toString());
                            }
                        }
                ).executeAsync();
            }


            @Override
            public void onCancel() {
                // App code
                Log.e("test", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("test", "error");
            }
        });


        tv_show = (TextView) findViewById(R.id.textViewXY);


        //这边往下是拍照时候获取地理位置信息
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }

        //加try catch解决秒退情况
        try {
            Location lc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            x = lc.getLongitude();
            y = lc.getLatitude();
            tv_show.setText("Lng: " + x + "\nLat: " + y);
            //location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            System.out.print("Error creating location service: " + ex.getMessage());

        }


        locationListener1 = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                x = location.getLongitude();
                y = location.getLatitude();
                tv_show.setText("Lng: " + x + "\nLat: " + y);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
// Register the listener with the Location Manager to receive location updates

        // if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, locationListener1);
        // } else{
        //    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, locationListener1);
        // }

        //设置间隔2秒获得一次GPS定位信息
        //动态获取位置信息结束


        swh_status = (Switch) findViewById(R.id.switch2);
        swh_status.setOnCheckedChangeListener(this);


        //reference the "listview" variable to the id-"listview" in the layout
        gridView = (GridView) findViewById(R.id.gridView);
        listView = (ListView) findViewById(R.id.listView1);
        // addItemEditText = (EditText) findViewById(R.id.txtNewItem);
        addEditItem = (Button) findViewById(R.id.btnAddItem);
        readItemsFromDatabase();


        //Create an adapter for the list view using Android's built-in item layout
        itemsAdapter = new ItemAdapter(this, items);


        //connect the listview and the adapter
        gridView.setAdapter(itemsAdapter);
        listView.setAdapter(itemsAdapter);
        listView.setVisibility(View.INVISIBLE);


        setupListViewListener();

        addEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, EditToDoItemActivity1.class);


                Item addItem = (Item) it.getSerializableExtra("item");
                if (it != null) {
//            put "extras" into the bundle for access in the edit activity
                    it.putExtra("item", addItem);

//            brings up the second activity
                    startActivityForResult(it, ADD_ITEM_REQUEST_CODE);
                }
            }


        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ITEM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Item editedItem = (Item) data.getSerializableExtra("item");
                items.add(editedItem);
                //itemsSorted.add(editedItem);
                // sort();
                Toast.makeText(this, "added:" + editedItem.getItem(), Toast.LENGTH_SHORT).show();
                itemsAdapter.notifyDataSetChanged();
                //  itemsAdapterSorted.notifyDataSetChanged();
                saveItemsToDatabase();
            }
        }
    }


    private void setupListViewListener() {
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long rowId) {
                Log.i("MainActivity", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //delete the item
                                items.remove(position);
                                //itemsSorted.remove(position);
                                // sort();
                                itemsAdapter.notifyDataSetChanged();
                                // itemsAdapterSorted.notifyDataSetChanged();
                                saveItemsToDatabase();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //User cancelled the dialog
                                //nothing happens
                            }
                        });
                builder.create().show();
                return true;
            }
        });
    }

    private void readItemsFromDatabase() {
        //read items from database
        //这边有可能也要改，解决空对象问题

        List<ToDoItem> itemsFromORM = ToDoItem.listAll(ToDoItem.class);
        // Item b = new Item();
        items = new ArrayList<Item>();
        if (itemsFromORM != null & itemsFromORM.size() > 0) {
            for (ToDoItem item1 : itemsFromORM) {
                Item temp = new Item();
                temp.changeItem(item1.getItem());
                temp.changeURL(item1.getUrl());
                temp.changeX(item1.getX());
                temp.changeY(item1.getY());
                items.add(temp);


            }
        }
    }

    private void saveItemsToDatabase() {

        ToDoItem.deleteAll(ToDoItem.class);
        for (Item temp : items) {
            ToDoItem item = new ToDoItem(temp.getItem(), temp.getUrl(), temp.getX(), temp.getY());
            item.save();

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean b) {
        if (buttonView.isChecked()) {//这个是gridview里面要写的东西
            gridView.setVisibility(View.INVISIBLE);

            lm.removeUpdates(locationListener1);

            //根据经纬度排序，顺序是从跟自己最近到最远
            Collections.sort(items, new Comparator<Item>() {
                @Override
                public int compare(Item a, Item b) {
                    int d = 0;
                    double result = (Math.abs(a.getX() - x) + Math.abs(a.getY() - y)) - (Math.abs(b.getX() - x) + Math.abs(b.getY() - y));
                    if (result > 0.0) {
                        d = 1;
                    } else if (result == 0.0) {
                        d = 0;
                    } else if (result < 0.0) {
                        d = -1;// Ascending
                    }
                    return d;
                }

            });


            //sort();
            itemsAdapter.notifyDataSetChanged();

            listView.setVisibility(View.VISIBLE);
            //Toast.makeText(this, "开关:ON", Toast.LENGTH_SHORT).show();
        } else {//这个是listview里面要写的东西
            gridView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);

            //  if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, locationListener1);
            // } else{
            //      lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, locationListener1);
            // }
            // Toast.makeText(this,"开关:OFF",Toast.LENGTH_SHORT).show();
        }
    }

}
