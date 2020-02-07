package basel.com.ProgressStatusBarSample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.basel.ProgressStatusBar.ProgressStatusBar;

public class MainActivity extends AppCompatActivity {

    private ProgressStatusBar mProgressStatusBar;
    private int curentProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressStatusBar = new ProgressStatusBar(this);

        Button fake = findViewById(R.id.btn_fake);
        fake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressStatusBar.startFakeProgress(3000);
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
                mProgressStatusBar.setProgress(curentProgress+10);
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
    protected void onPause() {
        super.onPause();
    }


}
