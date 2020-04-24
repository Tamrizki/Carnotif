package tam.pa.carnotif;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference databaseReference, databaseReferencealarm, databaseReferencelamp;
    private FirebaseDatabase database;
    private TextView tv_number;
    private RelativeLayout relativeLayout, btn_keluar;
    private CardView cardview_layout, btn_lamp;
//    private Button btn_exit;
    boolean doubleBackToExitPressedOnce = false;
    private Animation animation, anim2;
    private ImageView img_lamp;
    private int lamp = 1;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initialization();
        anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_out);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_animation);
        cardview_layout.setAnimation(animation);

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        databaseReference = FirebaseDatabase.getInstance().getReference("jarak");
        databaseReferencealarm = FirebaseDatabase.getInstance().getReference("alarm");
        databaseReferencelamp = FirebaseDatabase.getInstance().getReference("lampu");
        database = FirebaseDatabase.getInstance();

//        database.getReference("aktifitas");
        DatabaseReference databaseReference1 = database.getReference("aktifitas");
        databaseReference1.setValue(1);
        setIconLamp();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                setBackground(value);
                tv_number.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });

        btn_keluar.setOnClickListener(this);
        btn_lamp.setOnClickListener(this);
    }

    private void setIconLamp() {
        if (lamp%2 == 0){
            databaseReferencelamp.setValue(0);
            img_lamp.setImageResource(R.drawable.lamp);
        }else {
            databaseReferencelamp.setValue(2);
            img_lamp.setImageResource(R.drawable.wheel);
        }
    }

    private void Initialization() {
        tv_number = findViewById(R.id.tv_numb);
        relativeLayout = findViewById(R.id.relative);
        cardview_layout = findViewById(R.id.cardview);
        btn_keluar = findViewById(R.id.btn_keluar);
        btn_lamp = findViewById(R.id.btn_lamp);
        img_lamp = findViewById(R.id.img_lamp);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            new CountDownTimer(10000, 10000){

                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    DatabaseReference databaseReference1 = database.getReference("aktifitas");
                    databaseReference1.setValue(0);
                }
            }.start();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik 2 kali untuk keluar.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void setBackground(String value) {
        int number = Integer.valueOf(value);
        if (number <= 30)
        {
            vibrator.vibrate(10000);
            relativeLayout.setBackgroundResource(R.drawable.bg_number_red);
            tv_number.setTextColor(getResources().getColor(R.color.colorWhite));
//            tv_number.setText(number.toString());
        }
        else if (number >30 && number <= 100)
        {
            relativeLayout.setBackgroundResource(R.drawable.bg_number_yellow);
            tv_number.setTextColor(getResources().getColor(R.color.colorWhite));
            vibrator.vibrate(500);
//            tv_number.setText(number.toString());
        }else if (number > 100 && number < 250) {
            relativeLayout.setBackgroundResource(R.drawable.bg_number_green);
            tv_number.setTextColor(getResources().getColor(R.color.colorWhite));
//            tv_number.setText(number.toString());
        }else {
            relativeLayout.setBackgroundResource(R.drawable.bg_number_white);
            tv_number.setTextColor(getResources().getColor(R.color.colorBlack));
            tv_number.setText(">250");
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btn_keluar)
        {
            databaseReferencealarm = FirebaseDatabase.getInstance().getReference("alarm");
            databaseReferencealarm.setValue(0);
            finish();
            new CountDownTimer(10000, 10000){

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    DatabaseReference databaseReference1 = database.getReference("aktifitas");
                    databaseReference1.setValue(0);
                }
            }.start();
        }else if (v == btn_lamp){
            lamp++;
            setIconLamp();
        }
    }
}