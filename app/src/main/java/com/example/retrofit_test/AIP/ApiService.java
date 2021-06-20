package com.example.retrofit_test.AIP;

import android.app.DownloadManager;

import com.example.retrofit_test.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    // url : http://accountservermanagement.herokuapp.com/api/accounts
    public static final String DOMAIN = "http://accountservermanagement.herokuapp.com/api/";
    Gson gson = new GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @Multipart
    @POST("accounts")
    Call<User> registerAccount(@Part(Const.KEY_USERNAME)RequestBody username,
                               @Part (Const.KEY_PASSWORD) RequestBody password,
                               @Part MultipartBody.Part avt);

}
