package kz.ftsystem.yel.ftst.backend;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrdersResponse {

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrder(List<Order> order) {
        this.orders = order;
    }

}