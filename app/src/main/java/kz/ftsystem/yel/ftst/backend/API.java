package kz.ftsystem.yel.ftst.backend;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {


    @FormUrlEncoded
    @POST("translator/authentication/")
    Call<AuthenticationResponse> authentication(
            @Field("username") String username,
            @Field("password") String password);


    @FormUrlEncoded
    @POST("translator/get_orders/")
    Call<OrdersResponse> getOrders(
            @Field("tid") String tid,
            @Field("token") String token);


    @FormUrlEncoded
    @POST("translator/get_my_orders/")
    Call<OrdersResponse> getMyOrders(
            @Field("tid") String tid,
            @Field("token") String token);


    @FormUrlEncoded
    @POST("translator/get_archive/")
    Call<ResponseBody> getArchive(
            @Field("tid") String tid,
            @Field("token") String token,
            @Field("oid") String oid);


    @FormUrlEncoded
    @POST("translator/send_archive/")
    Call<ServerResponse> sendArchToEmail(
            @Field("tid") String tid,
            @Field("token") String token,
            @Field("oid") String oid);


    @FormUrlEncoded
    @POST("translator/take_order/")
    Call<ServerResponse> takeOrder(
            @Field("tid") String tid,
            @Field("oid") String oid,
            @Field("token") String token);


    @FormUrlEncoded
    @POST("translator/cancel_order/")
    Call<ServerResponse> cancelOrder(
            @Field("tid") String tid,
            @Field("oid") String oid,
            @Field("token") String token);


    @FormUrlEncoded
    @POST("translator/complete_order/")
    Call<ServerResponse> completeOrder(
            @Field("tid") String tid,
            @Field("token") String oid,
            @Field("oid") String token);


    @FormUrlEncoded
    @POST("management/set_token/")
    Call<ServerResponse> sendMyFcmToken(
            @Field("tid") String tid,
            @Field("token") String token,
            @Field("fcm_token") String fcmToken);


    @FormUrlEncoded
    @POST("translator/set_fcm_token/")
    Call<ServerResponse> sendFcmToken(
            @Field("tid") String myId,
            @Field("token") String myToken,
            @Field("fcm_token") String fcmToken);
}
