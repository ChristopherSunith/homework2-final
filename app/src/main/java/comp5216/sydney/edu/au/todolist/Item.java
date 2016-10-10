package comp5216.sydney.edu.au.todolist;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Item implements Serializable {
    private String item;
    private String url;
    private double x;
    private double y;

    public Item() {
        this.item = null;
        this.url = null;
        this.x = 0.0;
        this.y = 0.0;
    }

    public Item(String item,String url, double x, double y) {

        this.item = item;
        this.url = url;
        this.x = x;
        this.y = y;

    }


    public String changeItem(String a) {
        item = a;
        return item;
    }

    public String changeURL(String a) {
        url = a;
        return url;
    }

    public double changeX(double a) {
        this.x = a;
        return x;
    }

    public double changeY(double a) {
        this.y = a;
        return y;
    }


    public String getItem() {
        return item;
    }

    public String getUrl() {
        return url;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
