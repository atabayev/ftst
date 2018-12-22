package kz.ftsystem.yel.ftst.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.ftsystem.yel.ftst.R;
import kz.ftsystem.yel.ftst.backend.MyConstants;
import kz.ftsystem.yel.ftst.db.AsyncWorkWithDatabase;
import kz.ftsystem.yel.ftst.db.Orders;

public class DataRecyclerAdapterAcceptedOrders extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<Orders> orders = new ArrayList<>();
//    private List<Orders> dbOrders = new ArrayList<>();

    private DataRecyclerAdapterAcceptedOrders.OnMoreListener onMoreListener;
//    private OrdersDAO db = App.getInstance().getDatabase().ordersDAO();


    public DataRecyclerAdapterAcceptedOrders() {
    }

    public void updateRecyclerView(List<Orders> orders_) {
        AsyncWorkWithDatabase.UpdateDatabase updateDatabase = new AsyncWorkWithDatabase.UpdateDatabase();
        updateDatabase.execute(orders_);
        try {
            orders = updateDatabase.get();
        } catch (Exception e) {
            Log.d(MyConstants.TAG, e.toString());
        }

//        db.clearTable();
//        for (Orders ord : _orders) {
//            db.insert(ord);
//        }
//        orders.clear();
//        orders = db.getAll();
//        orders.clear();
        notifyDataSetChanged();
    }

    public void loadOrders() {
        AsyncWorkWithDatabase.GetFromDatabase getFromDatabase = new AsyncWorkWithDatabase.GetFromDatabase();
        getFromDatabase.execute();
        try {
            orders = getFromDatabase.get();
        } catch (Exception e) {
            Log.d(MyConstants.TAG, e.toString());
        }
//        orders = db.getAll();
        notifyDataSetChanged();
    }

    public void insertOrder(Orders order) {
        AsyncWorkWithDatabase.InsertToDatabase insertToDatabase = new AsyncWorkWithDatabase.InsertToDatabase();
        insertToDatabase.execute(order);
        orders.add(order);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new DataRecyclerAdapterAcceptedOrders.NewsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final DataRecyclerAdapterAcceptedOrders.NewsViewHolder viewHolder = (DataRecyclerAdapterAcceptedOrders.NewsViewHolder) holder;
        viewHolder.id.setText(orders.get(position).getId());
        viewHolder.deadline.setText(orders.get(position).getDeadline());
        viewHolder.language.setText(orders.get(position).getLanguage());
        viewHolder.direction.setText(orders.get(position).getDirection());
        viewHolder.pageCount.setText(orders.get(position).getPageCount());
        viewHolder.price.setText(orders.get(position).getPrice());
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvId)
        public TextView id;
        @BindView(R.id.tvDeadLine)
        public TextView deadline;
        @BindView(R.id.tvLang)
        public TextView language;
        @BindView(R.id.tvDirection)
        public TextView direction;
        @BindView(R.id.tvPageCount)
        public TextView pageCount;
        @BindView(R.id.tvPrice)
        public TextView price;
        @BindView(R.id.linearLayout)
        public LinearLayout linearLayout;


        private NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            linearLayout.setOnClickListener(view -> onMoreListener.onMore(orders.get(getAdapterPosition())));
        }
    }


    public void setOnMoreListener(DataRecyclerAdapterAcceptedOrders.OnMoreListener onMoreListener) {
        this.onMoreListener = onMoreListener;
    }


    public interface OnMoreListener {
        void onMore(Orders order);
    }

}
