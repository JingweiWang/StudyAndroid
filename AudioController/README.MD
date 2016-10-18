# 调整系统音量

[参见API](https://developer.android.com/reference/android/media/AudioManager.html)

分别用两种方法实现.

不过要注意的是, 在项目开发时用到此项功能, 最好不使用 `AudioManager.FLAG_SHOW_UI` 这个 *FLAG* , 因为每次调整音量时, 都会重新呼出音量条, 导致体验效果下降.

最好的办法是, 重新自己写一个音量条.

## 手动

```java
void setStreamVolume(int streamType, int index, int flags)
```

## 自动

```java
void adjustStreamVolume(int streamType, int direction, int flags)
```