package kz.ftsystem.yel.ftst.backend;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by User on 07.03.2018.
 */

public class MyConstants {

    public static String TAG = "Yel";

    public static String MY_DB = "fts_db";
    public static int MY_DB_VERSION = 1;
    public static String DB_TABLE_ORDER = "orders";
    public static String DB_TABLE_PREFERENCE = "preference";

    public static String ORDER_STANDARD_ID = "_id";
    public static String ORDER_ID = "id";
    public static String ORDER_DEADLINE = "deadline";
    public static String ORDER_LANGUAGE = "language";
    public static String ORDER_DIRECTION = "direction";
    public static String ORDER_PAGE_COUNT = "page_count";
    public static String ORDER_PRICE = "price";

    public static String APP_PREFERENCES  = "app_prefs";
    public static String PREFERENCE_IS_FIREST_RUN = "is_first_run";
    public static String PREFERENCE_IS_AUTO_ENTER = "is_auto_enter";
    public static String PREFERENCE_ORDER_ID = "order_id";
    public static String PREFERENCE_MY_ID = "my_id";
    public static String PREFERENCE_MY_TOKEN = "my_token";

    public static String BASE_URL = "http://134.0.115.28:8000/";
//    public static String BASE_URL = "http://10.0.2.2:8000/";

//    public static final String CONTENT_AUTHORITY = "kz.ftsystem.yel.ftst";
//    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY );
//    public static final Uri CONTENT_URI_ORDERS = Uri.withAppendedPath(BASE_CONTENT_URI, DB_TABLE_ORDER);
//    public static final Uri CONTENT_URI_PREFERENCE = Uri.withAppendedPath(BASE_CONTENT_URI, DB_TABLE_PREFERENCE);


}

