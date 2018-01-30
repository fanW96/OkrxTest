# OkrxTest

## OkGo
- 新版本OkGo的使用泛型作为返回参数试用
- 注意1：记得创建onSuccessConvert的JsonCallBack
- 注意2：给定的泛型的取回值在respose.body()中

## Glide
- 简单的Glide使用
- 添加Application模块注释GlideModule生成功能更多的Api即GlideAPP使用

## RecyclerView
- 使用其代替ListView显示
- 下拉刷新：原生的SwipeRefreshLayout，初始加载使用.post()，下拉实现使用.setOnRefreshListener()
- 上拉加载：使用RecyclerView的Scroll监听，在到达特定位置的时候使用Footer显示加载状态，再在postExcurte中去掉Footer行实现新行的添加
- 侧滑删除：使用第三方的SwipeLayout实现侧滑显示删除选项，添加点击事件更改adapter实现删除
- 点击事件：在viewHolder添加点击监听，在adapter方法中添加获得点击事件，在activity中具体实现

## AsyncTask
- 遇到问题：方法执行的过程中会有单独的线程，不会等待OkGo执行成功
- 解决办法：在doInBackground的return之前使用Thread.sleep(1000)沉睡一秒给OkGo时间完成请求

## 参考文档
- [RecyclerView代替 ListView](http://blog.csdn.net/never_cxb/article/details/50495505)
- [OkGo](https://github.com/jeasonlzy/okhttp-OkGo/wiki)
- [RecyclerView的下拉刷新和上拉加载更多](http://blog.csdn.net/never_cxb/article/details/50759109)
- [Glide](https://github.com/bumptech/glide/wiki)
- [SwipeLayout](https://github.com/daimajia/AndroidSwipeLayout/wiki/usage)
