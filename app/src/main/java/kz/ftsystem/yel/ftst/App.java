package kz.ftsystem.yel.ftst;

import android.app.Activity;
import android.app.Application;
import android.arch.persistence.room.Room;
import android.os.Bundle;

import com.facebook.stetho.Stetho;

public class App extends Application implements Application.ActivityLifecycleCallbacks {

    public static App instance;

    private int activityCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

        // Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this)
        );

        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);

        registerActivityLifecycleCallbacks(this);
    }

    public static App getInstance() {
        return instance;
    }

    public boolean isAppForeground() {
        return activityCount > 0;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        activityCount++;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        activityCount--;
    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }
}