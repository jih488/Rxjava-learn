package com.zzc.android.rxjavaexampleonandroid.net.retrofit;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * retrofit clint create helper
 * <p/>
 * Created by zczhang on 16/1/22.
 */
public class RetrofitCreator {
    private static final String API_URL = "https://api.github.com";
    private static volatile Retrofit mRetrofit = null;

    public static Retrofit getInstance() {
        if (mRetrofit == null) {
            synchronized (RetrofitCreator.class) {
                mRetrofit = new Retrofit.Builder()
                        .baseUrl(API_URL)
                        .client(setupHttpClient())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }
        return mRetrofit;
    }

    //config OkHttpClient that Retrofit need
    private static OkHttpClient setupHttpClient() {
        //set time out interval
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);

        //set http request and response log
        okHttpClient.interceptors().add(new HttpLoggingInterceptor((new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                HttpLoggingInterceptor.Logger.DEFAULT.log(message);
//                Log.i("Api", message);
            }
        })));
        return okHttpClient;
    }

    public <T> T getApiService(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

}
