package com.sitemap.wisdomjingjiang.fragments;


import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.activities.NewsDescActivity;
import com.sitemap.wisdomjingjiang.adapters.NewsListViewAdapter;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.config.RequestCode;
import com.sitemap.wisdomjingjiang.config.WebUrlConfig;
import com.sitemap.wisdomjingjiang.db.DBCheckNews;
import com.sitemap.wisdomjingjiang.http.HttpUtil;
import com.sitemap.wisdomjingjiang.models.NewsListModel;
import com.sitemap.wisdomjingjiang.utils.CacheUtils;
import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.sitemap.wisdomjingjiang.views.PullToRefreshLayout;
import com.sitemap.wisdomjingjiang.views.PullToRefreshLayout.OnRefreshListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * com.sitemap.wisdomjingjiang.fragments.Fragment1
 * @author zhang
 * create at 2016年4月27日 上午10:00:54
 */
@SuppressLint("ValidFragment")
public class NewsFragment extends Fragment implements OnClickListener{
	private NewsFragment context;// 本类
	private HttpUtil http;// 网络请求
	private View rootView;
	private String type="";//接收的种类
	private ListView newsList;//新闻列表
	private NewsListViewAdapter newsAdapter;//新闻列表适配器
	private PullToRefreshLayout ptrl;//加载刷新
	private int page=0;//页数
	private List<NewsListModel> lnewsList=new ArrayList<NewsListModel>();//显示的列表
	private List<NewsListModel> lnewsListMore=new ArrayList<NewsListModel>();//加载更多回来的列表	

	public NewsFragment(String type) {
		this.type=type;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		rootView = inflater.inflate(R.layout.fragment_news, (ViewGroup)getActivity().findViewById(R.id.pager), false);
		http = new HttpUtil(handler);		
	}
	
	@SuppressLint("ValidFragment")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ptrl = ((PullToRefreshLayout)rootView. findViewById(R.id.refresh_view));
		ptrl.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				
				if (MyApplication.getNetObject().isNetConnected()) {
					http.sendGet(
							RequestCode.GETNEWSLIST,
							WebUrlConfig.getNewsList(type,String.valueOf(0)));
				} else {
					ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
					ShowContentUtils.showShortToastMessage(getActivity(), RequestCode.NOLOGIN);
				}
			}
			
			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				if (MyApplication.getNetObject().isNetConnected()) {
					http.sendGet(
							RequestCode.GETNEWSLISTMORE,
							WebUrlConfig.getNewsList(type,String.valueOf(page)));
				} else {
					ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					ShowContentUtils.showShortToastMessage(getActivity(), RequestCode.NOLOGIN);
				}
			}
		});
		newsList=(ListView)rootView.findViewById(R.id.news_list);
		newsAdapter=new NewsListViewAdapter(getActivity(),lnewsList);
		newsList.setAdapter(newsAdapter);
		if (MyApplication.getNetObject().isNetConnected()) {
			http.sendGet(
					RequestCode.GETNEWSLIST,
					WebUrlConfig.getNewsList(type,String.valueOf(page)));
		} else {
			ShowContentUtils.showShortToastMessage(getActivity(), RequestCode.NOLOGIN);
		}
		newsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {				
				Intent intent = new Intent();
				intent.setClass(getActivity(), NewsDescActivity.class);
				NewsListModel model = lnewsList.get(position);
				intent.putExtra("newsUrl", model.getNewsUrl());
				intent.putExtra("img", model.getNewsImg());//图片
				intent.putExtra("title",model.getNewsTitle());//标题
				//本地缓存
//				CacheUtils.saveCache(getActivity(), model.getNewsID());
				MyApplication.db.insert(model.getNewsID());	
				TextView title = (TextView) view.findViewById(R.id.news_title);
				title.setTextColor(getActivity().getResources().getColor(R.color.texthui));
				startActivity(intent);
			}
		});
		
		return rootView;
	}
	@Override
	public void onClick(View v) {
		
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HttpUtil.SUCCESS:// 成功
				if (msg.arg1 == RequestCode.GETNEWSLIST) {//第一次和刷新成功
					page=0;
					Log.i("TAG", "request:" + msg.obj.toString());
					lnewsList.clear();
					lnewsList=JSONObject.parseArray(msg.obj.toString(),
							NewsListModel.class);
					newsAdapter=new NewsListViewAdapter(getActivity(),lnewsList);
					newsList.setAdapter(newsAdapter);
					// 千万别忘了告诉控件刷新完毕了哦！
					page+=1;
					ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
				if (msg.arg1 == RequestCode.GETNEWSLISTMORE) {//加载更多
					page+=1;
					Log.i("TAG", "request:" + msg.obj.toString());
					lnewsListMore.clear();
					lnewsListMore=JSONObject.parseArray(msg.obj.toString(),
							NewsListModel.class);
					for (int i = 0; i <lnewsListMore.size(); i++) {
						lnewsList.add(lnewsListMore.get(i));
					}
					newsAdapter=new NewsListViewAdapter(getActivity(),lnewsList);
					newsList.setAdapter(newsAdapter);
					// 千万别忘了告诉控件加载完毕了哦！
					ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
				break;
			case HttpUtil.FAILURE:// 失败
				ShowContentUtils.showLongToastMessage(getActivity(),
						RequestCode.ERRORINFO);
				break;
			case HttpUtil.LOADING:// 更新进度条
				if (msg.arg2 == RequestCode.LOGIN) {
					Log.i("TAG", "当前进度（0--100）： " + msg.obj.toString());
				}
				break;
			default:
				break;
			}
		}

	};
}
