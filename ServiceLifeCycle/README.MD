# *Service* 生命周期及相关研究

*Service* 是一个可以在后台执行长时间运行操作而不使用用户界面的应用组件. 服务可由其他应用组件启动, 而且即使用户切换到其他应用, 服务仍将在后台继续运行. 此外, 组件可以绑定到服务, 以与之进行交互, 甚至是执行进程间通信 (IPC). 例如, 服务可以处理网络事务、播放音乐, 执行文件 I/O 或与内容提供程序交互, 而所有这一切均可在后台进行.

## 重要概念

1. *`后台`* 不等于 *`子线程`* ;
2. 绝大部分 *Service* 在主线程中执行;
3. 只有 *IntentService* 中的 `onHandleIntent()` 在 *IntentService[ServiceForIntent]* 线程中执行;

## 服务基本上分为两种形式

1. 启动

   当应用组件（如 Activity）通过调用 startService() 启动服务时，服务即处于“启动”状态。一旦启动，服务即可在后台无限期运行，即使启动服务的组件已被销毁也不受影响。 已启动的服务通常是执行单一操作，而且不会将结果返回给调用方。例如，它可能通过网络下载或上传文件。 操作完成后，服务会自行停止运行。

2. 绑定

   当应用组件通过调用 bindService() 绑定到服务时，服务即处于“绑定”状态。绑定服务提供了一个客户端-服务器接口，允许组件与服务进行交互、发送请求、获取结果，甚至是利用进程间通信 (IPC) 跨进程执行这些操作。 仅当与另一个应用组件绑定时，绑定服务才会运行。 多个组件可以同时绑定到该服务，但全部取消绑定后，该服务即会被销毁。

## *Service* 的种类

1. *Service*

   这是适用于所有服务的基类。扩展此类时，必须创建一个用于执行所有服务工作的新线程，因为默认情况下，服务将使用应用的主线程，这会降低应用正在运行的所有 Activity 的性能。

2. *IntentService*

   这是 Service 的子类，它使用工作线程逐一处理所有启动请求。如果您不要求服务同时处理多个请求，这是最好的选择。 您只需实现 onHandleIntent() 方法即可，该方法会接收每个启动请求的 Intent，使您能够执行后台工作。

## 生命周期

1. 启动方式启动 *Service*

   `onCreate` -> `onStartCommand` -> `onDestroy`

   ps. 执行 `startService()` 后如果没有执行 `stopService()` 即再次执行 `startService()` , 则在生命周期中只执行 `onStartCommand` , 不再执行 `onCreate` .

2. 绑定方式启动 *Service*

   `onCreate` -> `onBind` -> `onServiceConnected` -> `onUnbind` -> `onDestroy`

3. 启动方式启动 *IntentService*

   `Constructor` -> `onCreate` -> `onStartCommand` -> `onHandleIntent` -> `onDestroy`

## 控制台日志

1. 启动方式启动 *Service*

    ```
    E/ServiceForStart: onCreate in main
    E/ServiceForStart: onStartCommand in main
    E/ServiceForStart: onStartCommand in main
    E/ServiceForStart: onStartCommand in main
    E/ServiceForStart: onDestroy in main
    ```

2. 绑定方式启动 *Service*

    ```
    E/ServiceForBind: onCreate in main
    E/ServiceForBind: onBind in main
    E/MainActivity: onServiceConnected in main
    E/ServiceForBind: doSomething() in main
    E/ServiceForBind: onUnbind in main
    E/ServiceForBind: onDestroy in main
    ```

3. 启动方式启动 *IntentService*

    ```
    E/ServiceForIntent: ServiceForIntent in main
    E/ServiceForIntent: onCreate in main
    E/ServiceForIntent: onStartCommand in main
    E/ServiceForIntent: onHandleIntent in IntentService[ServiceForIntent]
    E/ServiceForIntent: onDestroy in main
    ```

## 参考文献

[https://developer.android.com/guide/components/services.html](https://developer.android.com/guide/components/services.html)