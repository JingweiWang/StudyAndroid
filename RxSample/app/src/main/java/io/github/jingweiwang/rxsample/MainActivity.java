package io.github.jingweiwang.rxsample;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "RxAndroidSamples";

    //Creates an empty CompositeDisposable.
    private final CompositeDisposable disposables = new CompositeDisposable();

    private Button button_run_scheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_run_scheduler = (Button) findViewById(R.id.button_run_scheduler);
        button_run_scheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRunSchedulerExampleButtonClicked();
            }
        });
    }

    @Override
    protected void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }

    private void onRunSchedulerExampleButtonClicked() {
        disposables.add(sampleObservable()
                .subscribeOn(Schedulers.newThread())//执行 call() 的线程
                .observeOn(AndroidSchedulers.mainThread())//执行 onNext()、onError()、onComplete() 的线程
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String value) {
                        Log.d(TAG, "onNext(" + value + ") by " + Thread.currentThread().getName() + " Thread.");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError()", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete() by " + Thread.currentThread().getName() + " Thread.");
                    }
                })
        );
    }

    private static Observable<String> sampleObservable() {
        Log.e(TAG, "sampleObservable() by " + Thread.currentThread().getName() + " Thread.");

        //被观察者
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                SystemClock.sleep(2000);//忽略中断异常的sleep
                Log.e(TAG, "call() by " + Thread.currentThread().getName() + " Thread.");
                return Observable.just("one", "two", "three", "four", "five");
            }
        });
    }
}
