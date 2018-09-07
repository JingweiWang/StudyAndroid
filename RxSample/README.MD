# *RxAndroid 2* 与 *RxJava 2* 的使用

## 示例

### 示例1. *Run Schedulers Example* (官方示例)

#### 控制台输出

```
io.github.jingweiwang.rxsample E/RxAndroidSamples: sampleObservable() by main Thread.
io.github.jingweiwang.rxsample E/RxAndroidSamples: call() by RxNewThreadScheduler-1 Thread.
io.github.jingweiwang.rxsample D/RxAndroidSamples: onNext(one) by main Thread.
io.github.jingweiwang.rxsample D/RxAndroidSamples: onNext(two) by main Thread.
io.github.jingweiwang.rxsample D/RxAndroidSamples: onNext(three) by main Thread.
io.github.jingweiwang.rxsample D/RxAndroidSamples: onNext(four) by main Thread.
io.github.jingweiwang.rxsample D/RxAndroidSamples: onNext(five) by main Thread.
io.github.jingweiwang.rxsample D/RxAndroidSamples: onComplete() by main Thread.
```

## 依赖版本

```groovy
implementation 'io.reactivex.rxjava2:rxandroid:2.1.0'
implementation 'io.reactivex.rxjava2:rxjava:2.2.1'
```

## 关键词

1. Subscribe 订阅 v.
2. Observable 被观察者 n.
3. Observer 观察者 n.
4. dispose 处理, 处置 v.
