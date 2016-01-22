package com.zzc.android.rxjavaexampleonandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.StringBuilderPrinter;
import android.widget.TextView;

import org.w3c.dom.Text;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zczhang on 16/1/22.
 */
public class BaseActivity extends AppCompatActivity{
    private StringBuilder sbLog = new StringBuilder();
    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
    }

    protected void log2TextView(TextView textView, String log) {
        sbLog.append(log).append("\n");
        textView.setText(sbLog.toString());
    }

    protected void addSubscriptopn(Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }
}
