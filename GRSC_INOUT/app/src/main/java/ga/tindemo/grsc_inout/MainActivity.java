package ga.tindemo.grsc_inout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    String gettimeserver = "http://greenspeed.vn/qrcode/api/servertime.php";
    String getdateserver = "http://greenspeed.vn/qrcode/api/serverdate.php";
    String pushdatae = "http://greenspeed.vn/qrcode/api/input_data.php";
    String folder_cam = "";
    File filecam;
    Uri imageUri;
    String t_date="";
    String t_time="";

    Bitmap selectedBitmap;


    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static int SPLASH_TIME_OUT = 2000;
    private String TAG = "tag";
    private Database DB;
//    private Context m_app;
    private final String tb_qrcode="tb_qrcode";
    private final String tb_data= "tb_data";
    final IntentIntegrator intentIntegrator = new IntentIntegrator(this);
    public static IntentResult resultQR;

    private Integer t_mode;
    String filename = "";
    String t_qrcode = "";

    Button bt_in;
    Button bt_out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        bt_in = findViewById(R.id.bt_in);
        bt_out = findViewById(R.id.bt_out);

        folder_cam = Environment.getExternalStorageDirectory()+"/GRSC_INOUT";
        filecam = new File(folder_cam,filename);
        if(checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity

//                    Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
//                    startActivity(i);
//
//                    // close this activity
//                    finish();
                    if (!dir_exists(folder_cam)){
                        File directory = new File(folder_cam);
                        directory.mkdirs();
                    }

                    if (dir_exists(folder_cam)){
                        // 'Dir exists'
                    }else{
// Display Errormessage 'Dir could not creat!!'
                    }
                }
            }, SPLASH_TIME_OUT);
        }



        bt_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t_mode=0;
                QR_Scan();
            }
        });

        bt_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t_mode=1;
                QR_Scan();
            }
        });




//        Calendar c = Calendar.getInstance();
//        System.out.println("Current time => " + c.getTime());
//        c.set(2013, (12 - 1), 05);
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssS");
//        String formattedDate = df.format(c.getTime());
//        Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();




//        // formattedDate have current date/time
//        Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();

//        long time= System.currentTimeMillis();

        // Now we display formattedDate value in TextView
//        TextView txtView = new TextView(this);
//        txtView.setText("Current Date and Time : "+formattedDate);
//        txtView.setGravity(Gravity.CENTER);
//        txtView.setTextSize(20);
//        setContentView(txtView);

        initDataBase();




    }
        void initDataBase() {
        DB = new Database(this);
        DB.create_table(tb_qrcode);
        DB.add_col(tb_qrcode,"code","text");
        DB.add_col(tb_qrcode,"name","text");
        DB.add_col(tb_qrcode,"active","integer  DEFAULT 0");
        DB.add_col(tb_qrcode,"session_app","text  DEFAULT NULL");

        Cursor c_qrcode = DB.loaddata(tb_qrcode,null,null);
        int count_qrcode = c_qrcode.getCount();
        if(count_qrcode==0){
            DB.insterdata(tb_qrcode,"'grsc_0001','Thẻ số 1', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0002','Thẻ số 2', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0003','Thẻ số 3', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0004','Thẻ số 4', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0005','Thẻ số 5', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0006','Thẻ số 6', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0007','Thẻ số 7', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0008','Thẻ số 8', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0009','Thẻ số 9', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0010','Thẻ số 10', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0011','Thẻ số 11', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0012','Thẻ số 12', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0013','Thẻ số 13', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0014','Thẻ số 14', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0015','Thẻ số 15', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0016','Thẻ số 16', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0017','Thẻ số 17', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0018','Thẻ số 18', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0019','Thẻ số 19', 0, null");
            DB.insterdata(tb_qrcode,"'grsc_0020','Thẻ số 20', 0, null");
        }
        c_qrcode.close();

        DB.create_table(tb_data);
        DB.add_col(tb_data,"session_app","text");
        DB.add_col(tb_data,"qr_code","text");
        DB.add_col(tb_data,"path_img","text");
        DB.add_col(tb_data,"out_true","text");
    }

    public void QR_Scan() {

//        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        intentIntegrator.setPrompt(" t");
        intentIntegrator.setCameraId(0);  // Use a specific camera of the device
//        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();

    }

    public void handleResult(Result rawResult) {
        // Do something with the result here</p>
//        Log.e(&quot;handler&quot;, rawResult.getText()); // Prints scan results
//        Log.e(&quot;handler&quot;, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        // show the scanner result into dialog box.<br />
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle(&quot;Scan Result&quot;);
                builder.setMessage(rawResult.getText());
                AlertDialog alert1 = builder.create();
                alert1.show();
       // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);
    }

    // Get the results QR:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        resultQR = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (resultQR != null) {
            if (resultQR.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                t_qrcode =resultQR.getContents();
                Cursor c_qrcode = DB.loaddata(tb_qrcode,null,"code='"+t_qrcode+"'");
                Toast.makeText(this, resultQR.getContents(), Toast.LENGTH_LONG).show();

                int count_qrcode = c_qrcode.getCount();
                if(count_qrcode==0){
                    Toast.makeText(this, "Hệ thống không chấp nhận mã QR này", Toast.LENGTH_LONG).show();
                }
                else{
//                    Toast.makeText(this, "Mode="+t_mode+": "+t_qrcode, Toast.LENGTH_LONG).show();
                    String name_card= "";
                    if (c_qrcode.moveToFirst()) {
                        while (!c_qrcode.isAfterLast()) {
                            name_card = c_qrcode.getString(c_qrcode.getColumnIndex("name"));
                            c_qrcode.moveToNext();
                        }
                    }
                    Toast.makeText(this, name_card, Toast.LENGTH_LONG).show();

                    if(t_mode==0){
                        Integer t_active=0;
                        if (c_qrcode.moveToFirst()) {
                            while (!c_qrcode.isAfterLast()) {
                                t_active = Integer.parseInt(c_qrcode.getString(c_qrcode.getColumnIndex("active")));
                                c_qrcode.moveToNext();
                            }
                        }
                        if(t_active==1){
                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(this);
                            }
                            builder.setTitle("Thẻ đã đăng ký")
                                    .setMessage("Thẻ này đang đăng ký, Vui lòng chọn thẻ khác hoặc thực hiện kiểm tra Ra cổng để hoàn tất việc trả thẻ này để tiếp tục sử dụng!")
                                    .setPositiveButton("Chọn thẻ khác", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setNegativeButton("Chuyển sang quét Ra cổng", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            t_mode = 1;
                                            QR_Scan();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                           getdatetimeserver();
                            if((t_date=="")|(t_time=="")){
                                Calendar c = Calendar.getInstance();
                                System.out.println("Current time => " + c.getTime());
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                t_time = df.format(c.getTime());
                                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                                t_date = df2.format(c.getTime());
                            }
                            filename = t_qrcode+"_"+t_time+".jpg";

                            folder_cam = Environment.getExternalStorageDirectory()+"/GRSC_INOUT";
                            folder_cam = folder_cam+"/"+t_date;
                            if (!dir_exists(folder_cam)){
                                File directory = new File(folder_cam);
                                directory.mkdirs();
                            }

                            if (dir_exists(folder_cam)){
                                // 'Dir exists'
                            }else{
// Display Errormessage 'Dir could not creat!!'
                            }
                            filecam = new File(folder_cam,filename);
//                Toast.makeText(this, filename+"   ----    "+filecam, Toast.LENGTH_LONG).show();
                            Log.d(TAG, filename+"   ----    "+filecam);
                            capturePicture();
                        }
                    }
                    else {
                        Integer t_active=0;
                        String t_session_app="";
                        String t_image = "";
                        if (c_qrcode.moveToFirst()) {
                            while (!c_qrcode.isAfterLast()) {
                                t_active = Integer.parseInt(c_qrcode.getString(c_qrcode.getColumnIndex("active")));
                                t_session_app = c_qrcode.getString(c_qrcode.getColumnIndex("session_app"));
                                c_qrcode.moveToNext();
                            }
                        }
                        if(t_active==0){
                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(this);
                            }
                            builder.setTitle("Thẻ chưa đăng ký")
                                    .setMessage("Thẻ này chưa được đăng ký, Vui lòng chọn thẻ khác hoặc thực hiện đăng ký Vào cổng cho thẻ này!")
                                    .setPositiveButton("Chọn thẻ khác", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setNegativeButton("Chuyển sang đăng ký vào cổng", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            t_mode = 0;
                                            QR_Scan();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            getdatetimeserver();
                            if((t_date=="")|(t_time=="")){
                                Calendar c = Calendar.getInstance();
                                System.out.println("Current time => " + c.getTime());
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                t_time = df.format(c.getTime());
                                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                                t_date = df2.format(c.getTime());
                            }

                            Cursor c_data = DB.loaddata(tb_data,null,"session_app='"+t_session_app+"'");
                            int count_data = c_data.getCount();
                            if(count_data==0){
                                Toast.makeText(this, "Lỗi không xác định", Toast.LENGTH_LONG).show();
                            }
                            else {
                                if (c_data.moveToFirst()) {
                                    while (!c_data.isAfterLast()) {
                                        t_image = c_data.getString(3);
                                        c_data.moveToNext();
                                    }
                                }
                                showImage(t_image);
                                DB.updatedata(tb_data,"out_true='"+t_time+"'","session_app='"+t_session_app+"'");
                                DB.updatedata(tb_qrcode,"active=0, session_app=null","session_app='"+t_session_app+"' AND code='"+t_qrcode+"'");
                            }

                        }
                    }


                }
                c_qrcode.close();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        if(requestCode != 100 || filename == null)
            return;
//        Toast.makeText(this, filecam.toString(), Toast.LENGTH_SHORT).show();
        if(filecam.exists()){
            Toast.makeText(this, "Lưu hình ảnh thành công", Toast.LENGTH_SHORT).show();
            DB.updatedata(tb_qrcode,"active=1"+",session_app='"+t_time+"'","code='"+t_qrcode+"'");
            DB.insterdata(tb_data,"'"+t_time+"','"+t_qrcode+"','"+filecam+"','"+0+"'");
            selectedBitmap = BitmapFactory.decodeFile(filecam.getAbsolutePath());
            uploadPictureToServer(pushdatae,t_qrcode,t_time,filename);
        }
        else{
            Toast.makeText(this, "Lưu hình ảnh thất bại", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }

            if(t_qrcode!=""){
                builder.setTitle("Huỷ đăng ký thẻ")
                        .setMessage("Quá trình đăng ký vào thẻ đã bị bỏ qua, thẻ vừa quét vẫn đang trống dữ liệu.")
                        .setPositiveButton("Tôi biết rồi", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setNegativeButton("Chụp hình lại", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                capturePicture();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }


    private void  getdatetimeserver(){
        RequestQueue t_request = Volley.newRequestQueue(this);
        StringRequest t_srequest = new StringRequest(Request.Method.GET, getdateserver,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                            Toast.makeText(MainActivity.this, "web: "+response, Toast.LENGTH_SHORT).show();
                        t_date = response.trim();
                        Log.d(TAG, "My t_date: "+t_date);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity.this, "Lỗi kết nối máy chủ", Toast.LENGTH_SHORT).show();
                        Log.d("Lỗi", "Lỗi" + "\n" + error.toString());
                   }
                }
        );
        t_request.add(t_srequest);

        RequestQueue t_request2 = Volley.newRequestQueue(this);
        StringRequest t_srequest2 = new StringRequest(Request.Method.GET, gettimeserver,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                            Toast.makeText(MainActivity.this, "web: "+response, Toast.LENGTH_SHORT).show();
                        t_time = response.trim();
                        Log.d(TAG, "My t_date: "+t_time);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity.this, "Lỗi kết nối máy chủ", Toast.LENGTH_SHORT).show();
                        Log.d("Lỗi", "Lỗi" + "\n" + error.toString());
                    }
                }
        );
        t_request2.add(t_srequest2);

    }

    private void capturePicture() {
// Kiểm tra Camera trong thiết bị
        if (this.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
// Mở camera mặc định
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filecam));

// Tiến hành gọi Capture Image intent
            startActivityForResult(intent, 100);
        } else {
            Toast.makeText(this, "Camera is not support on this device", Toast.LENGTH_LONG).show();
        }
    }


    public void showImage(String t_path) {
        imageUri = Uri.parse(t_path);
//        Toast.makeText(this, t_path, Toast.LENGTH_LONG).show();
//        Toast.makeText(this, imageUri.toString(), Toast.LENGTH_LONG).show();
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageURI(imageUri);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

//        ImageView imageView = new ImageView(this);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//
//        params.height = getDeviceHeight(this);
//        params.width = getDeviceWidth(this);
//        FrameLayout framelayout = new FrameLayout(this);

//        framelayout.addView(imageView, 0, params);
        builder.show();
    }

    @SuppressLint("NewApi")
    public static int getDeviceWidth(Activity activity) {
        int deviceWidth = 0;

        Point size = new Point();
        WindowManager windowManager = activity.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            windowManager.getDefaultDisplay().getSize(size);
            deviceWidth = size.x;
        } else {
            Display display = windowManager.getDefaultDisplay();
            deviceWidth = display.getWidth();
        }
        return deviceWidth;
    }

    @SuppressLint("NewApi")
    public static int getDeviceHeight(Activity activity) {
        int deviceHeight = 0;

        Point size = new Point();
        WindowManager windowManager = activity.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            windowManager.getDefaultDisplay().getSize(size);
            deviceHeight = size.y;
        } else {
            Display display = windowManager.getDefaultDisplay();
            deviceHeight = display.getHeight();
        }
        return deviceHeight;
    }

    public boolean dir_exists(String dir_path)
    {
        boolean ret = false;
        File dir = new File(dir_path);
        if(dir.exists() && dir.isDirectory())
            ret = true;
        return ret;
    }




    private  boolean checkAndRequestPermissions() {
        int camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int readpermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int permissionLocation = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
//        int permissionRecordAudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);


        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
//        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }
        if (readpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
//                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
                        Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
//                        Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
//                        startActivity(i);
//                        finish();


                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                            showDialogOK("Service Permissions are required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?");
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
    private void explain(String msg){
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        dialog.setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  permissionsclass.requestPermission(type,code);
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.exampledemo.parsaniahardik.marshmallowpermission")));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }

    public class UploadToServerTask extends AsyncTask<Void, Void, String> {

        //URL để tải hình lên server
        private Activity context=null;
        private ProgressDialog progressDialog=null;
        private String tt_base64;
        private String tt_qr_code;
        private String tt_session_app;
        private String tt_img_name;

        public UploadToServerTask(Activity context, String t_basae64, String qr_code, String session_app, String img_name)
        {
            this.context=context;
            this.tt_base64=t_basae64;
            this.tt_qr_code=qr_code;
            this.tt_session_app=session_app;
            this.tt_img_name=img_name;
            this.progressDialog=new ProgressDialog(this.context);
        }
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog.setMessage("Vui lòng chờ hệ thống đang upload hình!");
            this.progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
//Coding gửi hình lên Server
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("img_base64", tt_base64));
//            nameValuePairs.add(new BasicNameValuePair("img_name", System.currentTimeMillis() + ".jpg"));
            nameValuePairs.add(new BasicNameValuePair("img_name", tt_img_name));
            nameValuePairs.add(new BasicNameValuePair("qr_code", tt_qr_code));
            nameValuePairs.add(new BasicNameValuePair("session_app", tt_session_app));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(pushdatae);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                String st = EntityUtils.toString(response.getEntity());
                Log.v("log_tag", "In the try Loop" + st);

            } catch (Exception e) {
                Log.v("log_tag", "Lỗi kết nối : " + e.toString());
            }
            return null;

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            this.progressDialog.hide();
            this.progressDialog.dismiss();
        }
    }


    /**
     * Hàm xử lý lấy encode hình để gửi lên Server
     */
    private void uploadPictureToServer(String t_base64, String qr_code, String session_app, String img_name) {
//        Log.e("path", "----------------" + picturePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bao);
        byte[] ba = bao.toByteArray();
        t_base64 = Base64.encodeToString(ba, Base64.DEFAULT);

        Log.e("base64", "-----" + t_base64);

// Upload hình  lên serverString
        UploadToServerTask uploadToServer=new UploadToServerTask(this,t_base64, qr_code, session_app, img_name);
        uploadToServer.execute();
    }


}
