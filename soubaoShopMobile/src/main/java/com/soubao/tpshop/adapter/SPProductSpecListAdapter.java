package com.soubao.tpshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soubao.tpshop.R;
import com.soubao.tpshop.model.shop.SPProductSpec;
import com.soubao.tpshop.utils.SMobileLog;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.view.tagview.Tag;
import com.soubao.tpshop.view.tagview.TagListView;
import com.soubao.tpshop.view.tagview.TagListView.OnTagClickListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by admin on 2016/6/23.
 */
public class SPProductSpecListAdapter extends BaseAdapter {

    private String TAG = "SPProductSpecListAdapter";
    private List<String>  keys ;
    private Context mContext ;
    private OnTagClickListener mTagClickListener;
    JSONObject mSpecJson;
    private  Collection<String> mSelectSpecs;


    public SPProductSpecListAdapter(Context context, OnTagClickListener tagClickListener , Collection<String> selectSpecs){
        this.mContext = context;
        this.mTagClickListener = tagClickListener;
        keys = new ArrayList<String>();

        this.mSelectSpecs = selectSpecs;
    }

    public void setData(JSONObject specJson){
        if(specJson == null)return;
        this.mSpecJson = specJson;
        keys.clear();
        Iterator<String> keyIterator =  specJson.keys();
        while(keyIterator.hasNext()){
            keys.add(keyIterator.next());
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        if(keys == null){
            count = 0;
        }else{
            count = keys.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        if(keys == null) return null;
        return keys.get(position);
    }

    @Override
    public long getItemId(int position) {
        //if(mProductSpecs == null) return -1;
        return -1;//Long.valueOf(mProductSpecs.get(position).getItemID());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //fragment_spproduct_list_filter
        final ViewHolder holder;
        float titleHeight = 0;

        if(convertView == null){

            //使用减少findView的次数
            holder = new ViewHolder();

            //使用自定义的list_items作为Layout
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_spproduct_list_filter_item, parent, false);
            //convertView.setMinimumHeight();
            holder.tagListv = (TagListView)convertView.findViewById(R.id.filter_taglstv);
            holder.tagListv.setTagViewBackgroundRes(R.drawable.tag_button_bg_unchecked);
            holder.tagListv.setTagViewTextColorRes(R.color.color_font_gray);
            holder.tagListv.setOnTagClickListener(mTagClickListener);
            holder.titleTxtv = (TextView)convertView.findViewById(R.id.filter_title_txtv);

            titleHeight = SPCommonUtils.dip2px(mContext, 20);

            //设置标记
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            String key = keys.get(position);
            //获取该行数据
            List<SPProductSpec> specs = (List<SPProductSpec>)mSpecJson.get(key);
            List<Tag> tags = getTags(specs);
            holder.tagListv.setTags(tags);
            //设置数据到View
            holder.titleTxtv.setText(key);

            float totalHeight = titleHeight + holder.tagListv.getTagListViewHeight();

            convertView.setMinimumHeight(Float.valueOf(totalHeight).intValue());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

     private List<Tag> getTags(List<SPProductSpec> specs) {
        if (specs==null)return null;
        List<Tag> mTags = new ArrayList<Tag>();
        int size = specs.size();
        for (int i = 0; i < size; i++) {
            SPProductSpec spec  = specs.get(i);
            Tag tag = new Tag();
            tag.setId(i);
            if (mSelectSpecs!= null && mSelectSpecs.contains(spec.getItemID())) {
                tag.setChecked(true);
            }else{
                tag.setChecked(false);
            }
            tag.setKey(spec.getSpecName());
            tag.setValue(spec.getItemID());
            tag.setTitle(spec.getItem());
            mTags.add(tag);
        }
         return mTags;
    }

    class ViewHolder{
        TextView titleTxtv;
        TagListView tagListv;

    }

}
