package kz.ftsystem.yel.ftst.fragments;


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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import kz.ftsystem.yel.ftst.backend.MessageEvent;
import kz.ftsystem.yel.ftst.backend.MyConstants;
import kz.ftsystem.yel.ftst.backend.Order;
import kz.ftsystem.yel.ftst.ui.AboutAcceptedOrderActivity;


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

        backend = new Backend(context, this);
        backend.getMyOrders(myId, myToken);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        backend = new Backend(context, this);
        backend.getMyOrders(myId, myToken);
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
    public void onMore(Order orders) {
        if (orders.getStatus().equals("4")) {
            Intent intent = new Intent(context, AboutAcceptedOrderActivity.class);
            intent.putExtra("id", orders.getId());
            intent.putExtra("deadline", orders.getDeadline());
            intent.putExtra("language", orders.getLanguage());
            intent.putExtra("direction", orders.getDirection());
            intent.putExtra("page_count", orders.getPageCount());
            intent.putExtra("price", orders.getPrice());
            startActivity(intent);
        }
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
        swipeRefreshLayout.setRefreshing(true);
        backend.getMyOrders(myId, myToken);
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
