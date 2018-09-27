# *RxAndroid2* 与 *RxJava2* 的使用

## 示例

### 示例1. *官方示例* (Run Schedulers Example)

#### 控制台输出

```
io.github.jingweiwang.rxjavasample E/OfficialExampleActivity: sampleObservable() by main Thread.
io.github.jingweiwang.rxjavasample E/OfficialExampleActivity: call() by RxCachedThreadScheduler-1 Thread.
io.github.jingweiwang.rxjavasample E/OfficialExampleActivity: onNext(one) by main Thread.
io.github.jingweiwang.rxjavasample E/OfficialExampleActivity: onNext(two) by main Thread.
io.github.jingweiwang.rxjavasample E/OfficialExampleActivity: onNext(three) by main Thread.
io.github.jingweiwang.rxjavasample E/OfficialExampleActivity: onNext(four) by main Thread.
io.github.jingweiwang.rxjavasample E/OfficialExampleActivity: onNext(five) by main Thread.
io.github.jingweiwang.rxjavasample E/OfficialExampleActivity: onComplete() by main Thread.
```

### 示例2. *背压*

#### MISSING
#### ERROR
#### BUFFER
#### DROP
#### LATEST

## 依赖版本

```groovy
implementation 'io.reactivex.rxjava2:rxandroid:2.1.0'
implementation 'io.reactivex.rxjava2:rxjava:2.2.2'
```

## 关键词

1. Subscribe 订阅 v.
2. Observable 被观察者 n.
3. Observer 观察者 n.
4. dispose 处理, 处置 v.
