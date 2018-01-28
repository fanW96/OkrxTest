# OkrxTest

## OkGo
- 新版本OkGo的使用泛型作为返回参数试用
- 注意1：记得创建onSuccessConvert的JsonCallBack
- 注意2：给定的泛型的取回值在respose.body()中

## RecyclerView
- 使用其代替ListView显示
- 未完成：下拉刷新，上拉加载

## AsyncTask
- 遇到问题：方法执行的过程中会有单独的线程，不会等待OkGo执行成功
- 解决办法：在doInBackground的return之前使用Thread.sleep(1000)沉睡一秒给OkGo时间完成请求

## 参考文档
- [RecyclerView代替 ListView](http://blog.csdn.net/never_cxb/article/details/50495505)
- [OkGo](https://github.com/jeasonlzy/okhttp-OkGo/wiki)
