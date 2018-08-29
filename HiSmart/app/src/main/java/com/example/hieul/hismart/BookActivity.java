package com.example.hieul.hismart;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;

    public final boolean tt_thanhtoan = false;
    String booking = "http://lengocminhhieu.ga/App/dsmon.php";
    public String IDmon;
    public String IDcuahang;
    public String Tenmon;
    public String Gia;
    public String Imgurl;
    Db db = new Db(this);

    List<String> ArrTenmon = new ArrayList<String>();
    List<String> ArrGia = new ArrayList<String>();
    List<String> ArrImgLocal = new ArrayList<String>();
    List<String> ArrImgUrl = new ArrayList<String>();
    List<String> ArrIDMon = new ArrayList<String>();
    List<String> ArrIDTable = new ArrayList<String>();
    ImageView image;
    String folder_main = "hismart/hinhmon";
    File fileloc = new File(Environment.getExternalStorageDirectory(), folder_main);
    Boolean tt = false;
    public static boolean check = false;
    Dialog customProgress;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_book);


        this.setFinishOnTouchOutside(true);
        image = (ImageView) findViewById(R.id.thumbnail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, albumList);

        final RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //dialog

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // popup
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tt_thanhtoan) {
                    // đã thanh toán

                } else {
                    // chưa thanh toán

                    Intent m = new Intent(BookActivity.this, CartActivity.class);
                    startActivity(m);
                }
            }
        });


// SWIPE DOWN PROCESS


//        // lay csdl tu web
        LoadDtaWeb();
        // tao bang tbl_order
        db.querydata("Create table if not exists tbl_order (ID_ integer primary key, IDCH_book integer not null, IDmon_book integer not null, TT_tt text not null)");
        db.close();
    }


    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Chọn Món");

                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onBackPressed() {




            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    BookActivity.this);
            alertDialog.setTitle("Leave application?");
            alertDialog.setMessage("Are you sure you want to leave the application?");
            alertDialog.setIcon(R.drawable.icon1);
            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();

    }

    public void LoadDtaWeb() {

        db.querydata("Create table if not exists tbl_mon_app (_ID integer primary key, IDMon integer not null, IDCH integer not null, TenMon text not null, Gia text not null, ImgUrl text not null, ImgLocal text)");
        Cursor c = db.getdata("select * from tbl_mon_app");
        int count = c.getCount();


// getdata web

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, booking,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("{\"DSMON\":null}")) {

                        } else

                            try {


                                JSONObject jsonRootObject = new JSONObject(response);
                                JSONArray jsonArray = jsonRootObject.optJSONArray("DSMON");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    boolean check = false;
                                    IDmon = jsonObject.optString("ID");
                                    IDcuahang = jsonObject.optString("IDCH");
                                    Tenmon = jsonObject.optString("TenMon");
                                    Gia = jsonObject.optString("Gia");
                                    Imgurl = jsonObject.optString("ImgUrl");
                                    String imgname = Imgurl.substring(Imgurl.lastIndexOf("/") + 1);
//=========================================================================================================


                                    Album a = new Album(IDmon, Tenmon, Gia, imgname, Imgurl);
                                    albumList.add(a);
                                    adapter.notifyDataSetChanged();
                                    Cursor ds = db.getdata("select * from tbl_mon_app");
                                    if (ds.moveToFirst()) {
                                        while (!ds.isAfterLast()) {
                                            if (IDmon.equals(ds.getString(ds.getColumnIndex("IDMon")))) {
                                                check = true;
                                                break;
                                            }
                                            ds.moveToNext();
                                        }
                                    }
                                    if (check) {
                                        db.querydata("update tbl_mon_app set IDCH='" + IDcuahang + "', TenMon='" + Tenmon + "', Gia='" + Gia + "', ImgUrl='" + Imgurl + "', ImgLocal='" + "hismart/hinhmon/" + imgname + "' where IDMon='" + IDmon + "'");


                                    } else {
                                        db.querydata("insert into tbl_mon_app values(null,'" + IDmon + "','"

                                                + IDcuahang + "','" + Tenmon + "','" + Gia + "','" + Imgurl + "','" + "hismart/hinhmon/" + imgname + "')");


                                    }
                                    ds.close();
                                    tt = true;

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Lỗi", "Lỗi" + "\n" + error.toString());
                    }


                }) {

//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("id_ban", MainActivity.resultQR.getContents());
//                return params;
//            }
        };
        requestQueue.add(stringRequest);
// pos


        // Cursor cur = db.getdata("select * from tbl_mon_app");
//        if (cur.moveToFirst()) {
//
//            while (!cur.isAfterLast()) {
//
//                // Toast.makeText(BookActivity.this, cur.getString(cur.getColumnIndex("Gia")), Toast.LENGTH_SHORT).show();
//                ArrIDMon.add(cur.getString(cur.getColumnIndex("IDMon")));
//                cur.moveToNext();
//
//            }
//        }
//
//        if (cur.moveToFirst()) {
//
//            while (!cur.isAfterLast()) {
//
//                // Toast.makeText(BookActivity.this, cur.getString(cur.getColumnIndex("TenMon")), Toast.LENGTH_SHORT).show();
//                ArrTenmon.add(cur.getString(cur.getColumnIndex("TenMon")));
//                cur.moveToNext();
//
//            }
//        }
//
//        if (cur.moveToFirst()) {
//
//            while (!cur.isAfterLast()) {
//
//                // Toast.makeText(BookActivity.this, cur.getString(cur.getColumnIndex("Gia")), Toast.LENGTH_SHORT).show();
//                ArrGia.add(cur.getString(cur.getColumnIndex("Gia")));
//                cur.moveToNext();
//
//            }
//        }
//        if (cur.moveToFirst()) {
//
//            while (!cur.isAfterLast()) {
//
//                // Toast.makeText(BookActivity.this, cur.getString(cur.getColumnIndex("Gia")), Toast.LENGTH_SHORT).show();
//                ArrImgUrl.add(cur.getString(cur.getColumnIndex("ImgUrl")));
//                cur.moveToNext();
//                adapter.notifyDataSetChanged();
//
//            }
//        }
//
//        if (cur.moveToFirst()) {
//
//            while (!cur.isAfterLast()) {
//
//                // Toast.makeText(BookActivity.this, cur.getString(cur.getColumnIndex("Gia")), Toast.LENGTH_SHORT).show();
//                ArrImgLocal.add(cur.getString(cur.getColumnIndex("ImgLocal")));
//                cur.moveToNext();
//
//            }
//        }


//        String abc = MainActivity.resultQR.getContents();
//        Toast.makeText(this, String.valueOf(count), Toast.LENGTH_SHORT).show();
//
//        // add data to arraylist
//        for (int i = 0; i < count; i++) {
//
//            Album a = new Album(abc, ArrIDMon.get(i), ArrTenmon.get(i), ArrGia.get(i), ArrImgLocal.get(i), ArrImgUrl.get(i));
//            albumList.add(a);
//            adapter.notifyDataSetChanged();
//        }


        // cur.close();

    }


}
