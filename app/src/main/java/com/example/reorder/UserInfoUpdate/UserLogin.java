package com.example.reorder.UserInfoUpdate;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.reorder.LoginActivity;
import com.example.reorder.LoginResult;
import com.example.reorder.NavigationActivity;
import com.example.reorder.api.LoginApi;
import com.example.reorder.api.RetrofitApi;
import com.example.reorder.globalVariables.CurrentUserInfo;
import com.example.reorder.globalVariables.IsLogin;
import com.example.reorder.globalVariables.serverURL;
import com.example.reorder.info.UserInfo;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserLogin {
    String url= serverURL.getUrl();

    public void Login(final String client_email,final String client_password, final Intent intent, final Activity activity){

        final String strEmail=client_email;
        final String strPassword=client_password;

        try {
            HashMap<String, String> input = new HashMap<>();
            input.put("client_email", strEmail);
            input.put("client_password", strPassword);
            Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
            RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
            retrofitApi.postLoginUserInfo(input).enqueue(new Callback<LoginResult>() {
                @Override
                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                    if (response.isSuccessful()) {
                        LoginResult map = response.body();
                        Log.d("11111", "***?$?!#@");
                        if (map != null) {
                            switch (map.getResult()) {
                                case -1:
                                    Log.d("11111", "잘못된 비밀번호입니다");
                                    break;
                                case 0:
                                    Log.d("11111", "가입되지 않은 이메일입니다");
                                    break;
                                case 1:
                                    Log.d("11111", "login 성공");
                                    UserInfo userinfo = map.getUserInfo();
                                    userinfo.setChange(true);//???
                                    CurrentUserInfo.getUser().setUserInfo(userinfo);
                                    UserInfo u_info = CurrentUserInfo.getUser().getUserInfo();
                                    IsLogin.setIsLogin(true);
                                    Log.d("11111", String.valueOf(u_info.getClient_id()));//int값이라 이렇게 로그 받아와도 되나
                                    activity.startActivity(intent);
                                    activity.finish();
                                    break;
                            }
                        }
                    }
                }


                @Override
                public void onFailure(Call<LoginResult> call, Throwable t) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
