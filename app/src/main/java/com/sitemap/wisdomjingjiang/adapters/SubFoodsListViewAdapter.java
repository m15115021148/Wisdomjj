package com.sitemap.wisdomjingjiang.adapters;

import java.util.List;

import org.xutils.x;

import com.sitemap.wisdomjingjiang.R;
import com.sitemap.wisdomjingjiang.application.MyApplication;
import com.sitemap.wisdomjingjiang.models.CartFoodsInfoModel;
import com.sitemap.wisdomjingjiang.models.CartFoodsModel;
import com.sitemap.wisdomjingjiang.models.OrderFoodsModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * com.sitemap.wisdomjingjiang.adapters
 * 
 * @author chenmeng
 * @Description �ύ����  �Ź� ������
 * @date create at 2016��6��3�� ����10:26:00
 */
public class SubFoodsListViewAdapter extends BaseExpandableListAdapter {

	Context mContext;
	/** ���� holder */
	private GroupHolder groupHolder;
	/** ���� holder */
	private ChildHolder childHolder;
	
	/**����  ����*/ 
	private List<CartFoodsInfoModel> mChildList;
	private String name;
	
	public SubFoodsListViewAdapter(Context context ,
			List<CartFoodsInfoModel> list,String name){
		this.mContext = context;
		this.mChildList = list;
		this.name = name;
	}
	
	/**
	 * ��ȡ�ӳ����������
	 * 
	 * @return
	 */
	@Override
	public int getGroupCount() {
		return 1;
	}

	/**
	 * ���������
	 * 
	 * @param groupPosition
	 * @return
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return mChildList.size();
	}

	/**
	 * ��ȡÿ������Ķ���
	 * 
	 * @param groupPosition
	 * @return
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	/**
	 * ��ȡ����
	 * 
	 * @param groupPosition
	 * @param childPosition
	 * @return
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mChildList.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	/**
	 * �����������ͼ
	 * 
	 * @param groupPosition
	 * @param isExpanded
	 * @param convertView
	 * @param parent
	 * @return
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.lv_sub_order_group_item, null);
			groupHolder = new GroupHolder();
			groupHolder.name = (TextView) convertView
					.findViewById(R.id.name);
			convertView.setTag(groupHolder);
			//ȥ������ĵ���¼� �޷���Ӧ���
            convertView.setClickable(true);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}
		groupHolder.name.setText(name);
		
		return convertView;
	}

	/**
	 * �����������ͼ
	 * 
	 * @param groupPosition
	 * @param childPosition
	 * @param isLastChild
	 * @param convertView
	 * @param parent
	 * @return
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.lv_sub_foods_order_item, null);
			childHolder = new ChildHolder();
			childHolder.shopsName = (TextView) convertView.findViewById(R.id.order_shops_name);
			childHolder.shopsNum = (TextView) convertView.findViewById(R.id.order_number);
			childHolder.shopsPrice = (TextView) convertView.findViewById(R.id.order_price);
			childHolder.shopsImg = (ImageView) convertView.findViewById(R.id.order_img);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}
		
		CartFoodsInfoModel model = mChildList.get(childPosition);
		x.image().bind(childHolder.shopsImg, model.getFoodsImg(),MyApplication.imageOptions);
		childHolder.shopsName.setText(model.getFoodsName());
		childHolder.shopsNum.setText("x"+model.getNumber());

		if(model.getFoodsPrice().equals("")){
			childHolder.shopsPrice.setText("��0.00");
		}else{
			childHolder.shopsPrice.setText("��"+model.getFoodsPrice());
		}		

		return convertView;
	}

	/** ExpandableListView �������Ŀ��Ҫ��Ӧclick�¼�,���践��true */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * ����� holder
	 * 
	 */
	private class GroupHolder {
		TextView name;// �̼�����
	}

	/**
	 * ����� holder
	 * 
	 */
	private class ChildHolder {
		/**��Ʒ����  ����  ��ɫ  �ߴ�  �۸�*/ 
		TextView shopsName,shopsNum,shopsPrice;
		ImageView shopsImg;
	}
}
