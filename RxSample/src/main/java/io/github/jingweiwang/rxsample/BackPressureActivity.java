package io.github.jingweiwang.rxsample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BackPressureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_pressure);
        findViewById(R.id.button_missing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.button_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flowable
                        .create(new FlowableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                                for (int i = 0; i < 129; i++) {
                                    emitter.onNext(i);
                                }
                            }
                        }, BackpressureStrategy.ERROR)
                        .subscribeOn(Schedulers.newThread(), true)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                Log.d("ERROR", integer + "");
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d("ERROR", "Throwable: " + throwable.getMessage());
                            }
                        });
            }
        });
        findViewById(R.id.button_buffer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flowable
                        .create(new FlowableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                                for (int i = 0; ; i++) {
                                    emitter.onNext(i);
                                }
                            }
                        }, BackpressureStrategy.BUFFER)
                        .subscribeOn(Schedulers.newThread(), true)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                Log.d("BUFFER", integer + "");
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d("BUFFER", "Throwable: " + throwable.getMessage());
                            }
                        });
            }
        });
        findViewById(R.id.button_drop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flowable
                        .create(new FlowableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                                for (int i = 0; i < 129; i++) {
                                    emitter.onNext(i);
                                }
                            }
                        }, BackpressureStrategy.DROP)
                        .subscribeOn(Schedulers.newThread(), true)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                Log.d("DROP", integer + "");
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d("DROP", "Throwable: " + throwable.getMessage());
                            }
                        });
            }
        });
        findViewById(R.id.button_latest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flowable
                        .create(new FlowableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                                for (int i = 0; i < 1000; i++) {
                                    emitter.onNext(i);
                                }
                            }
                        }, BackpressureStrategy.LATEST)
                        .subscribeOn(Schedulers.newThread(), true)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                Log.d("LATEST", integer + "");
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d("LATEST", "Throwable: " + throwable.getMessage());
                            }
                        });
            }
        });
    }
}
