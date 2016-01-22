package com.zzc.android.rxjavaexampleonandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.view.ViewClickEvent;
import com.zzc.android.rxjavaexampleonandroid.net.ExampleService;
import com.zzc.android.rxjavaexampleonandroid.net.entity.Contributor;
import com.zzc.android.rxjavaexampleonandroid.net.retrofit.RetrofitCreator;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zczhang on 16/1/22.
 */
public class TestMultiThreadActivity extends BaseActivity{
    TextView tvLog;
    Button btnStartThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_thread);
        tvLog = (TextView) findViewById(R.id.tv_log);
        btnStartThread = (Button) findViewById(R.id.btn_start_new_thread);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RxView.clickEvents(btnStartThread).subscribe(new Subscriber<ViewClickEvent>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ViewClickEvent viewClickEvent) {
                log2TextView(tvLog, "(按钮点击)"+ Thread.currentThread().getName());
                Subscription subscription =  RetrofitCreator.getInstance().create(ExampleService.class).contributors("square", "retrofit")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<List<Contributor>>() {
                            @Override
                            public void onCompleted() {
                                log2TextView(tvLog, "(任务完成)"+ Thread.currentThread().getName());
                            }

                            @Override
                            public void onError(Throwable e) {
                                log2TextView(tvLog, "(任务出错)"+ Thread.currentThread().getName());
                            }

                            @Override
                            public void onNext(List<Contributor> contributors) {
                                log2TextView(tvLog, "(任务结果返回)"+contributors.size()+ Thread.currentThread().getName());
                            }
                        });
                addSubscriptopn(subscription);
//                Observable<List<Contributor>> observable = Observable.create(new Observable.OnSubscribe<List<Contributor>>() {
//                    @Override
//                    public void call(Subscriber<? super List<Contributor>> subscriber) {
//                        log2TextView(tvLog, "(任务开始)"+ Thread.currentThread().getName());
//                        subscriber.onNext(RetrofitCreator.getInstance().create(ExampleService.class).contributors2("square", "retrofit"));
//                        subscriber.onCompleted();
//                    }
//                });
//
//
//            Subscription subscription = observable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                    .subscribe(new Subscriber<List<Contributor>>() {
//                        @Override
//                        public void onCompleted() {
//                            log2TextView(tvLog, "(任务完成)"+ Thread.currentThread().getName());
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            log2TextView(tvLog, "(任务出错)"+ Thread.currentThread().getName());
//                        }
//
//                        @Override
//                        public void onNext(List<Contributor> contributors) {
//                            log2TextView(tvLog, "(任务结果返回)"+contributors.size()+ Thread.currentThread().getName());
//                        }
//                    });
//                addSubscriptopn(subscription);
            }
        });
    }
}
