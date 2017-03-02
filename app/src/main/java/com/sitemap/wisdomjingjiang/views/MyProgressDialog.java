package com.sitemap.wisdomjingjiang.views;


import com.sitemap.wisdomjingjiang.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;


public class MyProgressDialog extends Dialog {

	private static MyProgressDialog customProgressDialog = null;

	public MyProgressDialog(Context context) {
		super(context);
	}
	public MyProgressDialog(Context context, AttributeSet attrs, int theme)     {
		super(context, theme);
	}

	public static MyProgressDialog createDialog(Context context) {
		customProgressDialog = new MyProgressDialog(context,null,
				R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.style_progressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressDialog.setCanceledOnTouchOutside(false);

		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (customProgressDialog == null) {
			return;
		}

		ImageView imageView = (ImageView) customProgressDialog
				.findViewById(R.id.loadingImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getBackground();
		animationDrawable.start();
	}

	/**
	 * 
	 * setMessage
	 * 
	 * @param strMessage
	 * 
	 */
	public void setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog
				.findViewById(R.id.id_tv_loadingmsg);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

	}
}
