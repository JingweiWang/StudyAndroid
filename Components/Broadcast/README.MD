# 简单实现一个 *Broadcast* 及 *BroadcastReceiver*

本实例是通过动态注册方式实现的。

## BroadcastReceiver注册方式

### 静态注册

    Note: If your app targets API level 26 or higher, you cannot use the manifest to declare a receiver for implicit broadcasts (broadcasts that do not target your app specifically), except for a few implicit broadcasts that are exempted from that restriction. In most cases, you can use scheduled jobs instead.

### 动态注册

## Logcat Regex

```
E/MainActivity|E/MyReceiver
```