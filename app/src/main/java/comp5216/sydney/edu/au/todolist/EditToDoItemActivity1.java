package comp5216.sydney.edu.au.todolist;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import static android.R.attr.x;

public class EditToDoItemActivity1 extends Activity {
    EditText addItem;
    private Button btn_start;
    private File currentImageFile = null;
    int flag = 0;
    LocationManager lm1;
    LocationListener locationListener;
    double x1 = 0.0;
    double y1 = 0.0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //populate the screen using the layout
        setContentView(R.layout.activity_edit_item);
        bindViews();


        //这边往下是拍照时候获取地理位置信息
        lm1 = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        try {
            Location lc = lm1.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            x1 = lc.getLongitude();
            y1 = lc.getLatitude();
            //location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }catch (Exception ex)  {
            System.out.print( "Error creating location service: " + ex.getMessage() );

        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                x1 = location.getLongitude();
                y1 = location.getLatitude();

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

        lm1.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, locationListener);
    }

    private void bindViews() {
        // img_show = (ImageView) findViewById(R.id.img_show);
        btn_start = (Button) findViewById(R.id.btn_start);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File dir = new File(Environment.getExternalStorageDirectory(), "CameraSample");
                if (dir.exists()) {
                    dir.mkdirs();
                }
                currentImageFile = new File(dir, System.currentTimeMillis() + ".jpg");
                if (!currentImageFile.exists()) {
                    try {
                        currentImageFile.createNewFile();
                        flag = 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
                startActivityForResult(it, Activity.DEFAULT_KEYS_DIALER);

            }
        });
    }

    public void onSubmit(View v) {
        addItem = (EditText) findViewById(R.id.etEditItem);
        String toAddString = addItem.getText().toString();
        if (toAddString != null && toAddString.length() > 0 && flag == 1) {
            Item a = new Item(toAddString, Uri.fromFile(currentImageFile).toString(), x1, y1);
            Intent data = new Intent();
            data.putExtra("item", a);
            setResult(RESULT_OK, data);
            lm1.removeUpdates(locationListener);
            finish();
        }
    }


    public void onCancel(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditToDoItemActivity1.this);
        builder.setTitle(R.string.dialog_give_up_title)
                .setMessage(R.string.dialog_give_up_msg)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //delete the item
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //User cancelled the dialog
                        //nothing happens
                    }
                });
        builder.create().show();
    }
}
