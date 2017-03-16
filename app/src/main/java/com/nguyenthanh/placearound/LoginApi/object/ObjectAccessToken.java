package com.nguyenthanh.placearound.LoginApi.object;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 3/15/2017.
 */

public class ObjectAccessToken {
    @SerializedName("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
