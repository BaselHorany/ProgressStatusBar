package basel.com.ProgressStatusBarSample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;


import com.basel.ProgressStatusBar.ProgressStatusBar;



public class MainActivity extends AppCompatActivity {

    ProgressStatusBar mProgressStatusBar;
    CheckBox isShowPer;
    int curentProgress = 0;
    
    //overlay permission only if above Oreo
    @SuppressLint("NewApi")
    public void checkDrawOverlayPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 11);
            }
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressStatusBar = new ProgressStatusBar(this);

        isShowPer = findViewById(R.id.isShowPer);

        Button fake = findViewById(R.id.btn_fake);
        fake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressStatusBar.setFakeProgress(3000,isShowPer.isChecked());
            }
        });

        Button handled = findViewById(R.id.handled);
        handled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curentProgress<100){
                    curentProgress = curentProgress+10;
                }else{
                    curentProgress = 0;
                }
                mProgressStatusBar.setProgress(curentProgress+10,isShowPer.isChecked());
            }
        });


        Button wait = findViewById(R.id.btn_wait);
        wait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressStatusBar.setWaiting(6000);
            }
        });
        
        Button toast = findViewById(R.id.toast);
        toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressStatusBar.showToast("1 new message",3000);
            }
        });

        mProgressStatusBar.setProgressListener(new ProgressStatusBar.OnProgressListener() {
            public void onStart() {
                //ex: lock the UI or tent it
            }
            public void onUpdate(int progress) {
                //ex: simulate with another progressView
            }
            public void onEnd() {
                //ex: continue the jop
            }
        });


    }
    
    @Override
    protected void onResume() {
        super.onResume();
        //!important;
        checkDrawOverlayPermission();
    }

    @Override
    protected void onPause() {
        mProgressStatusBar.remove();
        super.onPause();
    }
}
