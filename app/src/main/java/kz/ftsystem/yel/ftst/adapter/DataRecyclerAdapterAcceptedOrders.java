package kz.ftsystem.yel.ftst.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import kz.ftsystem.yel.ftst.backend.Order;

public class DataRecyclerAdapterAcceptedOrders extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Order> orders = new ArrayList<>();
    private OnMoreListener onMoreListener;


    public DataRecyclerAdapterAcceptedOrders() {
    }

    public void updateRecyclerView(List<Order> _orders) {
        orders.clear();
        orders = _orders;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new NewsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        final NewsViewHolder viewHolder = (NewsViewHolder) holder;
        viewHolder.id.setText(orders.get(position).getId());
        if (orders.get(position).getStatus().equals("5")) {
            viewHolder.id.setTextColor(Color.DKGRAY);
            viewHolder.pageCountPages.setTextColor(Color.DKGRAY);
        }
        viewHolder.deadline.setText(orders.get(position).getDeadline());
        viewHolder.language.setText(orders.get(position).getLanguage());
        viewHolder.direction.setText(orders.get(position).getDirection());
        viewHolder.pageCount.setText(orders.get(position).getPageCount());
        viewHolder.price.setText(orders.get(position).getPrice());
        if (orders.get(position).getStatus().equals("5")) {
            viewHolder.deadline.setTextColor(Color.DKGRAY);
            viewHolder.language.setTextColor(Color.DKGRAY);
            viewHolder.direction.setTextColor(Color.DKGRAY);
            viewHolder.pageCount.setTextColor(Color.DKGRAY);
            viewHolder.price.setTextColor(Color.DKGRAY);
        }
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
        @BindView(R.id.tvPageCountPages)
        public TextView pageCountPages;
        @BindView(R.id.tvPrice)
        public TextView price;
        @BindView(R.id.linearLayout)
        public LinearLayout linearLayout;

        private NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            linearLayout.setOnClickListener(view -> {
                onMoreListener.onMore(orders.get(getAdapterPosition()));
            });
        }
    }


    public void setOnMoreListener(OnMoreListener onMoreListener) {
        this.onMoreListener = onMoreListener;
    }



    public interface OnMoreListener {
        void onMore(Order order);
    }

}
