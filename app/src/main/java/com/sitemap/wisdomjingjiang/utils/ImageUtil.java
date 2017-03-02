package com.sitemap.wisdomjingjiang.utils;

import com.sitemap.wisdomjingjiang.views.PhotoCameraDialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * 相机和相册的处理工具
 * 
 * @author chenhao
 * 
 */
public class ImageUtil {
	private static String path = "";

	/**
	 * 从相册取图片时，调用；返回图片路径
	 * 
	 * @param context
	 * @param originalUri
	 * @param imageView
	 * @return
	 */
	public static String setPhoto(Activity context, Uri originalUri,
			ImageView imageView) {
		path = "";
		path = GetImageAbsolutePath.getImageAbsolutePath(context, originalUri);
		if (path != "") {
			int width = imageView.getWidth();
			int height = imageView.getHeight();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			int imageWidth = options.outWidth;
			int imageHeight = options.outHeight;
			int scale = Math.min(imageWidth / width, imageHeight / height);// 计算缩放比
			options.inJustDecodeBounds = false;
			options.inSampleSize = scale;
			Bitmap bitmap = BitmapFactory.decodeFile(path, options);
			imageView.setImageBitmap(bitmap);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setVisibility(View.VISIBLE);
		}

		return path;
	}


	/**
	 * 从照相机照相时，调用；返回图片路径
	 * 
	 * @param context
	 * @param photo
	 * @param imageView
	 * @return
	 */
	public static String setCameraPhoto(Activity context,
			PhotoCameraDialog photo, ImageView imageView) {
		path = "";
		int width = imageView.getWidth();
		int height = imageView.getHeight();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photo.getFileUri().getPath(), options);
		int imageWidth = options.outWidth;
		int imageHeight = options.outHeight;
		int scale = Math.min(imageWidth / width, imageHeight / height);// 计算缩放比
		options.inJustDecodeBounds = false;
		options.inSampleSize = scale;
		Bitmap bitmap = BitmapFactory.decodeFile(photo.getFileUri().getPath(),
				options);
		imageView.setImageBitmap(bitmap);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setVisibility(View.VISIBLE);
		path = photo.getFileUri().getPath();
		return path;
	}

}
