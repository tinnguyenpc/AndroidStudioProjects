package com.example.hieul.hismart;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hieul on 26-Nov-17.
 */

public class listviewAdapterThanhtoan extends BaseAdapter {

    public ArrayList<GetsetThanhtoan> productList;

    private LayoutInflater inflater;
    private Context context;
    private Activity activity;

    public listviewAdapterThanhtoan(Activity activity, ArrayList<GetsetThanhtoan> productList) {

        this.activity = activity;
        this.productList = productList;
    }


    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mSNo;
        TextView mProduct;
        TextView mCategory;
        TextView mPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row_thanhtoan, null);
            holder = new ViewHolder();
            holder.mSNo = (TextView) convertView.findViewById(R.id.sNo);
            holder.mProduct = (TextView) convertView.findViewById(R.id.product);
            holder.mCategory = (TextView) convertView
                    .findViewById(R.id.category);
            holder.mPrice = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GetsetThanhtoan item = productList.get(position);
        holder.mSNo.setText(item.getSTT().toString());
        holder.mProduct.setText(item.getTENMON().toString());
        holder.mCategory.setText(item.getTIME().toString());
        holder.mPrice.setText(item.getGIA().toString());

        return convertView;
    }
}
