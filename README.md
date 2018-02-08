[![](https://jitpack.io/v/BaselHorany/ProgressStatusBar.svg)](https://jitpack.io/#BaselHorany/ProgressStatusBar)


# ProgressStatusBar
Another way to show progress. A progress View over the system StatusBar.
in addition to showing a toast message.

<p align="left">
The first form is suitable for showing that the activity is being loaded like fetching data from server, meanwhile the second form is better for real process and the third is for waiting.
</p>

<p align="center">
  <img src="https://github.com/BaselHorany/ProgressStatusBar/blob/master/showcase.gif?raw=true" />
</p>

<p align="center">
  <img src="https://github.com/BaselHorany/ProgressStatusBar/blob/master/showtoast.png?raw=true" width="360" />
</p>


## Setup
1- Add jitpack.io repositories to you project `build.gradle`
```groovy 
allprojects {
	repositories {
	    maven { url 'https://jitpack.io' }
	}
}
```
2- Add it as a dependency to your app `build.gradle`
```groovy
dependencies {
  compile 'com.github.BaselHorany:ProgressStatusBar:1.1.4'
}
```
3- Add `SYSTEM_ALERT_WINDOW` permission
```xml
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

## Usage
1- In your Activity class

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
        mProgressStatusBar.setWaiting(6000); //show waitting balls for 6 sec.
	
	//show toast message
	mProgressStatusBar.showToast("1 new message", 3000); //(Sting message, int duratoion)
		
	/*Addidional*/
	//options, anytime before you start a new progress 
	mProgressStatusBar.setProgressColor(COLOR);//default #40212121
	mProgressStatusBar.setProgressBackgroundColor(COLOR);//default transparent or colorPrimaryDark
	mProgressStatusBar.setBallsColor(COLOR);//default #ffffff
	mProgressStatusBar.setTextColor(COLOR);//default #ffffff

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

## Important Note
Due to Android O changes, this will not work on API 27 and above so you have to check:

```java
if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
    // use this library 
} else { 
    // another progress way 
}
```


## Author
Basel Horany 
[http://baselhorany.com](http://baselhorany.com)

