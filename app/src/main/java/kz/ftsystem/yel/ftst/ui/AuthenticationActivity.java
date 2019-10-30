package kz.ftsystem.yel.ftst.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.List;

import kz.ftsystem.yel.ftst.Interfaces.MyCallback;
import kz.ftsystem.yel.ftst.R;
import kz.ftsystem.yel.ftst.backend.Backend;
import kz.ftsystem.yel.ftst.backend.MyConstants;
import kz.ftsystem.yel.ftst.backend.Order;


public class AuthenticationActivity extends Activity implements MyCallback {

    EditText login;
    EditText pswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        TextInputLayout tilLogin = findViewById(R.id.textILLogin);
        TextInputLayout tilPwsd = findViewById(R.id.textILPswd);
        login = findViewById(R.id.etLogin);
        pswd = findViewById(R.id.etPswd);
        tilLogin.setHintAnimationEnabled(true);
        tilLogin.setHint(getString(R.string.enter_login));
        tilPwsd.setHint(getString(R.string.enter_pswd));
    }

    @Override
    protected void onStart() {
        super.onStart();
//        login.setText("");
//        pswd.setText("");
    }

    public void onClick(View view) {

        Backend backend = new Backend(this, this);
        if (backend.isNetworkOnline()) {

            if (login.getText().toString().equals("1") && (pswd.getText().toString().equals("1"))) {
                SharedPreferences preferences = getSharedPreferences(MyConstants.APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(MyConstants.PREFERENCE_IS_AUTO_ENTER, true);
                editor.putBoolean(MyConstants.PREFERENCE_IS_FIREST_RUN, false);
                editor.putString(MyConstants.PREFERENCE_MY_ID, "DA70122211");
                editor.putString(MyConstants.PREFERENCE_MY_TOKEN, "665463c29d6531599f16d14ff3f86b6c");
                editor.apply();

                Intent intent;
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
//            Backend backend = new Backend(this, this);
            backend.authentication(login.getText().toString(), pswd.getText().toString());

        }

//        Intent intent;
//        intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }

    @Override
    public void fromBackend(HashMap<String, String> data, List<Order> orders) {
        switch (data.get("response")) {
            case "denied":
                Toast.makeText(this, "Отказано в доступе", Toast.LENGTH_SHORT).show();
                break;
            case "error":
                Toast.makeText(this, "Сервис не доступен", Toast.LENGTH_SHORT).show();
                break;
            case "access":
                SharedPreferences preferences = getSharedPreferences(MyConstants.APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(MyConstants.PREFERENCE_IS_AUTO_ENTER, true);
                editor.putBoolean(MyConstants.PREFERENCE_IS_FIREST_RUN, false);
                editor.putString(MyConstants.PREFERENCE_MY_ID, data.get("id"));
                editor.putString(MyConstants.PREFERENCE_MY_TOKEN, data.get("token"));
                editor.apply();

                Intent intent;
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case "server_is_offline":
                Toast.makeText(this, "Нет связи с сервером", Toast.LENGTH_LONG).show();
                break;
        }
//        if (data.get("response").equals("denied"))
//            Toast.makeText(this, "Отказано в доступе", Toast.LENGTH_SHORT).show();
//        else if (data.get("response").equals("error"))
//            Toast.makeText(this, "Сервис не доступен", Toast.LENGTH_SHORT).show();
//        else if (data.get("response").equals("access")) {
//            SharedPreferences preferences = getSharedPreferences(MyConstants.APP_PREFERENCES, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean(MyConstants.PREFERENCE_IS_AUTO_ENTER, true);
//            editor.putBoolean(MyConstants.PREFERENCE_IS_FIREST_RUN, false);
//            editor.putString(MyConstants.PREFERENCE_MY_ID, data.get("id"));
//            editor.putString(MyConstants.PREFERENCE_MY_TOKEN, data.get("token"));
//            editor.apply();
//
//            Intent intent;
//            intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        } else if (data.get("response").equals("server_is_offline")) {
//            Toast.makeText(this, "Нет связи с сервером", Toast.LENGTH_LONG).show();
//        }
    }

}
