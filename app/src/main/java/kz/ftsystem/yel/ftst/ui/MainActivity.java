package kz.ftsystem.yel.ftst.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.List;

import kz.ftsystem.yel.ftst.Interfaces.MyCallback;
import kz.ftsystem.yel.ftst.R;
import kz.ftsystem.yel.ftst.backend.Backend;
import kz.ftsystem.yel.ftst.backend.MyConstants;
import kz.ftsystem.yel.ftst.backend.Order;
import kz.ftsystem.yel.ftst.fragments.AcceptedOrdersFragment;
import kz.ftsystem.yel.ftst.fragments.NewOrdersFragment;

public class MainActivity extends AppCompatActivity implements MyCallback {


    SectionsPagerAdapter sectionsPagerAdapter;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startService(new Intent(this, MyFirebaseMessagingService.class));

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Log.d(MyConstants.TAG, "TOKEN: " + token);
                SharedPreferences preferences = getApplicationContext().getSharedPreferences(MyConstants.APP_PREFERENCES, Context.MODE_PRIVATE);
                String myId = preferences.getString(MyConstants.PREFERENCE_MY_ID, "");
                String myToken = preferences.getString(MyConstants.PREFERENCE_MY_TOKEN, "");
                Backend backend = new Backend(MainActivity.this, MainActivity.this);
                backend.sendToken(myId, myToken, token);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewpager_container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                return true;
            case R.id.menu_exit:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void fromBackend(HashMap<String, String> data, List<Order> orders) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new NewOrdersFragment();
                case 1:
                    return new AcceptedOrdersFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

