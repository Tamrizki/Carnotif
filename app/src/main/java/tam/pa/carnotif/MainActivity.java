package tam.pa.carnotif;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
    private RelativeLayout relativeLayout;
    private CardView cardview_layout;
    private Button btn_exit;
    boolean doubleBackToExitPressedOnce = false;
    private Animation animation, anim2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initialization();
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_animation);
        animation.setRepeatCount(Animation.INFINITE);
        cardview_layout.setAnimation(animation);

        databaseReference = FirebaseDatabase.getInstance().getReference("jarak");
        databaseReferencealarm = FirebaseDatabase.getInstance().getReference("alarm");
        databaseReferencelamp = FirebaseDatabase.getInstance().getReference("lampu");
        database = FirebaseDatabase.getInstance();

        database.getReference("aktifitas");
        DatabaseReference databaseReference1 = database.getReference("aktifitas");
        databaseReference1.setValue(1);

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

        btn_exit.setOnClickListener(this);
    }

    private void Initialization() {
        tv_number = findViewById(R.id.tv_numb);
        relativeLayout = findViewById(R.id.relative);
        cardview_layout = findViewById(R.id.cardview);
        btn_exit = findViewById(R.id.btn_exit);
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
                    database.getReference("aktifitas");
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
        Double number = Double.valueOf(value);
        if (number <= 30)
        {
            relativeLayout.setBackgroundResource(R.drawable.bg_number_red);
            tv_number.setTextColor(getResources().getColor(R.color.colorWhite));
            tv_number.setText(number.toString());
        }
        else if (number >30 && number <= 100)
        {
            relativeLayout.setBackgroundResource(R.drawable.bg_number_yellow);
            tv_number.setTextColor(getResources().getColor(R.color.colorWhite));
            tv_number.setText(number.toString());
        }else if (number > 50 && number < 200)
        {
            relativeLayout.setBackgroundResource(R.drawable.bg_number_green);
            tv_number.setTextColor(getResources().getColor(R.color.colorWhite));
            tv_number.setText(number.toString());
        }else {
            relativeLayout.setBackgroundResource(R.drawable.bg_number_white);
            tv_number.setTextColor(getResources().getColor(R.color.colorBlack));
            tv_number.setText(">200");
        }

    }

    @Override
    public void onClick(View v) {
//        if (v == cv_alarm)
//        {database.getReference("alarm");
//            DatabaseReference databaseReference1 = database.getReference("alarm");
//            if (bol_alarm  == false){
//                databaseReference1.setValue(1);
//            }else {
//                databaseReference1.setValue(0);
//            }
//
//        }
//        else if (v == cv_lamp)
//        {
//            database.getReference("lampu");
//            DatabaseReference databaseReference1 = database.getReference("lampu");
//            if (bol_lamp == false){
//                databaseReference1.setValue(1);
//            }else {
//                databaseReference1.setValue(0);
//            }
//
//        }
        if (v == btn_exit)
        {
            finish();

            new CountDownTimer(10000, 10000){

                @Override
                public void onTick(long millisUntilFinished) {
                    Toast.makeText(MainActivity.this, "On Tick...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {
                    database.getReference("aktifitas");
                    DatabaseReference databaseReference1 = database.getReference("aktifitas");
                    databaseReference1.setValue(0);
                }
            }.start();
        }
    }
}