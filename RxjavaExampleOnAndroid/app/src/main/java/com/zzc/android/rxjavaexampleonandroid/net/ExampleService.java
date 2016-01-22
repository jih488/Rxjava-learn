package com.zzc.android.rxjavaexampleonandroid.net;

import com.zzc.android.rxjavaexampleonandroid.net.entity.Contributor;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * some test service api
 *
 * Created by zczhang on 16/1/22.
 */
public interface ExampleService {
    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> contributors(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/contributors")
    List<Contributor> contributors2(@Path("owner") String owner, @Path("repo") String repo);

}
