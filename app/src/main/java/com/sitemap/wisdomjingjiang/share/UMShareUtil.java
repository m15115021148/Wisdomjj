package com.sitemap.wisdomjingjiang.share;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.sitemap.wisdomjingjiang.utils.ShowContentUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;

/**
 * 
 * Description: 友盟分享工具类
 * 
 * @author chenhao
 * @date 2016-3-25
 */
public class UMShareUtil {

	private Activity context;// 上下文对象
	private UMSharePlatformListener listener;// 分享回调监听
	// 分享平台列表
	private final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[] {
			SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QZONE,
			SHARE_MEDIA.QQ  };
//	SHARE_MEDIA.SINA,新浪微博暂时屏蔽
 
	public UMShareUtil(Activity context) {
		this.context = context;
		UMShareConfig.init();
		 listener=new UMSharePlatformListener(context);
	}

	/**
	 * 打开分享窗口,分享网络图片形式的url
	 * 
	 * @param title
	 *            标题
	 * @param content
	 *            描述
	 * @param imageUrl
	 *            分享时展示的图片（URL）
	 * @param url
	 *            要分享的URL
	 */
	public void shareNetImage(String title, String content, String imageUrl,
			String url) {
		if(TextUtils.isEmpty(imageUrl)||TextUtils.isEmpty(url)){
			ShowContentUtils.showShortToastMessage(context, "分享信息不完整 ，无法分享！");
			return;
		}
		// 网络图片
		UMImage image = new UMImage(context, imageUrl);
		new ShareAction(context).setDisplayList(displaylist).withTitle(title)
				.withMedia(image).withText(content).withTargetUrl(url)
				.setCallback(listener).open();
	}

	/**
	 * 打开分享窗口,分享app系统中自带的图片形式的url
	 * 
	 * @param title
	 *            标题
	 * @param content
	 *            描述
	 * @param drawableID
	 *            要分享的图片id
	 * @param url
	 *            要分享的URL
	 */
	public void shareDrawableImage(String title, String content,
			int drawableID, String url) {
		// 程序中的图片
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				drawableID);
		UMImage image = new UMImage(context, bitmap);
		new ShareAction(context).setDisplayList(displaylist).withTitle(title)
				.withMedia(image).withText(content).withTargetUrl(url)
				.setCallback(listener).open();
	}

	/**
	 * 打开分享窗口,分享外部存储的图片形式的url
	 * 
	 * @param title
	 *            标题
	 * @param content
	 *            描述
	 * @param path
	 *            要分享的图片路径
	 * @param url
	 *            要分享的URL
	 */
	public void shareFileImage(String title, String content, String path,
			String url) {
		UMImage image = new UMImage(context, new File(path));
		new ShareAction(context).setDisplayList(displaylist).withTitle(title)
				.withMedia(image).withText(content).withTargetUrl(url)
				.setCallback(listener).open();
	}

	/**
	 * 打开分享窗口,分享music
	 * 
	 * @param title
	 *            标题
	 * @param content
	 *            描述
	 * @param musicTitle
	 *            音乐标题
	 * @param musicUrl
	 *            音乐路径url
	 * @param musicImageUrl
	 *            音乐图片url
	 */
	public void shareMusic(String title, String content, String musicUrl,
			String musicTitle, String musicImageUrl) {
		UMusic music = new UMusic(musicUrl);
		music.setTitle(musicTitle);
		music.setThumb(new UMImage(context, musicImageUrl));
		new ShareAction(context).setDisplayList(displaylist).withTitle(title)
				.withMedia(music).withText(content).setCallback(listener)
				.open();
	}

	/**
	 * 打开分享窗口,分享video
	 * 
	 * @param title
	 *            标题
	 * @param content
	 *            描述
	 * @param videoTitle
	 *            video标题
	 * @param videoUrl
	 *            video路径url
	 * @param videoImageUrl
	 *            video图片url
	 */
	public void shareVideo(String title, String content, String videoUrl,
			String videoTitle, String videoImageUrl) {
		UMVideo video = new UMVideo(videoUrl);
		video.setTitle(videoTitle);
		video.setThumb(new UMImage(context, videoImageUrl));
		new ShareAction(context).setDisplayList(displaylist).withTitle(title)
				.withMedia(video).withText(content).setCallback(listener)
				.open();
	}
}
