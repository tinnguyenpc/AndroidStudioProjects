package ga.tindemo.test_marquee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView marque = findViewById(R.id.MarqueeText);
        Animation marquee = AnimationUtils.loadAnimation(this, R.anim.my_marquee);
        marque.startAnimation(marquee);
    }
}
