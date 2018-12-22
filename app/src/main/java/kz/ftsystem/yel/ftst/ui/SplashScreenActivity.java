package kz.ftsystem.yel.ftst.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import kz.ftsystem.yel.ftst.R;
import kz.ftsystem.yel.ftst.backend.Backend;

public class SplashScreenActivity extends Activity {
    //AppCompatActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Backend backend = new Backend(this, null);
        if (backend.isNetworkOnline()) {
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
            finish();
        } else {
            throw  new IllegalStateException(getResources().getString(R.string.no_internet));
        }
    }
}
