package com.example.fanw.okrxtest;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanw on 2018/1/28.
 */

public class ItemNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //新闻列表
    private List<NewsInfo> newsInfoList;

    //context
    private Context context;
    private LayoutInflater layoutInflater;

    //viewType的类型
//    public final static int TYPE_MULTI_IMAGES = 3; // 多个图片的文章（暂时未用）
    public final static int TYPE_FOOTER = 2;//底部--往往是loading_more
    public final static int TYPE_NORMAL = 1; // 正常的一条文章

    //监听事件
    private OnItemClickListener mOnItemClickListener;

    public ItemNewsAdapter(List<NewsInfo> newsInfoList, Context context) {
        if(newsInfoList == null){
            this.newsInfoList = new ArrayList<NewsInfo>();
        }else {
            this.newsInfoList = newsInfoList;
        }
        this.context = context;
        this.layoutInflater =  LayoutInflater.from(context);
    }

    //监听事件获取
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        NewsInfo simpleNews = newsInfoList.get(position);
        if(/*simpleNews.getPostid() == ""*/simpleNews == null){
/*            //错误：空指针异常
            //原因：simple对null的判断一直失效，留给footer的null的哪一行被带进了normal
            //解决：不再直接add(null),而是给一个实例化的对象，对实例化的对象的必要值做判断*/
            /*
            * 判断失误，由于在TYPE_FOOTER的返回值中同样给了ItemNewsViewHolder
            * 导致判断一直失效
            * */
            return TYPE_FOOTER;
        }else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View view;
        switch (viewType){
            //其他无法处理的情况使用viewholder_article_simple
            default:
            case TYPE_NORMAL:
                view = layoutInflater.inflate(R.layout.item_news,parent,false);
                vh = new ItemNewsViewHolder(view);
                return vh;
            case TYPE_FOOTER:
                view = layoutInflater.inflate(R.layout.recyclerview_footer,parent,false);
                vh = new FooterViewHolder(view);
                return vh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder){
            ((FooterViewHolder)holder).progressBar.setVisibility(View.VISIBLE);
            //问题：出现空指针异常
            //原因：没有在此处返回，下面的数据要获得会导致footer使用的null被传了下去
            //解决办法：直接使用return结束
            return;
        }

        NewsInfo newsInfo = newsInfoList.get(position);
//        List<NewsInfo.ImgextraBean> imgExtra = newsInfo.getImgextra();

        if(holder instanceof ItemNewsViewHolder){
//            NewsInfo newsInfo = newsInfoList.get(position);
            ItemNewsViewHolder itemNewsViewHolder = (ItemNewsViewHolder)holder;
            itemNewsViewHolder.news_title_tv.setText(newsInfo.getLtitle() == null ? newsInfo.getTitle():newsInfo.getLtitle());
            itemNewsViewHolder.news_ptime_tv.setText(newsInfo.getPtime());
            itemNewsViewHolder.news_digest_tv.setText(newsInfo.getDigest());
            GlideApp.with(context).load(newsInfo.getImgsrc()).placeholder(R.mipmap.ic_launcher).into(itemNewsViewHolder.news_photo_iv);
            //给Holder的组件设置点击事件
            /*itemNewsViewHolder.news_title_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v,"nihao",Snackbar.LENGTH_LONG).show();
                }
            });*/
            //swipeLayout设置
            //set show mode.
            itemNewsViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
            itemNewsViewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, itemNewsViewHolder.bottom_wrapper);

            //删除监听
            /*itemNewsViewHolder.cv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/
        }
    }

    //不单独使用
/*    @Override
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
    }*/

    @Override
    public int getItemCount() {
        return newsInfoList.size();
    }

    public class ItemNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        OnItemClickListener mOnItemClickListenerTemp;
        ImageView news_photo_iv;
        TextView news_title_tv;
        TextView news_digest_tv;
        TextView news_ptime_tv;
        SwipeLayout swipeLayout;
        CardView cv_delete;
        LinearLayout bottom_wrapper;


        public ItemNewsViewHolder(View itemView) {
            super(itemView);//holder设定
            news_photo_iv = itemView.findViewById(R.id.news_photo_iv);
            news_title_tv = itemView.findViewById(R.id.news_title_tv);
            news_digest_tv = itemView.findViewById(R.id.news_digest_tv);
            news_ptime_tv = itemView.findViewById(R.id.news_ptime_tv);
            swipeLayout = itemView.findViewById(R.id.delete_swipe);
            cv_delete = itemView.findViewById(R.id.cv_Delete);
            bottom_wrapper = itemView.findViewById(R.id.bottom_wrapper);
            news_title_tv.setOnClickListener(this);
            cv_delete.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition() , v);
            }
        }
    }

    /*
    * 底部加载
    * */
    public class FooterViewHolder extends RecyclerView.ViewHolder{

        ProgressBar progressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.rcv_progressBar);
        }
    }
}
