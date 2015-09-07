package com.example.rimaraksa.approve.Model;

/**
 * Created by rimaraksa on 9/7/15.
 */
public class NavDrawerItem {

    private String title;
    private int icon;
    private String count = "0";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;
    private boolean isSubheader = false;
    private boolean isHeader = false;

    public NavDrawerItem(){}

    public NavDrawerItem(int icon){
        this.icon = icon;
        this.isHeader = true;
    }

    public NavDrawerItem(String title){
        this.title = title;
        this.isSubheader = true;
    }


    public NavDrawerItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }



    public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public String getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    public boolean isSubheaderType(){
        return this.isSubheader;
    }

    public boolean isHeaderType(){
        return this.isHeader;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public void setCount(String count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }

}
