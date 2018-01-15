[![](https://jitpack.io/v/BaselHorany/ProgressStatusBar.svg)](https://jitpack.io/#BaselHorany/ProgressStatusBar)



# ProgressStatusBar
Another way to show progress. A progress View over the system StatusBar.  

<p align="left">
The first form is suitable for showing tat the activity is being loaded like fetching data from server, meanwhile the second form is better for real proccess and the thierd is for waitting.
</p>

<p align="center">
  <img src="https://github.com/BaselHorany/ProgressStatusBar/blob/master/showcase.gif?raw=true" />
</p>


## Setup
1- add jitpack.io repositories to you project `build.gradle`
```java 
allprojects {
	repositories {
		...
	        maven { url 'https://jitpack.io' }
	}
}
```
2-add it as a dependency to your app `build.gradle`
```java
dependencies {
  compile 'com.github.BaselHorany:ProgressStatusBar:1.0.1'
}
```
3-add SYSTEM_ALERT_WINDOW permission
```xml
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

## Usage
1- in activity
```java
public class MainActivity extends AppCompatActivity {

    ProgressStatusBar mProgressStatusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_main);
	
	//initialize
        mProgressStatusBar = new ProgressStatusBar(this); 
	
	//show progress
        mProgressStatusBar.setFakeProgress(3000,true); //make fake progress from 0 to 100 in 3 sec. true/false for display the percentage text.
	//or
        mProgressStatusBar.setProgress(60,false); //set progress value manually
	//or
        mProgressStatusBar.setWaitting(6000); //show waitting balls for 6 sec.
	
	/*Addidional*/
	//options
	mProgressStatusBar.setProgressColor(COLOR);//default #40212121
	mProgressStatusBar.setProgressBackgroundColor(COLOR);//default transparent or colorPrimaryDark
	mProgressStatusBar.setBallsColor(COLOR);//default #ffffff

	//Listener
        mProgressStatusBar.setProgressListener(new ProgressStatusBar.OnProgressListener() {
            public void onStart() {
                //ex: lock the UI or tent it
            }
            public void onUpdate(int progress) {
                //ex: simulate with another progressView
            }
            public void onEnd() {
                //ex: continue the job
            }
        });
	
    }
    

    @Override
    protected void onPause() {
        mProgressStatusBar.remove(); //remove progress view in case user went out before the progress end
        super.onPause();
    }
    
}
```

## Important note
due to android O changes this will not work on api 27 and up so you have to check

<p align="center">
if < 27 { use this } else { another progress way };
</p>

## Author
Basel Horany 
http://baselhorany.com

