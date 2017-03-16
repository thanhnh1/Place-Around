package com.nguyenthanh.placearound.LoginApi.object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/15/2017.
 */

public class ObjectFullResponseData {

    @SerializedName("data")
    private ArrayList<ObjectResponseData> data;

    public ArrayList<ObjectResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<ObjectResponseData> data) {
        this.data = data;
    }
}
