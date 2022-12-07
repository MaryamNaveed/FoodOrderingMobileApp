package com.project.i190426_i190435_i190660;

import android.provider.BaseColumns;

public class MyProject {

    public static class MyProducts implements BaseColumns {
        public static String TABLE_NAME="products";
        public static String _ID="id";
        public static String _NAME="name";
        public static String _PRICE="price";
        public static String _PHOTO="photo";
        public static String _DESCRIPTION="description";
        public static  String _CATEGORY="category";
    }

    public static class MyCustomer implements BaseColumns {
        public static String TABLE_NAME="customers";
        public static String _ID="id";
        public static String _NAME="name";
        public static String _EMAIL="email";
        public static String _PHONE="phone";
    }

    public static class MyCart implements BaseColumns {
        public static String TABLE_NAME="cart";
        public static String ITEM_ID="item_id";
        public static String CUST_ID="cust_id";
        public static String QUANTITY="quantity";
    }

    public static class MyOrder implements BaseColumns {
        public static String TABLE_NAME="orders";
        public static String _ID="order_id";
        public static String _CUST_ID="cust_id";
        public static String _DATETIME="dateTime";
        public static String _TAX="tax";
        public static String _STATUS="status";
    }

    public static class MyOrderItem implements BaseColumns {
        public static String TABLE_NAME="orderitems";
        public static String ORDER_ID="order_id";
        public static String ITEM_ID="item_id";
        public static String _QUANTITY="quantity";
        public static String _PRICE="price";
    }
}
