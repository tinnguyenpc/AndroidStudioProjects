package com.example.hieul.hismart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by hieul on 29-Oct-17.
 */

public class CartArrayAdapter extends ArrayAdapter<CartGetSetListView> {
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    URL url;
    Bitmap bmp = null;

    public CartArrayAdapter(Context ctx, int resourceId, List<CartGetSetListView> objects) {
        super(ctx, resourceId, objects);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = (LinearLayout) inflater.inflate(resource, null);
        CartGetSetListView cart = getItem(position);

        ImageView imgMon = (ImageView) convertView.findViewById(R.id.imgMon);
        TextView tenMon = (TextView) convertView.findViewById(R.id.tenMon);
        TextView idmon = (TextView) convertView.findViewById(R.id.idmon);
        tenMon.setText(cart.getTenmon());
        idmon.setText(cart.getIdmon());

        ImageButton imgDel = (ImageButton) convertView.findViewById(R.id.Del);
        TextView gia = (TextView) convertView.findViewById(R.id.giaMon);
        gia.setText("Giá: " + cart.getGia() + " vnđ");


        Picasso.with(getContext()).load(cart.getImgurlmon()).into(imgMon);


        String uri1 = "drawable/" + cart.getImgdel();
        int imageResource1 = context.getResources().getIdentifier(uri1, null, context.getPackageName());
        Drawable image1 = context.getResources().getDrawable(imageResource1);
        imgDel.setImageDrawable(image1);


        return convertView;
    }



}

