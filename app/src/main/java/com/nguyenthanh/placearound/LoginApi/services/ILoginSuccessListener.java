package com.nguyenthanh.placearound.LoginApi.services;

import com.nguyenthanh.placearound.LoginApi.object.ObjectAccessToken;

/**
 * Created by GameNet on 3/13/2017.
 */

public interface ILoginSuccessListener {

    void onSuccess(ObjectAccessToken account);
}
