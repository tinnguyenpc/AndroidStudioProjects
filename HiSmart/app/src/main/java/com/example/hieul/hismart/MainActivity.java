package com.example.hieul.hismart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.GET;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    int y;
    ProgressDialog dialog;
    int a;
    JSONArray jsonArray;
    Location Startpoint = new Location("Startpoint");
    Location Endpoint = new Location("Endpoint");
    Db db = new Db(this);
    Button btnScasnQR, btnGoback;
    TextView txtViewVitri, txtviewResult;
    String url = "http://lengocminhhieu.ga/App/getjsonweb.php";
    String url1 = "http://lengocminhhieu.ga/App/KH_booking.php";
    String url2 = "http://lengocminhhieu.ga/App/thanhtoan.php";
    String url3 = "http://lengocminhhieu.ga/App/checkorder.php";
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    List<String> ArrCheck = new ArrayList<String>();
    List<String> ArrID_luotkhach = new ArrayList<String>();
    public static IntentResult resultQR;
    public static String IDluotkhach = "";
    private Location location;
    private GoogleApiClient gac;
    public double latitude, longitude;
    double distance = 0;
    Boolean distancetem = true, checkSCan = false;
    public String kinhdojon;
    public String vidojson;
    List<String> Arr = new ArrayList<String>();

    final IntentIntegrator intentIntegrator = new IntentIntegrator(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        db.querydata("Create table if not exists tbl_luotkhach (_ID integer primary key, ID_luotkhach integer not null)");

        txtviewResult = (TextView) findViewById(R.id.textViewResult);
        txtViewVitri = (TextView) findViewById(R.id.textViewVitri);
        btnScasnQR = (Button) findViewById(R.id.btnScan);

        // Lấy vị trí
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();

        }
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Waiting...");
        dialog.show();
//        MainActivity.BackgroundTask task = new BackgroundTask(MainActivity.this);
//        task.execute();


        if (distancetem)/*(distance <= 5000) &&*/ /*(distance != 0)*/ {

            Cursor cus = db.getdata("select * from tbl_luotkhach");
            if (cus.moveToFirst()) {

                while (!cus.isAfterLast()) {

                    // Toast.makeText(BookActivity.this, cur.getString(cur.getColumnIndex("Gia")), Toast.LENGTH_SHORT).show();
                    Arr.add(cus.getString(cus.getColumnIndex("ID_luotkhach")));
                    cus.moveToNext();

                }

            }
            cus.close();
            if (Arr.isEmpty()) {
                dialog.dismiss();
                QR_Scan();

            } else {
                dialog.dismiss();
                Intent as = new Intent(MainActivity.this, BookActivity.class);
                startActivity(as);
                finish();

            }


        } else {
            Toast.makeText(MainActivity.this, "Bạn phải đến cửa hàng để gọi món!", Toast.LENGTH_LONG).show();
        }

    }

    public void QR_Scan() {

        //intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        intentIntegrator.setPrompt(" ");
        //intentIntegrator.setCameraId(0);  // Use a specific camera of the device
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    //    // lấy vị trí
    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
            checkPlayServices();
            dialog.dismiss();
        } else {
            location = LocationServices.FusedLocationApi.getLastLocation(gac);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
//                Toast.makeText(this, latitude + " " + longitude, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Tạo đối tượng google api client
     */
    protected synchronized void buildGoogleApiClient() {

        gac = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

    }

    /**
     * Phương thức kiểm chứng google play services trên thiết bị
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1000).show();
            } else {
                Toast.makeText(this, "Thiết bị này không hỗ trợ.", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Đã kết nối với google api, lấy vị trí
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        gac.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Lỗi kết nối: " + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        checkPlayServices();
    }

    protected void onStart() {
        super.onStart();
        if (gac != null) {
           // gac.connect();

        }

    }

    protected void onStop() {
      //  gac.disconnect();
        super.onStop();
    }


    // Get the results QR:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        resultQR = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (resultQR != null) {
            if (resultQR.getContents() == null) {
//                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {

                //có kết quả quét QR
                // kiem tra ban trong
                RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url1,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response.trim().contains("id_ban-successful")) {
                                    String[] tr = response.split("[,]");
                                    IDluotkhach = tr[0];


                                    db.querydata("insert into tbl_luotkhach values(null,'" + IDluotkhach + "')");
                                    ;

                                    Cursor cus = db.getdata("select * from tbl_luotkhach");
                                    if (cus.moveToFirst()) {

                                        while (!cus.isAfterLast()) {

                                            // Toast.makeText(BookActivity.this, cur.getString(cur.getColumnIndex("Gia")), Toast.LENGTH_SHORT).show();
                                            Arr.add(cus.getString(cus.getColumnIndex("ID_luotkhach")));
                                            cus.moveToNext();

                                        }

                                    }
                                    cus.close();
                                    db.close();

                                    if (Arr.isEmpty()) {
                                        dialog.dismiss();
                                        QR_Scan();

                                    } else {
                                        dialog.dismiss();
                                        Intent as = new Intent(MainActivity.this, BookActivity.class);
                                        startActivity(as);
                                        finish();

                                    }




                                } else if (response.trim().equals("id_ban-false")) {
                                    Toast.makeText(MainActivity.this, "Bàn đã có khách vui lòng chọn bàn khác!", Toast.LENGTH_SHORT).show();
                                    QR_Scan();

                                } else if (response.trim().equals("QR-false")) {
                                    Toast.makeText(MainActivity.this, "Mã QR không hợp lệ, vui lòng quét lại!", Toast.LENGTH_SHORT).show();
                                    QR_Scan();

                                } else
                                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Lỗi kết nối máy chủ", Toast.LENGTH_SHORT).show();
                                Log.d("Lỗi", "Lỗi" + "\n" + error.toString());
                                QR_Scan();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id_ban", resultQR.getContents());

                        return params;
                    }
                };
                requestQueue1.add(stringRequest);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void Getdatajson() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("{\"AREA\":null}")) {
                            Toast.makeText(MainActivity.this, "Kiểm tra lại kết nối Internet", Toast.LENGTH_SHORT).show();
                        } else

                            try {

                                //1. Khai báo đối tượng json root object
                                JSONObject jsonRootObject = new JSONObject(response);
                                //2. Đưa jsonRootObject vào array object
                                JSONArray jsonArray = jsonRootObject.optJSONArray("AREA");
                                //3. Duyệt từng đối tượng trong Array và lấy giá trị ra
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    boolean check = false;
                                    String idjson = jsonObject.optString("ID");
                                    String TenCHjson = jsonObject.optString("TenCH");
                                    kinhdojon = jsonObject.optString("kinhdo");
                                    vidojson = jsonObject.optString("vido");

                                    Startpoint.setLatitude(latitude);
                                    Startpoint.setLongitude(longitude);
                                    Endpoint.setLatitude(Float.parseFloat(kinhdojon));
                                    Endpoint.setLongitude(Float.parseFloat(vidojson));
                                    distance = Startpoint.distanceTo(Endpoint);
                                    // Toast.makeText(MainActivity.this, String.valueOf(distance), Toast.LENGTH_SHORT).show();


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
                        Toast.makeText(MainActivity.this, "Lỗi kết nối máy chủ, kiểm tra lại kết nối internet", Toast.LENGTH_SHORT).show();
                        Log.d("Lỗi", "Lỗi" + "\n" + error.toString());
                    }
                }
        );

        requestQueue.add(stringRequest);
    }


}
