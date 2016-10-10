package comp5216.sydney.edu.au.todolist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URL;
import java.util.ArrayList;

import java.util.ArrayList;


public class ItemAdapter extends ArrayAdapter<Item> {
    //private int resource;
    public ItemAdapter(Context context, ArrayList<Item> items) {
        super(context, 0,items);
        // 记录下来稍后使用
      //  resource = resourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // 获取数据
        Item item = getItem(position);
      //  String fileName = file.getName();
       // Bitmap bitmap = getBitmapFromFile(file);

        // 系统显示列表时，首先实例化一个适配器（这里将实例化自定义的适配器）。
        // 当手动完成适配时，必须手动映射数据，这需要重写getView（）方法。
        // 系统在绘制列表的每一行的时候将调用此方法。
        // getView()有三个参数，
        // position表示将显示的是第几行，
        // covertView是从布局文件中inflate来的布局。
        // 我们用LayoutInflater的方法将定义好的image_item.xml文件提取成View实例用来显示。
        // 然后将xml文件中的各个组件实例化（简单的findViewById()方法）。
        // 这样便可以将数据对应到各个组件上了。
        //

            // 看一下android文档中关于LayoutInflater的定义吧
            // This class is used to instantiate layout XML file into its corresponding View objects.
            // It is never be used directly -- use getLayoutInflater() or getSystemService(String)
            // to retrieve a standard LayoutInflater instance that is already hooked up to the current
            // context and correctly configured for the device you are running on. . For example:
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
            }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        ImageView imageView =(ImageView) convertView.findViewById(R.id.imageView);
        // Populate the data into the template view using the data object
        tvName.setText(item.getItem() + "\n"+item.getX() +" // " +item.getY());
        // Uri takenPhotoUri = getFileUri(photoFileName);
        Uri url = Uri.parse(item.getUrl());

        Glide.with(getContext()).load(url).override(100,100).fitCenter().into(imageView);
            return convertView;

        // 填充自定义数据


    }


}
