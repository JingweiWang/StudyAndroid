# StudyAndroid

这是我在学习、工作中，比较精华的东西或者工作笔记。

每一个文件夹即为一个工程项目。

## 运行环境

* Android Studio 3.2
* gradle 4.10.2
* compileSdkVersion 28
* androidx_version 1.0.0

## 目录

### Components

* ActivityLaunchMode: 对于 *Activity* 启动模式的研究
* ActivityLifeCycle: 对于 *Activity* 生命周期的研究
* Broadcast: 简单实现一个 *Broadcast* 及 *BroadcastReceiver*
* ContentProvider: 简单实现向 *ContentProvider* 进行增删改查
* ServiceLifeCycle: 对于 *Service* 生命周期及相关研究

### ThirdPartyLibrary

* AndroidTimesSquare: *AndroidTimesSquare* 库的使用
    * *sample1*: 简单示例, 以dialog方式打开
    * *sample2*: 官方示例
* LottieSample: *Lottie* 的使用
* Protobuf: *Protobuf 3* 的使用示例
    * *ProtobufDemo*: 使用java版本的Protobuf, 对Any等类型的支持较好
    * *ProtobufLiteDemo*: 使用java lite版本的Protobuf, 虽然对Android支持较好, 但是不支持Any等类型
* RxJavaSample: *RxAndroid2* 与 *RxJava2* 的使用, 包括了 *官方实例* 和 *背压*

### Others

* AudioController: 音量, 包括 *全局音量* 与 *Mediaplayer局部音量*
* BluetoothSocket: 蓝牙 *Socket* 的使用
* CircularPictureUpload: 使用原生方法 *剪裁圆形头像* 并上传到服务器( *PHP* )
* FittsTest: 基于移动设备可触摸屏幕 *费茨定律* 的研究
* GPSLocation: 基于 *Android* 系统的原生 *GPS* 使用
* GradleForAndroid: 讲座 Gradle for Android and Groovy
* HeadsetStatus: 耳机孔状态监听
* HelloJni: *Hello, Jni & NDK*

* SHA1WithRSA: 基于 *SHA1WithRSA* 的签名与验签
* TcpPool: *TCP* 连接池的实现
