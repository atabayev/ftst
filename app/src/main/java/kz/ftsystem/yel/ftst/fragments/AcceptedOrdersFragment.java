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
import kz.ftsystem.yel.ftst.Interfaces.MyCallback;
import kz.ftsystem.yel.ftst.R;
import kz.ftsystem.yel.ftst.adapter.DataRecyclerAdapterAcceptedOrders;
import kz.ftsystem.yel.ftst.backend.Backend;
import kz.ftsystem.yel.ftst.backend.MyConstants;
import kz.ftsystem.yel.ftst.backend.Order;
import kz.ftsystem.yel.ftst.db.Orders;
import kz.ftsystem.yel.ftst.ui.AboutAcceptedOrderActivity;
import kz.ftsystem.yel.ftst.ui.AboutNewOrderActivity;
//import kz.ftsystem.yel.ftst.db.DatabaseHelper;


public class AcceptedOrdersFragment extends Fragment implements DataRecyclerAdapterAcceptedOrders.OnMoreListener, MyCallback, SwipeRefreshLayout.OnRefreshListener {


//    private static final int REQUEST_CODE_ABOUT_ORDER = 22;

    private Backend backend;
    private String myId;
    private String myToken;
    public Context context;
    private Unbinder unbinder;

    @BindView(R.id.recyclerView_accepted)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    DataRecyclerAdapterAcceptedOrders recyclerAdapter;

    public AcceptedOrdersFragment() {
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accepted_orders, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = view.getContext();

        recyclerAdapter = new DataRecyclerAdapterAcceptedOrders();
        recyclerAdapter.setOnMoreListener(this);
        recyclerView.setAdapter(recyclerAdapter);

        recyclerView = view.findViewById(R.id.recyclerView_accepted);
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

        recyclerAdapter.loadOrders();

        backend = new Backend(context, this);
        backend.getMyOrders(myId, myToken);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        recyclerAdapter.loadOrders();
        backend = new Backend(context, this);
        backend.getMyOrders(myId, myToken);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onMore(Orders orders) {
        Intent intent = new Intent(context, AboutAcceptedOrderActivity.class);
        intent.putExtra("id", orders.getId());
        intent.putExtra("deadline", orders.getDeadline());
        intent.putExtra("language", orders.getLanguage());
        intent.putExtra("direction", orders.getDirection());
        intent.putExtra("page_count", orders.getPageCount());
        intent.putExtra("price", orders.getPrice());
        startActivity(intent);
    }


    @Override
    public void fromBackend(HashMap<String, String> data, List<Order> orders) {
        switch (data.get("response")) {
            case "error_f":
                Toast.makeText(context, "Ошибка полей", Toast.LENGTH_SHORT).show();
                break;
            case "error":
                Toast.makeText(context, "Ошибка передачи данных", Toast.LENGTH_SHORT).show();
                break;
            case "denied":
                Toast.makeText(context, "Номер не зарегистрирован", Toast.LENGTH_SHORT).show();
                break;
            case "token_error":
                Toast.makeText(context, "Ошибка токена", Toast.LENGTH_SHORT).show();
                break;
            case "no_orders":
//                Toast.makeText(context, "У Вас нет принятых заказов", Toast.LENGTH_SHORT).show();
                List<Orders> emptyDbOrdersList = new ArrayList<>();
                recyclerAdapter.updateRecyclerView(emptyDbOrdersList);
                break;
            case "ok":
                List<Orders> dbOrdersList = new ArrayList<>();
                for (Order ord : orders) {
                    Orders dbOrd = new Orders();
                    dbOrd.setId(ord.getId());
                    dbOrd.setDeadline(ord.getDeadline());
                    dbOrd.setPrice(ord.getPrice());
                    dbOrd.setLanguage(ord.getLanguage());
                    dbOrd.setPageCount(ord.getPageCount());
                    dbOrd.setDirection(ord.getDirection());
                    dbOrdersList.add(dbOrd);
                }
                recyclerAdapter.updateRecyclerView(dbOrdersList);
                break;
        }
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        backend.getMyOrders(myId, myToken);
    }


}
