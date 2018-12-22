package kz.ftsystem.yel.ftst.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kz.ftsystem.yel.ftst.adapter.DataRecyclerAdapterNewOrders;
import kz.ftsystem.yel.ftst.ui.AboutNewOrderActivity;
import kz.ftsystem.yel.ftst.Interfaces.MyCallback;
import kz.ftsystem.yel.ftst.R;
import kz.ftsystem.yel.ftst.backend.Backend;
import kz.ftsystem.yel.ftst.backend.MyConstants;
import kz.ftsystem.yel.ftst.backend.Order;
//import kz.ftsystem.yel.ftst.db.DatabaseHelper;
//import kz.ftsystem.yel.ftst.db.model.DataModel;


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
        Log.d(MyConstants.TAG, "onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(MyConstants.TAG, "onStart");
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
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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
                Toast.makeText(context, "Нет новых заказов", Toast.LENGTH_SHORT).show();
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
        backend = new Backend(context, this);
        if (backend.isNetworkOnline()) {
            swipeRefreshLayout.setRefreshing(true);
            backend.getOrders(myId, myToken);
        } else {
            Toast.makeText(context, "Нет связи с интернетом", Toast.LENGTH_SHORT).show();
        }

    }
}
