package com.example.hieul.hismart;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    JSONArray parameters;
    Button btnthanhtoan, btngoimon;
    final static boolean tt_thanhtoan = true;
    public ListView listViewMon;
    private Context ctx;
    Db db = new Db(this);
    List<String> ArrTenmon = new ArrayList<String>();
    List<String> ArrGia = new ArrayList<String>();
    List<String> ArrImgUrl = new ArrayList<String>();
    List<String> ArrIdorder = new ArrayList<String>();
    List<String> ArrIDmon_post = new ArrayList<String>();
    List<String> Arrluotkhach = new ArrayList<String>();

    List<CartGetSetListView> listtao = new ArrayList<CartGetSetListView>();
    ArrayList arr = new ArrayList();
    CartArrayAdapter myadapter;
    String url11 = "http://lengocminhhieu.ga/App/thanhtoan.php";
    String urlpost = "http://lengocminhhieu.ga/App/input-order.php";
    private String id_luotkhach_fromMain = MainActivity.IDluotkhach;
    public int y;
    Boolean check = false;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart);


        this.setFinishOnTouchOutside(true);

        btnthanhtoan = (Button) findViewById(R.id.thanhtoan);
        btngoimon = (Button) findViewById(R.id.goimon);
//        btngoimon.setVisibility(View.INVISIBLE);
        listViewMon = (ListView) findViewById(R.id.lvmon);


        Cursor cus = db.getdata("select * from tbl_luotkhach");
        if (cus.moveToFirst()) {

            while (!cus.isAfterLast()) {

                // Toast.makeText(BookActivity.this, cur.getString(cur.getColumnIndex("Gia")), Toast.LENGTH_SHORT).show();
                Arrluotkhach.add(cus.getString(cus.getColumnIndex("ID_luotkhach")));
                cus.moveToNext();

            }

        }
        cus.close();


        btngoimon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cu = db.getdata("select * from tbl_order");
                final int dem = cu.getCount();
                if (dem != 0) {
                    if (cu.moveToFirst()) {
                        while (!cu.isAfterLast()) {
                            // Toast.makeText(BookActivity.this, cur.getString(cur.getColumnIndex("Gia")), Toast.LENGTH_SHORT).show();
                            ArrIDmon_post.add(cu.getString(cu.getColumnIndex("IDmon_book")));
                            final String idmon_s = cu.getString(cu.getColumnIndex("IDmon_book"));
                            RequestQueue queue = Volley.newRequestQueue(CartActivity.this);
                            StringRequest postRequest = new StringRequest(Request.Method.POST, urlpost,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // response
                                            // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                                            if (response.trim().equals("order-true")) {
                                                db.delete_table("tbl_order");
                                                db.close();
                                                load_cart();
                                                finish();
                                                // DisplayOrder();
                                                Toast.makeText(ctx, "Gọi món thành công", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                            ) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("id_luot", Arrluotkhach.get(0));
                                    params.put("id_mon", idmon_s);

                                    return params;
                                }
                            };
                            queue.add(postRequest);

                            cu.moveToNext();


                        }

                    }

                } else {
                    Toast.makeText(ctx, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();

                }


                cu.close();

            }
        });


        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundTask task = new BackgroundTask(CartActivity.this);
                task.execute();


                if (check) {

                    check = false;
                }
            }
        });
        // load data from table order
        load_cart();
// hien thi danh sach order
        DisplayOrder();

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void load_cart() {


        ctx = this;
        db.querydata("Create table if not exists tbl_order (ID_ integer primary key, IDCH_book integer not null, IDmon_book integer not null, TT_tt text not null)");
        Cursor curs = db.getdata("select * from tbl_order, tbl_mon_app where tbl_order.IDmon_book = tbl_mon_app.IDMon");
        int cc = curs.getCount();
        if (curs.moveToFirst()) {

            while (!curs.isAfterLast()) {

                // Toast.makeText(BookActivity.this, cur.getString(cur.getColumnIndex("TenMon")), Toast.LENGTH_SHORT).show();
                ArrImgUrl.add(curs.getString(curs.getColumnIndex("ImgUrl")));
                curs.moveToNext();

            }
        }
        if (curs.moveToFirst()) {

            while (!curs.isAfterLast()) {

                // Toast.makeText(BookActivity.this, cur.getString(cur.getColumnIndex("TenMon")), Toast.LENGTH_SHORT).show();
                ArrTenmon.add(curs.getString(curs.getColumnIndex("TenMon")));
                ArrIdorder.add(curs.getString(curs.getColumnIndex("ID_")));
                curs.moveToNext();

            }
        }
        if (curs.moveToFirst()) {

            while (!curs.isAfterLast()) {

                // Toast.makeText(BookActivity.this, cur.getString(cur.getColumnIndex("TenMon")), Toast.LENGTH_SHORT).show();
                ArrGia.add(curs.getString(curs.getColumnIndex("Gia")));
                curs.moveToNext();

            }
        }

        for (int i = 0; i < cc; i++) {

            listtao.add(new CartGetSetListView(ArrImgUrl.get(i), ArrTenmon.get(i), ArrIdorder.get(i), ArrGia.get(i), "delete1"));
            // myadapter.setNotifyOnChange(true);
        }
        curs.close();
        db.close();
    }

    public void DisplayOrder() {

        myadapter = new CartArrayAdapter(ctx, R.layout.single_list_cart, listtao);
        listViewMon.setAdapter(myadapter);

        listViewMon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public String variable, idorder;

            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                CartGetSetListView o = (CartGetSetListView) adapterView.getItemAtPosition(position);
                variable = o.getTenmon();
                idorder = o.getIdmon();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        CartActivity.this);
                // Setting Dialog Title
                alertDialog.setTitle("XÓA MÓN");
                // Setting Dialog Message
                alertDialog.setMessage("Bạn có muốn xóa " + "[" + variable + "]" + " khỏi giỏ hàng?");
                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.warning);
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                listtao.remove(position);
                                myadapter.notifyDataSetChanged();
                                db.delete_row("tbl_order", "ID_", idorder);
                                Toast.makeText(ctx, "Đã xóa", Toast.LENGTH_SHORT).show();
                            }
                        });
                // Showing Alert Message
                alertDialog.show();
            }
        });
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Integer> {


        public BackgroundTask(CartActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Just a moment!");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }


            if (y > 0) {
                dialog.dismiss();
                db.querydata("Create table if not exists tbl_order (ID_ integer primary key, IDCH_book integer not null, IDmon_book integer not null, TT_tt text not null)");
                Cursor c = db.getdata("select * from tbl_order");
                int co = c.getCount();

                if (co == 0) {
                    finish();
                    Intent m = new Intent(CartActivity.this, ThanhtoanActivity.class);
                    startActivity(m);
                } else {
                    Toast.makeText(ctx, "Bạn phải gọi món hoặc xóa hết món trong giỏ hàng trước khi thanh toán ", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
                c.close();
            } else {
                BackgroundTask task1 = new BackgroundTask(CartActivity.this);
                task1.execute();

            }
        }

        @Override
        protected Integer doInBackground(final Void... params) {

            RequestQueue requestQueue11 = Volley.newRequestQueue(CartActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url11,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {

                                //1. Khai báo đối tượng json root object
                                JSONObject jsonRootObject = new JSONObject(response);
                                //2. Đưa jsonRootObject vào array object
                                JSONArray jsonArray = jsonRootObject.optJSONArray("ORDER");
                                //3. Duyệt từng đối tượng trong Array và lấy giá trị ra
                                y = jsonArray.length();
                                if (y == 0)
                                    check = true;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CartActivity.this, "Lỗi kết nối máy chủ", Toast.LENGTH_SHORT).show();
                            Log.d("Lỗi", "Lỗi" + "\n" + error.toString());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_luot_TT", Arrluotkhach.get(0));


                    return params;
                }
            };

            requestQueue11.add(stringRequest);

//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            publishProgress(params);
            return y;
        }

    }


    private class BackgroundTask1 extends AsyncTask<Void, Void, Integer> {


        public BackgroundTask1(CartActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Just a moment!");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }


            if (y > 0) {
                dialog.dismiss();
                db.querydata("Create table if not exists tbl_order (ID_ integer primary key, IDCH_book integer not null, IDmon_book integer not null, TT_tt text not null)");
                Cursor c = db.getdata("select * from tbl_order");
                int co = c.getCount();

                if (co == 0) {
                    Intent m = new Intent(CartActivity.this, ThanhtoanActivity.class);
                    startActivity(m);
                    finish();
                } else {
                    Toast.makeText(ctx, "Bạn phải gọi món hoặc xóa hết món trong giỏ hàng trước khi thanh toán ", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                c.close();
            } else {
                Toast.makeText(ctx, "Chọn món trước khi thanh toán", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Integer doInBackground(final Void... params) {

            RequestQueue requestQueue11 = Volley.newRequestQueue(CartActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url11,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {

                                //1. Khai báo đối tượng json root object
                                JSONObject jsonRootObject = new JSONObject(response);
                                //2. Đưa jsonRootObject vào array object
                                JSONArray jsonArray = jsonRootObject.optJSONArray("ORDER");
                                //3. Duyệt từng đối tượng trong Array và lấy giá trị ra
                                y = jsonArray.length();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CartActivity.this, "Lỗi kết nối máy chủ", Toast.LENGTH_SHORT).show();
                            Log.d("Lỗi", "Lỗi" + "\n" + error.toString());
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_luot_TT", Arrluotkhach.get(0));


                    return params;
                }
            };

            requestQueue11.add(stringRequest);

//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            publishProgress(params);
            return y;
        }

    }
}