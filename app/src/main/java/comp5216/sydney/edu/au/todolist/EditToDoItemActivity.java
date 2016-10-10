package comp5216.sydney.edu.au.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class EditToDoItemActivity extends Activity {
    public int position = 0;
//    private Item editItem;
    EditText etItem;
    TextView txtshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //populate the screen using the layout
        setContentView(R.layout.activity_edit_item);
        txtshow = (TextView)findViewById(R.id.textViewTime);

        //Get the data from the main screen
        //String editItem = getIntent().getStringExtra("item");
        Item editItem = (Item)getIntent().getSerializableExtra("item");
        position = getIntent().getIntExtra("position", -1);
        txtshow.setText(editItem.getTime());
        // show original content in the text field
        etItem = (EditText) findViewById(R.id.etEditItem);
        etItem.setText(editItem.getItem());
    }

    public void onSubmit(View v) {//这个地方是在edit的那个地方点击save
        etItem = (EditText) findViewById(R.id.etEditItem);
        Item editItem1 = (Item)getIntent().getSerializableExtra("item");
        editItem1.changeItem(etItem.getText().toString());
        // Prepare data intent for sending it back
        Intent data = new Intent();

        // Pass relevant data back as a result
        data.putExtra("item", editItem1);
        data.putExtra("position", position);


        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }


    public void onCancel(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditToDoItemActivity.this);
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
