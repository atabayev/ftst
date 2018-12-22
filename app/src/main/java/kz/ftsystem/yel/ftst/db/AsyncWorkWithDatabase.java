package kz.ftsystem.yel.ftst.db;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import kz.ftsystem.yel.ftst.App;
import kz.ftsystem.yel.ftst.adapter.DataRecyclerAdapterAcceptedOrders;

public class AsyncWorkWithDatabase {

    public static class GetFromDatabase extends AsyncTask<Void, Void, List<Orders>> {

        OrdersDAO db = App.getInstance().getDatabase().ordersDAO();

        @Override
        protected List<Orders> doInBackground(Void... params) {
            return db.getAll();
        }
    }

    public static class InsertToDatabase extends AsyncTask<Orders, Void, Void> {

        OrdersDAO db = App.getInstance().getDatabase().ordersDAO();

        @Override
        protected Void doInBackground(Orders... orders) {
            db.insert(orders[0]);
            return null;
        }
    }

    public static class UpdateDatabase extends AsyncTask<List<Orders>, Void, List<Orders>> {

        OrdersDAO db = App.getInstance().getDatabase().ordersDAO();

        @SafeVarargs
        @Override
        protected final List<Orders> doInBackground(List<Orders>... orders) {
            db.clearTable();
            for (Orders order : orders[0]) {
                db.insert(order);
            }
            return db.getAll();
        }
    }


}
