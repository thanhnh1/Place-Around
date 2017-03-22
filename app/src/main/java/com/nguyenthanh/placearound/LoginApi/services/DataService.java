package com.nguyenthanh.placearound.LoginApi.services;

import com.google.gson.Gson;
import com.nguyenthanh.placearound.LoginApi.object.ObjectAccessToken;
import com.nguyenthanh.placearound.LoginApi.object.ObjectFullResponseData;

import org.androidannotations.annotations.EBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

/**
 * Created by Administrator on 3/15/2017.
 */


@EBean(scope = Singleton)
public class DataService {

    private Retrofit retrofit;

    private IRestService iRestService;

    private ObjectAccessToken account = new ObjectAccessToken();

    private ObjectFullResponseData listData = new ObjectFullResponseData();

    public DataService() {

        retrofit = new Retrofit.Builder()
                .baseUrl("http://ibss.io:5599/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        iRestService = retrofit.create(IRestService.class);
    }

    public ObjectAccessToken getAccount() {
        return account;
    }

    public ObjectFullResponseData getListData(){
        return listData;
    }

    public void login(final String grant_type, final String username, final String password,
                      final ILoginSuccessListener loginSuccessListener){

        iRestService.login(grant_type, username, password).enqueue(new Callback<ObjectAccessToken>() {
            @Override
            public void onResponse(Call<ObjectAccessToken> call, Response<ObjectAccessToken> response) {
                account = response.body();
                if (loginSuccessListener != null)
                    loginSuccessListener.onSuccess(account);
            }

            @Override
            public void onFailure(Call<ObjectAccessToken> call, Throwable t) {

            }
        });
    }

    public void loadData(final String header, final ILoadDataSuccessListener loadDataSuccessListener) {
        iRestService.loadData("Bearer " + header).enqueue(new Callback<ObjectFullResponseData>() {
            @Override
            public void onResponse(Call<ObjectFullResponseData> call, Response<ObjectFullResponseData> response) {
                listData = response.body();
                if (loadDataSuccessListener != null)
                    loadDataSuccessListener.onSuccess(listData);
            }

            @Override
            public void onFailure(Call<ObjectFullResponseData> call, Throwable t) {

            }
        });
    }
}
