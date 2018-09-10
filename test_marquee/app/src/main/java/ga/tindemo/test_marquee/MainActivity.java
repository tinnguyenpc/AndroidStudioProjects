package ga.tindemo.test_marquee;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView marque = findViewById(R.id.MarqueeText);
        ImageView imageView = findViewById(R.id.imageView);

        Animation marquee = AnimationUtils.loadAnimation(this, R.anim.my_marquee);
        marque.startAnimation(marquee);

        String imageUrl = "http://greenspeed.vn/qrcode/api/upload/grsc_0001_2018-09-10%2010:47:48.jpg";
//        Uri imageUri = Uri.parse(imageUrl);
//        imageView.setImageURI(imageUri);
        Picasso.get().load(imageUrl).into(imageView);


    }


}
