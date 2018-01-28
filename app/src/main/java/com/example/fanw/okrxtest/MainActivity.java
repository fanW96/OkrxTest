package com.example.fanw.okrxtest;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rcvArticle;
    private List<NewsInfo> newsInfoList = new ArrayList<NewsInfo>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        rcvArticle = findViewById(R.id.rcv_article);
        rcvArticle.setLayoutManager(new LinearLayoutManager(context));
        new LatestArticleTask().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    private class LatestArticleTask extends AsyncTask<String,Void,List<NewsInfo>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<NewsInfo> doInBackground(String... strings) {
            String str ="http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html";
            OkGo.<simple<List<NewsInfo>>>get("http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html")
                    .tag(this)
                    .execute(new JsonCallBack<simple<List<NewsInfo>>>() {
                        @Override
                        public void onSuccess(Response<simple<List<NewsInfo>>> response) {
                            newsInfoList = response.body().getT1348647909107();
                        }
                    });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return newsInfoList;
        }

        @Override
        protected void onPostExecute(List<NewsInfo> infoList) {
            super.onPostExecute(infoList);
//            出错：data没有获得到数据
//            原因：doinbackground的return在okgo请求期间就返回了
//            解决方式：在return之前让线程休眠1秒
//            Toast.makeText(context,data.get(1).getPtime(),Toast.LENGTH_LONG).show();
            ItemNewsAdapter adapter = new ItemNewsAdapter(infoList,context);
            rcvArticle.setAdapter(adapter);
        }
    }
}
