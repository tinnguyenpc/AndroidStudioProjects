package com.example.hieul.hismart;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Album> albumList;
    public CardView cardView;
    public View itemView;
    public ClipData.Item currentItem;
    String folder_main = "hismart/hinhmon";
    File fileloc = new File(Environment.getExternalStorageDirectory(), folder_main);
    String IDCH = "1";
    SwipeRefreshLayout swipeContainer;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        }
    }


    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Db db = new Db(mContext);


        Album album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText(album.getGia() + " vnđ");
        Glide.with(mContext).load(album.getUrl()).into(holder.thumbnail);


        //load local
//        Glide.with(mContext).load(imageUri).into(holder.thumbnail);

        //load using picaso
//        Picasso.with(mContext).load(albumList.get(position).getUrl()).resize(300, 300).into(holder.thumbnail);


        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {


                Db db = new Db(view.getContext());
                db.querydata("Create table if not exists tbl_order (ID_ integer primary key, IDCH_book integer not null, IDmon_book integer not null, TT_tt text not null)");
                Cursor tb_or = db.getdata("select * from tbl_order");

                Date currentTime = Calendar.getInstance().getTime();
                Album alb = albumList.get(position);
                int count = tb_or.getCount();
//                Toast.makeText(mContext, alb.getId(), Toast.LENGTH_SHORT).show();

                for (int i = 0; i <= position; i++) {

                    if (i == position) {
                        db.querydata("insert into tbl_order values(null, '" + IDCH + "','" + alb.getId() + "','" + "0" + "')");
                        Toast.makeText(mContext, "Đã thêm " + alb.getName() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        break;
                    }

                }
                db.close();
                tb_or.close();
            }
        });


    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


}
