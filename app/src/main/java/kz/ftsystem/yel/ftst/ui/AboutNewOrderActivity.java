package kz.ftsystem.yel.ftst.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.ftsystem.yel.ftst.Interfaces.MyCallback;
import kz.ftsystem.yel.ftst.R;
import kz.ftsystem.yel.ftst.backend.Backend;
import kz.ftsystem.yel.ftst.backend.MyConstants;
import kz.ftsystem.yel.ftst.backend.Order;

public class AboutNewOrderActivity extends AppCompatActivity implements MyCallback {

    //    @BindView(R.id.ao_btn_dwn)
//    Button btnDwn;
    @BindView(R.id.aoIdText)
    TextView tvID;
    @BindView(R.id.aoDeadlineText)
    TextView tvDeadline;
    @BindView(R.id.aoLanguageText)
    TextView tvLanguage;
    @BindView(R.id.aoDirectionText)
    TextView tvDirection;
    @BindView(R.id.aoPageCountText)
    TextView tvPageCount;
    @BindView(R.id.aoPriceText)
    TextView tvPrice;
    @BindString(R.string.aoa_pages)
    String pages;
    @BindString(R.string.aoa_money_currency)
    String moneyCurrency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_new_order);
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

    @OnClick({R.id.ao_btn_dwn, R.id.ao_btn_accept, R.id.ao_btn_skip})
    public void onClick(View view) {
        SharedPreferences preferences = getSharedPreferences(MyConstants.APP_PREFERENCES, Context.MODE_PRIVATE);
        String myId = preferences.getString(MyConstants.PREFERENCE_MY_ID, "");
        String myToken = preferences.getString(MyConstants.PREFERENCE_MY_TOKEN, "");
        Backend backend = new Backend(this, this);
        switch (view.getId()) {
            case R.id.ao_btn_dwn:
                backend.sendArchToEmail(myId, myToken, tvID.getText().toString());
                break;
            case R.id.ao_btn_accept:
                backend.takeOrder(myId, tvID.getText().toString(), myToken);
                break;
            case R.id.ao_btn_skip:
                backend.cancelOrder(myId, tvID.getText().toString(), myToken);
                break;
            default:
                break;
        }
    }

    @Override
    public void fromBackend(HashMap<String, String> data, List<Order> orders) {
        switch (data.get("response")) {
            case "ca_ok":
                Intent intent = new Intent();
                intent.putExtra("result", "0");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case "send_email_ok":
                Toast.makeText(this, "Письмо оптравлено на почту " + data.get("email"),
                        Toast.LENGTH_SHORT).show();
                break;
            case "tao_ok":
//                Orders tmpOrder = new Orders();
//                tmpOrder.setId(tvID.getText().toString());
//                tmpOrder.setDeadline(tvDeadline.getText().toString());
//                tmpOrder.setLanguage(tvLanguage.getText().toString());
//                tmpOrder.setDirection(tvDirection.getText().toString());
//                tmpOrder.setPageCount(tvPageCount.getText().toString());
//                tmpOrder.setPrice(tvPrice.getText().toString());
//                DataRecyclerAdapterAcceptedOrders acceptedOrdersAdapter = new DataRecyclerAdapterAcceptedOrders();
//                acceptedOrdersAdapter.insertOrder(tmpOrder);
                Toast.makeText(this, "Заказ оформлен", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent();
                intent1.putExtra("result", "1");
                setResult(RESULT_OK, intent1);
                finish();
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
            case "error_one":
                Toast.makeText(this, "Заказ не существует", Toast.LENGTH_SHORT).show();
                break;
            case "error_s":
                Toast.makeText(this, "К сожалению Вы опоздали", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, data.get("response"), Toast.LENGTH_SHORT).show();
        }
    }


}
