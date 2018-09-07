# 蓝牙 *Socket* 的使用

最近在做蓝牙相关的项目, 但是网上所有的 *demo* 都是零零碎碎, 没有一个是从 *开启蓝牙* 到 *扫描蓝牙* 到 *蓝牙自动配对* 到 *建立Socket连接* 再到 *通信* 的例子, 于是边做项目, 边整理了这个示例. 

by JingweiWang

## 此示例包括

1. 蓝牙强制自动开启; (权限)
2. 扫描蓝牙设备; (反射 + 广播)
3. 打开发现模式; (反射)
4. 自动配对; (广播 + 反射)
5. 连接服务器;
6. 服务器端自动重启保护;
7. 客户端自动化连接 Socket;

## 使用方法

使用两个带有蓝牙功能的手机即可测试.

## Android 版本要求

引用 API 中 *KITKAT* 中的注释:

``` 
Android 4.4, KitKat, another tasty treat.
```

## 辅助的三方库

EventBus 3.1.1


## 注意 1

某些机型随着Android发布的安全补丁，会对反射调用 `setPairingConfirmation` 方法抛出SecurityException:

```
Caused by: java.lang.SecurityException: Need BLUETOOTH PRIVILEGED permission: Neither user 10143 nor current process has android.permission.BLUETOOTH_PRIVILEGED.
```

解决此问题, 需要申请 `BLUETOOTH_PRIVILEGED` 权限, 并且打包apk成为系统包(需要系统签名)。

## 注意 2

在 Android 9（API 级别 28) 出现以后, 官方禁止了android开发过程中使用反射方式调用隐藏API。
详情请见：[对非 SDK 接口的限制](https://developer.android.com/about/versions/pie/restrictions-non-sdk-interfaces)
