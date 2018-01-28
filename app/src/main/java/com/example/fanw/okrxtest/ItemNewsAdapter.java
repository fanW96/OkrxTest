package com.example.fanw.okrxtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

/**
 * Created by fanw on 2018/1/28.
 */

public class ItemNewsAdapter extends RecyclerView.Adapter<ItemNewsAdapter.ItemNewsViewHolder>{
    //新闻列表
    private List<NewsInfo> newsInfoList;

    //context
    private Context context;
    private LayoutInflater layoutInflater;

    public ItemNewsAdapter(List<NewsInfo> newsInfoList, Context context) {
        this.newsInfoList = newsInfoList;
        this.context = context;
        this.layoutInflater =  LayoutInflater.from(context);
    }

    @Override
    public ItemNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_news,parent,false);
        return new ItemNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemNewsViewHolder holder, int position) {
        NewsInfo newsInfo = newsInfoList.get(position);
        holder.news_title_tv.setText(newsInfo.getLtitle() == null ? newsInfo.getTitle():newsInfo.getLtitle());
        holder.news_ptime_tv.setText(newsInfo.getPtime());
        holder.news_digest_tv.setText(newsInfo.getDigest());
        GlideApp.with(context).load(newsInfo.getImgsrc()).placeholder(R.mipmap.ic_launcher).into(holder.news_photo_iv);
    }

    @Override
    public int getItemCount() {
        return newsInfoList.size();
    }

    public class ItemNewsViewHolder extends RecyclerView.ViewHolder{
        ImageView news_photo_iv;
        TextView news_title_tv;
        TextView news_digest_tv;
        TextView news_ptime_tv;

        public ItemNewsViewHolder(View itemView) {
            super(itemView);
            news_photo_iv = itemView.findViewById(R.id.news_photo_iv);
            news_title_tv = itemView.findViewById(R.id.news_title_tv);
            news_digest_tv = itemView.findViewById(R.id.news_digest_tv);
            news_ptime_tv = itemView.findViewById(R.id.news_ptime_tv);

        }
    }
}
