package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class MyData {

    private Drawable image;
    private String name;
    private String tel;
    private String email;
    private Bitmap bitmap;

    public MyData(Drawable image, String name, String tel, String email, Bitmap bitmap){
        this.image = image;
        this.name = name;
        this.tel = tel;
        this.email = email;
        this.bitmap = bitmap;
    }

    public void modifydata(int i,String s){
        if (i==0){
            name =s;
        }
        if(i==1){
            tel =s;
        }
        if(i==2){
            email = s;
        }
    }

    public Drawable getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getEmail() {
        return email;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setImage(Drawable im) {
        image = im;
    }

    public void setName(String n) {
        name = n;
    }

    public void setTel(String t) {
       tel = t;
    }

    public void setEmail(String e)
    {
        email = e;
    }

    public void setBitmap(Bitmap b)
    {
        bitmap = b;
    }

    public void modify(Drawable i, String n, String t, String e, Bitmap b){
        image =i;
        name =n;
        tel = t;
        email = e;
        bitmap = b;
    }

}