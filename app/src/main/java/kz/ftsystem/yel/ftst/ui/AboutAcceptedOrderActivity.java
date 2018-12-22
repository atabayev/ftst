package kz.ftsystem.yel.ftst.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.ftsystem.yel.ftst.Interfaces.MyCallback;
import kz.ftsystem.yel.ftst.R;
import kz.ftsystem.yel.ftst.adapter.DataRecyclerAdapterAcceptedOrders;
import kz.ftsystem.yel.ftst.backend.Backend;
import kz.ftsystem.yel.ftst.backend.MyConstants;
import kz.ftsystem.yel.ftst.backend.Order;
import kz.ftsystem.yel.ftst.db.Orders;

public class AboutAcceptedOrderActivity extends AppCompatActivity implements MyCallback {

    @BindView(R.id.aoa_btn_dwn)
    Button btnDwn;
    @BindView(R.id.aoaIdText)
    TextView tvID;
    @BindView(R.id.aoaDeadlineText)
    TextView tvDeadline;
    @BindView(R.id.aoaLanguageText)
    TextView tvLanguage;
    @BindView(R.id.aoaDirectionText)
    TextView tvDirection;
    @BindView(R.id.aoaPageCountText)
    TextView tvPageCount;
    @BindView(R.id.aoaPriceText)
    TextView tvPrice;
    @BindString(R.string.aoa_pages)
    String pages;
    @BindString(R.string.aoa_money_currency)
    String moneyCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_accepted_order);

        ButterKnife.bind(this);
        Intent intent = getIntent();
        tvID.setText(intent.getStringExtra("id"));
        tvDeadline.setText(intent.getStringExtra("deadline"));
        tvLanguage.setText(intent.getStringExtra("language"));
        tvDirection.setText(intent.getStringExtra("direction"));
        String tmpStr = intent.getStringExtra("page_count") + " " + pages;
        tvPageCount.setText(tmpStr);
        tmpStr = intent.getStringExtra("price") + " " + moneyCurrency;
        tvPrice.setText(tmpStr);
    }

    @OnClick({R.id.aoa_btn_dwn, R.id.aoa_btn_finish})
    public void onClick(View view) {

        if (view.getId() == R.id.aoa_btn_dwn) {
            SharedPreferences preferences = getSharedPreferences(MyConstants.APP_PREFERENCES, Context.MODE_PRIVATE);
            String myId = preferences.getString(MyConstants.PREFERENCE_MY_ID, "");
            String myToken = preferences.getString(MyConstants.PREFERENCE_MY_TOKEN, "");
            String orderId = tvID.getText().toString();
            Backend backend = new Backend(this, this);
            backend.sendArchToEmail(myId, myToken, orderId);
        }
        if (view.getId() == R.id.aoa_btn_finish) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Завершить проект?");
            builder.setMessage("Вы отправили результат перевода на почту менеджера?");
            builder.setCancelable(true);
            builder.setPositiveButton("Отправил", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences preferences = getSharedPreferences(MyConstants.APP_PREFERENCES, Context.MODE_PRIVATE);
                    String myId = preferences.getString(MyConstants.PREFERENCE_MY_ID, "");
                    String myToken = preferences.getString(MyConstants.PREFERENCE_MY_TOKEN, "");
                    String orderId = tvID.getText().toString();
                    Backend backend = new Backend(AboutAcceptedOrderActivity.this,
                            AboutAcceptedOrderActivity.this);
                    backend.completeOrder(myId, myToken, orderId);
                }
            });
            builder.setNegativeButton("Нет еще", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(AboutAcceptedOrderActivity.this,
                            "Проект не завершен!",
                            Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    @Override
    public void fromBackend(HashMap<String, String> data, List<Order> orders) {
        switch (data.get("response")) {
            case "send_email_ok":
                Toast.makeText(this, "Письмо оптравлено на почту " + data.get("email"),
                        Toast.LENGTH_SHORT).show();
                break;
            case "error_send":
                Toast.makeText(this, "Ошибка отправки файла", Toast.LENGTH_SHORT).show();
                break;
            case "error_f":
                Toast.makeText(this, "Ошибка полей", Toast.LENGTH_SHORT).show();
                break;
            case "denied":
                Toast.makeText(this, "Доступ запрещен", Toast.LENGTH_SHORT).show();
                break;
            case "error_itb":
                Toast.makeText(this, "Ошибка на сервере, обратитесь к менеджеру",
                        Toast.LENGTH_SHORT).show();
                break;
            case "error_t":
                Toast.makeText(this, "Ошибка токена, перезайдите в приложение",
                        Toast.LENGTH_SHORT).show();
                break;
            case "finish_ok":
                Toast.makeText(this, "Проект завершен, с Вами свяжется менеджер",
                        Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                Toast.makeText(this, data.get("response"), Toast.LENGTH_SHORT).show();
        }
    }
}