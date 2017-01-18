package com.soubao.tpshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.model.shop.SPFilter;
import com.soubao.tpshop.model.shop.SPFilterItem;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.view.tagview.Tag;
import com.soubao.tpshop.view.tagview.TagListView;
import com.soubao.tpshop.view.tagview.TagListView.OnTagClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/23.
 */
public class SPProductListFilterAdapter extends BaseAdapter {

    private String TAG = "SPProductListFilterAdapter";
    private List<SPFilter> mFilters ;
    private Context mContext ;
    private int curSelectRow = -1;	//当前被选中的行索引
    private OnTagClickListener mTagClickListener;
    public SPProductListFilterAdapter(Context context , OnTagClickListener tagClickListener){
        this.mContext = context;
        this.mTagClickListener = tagClickListener;
    }


    public void setData(List<SPFilter> filters){
        if(filters == null)return;
        this.mFilters = filters;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = 0;
        if(mFilters == null){
            count = 0;
        }else{
            count = mFilters.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        if(mFilters == null) return null;
        return mFilters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //fragment_spproduct_list_filter
        final ViewHolder holder;
        if(convertView == null){

            //使用减少findView的次数
            holder = new ViewHolder();

            //使用自定义的list_items作为Layout
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_spproduct_list_filter_item, parent, false);

            holder.tagListv = (TagListView)convertView.findViewById(R.id.filter_taglstv);
            holder.tagListv.setTagViewBackgroundRes(R.drawable.tag_button_bg_unchecked);
            holder.tagListv.setTagViewTextColorRes(R.color.color_font_gray);
            //holder.tagListv.setOnTagCheckedChangedListener(this);
            holder.tagListv.setOnTagClickListener(mTagClickListener);
            holder.titleTxtv = (TextView)convertView.findViewById(R.id.filter_title_txtv);

            //设置标记
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        /** 选中的行: 背景白色, 否则灰色  */
        if(curSelectRow == position){
            convertView.setBackgroundResource(R.drawable.list_row_pressed);
        }else{
            convertView.setBackgroundResource(R.drawable.list_row_normal);
        }

        //获取该行数据
        SPFilter filter = mFilters.get(position);
        List<Tag> tags = getTags(filter);
        holder.tagListv.setTags(tags);
        //设置数据到View
        holder.titleTxtv.setText(filter.getName());
        return convertView;
    }

     private List<Tag> getTags(SPFilter filter) {
        if (filter==null)return null;
        List<Tag> mTags = new ArrayList<Tag>();
        int size = filter.getItems().size();
        for (int i = 0; i < size; i++) {
            SPFilterItem filterItem = filter.getItems().get(i);
            Tag tag = new Tag();
            tag.setId(i);
            if (filterItem.isHighLight()){
                tag.setChecked(true);
            }else {
                tag.setChecked(false);
            }

            tag.setValue(filterItem.getHref());
            tag.setTitle(filterItem.getName());
            mTags.add(tag);
        }
         return mTags;
    }

    class ViewHolder{
        TextView titleTxtv;
        TagListView tagListv;
    }

}
