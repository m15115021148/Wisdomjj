package com.sitemap.wisdomjingjiang.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * Description: 购物商品详情数量model
 * @author chenhao
 * @date   2016-6-7
 */
public class NumberModel implements Parcelable{
	private String color    ;     //    颜色
	private String size      ;      //  尺寸
	private String number  ;    //     数量
	private String price   ;//          价格
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	/**
	 * 序列化实体类
	 */
	public static final Parcelable.Creator<NumberModel> CREATOR = new Creator<NumberModel>() {
		public NumberModel createFromParcel(Parcel source) {
			NumberModel task = new NumberModel();
			// 重点：下面的顺序不能乱，否则传递的数据就会造成混乱
			task.color = source.readString();
			task.size = source.readString();
			task.number = source.readString();
			task.price= source.readString();
			return task;
		}

		public NumberModel[] newArray(int size) {
			return new NumberModel[size];
		}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		// 重点：下面的顺序不能乱，否则传递的数据就会造成混乱
				parcel.writeString(color);
				parcel.writeString(size);
				parcel.writeString(number);
				parcel.writeString(price);
		
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

}
