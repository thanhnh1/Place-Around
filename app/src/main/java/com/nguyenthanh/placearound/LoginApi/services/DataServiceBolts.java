package com.nguyenthanh.placearound.LoginApi.services;

import com.google.gson.Gson;
import com.nguyenthanh.placearound.LoginApi.object.ObjectAccessToken;
import com.nguyenthanh.placearound.LoginApi.object.ObjectFullResponseData;

import org.androidannotations.annotations.EBean;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

/**
 * Created by Administrator on 3/22/2017.
 */

@EBean(scope = Singleton)
public class DataServiceBolts {

    private Retrofit retrofit;

    private IRestService iRestService;

    private ObjectAccessToken account = new ObjectAccessToken();

    private ObjectFullResponseData listData = new ObjectFullResponseData();

    final TaskCompletionSource<ObjectAccessToken> taskLogin = new TaskCompletionSource<>();
    final TaskCompletionSource<ObjectFullResponseData> taskLoadData = new TaskCompletionSource<>();

    public DataServiceBolts() {

        retrofit = new Retrofit.Builder()
                .baseUrl("http://ibss.io:5599/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        iRestService = retrofit.create(IRestService.class);
    }

    public Task<ObjectAccessToken> login(final String grant_type, final String username, final String password) {

        iRestService.login(grant_type, username, password).enqueue(new Callback<ObjectAccessToken>() {
            @Override
            public void onResponse(Call<ObjectAccessToken> call, Response<ObjectAccessToken> response) {
                taskLogin.setResult(response.body());
            }

            @Override
            public void onFailure(Call<ObjectAccessToken> call, Throwable t) {

            }
        });
        return taskLogin.getTask();
    }

    public Task<ObjectFullResponseData> loadData(final String grant_type, final String username, final String password) {
        login(grant_type, username, password).continueWithTask(new Continuation<ObjectAccessToken, Task<ObjectAccessToken>>() {
            @Override
            public Task<ObjectAccessToken> then(Task<ObjectAccessToken> task) throws Exception {
                iRestService.loadData("Bearer " + task.getResult().getAccessToken()).enqueue(new Callback<ObjectFullResponseData>() {
                    @Override
                    public void onResponse(Call<ObjectFullResponseData> call, Response<ObjectFullResponseData> response) {
                        taskLoadData.setResult(response.body());

                    }

                    @Override
                    public void onFailure(Call<ObjectFullResponseData> call, Throwable t) {

                    }
                });
                return null;
            }
        });
        return taskLoadData.getTask();
    }
}


