[![](https://jitpack.io/v/BaselHorany/ProgressStatusBar.svg)](https://jitpack.io/#BaselHorany/ProgressStatusBar)



# ProgressStatusBar
An another way to show progress. A progress View over the system StatusBar.  

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
  compile 'com.github.BaselHorany:ProgressStatusBar:1.0.0'
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

        mProgressStatusBar = new ProgressStatusBar(this); //initialize
	
	//show progress
        mProgressStatusBar.setFakeProgress(3000,true); //make fake progress from 0 to 100 in 3 sec. true/false for display percentage text.
	//or
        mProgressStatusBar.setProgress(60,false); //set progress value manually
	
	//options
	mProgressStatusBar.setProgressColor(COLOR);//default#40212121
	mProgressStatusBar.setProgressBackgroundColor(COLOR);//default transparent or colorPrimaryDark

    }
    

    @Override
    protected void onPause() {
        mProgressStatusBar.remove(); //remove progress view in case user went out before progress end
        super.onPause();
    }
    
}
```

## Important note
due to android O changes this will not work on api 27 and up so you have to check
<Enter>
if < 27 { use this } else { another progress way };

## Auther
Basel Horany 
http://baselhorany.com

