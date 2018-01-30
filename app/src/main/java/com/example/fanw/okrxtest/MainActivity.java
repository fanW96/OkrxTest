package com.example.fanw.okrxtest;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rcvArticle;
    private List<NewsInfo> newsInfoList = new ArrayList<NewsInfo>();
    private List<NewsInfo> moreNewsInfoList = new ArrayList<NewsInfo>();
    private Context context;
    private SwipeRefreshLayout news_refresh;
    private boolean loading = false;
    ItemNewsAdapter itemNewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        rcvArticle = findViewById(R.id.rcv_article);
        news_refresh = findViewById(R.id.news_refresh);
        rcvArticle.setLayoutManager(new LinearLayoutManager(context));
        itemNewsAdapter = new ItemNewsAdapter(newsInfoList,context);

        // 设置item及item中控件的点击事件
        itemNewsAdapter.setOnItemClickListener(onItemClickListener);

        rcvArticle.setAdapter(itemNewsAdapter);
        rcvArticle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                //获得全部以获得的Item数量
                int totalItemCount = linearLayoutManager.getItemCount();
                //后的当前可见的Item的position
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount < (lastVisibleItemPosition + 3)){
                    loading =true;
                    new LatestArticleTask().execute();
                }
            }
        });
        //设置刷新图案的颜色
        news_refresh.setColorSchemeColors(Color.RED,Color.BLUE);
        //设置下拉刷新的事件
        news_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsInfoList.clear();
                new LatestArticleTask().execute();
            }
        });
        //设置初始状态加载动画
        news_refresh.post(new Runnable() {
            @Override
            public void run() {
                news_refresh.setRefreshing(true);
                new LatestArticleTask().execute();
            }
        });


    }

    /**
     * Item点击监听
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position , View v) {
            switch (v.getId()){
                case R.id.news_title_tv:
                    Snackbar.make(v,"tran", Snackbar.LENGTH_LONG).show();
                    break;
                case R.id.cv_Delete:
                    Snackbar.make(v,"del",Snackbar.LENGTH_LONG).show();
                    newsInfoList.remove(position);
                    itemNewsAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);//结束OkGo
    }

    private class LatestArticleTask extends AsyncTask<String,Void,List<NewsInfo>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(newsInfoList != null&&newsInfoList.size()>0){
                //添加footer
//                NewsInfo newsInfoTemp = new NewsInfo();
                newsInfoList.add(null);
                // notifyItemInserted(int position)，这个方法是在第position位置
                // 被插入了一条数据的时候可以使用这个方法刷新，
                // 注意这个方法调用后会有插入的动画，这个动画可以使用默认的，也可以自己定义。
                itemNewsAdapter.notifyItemInserted(newsInfoList.size() -1);
            }
        }

        @Override
        protected List<NewsInfo> doInBackground(String... strings) {
            String str ="http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html";
            OkGo.<simple<List<NewsInfo>>>get("http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html")//添加泛型
                    .tag(this)
                    .execute(new JsonCallBack<simple<List<NewsInfo>>>() {//编写需要的返回方法，在里面做OnSuccessConvert
                        @Override
                        public void onSuccess(Response<simple<List<NewsInfo>>> response) {
                            moreNewsInfoList = response.body().getT1348647909107();
                        }
                    });
            try {
                Thread.sleep(1000);//沉睡一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return moreNewsInfoList;
        }

        @Override
        protected void onPostExecute(List<NewsInfo> infoList) {
            if(news_refresh != null){
                news_refresh.setRefreshing(false);
            }
            //新增新闻数据
            super.onPostExecute(infoList);
//            出错：data没有获得到数据
//            原因：doinbackground的return在okgo请求期间就返回了
//            解决方式：在return之前让线程休眠1秒
            if(newsInfoList.size() == 0){
                newsInfoList.addAll(infoList);
                itemNewsAdapter.notifyDataSetChanged();
            }else{
                //删除footer
                newsInfoList.remove(newsInfoList.size() -1);
                newsInfoList.addAll(infoList);
                itemNewsAdapter.notifyDataSetChanged();
                loading =false;
            }

        }
    }
}
