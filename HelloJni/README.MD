# *Hello, Jni & NDK*

在 *Android Studio 2.2* 开发 *NDK* 已经是一件非常简单的事情, 因为 *Google* 已经对其进行了支持.

新的构建工具使用了 *[cmake](https://cmake.org/)* . 极大的改观了可用性和易用性.

本示例着重展示 *NDK* 的工程结构.

## 示例内容

1. TextView 输出 `Hello JingweiWang.`
2. 控制台 输出 `E/multiplication: 6.8 * 8.9 = 60.52`

## 重点文件

1. [./app/build.gradle](./app/build.gradle)
2. [UseNative.java](./app/src/main/java/io/github/jingweiwang/hellojni/UseNative.java)
3. [CMakeLists.txt](./app/src/main/cpp/CMakeLists.txt)
4. [native-lib.cpp](./app/src/main/cpp/native-lib.cpp)