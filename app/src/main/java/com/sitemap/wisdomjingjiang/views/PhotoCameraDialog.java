package com.sitemap.wisdomjingjiang.views;

import java.io.File;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.utils.FileNames;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 进行照片 拍照的对话框
 * 
 * @author xym
 * 
 */

public class PhotoCameraDialog {

	private Context context;
	private final String IMAGE_TYPE = "image/*";
	public final static int IMAGE_CODE = 1;
	public final static int TAKE_PICTURE = 0;
	private Uri fileUri;

	public PhotoCameraDialog(final Activity context, final LinearLayout layout) {
		this.context = context;
		View contentView = LayoutInflater.from(this.context).inflate(
				R.layout.sytle_addphoto, null);
		final PopupWindow popupWindow = new PopupWindow(contentView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		TextView showcamera = (TextView) contentView
				.findViewById(R.id.showcamera);
		TextView showphoto = (TextView) contentView
				.findViewById(R.id.showphoto);

		showcamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 调用android自带的照相机
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				FileNames name = new FileNames();

				File file = new File(
						Environment
								.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						name.getImageName());
				setFileUri(Uri.fromFile(file));
				intent.putExtra(MediaStore.EXTRA_OUTPUT, getFileUri());
				context.startActivityForResult(intent, TAKE_PICTURE);
				popupWindow.dismiss();
			}
		});

		showphoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 调用android自带的相册
				Intent getAlbum = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				getAlbum.setType(IMAGE_TYPE);
				context.startActivityForResult(getAlbum, IMAGE_CODE);
               
				popupWindow.dismiss();
			}
		});

		mypopupwindow(contentView, popupWindow, layout);
	}

	// 建立一个对话框
	private void mypopupwindow(View contentView, PopupWindow popupWindow,
			LinearLayout layout) {
		popupWindow.setTouchable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.showAtLocation(layout, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	public Uri getFileUri() {
		return fileUri;
	}

	public void setFileUri(Uri fileUri) {
		this.fileUri = fileUri;
	}

}
