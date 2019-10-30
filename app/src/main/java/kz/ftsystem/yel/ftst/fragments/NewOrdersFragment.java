package kz.ftsystem.yel.ftst.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kz.ftsystem.yel.ftst.adapter.DataRecyclerAdapterNewOrders;
import kz.ftsystem.yel.ftst.backend.MessageEvent;
import kz.ftsystem.yel.ftst.ui.AboutNewOrderActivity;
import kz.ftsystem.yel.ftst.Interfaces.MyCallback;
import kz.ftsystem.yel.ftst.R;
import kz.ftsystem.yel.ftst.backend.Backend;
import kz.ftsystem.yel.ftst.backend.MyConstants;
import kz.ftsystem.yel.ftst.backend.Order;


public class NewOrdersFragment extends Fragment implements DataRecyclerAdapterNewOrders.OnMoreListener, MyCallback, SwipeRefreshLayout.OnRefreshListener {


    private static final int REQUEST_CODE_ABOUT_ORDER = 11;

    Backend backend;
    String myId;
    String myToken;
    Unbinder unbinder;
    Context context;

    @BindView(R.id.recyclerView_new)
    RecyclerView recyclerView;
    //        @BindView(R.id.tvNoOrders)
//    TextView tvNoOrders;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    //    RecyclerView recyclerView;
    DataRecyclerAdapterNewOrders recyclerAdapter;


    public NewOrdersFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_orders, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = view.getContext();

        recyclerAdapter = new DataRecyclerAdapterNewOrders();
        recyclerAdapter.setOnMoreListener(this);
        recyclerView.setAdapter(recyclerAdapter);

        recyclerView = view.findViewById(R.id.recyclerView_new);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        SharedPreferences preferences = context.getSharedPreferences(MyConstants.APP_PREFERENCES, Context.MODE_PRIVATE);
        myId = preferences.getString(MyConstants.PREFERENCE_MY_ID, "");
        myToken = preferences.getString(MyConstants.PREFERENCE_MY_TOKEN, "");

        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        backend = new Backend(context, this);
        backend.getOrders(myId, myToken);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        backend = new Backend(context, this);
        backend.getOrders(myId, myToken);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_ABOUT_ORDER) {
            String res = data.getStringExtra("result");
            if (res.equals("1")) {
                backend.getOrders(myId, myToken);
            }
            if (res.equals("0")) {
                backend.getOrders(myId, myToken);
            }

        }
    }



    @Override
    public void onMore(Order order) {
        Intent intent = new Intent(context, AboutNewOrderActivity.class);
        intent.putExtra("id", order.getId());
        intent.putExtra("deadline", order.getDeadline());
        intent.putExtra("language", order.getLanguage());
        intent.putExtra("direction", order.getDirection());
        intent.putExtra("page_count", order.getPageCount());
        intent.putExtra("price", order.getPrice());
        startActivityForResult(intent, REQUEST_CODE_ABOUT_ORDER);
    }


    @Override
    public void fromBackend(HashMap<String, String> data, List<Order> orders) {
        switch (data.get("response")) {
            case "error":
                Toast.makeText(context, "Ошибка передачи данных", Toast.LENGTH_SHORT).show();
                break;
            case "translator_not_exist":
                Toast.makeText(context, "Номер не зарегестрирован", Toast.LENGTH_SHORT).show();
                break;
            case "token_error":
                Toast.makeText(context, "Ошибка токена", Toast.LENGTH_SHORT).show();
                break;
            case "no_orders":
//                Toast.makeText(context, "Нет новых заказов", Toast.LENGTH_SHORT).show();
                List<Order> emptyOrdersList = new ArrayList<>();
                recyclerAdapter.updateRecyclerView(emptyOrdersList);
                break;
            case "ok":
                List<Order> ordersList;
                ordersList = orders;
                recyclerAdapter.updateRecyclerView(ordersList);
                break;
        }
        swipeRefreshLayout.setRefreshing(false);

    }


    @Override
    public void onRefresh() {
       refresh();
    }

    private void refresh() {
        backend = new Backend(context, this);
        if (backend.isNetworkOnline()) {
            swipeRefreshLayout.setRefreshing(true);
            backend.getOrders(myId, myToken);
        } else {
            Toast.makeText(context, "Нет связи с интернетом", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.message.equals("1")) {
            refresh();
        }

    }

    // This method will be called when a SomeOtherEvent is posted
    @Subscribe
    public void handleSomethingElse(MessageEvent event) {
//        doSomethingWith(event);
    }
}
