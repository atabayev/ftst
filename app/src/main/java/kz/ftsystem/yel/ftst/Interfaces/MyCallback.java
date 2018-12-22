package kz.ftsystem.yel.ftst.Interfaces;

import java.util.HashMap;
import java.util.List;

import kz.ftsystem.yel.ftst.backend.Order;
import kz.ftsystem.yel.ftst.backend.OrdersResponse;

public interface MyCallback {
    void fromBackend(HashMap<String, String> data, List<Order> orders);
}
