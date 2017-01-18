package com.soubao.tpshop.activity.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.soubao.tpshop.R;

import org.androidannotations.annotations.AfterViews;

import java.util.List;


/**
 * Intent List<String> data=  getIntent().getStringArrayListExtra("data")
 *
 * @author lenovo
 *         <p/>
 *         setResult list index
 */
public class SPPopuListViewActivity extends SPBaseActivity implements View.OnClickListener {

    Button mConfirmBtn;
    int selectIndex = -1;

    List<String> data;
    String[] strDatas;
    ListView mListView;
    int defaultIndex = -1;
    String defaultText = "";

    ListAdapter listAdapter;

    SimpleAdapter listItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.title_getcontent));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popu_list);

        mConfirmBtn = (Button) findViewById(R.id.ok_btn);
        mConfirmBtn.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(mOnItemClickListener);
        data = getIntent().getStringArrayListExtra("listdata");
        strDatas = getIntent().getStringArrayExtra("data");
        selectIndex = getIntent().getIntExtra("defaultIndex", -1);
        defaultText = getIntent().getStringExtra("defaultText");
    /*
     //生成动态数组，加入数据
    ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    if (data != null) {
        for (int i = 0; i < data.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemText", data.get(i));
            listItem.add(map);
        }
    }else if(strDatas != null){
        for (int i = 0; i < strDatas.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemText", strDatas[i]);
            listItem.add(map);
        }
    }

    listItemAdapter = new SimpleAdapter(this,listItem,//数据源
        R.layout.activity_popu_list_item,//ListItem的XML实现
        //动态数组与ImageItem对应的子项
        new String[] { "ItemText"},
        //ImageItem的XML文件里面的一个ImageView,两个TextView ID
        new int[] {R.id.ItemText}
    );

            //添加并且显示
     mListView.setAdapter(listItemAdapter);
     */
        listAdapter = new ListAdapter(this);
        mListView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        super.init();
    }


    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    private class ListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局 /*构造函数*/

        public ListAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (data != null) {
                return data.size();
            } else if (strDatas != null) {
                return strDatas.length;
            }
            return 0;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(
                        R.layout.activity_popu_list_item, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView
                        .findViewById(R.id.ItemText);
                holder.title.setText(strDatas[position]);
                holder.check_item = (ImageView) convertView.findViewById(R.id.check_item);
                if (selectIndex == position) {// 选中的条目和当前的条目是否相等
                    holder.check_item.setVisibility(View.VISIBLE);
                } else {
                    holder.check_item.setVisibility(View.INVISIBLE);
                }
                if (selectIndex == -1 && !"".equals(defaultText)
                        && defaultText != null
                        && defaultText.equals(strDatas[position])) {
                    holder.check_item.setVisibility(View.VISIBLE);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                if (data != null) {
                    holder.title.setText(data.get(position));
                } else if (strDatas != null) {
                    holder.title.setText(strDatas[position]);
                }

                if (selectIndex == position) {// 选中的条目和当前的条目是否相等
                    holder.check_item.setVisibility(View.VISIBLE);
                } else {
                    holder.check_item.setVisibility(View.INVISIBLE);
                }
                if (selectIndex == -1 && !"".equals(defaultText)
                        && defaultText != null
                        && defaultText.equals(strDatas[position])) {
                    holder.check_item.setVisibility(View.VISIBLE);
                }
            }
            return convertView;
        }

        public final class ViewHolder {
            public TextView title;
            public ImageView check_item;
        }

    }

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos,
                                long arg3) {
            if (selectIndex != pos) {
//				setCheckItem(adapterView,selectIndex,View.INVISIBLE);
//				setCheckItem(adapterView,pos,View.VISIBLE);
                selectIndex = pos;
//				listItemAdapter.notifyDataSetChanged();

                listAdapter.notifyDataSetChanged();
            }
        }
    };

    private void setCheckItem(AdapterView<?> adapterView, int index, int visiable) {
        View v = adapterView.getChildAt(index);
        if (v != null) {
            v.findViewById(R.id.check_item).setVisibility(visiable);
        }
    }


    @Override
    public void onClick(View arg0) {
        onResultOkClick();
        this.finish();
    }

    private void onResultOkClick() {
        Intent data = new Intent();
        data.putExtra("index", selectIndex);
        setResult(RESULT_OK, data);
    }

}

