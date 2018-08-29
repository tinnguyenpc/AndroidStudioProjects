package com.example.hieul.hismart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ThanhtoanActivity extends AppCompatActivity {
    String url1 = "http://lengocminhhieu.ga/App/thanhtoan.php";
    List<String> ArrTenmonTT = new ArrayList<String>();
    List<String> ArrGiaTT = new ArrayList<String>();
    List<String> ArrDatetimeTT = new ArrayList<String>();
    List<String> ArrIDMonTT = new ArrayList<String>();
    private ArrayList<GetsetThanhtoan> productList;
    float dem = 0;
    Button xacnhan_thanhtoan;
    TextView Tongtien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanhtoan);
        xacnhan_thanhtoan = (Button) findViewById(R.id.TT_final);
        productList = new ArrayList<GetsetThanhtoan>();
        ListView lview = (ListView) findViewById(R.id.listview);
        final listviewAdapterThanhtoan adapter = new listviewAdapterThanhtoan(this, productList);
        lview.setAdapter(adapter);

        RequestQueue requestQueue1 = Volley.newRequestQueue(ThanhtoanActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            //1. Khai báo đối tượng json root object
                            JSONObject jsonRootObject = new JSONObject(response);
                            //2. Đưa jsonRootObject vào array object
                            JSONArray jsonArray = jsonRootObject.optJSONArray("ORDER");
                            //3. Duyệt từng đối tượng trong Array và lấy giá trị ra

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                boolean check = false;
                                String idmon = jsonObject.optString("ID");
                                String tenmon = jsonObject.optString("TenMon");
                                String gia = jsonObject.optString("Gia");
                                String datetime = jsonObject.optString("DateTime_Mon");
                                ArrIDMonTT.add(idmon);
                                ArrTenmonTT.add(tenmon);
                                ArrGiaTT.add(gia);
                                ArrDatetimeTT.add(datetime);
                                dem += Float.parseFloat(ArrGiaTT.get(i));
                                GetsetThanhtoan item1 = new GetsetThanhtoan(String.valueOf(i + 1), ArrTenmonTT.get(i), ArrDatetimeTT.get(i), ArrGiaTT.get(i));
                                productList.add(item1);
                                adapter.notifyDataSetChanged();
                            }
                            ;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThanhtoanActivity.this, "Lỗi kết nối máy chủ", Toast.LENGTH_SHORT).show();
                        Log.d("Lỗi", "Lỗi" + "\n" + error.toString());
                    }
                }
        );

        requestQueue1.add(stringRequest);


        Tongtien = (TextView) findViewById(R.id.tongtien);
        //  Tongtien.setText(String.valueOf(dem) + " VNĐ");

        // populateList();

//        adapter.notifyDataSetChanged();

        //onclick listview
        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String sno = ((TextView) view.findViewById(R.id.sNo)).getText().toString();
                String product = ((TextView) view.findViewById(R.id.product)).getText().toString();
                String category = ((TextView) view.findViewById(R.id.category)).getText().toString();
                String price = ((TextView) view.findViewById(R.id.price)).getText().toString();

                Toast.makeText(getApplicationContext(),
                        "Món: " + product + "\n"
                                + "Giá : " + price + " vnđ" + "\n"
                                + "Thời gian gọi : " + category
                        , Toast.LENGTH_LONG).show();
            }
        });


        xacnhan_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ThanhtoanActivity.this, "thanhtoan", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }


}