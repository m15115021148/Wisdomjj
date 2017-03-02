package com.sitemap.wisdomjingjiang.share;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 
 * Description: 友盟分享监听；
 * 注： // attention to this below ,must add this在activity中的onActivityResult（）函数中，监听才生效
*     // UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
 * @author chenhao
 * @date   2016-3-25
 */
public class UMSharePlatformListener implements UMShareListener{
	private Context context;
	public UMSharePlatformListener(Context context){
		this.context=context;
	}
	  @Override
      public void onResult(SHARE_MEDIA platform) {
//          Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onError(SHARE_MEDIA platform, Throwable t) {
//    	  Log.i("will", t.getMessage()+"----  "+t.toString());
      }

      @Override
      public void onCancel(SHARE_MEDIA platform) {
//          Toast.makeText(context,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
      }

}
