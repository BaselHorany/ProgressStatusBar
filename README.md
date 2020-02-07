[![](https://jitpack.io/v/BaselHorany/ProgressStatusBar.svg)](https://jitpack.io/#BaselHorany/ProgressStatusBar)


# ProgressStatusBar
Another way to show progress. A progress View over the system StatusBar.
in addition to showing a toast message.

<p align="left">
The first form is suitable for showing that the activity is being loaded like fetching data from server, meanwhile the second form is better for real process.
</p>

# 1.2.0: Toast, waiting balls and percentage text has been removed due to chaotic notches on phones which will cause them to be covered at least on some devices even if added a method to position them manually and caluclating notch coordinates is a lot of work results in ugly results for a simple idea

Another way to show progress. A progress View over the system StatusBar.

<p align="center">
  <img src="https://github.com/BaselHorany/ProgressStatusBar/blob/master/showcase.gif?raw=true" />
</p>

That was for android pre-oreo 
on oreo and above statusbar will remain visible for all options like this:
<p align="center">
  <img src="https://github.com/BaselHorany/ProgressStatusBar/blob/master/showtoastoreo.png?raw=true" width="360" />
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
  compile 'com.github.BaselHorany:ProgressStatusBar:1.2.0'
}
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
        mProgressStatusBar.startFakeProgress(3000); //make fake progress from 0 to 100 in 3 sec.
	//or
        mProgressStatusBar.setProgress(60,false); //set progress value manually
	
		
	/*Addidional*/
	//options, anytime before you start a new progress 
	mProgressStatusBar.setProgressColor(COLOR);//default #40212121
	mProgressStatusBar.setProgressBackgroundColor(COLOR);//default transparent

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

}
```


## Author
Basel Horany 
[http://baselhorany.com](http://baselhorany.com)

