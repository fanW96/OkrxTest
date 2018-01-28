package com.example.fanw.okrxtest;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * Created by fanw on 2018/1/28.
 */

public class TestApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initOkGo();
    }

    private void initOkGo(){
        HttpHeaders headers = new HttpHeaders();
        headers.put("headersKey","headersValue");//不支持中文
        HttpParams params = new HttpParams();
        params.put("paramsKey","params值");//支持中文

        //构建OkHttpClient.Builder
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log配置
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log的打印级别，决定了log的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色，决定了在控制台输出的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        //配置超时时间
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS,TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS,TimeUnit.MILLISECONDS);

        //自动管理cookie
        //与之前的对：在OkGo.init(this)下使用setCookieStore来自动进行cookie的管理
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));//使用数据库保持cookie，cookie的生命周期期间一直存在

        //http相关证书配置，https的域名匹配规则配置先略过

        //正式的OkGo配置
        OkGo.getInstance().init(this)//初始化
                .setOkHttpClient(builder.build())//使用自定义的OkHttpClient
                .setCacheMode(CacheMode.NO_CACHE)//全局统一缓存模式，没有缓存
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)//全局统一缓存时间，永不过期
                .setRetryCount(3);         //全局统一超时重连次数
//                .addCommonHeaders(headers) //全局的公共头
//                .addCommonParams(params);  //全局的公共参数
    }
}
