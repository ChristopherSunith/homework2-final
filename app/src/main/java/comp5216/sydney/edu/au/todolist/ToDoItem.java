package comp5216.sydney.edu.au.todolist;

//import com.google.common.base.Strings;
import com.orm.SugarRecord;

import java.io.Serializable;

public class ToDoItem extends SugarRecord implements Serializable{
    private String item;
    private String url;
    private double x;
    private double y;

    public ToDoItem() {

    }

    public ToDoItem( String item, String time) {

        this.item = item;
        this.url = url;
        this.x = x;
        this.y = y;
    }

    public String getItem(){
        return item;
    }

    public String getUrl(){
        return  url;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
