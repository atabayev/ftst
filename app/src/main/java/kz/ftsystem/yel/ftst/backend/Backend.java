package kz.ftsystem.yel.ftst.backend;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import kz.ftsystem.yel.ftst.Interfaces.MyCallback;
import kz.ftsystem.yel.ftst.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Backend {

    private Context context;

    private MyCallback myCallback;

    public Backend(Context context, MyCallback myCallback) {
        this.context = context;
        this.myCallback = myCallback;
    }

    public void sendToken(String tid, String token, String fcmToken) {
        Call<ServerResponse> call = getApi().sendFcmToken(tid, token, fcmToken);
        if (isNetworkOnline()) {
            try {
                call.enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {

                    }
                });
            } catch (Exception e) {

            }
        }
    }


    // авторизация переводчика
    public void authentication(String user, String pswd) {
        Call<AuthenticationResponse> call = getApi().authentication(user, pswd);
        if (isNetworkOnline()) {
            try {
                call.enqueue(new Callback<AuthenticationResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<AuthenticationResponse> call, @NonNull Response<AuthenticationResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            HashMap<String, String> data = new HashMap<>();
                            data.put("response", response.body().getResponse());
                            data.put("id", response.body().getId());
                            data.put("token", response.body().getToken());
                            myCallback.fromBackend(data, null);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AuthenticationResponse> call, @NonNull Throwable t) {
                        HashMap<String, String> data = new HashMap<>();
                        data.put("response", "server_is_offline");
                        myCallback.fromBackend(data, null);
                    }
                });
            } catch (Exception e) {
                Log.d(MyConstants.TAG, e.toString());
            }
        }

    }


    // запрос на получение доступных переводчику заказов
    public void getOrders(String myId, String myToken) {
        Call<OrdersResponse> call = getApi().getOrders(myId, myToken);
        if (isNetworkOnline()) {
            try {
                call.enqueue(new Callback<OrdersResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<OrdersResponse> call, @NonNull Response<OrdersResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            HashMap<String, String> data = new HashMap<>();
                            data.put("response", response.body().getResponse());
                            myCallback.fromBackend(data, response.body().getOrders());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<OrdersResponse> call, @NonNull Throwable t) {
                    }
                });
            } catch (Exception e) {
                Log.d(MyConstants.TAG, e.toString());
            }
        }
    }


    // запрос на получение доступных переводчику заказов
    public void getMyOrders(String myId, String myToken) {
        Call<OrdersResponse> call = getApi().getMyOrders(myId, myToken);
        if (isNetworkOnline()) {
            try {
                call.enqueue(new Callback<OrdersResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<OrdersResponse> call, @NonNull Response<OrdersResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            HashMap<String, String> data = new HashMap<>();
                            data.put("response", response.body().getResponse());
                            myCallback.fromBackend(data, response.body().getOrders());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<OrdersResponse> call, @NonNull Throwable t) {
                    }
                });
            } catch (Exception e) {
                Log.d(MyConstants.TAG, e.toString());
            }
        }
    }


    // Переводчик просит отправить материалы заказа на его почту.
    public void sendArchToEmail(String myId, String token, String orderId) {
        Call<ServerResponse> call = getApi().sendArchToEmail(myId, token, orderId);
        if (isNetworkOnline()) {
            try {
                call.enqueue(new Callback<ServerResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            HashMap<String, String> data = new HashMap<>();
                            data.put("response", response.body().getResponse());
                            data.put("email", response.body().getData());
                            myCallback.fromBackend(data, null);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {

                    }
                });
            } catch (Exception e) {
                Log.d(MyConstants.TAG, e.toString());
            }
        }
    }


    public void takeOrder(String myId, String orderId, String myToken) {
        Call<ServerResponse> call = getApi().takeOrder(myId, orderId, myToken);
        if (isNetworkOnline()) {
            try {
                call.enqueue(new Callback<ServerResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            HashMap<String, String> data = new HashMap<>();
                            data.put("response", response.body().getResponse());
                            myCallback.fromBackend(data, null);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                    }
                });
            } catch (Exception e) {
                Log.d(MyConstants.TAG, e.toString());
            }
        }
    }


    public void cancelOrder(String myId, String orderId, String myToken) {
        Call<ServerResponse> call = getApi().cancelOrder(myId, orderId, myToken);
        if (isNetworkOnline()) {
            try {
                call.enqueue(new Callback<ServerResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            HashMap<String, String> data = new HashMap<>();
                            data.put("response", response.body().getResponse());
                            myCallback.fromBackend(data, null);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                    }
                });
            } catch (Exception e) {
                Log.d(MyConstants.TAG, e.toString());
            }
        }
    }


    public void completeOrder(String myId, String myToken, String orderId) {
        Call<ServerResponse> call = getApi().completeOrder(myId, myToken, orderId);
        if (isNetworkOnline()) {
            try {
                call.enqueue(new Callback<ServerResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            HashMap<String, String> data = new HashMap<>();
                            data.put("response", response.body().getResponse());
                            myCallback.fromBackend(data, null);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                    }
                });
            } catch (Exception e) {
                Log.d(MyConstants.TAG, e.toString());
            }
        }
    }


    public void sendNewFcmToken(String myId, String myToken, String fmcToken) {
        Call<ServerResponse> call = getApi().sendFcmToken(myId, myToken, fmcToken);
        if (isNetworkOnline()) {
            try {
                call.enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            HashMap<String, String> data = new HashMap<>();
                            data.put("response", response.body().getResponse());
                            myCallback.fromBackend(data, null);
                        } else {
                            Log.d(MyConstants.TAG, "failure response is: " + response.raw().toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                        HashMap<String, String> data = new HashMap<>();
                        data.put("response", "failure");
                        myCallback.fromBackend(data, null);
                        Log.d(MyConstants.TAG, "Error: " + t.getMessage());
                    }
                });
            } catch (Exception e) {
                Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private API getApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(API.class);
    }


    // переводчик хочет скачать материалы заказа на телефон (сервер возвращает ответом переводчику файл - материалы заказа)
    public void getOrderFile(String tid, String token, String oid) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> call = api.getArchive(tid, token, oid);
        if (isNetworkOnline()) {
            try {
                call.enqueue(new Callback<ResponseBody>() {
                    Context localContext = context;
                    String localOid = oid;

                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                boolean writtenToDisk = writeResponseBodyToDisk(response.body(), localOid);
                                if (writtenToDisk) {
                                    Toast.makeText(localContext, "Скачан архив " + localOid + ".zip", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.d(MyConstants.TAG, "server contact failed");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                    }
                });
            } catch (Exception e) {
                Log.d(MyConstants.TAG, e.toString());
            }
        }
    }


    // вспомогательный метод getOrderFile, записывает принятый от сервера файл (материалы перевода) на диск телефона
    private boolean writeResponseBodyToDisk(ResponseBody body, String fileName) {
        File ordersZipFile = new File(context.getFilesDir() + File.separator + fileName + ".zip");
        long fileSizeDownloaded = 0;
        long fileSize = body.contentLength();
        byte[] fileReader = new byte[4096];
        try (InputStream inputStream = body.byteStream(); OutputStream outputStream = new FileOutputStream(ordersZipFile)) {
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;
                Log.d(MyConstants.TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
            }
            outputStream.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    // проверка связи с интернетом
    public boolean isNetworkOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null)
                return false;
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

}



