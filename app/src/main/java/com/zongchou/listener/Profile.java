package com.zongchou.listener;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 28-Jan-18.
 */

public class Profile{
    public String name;
    public String age;

    public Profile(String name, String age) {
        this.name = name;
        this.age = age;
    }
}
