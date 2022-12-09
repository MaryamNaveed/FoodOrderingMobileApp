package com.project.i190426_i190435_i190660;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {
    public static String DBNAME="myProjectdb.db";
    public static int VERSION=1;

    public static String CREATE_PRODUCTS_TABLE="CREATE TABLE "+
            MyProject.MyProducts.TABLE_NAME+" ( " +
            MyProject.MyProducts._ID +" INTEGER PRIMARY KEY, "+
            MyProject.MyProducts._NAME +" TEXT, "+
            MyProject.MyProducts._PRICE +" DOUBLE, "+
            MyProject.MyProducts._DESCRIPTION+" TEXT, "+
            MyProject.MyProducts._CATEGORY+" TEXT, "+
            MyProject.MyProducts._PHOTO+" BLOB "+
            " );";

    public static String DROPE_PRODUCTS_TABLE= "DROP TABLE IF EXISTS "+
            MyProject.MyProducts.TABLE_NAME;

    public static String CREATE_CUSTOMERS_TABLE="CREATE TABLE "+
            MyProject.MyCustomer.TABLE_NAME+" ( " +
            MyProject.MyCustomer._ID +" INTEGER PRIMARY KEY, "+
            MyProject.MyCustomer._NAME +" TEXT, "+
            MyProject.MyCustomer._EMAIL +" TEXT, "+
            MyProject.MyCustomer._PHONE+" TEXT "+
            " );";

    public static String DROPE_CUSTOMERS_TABLE= "DROP TABLE IF EXISTS "+
            MyProject.MyCustomer.TABLE_NAME;

    public static String CREATE_CART_TABLE="CREATE TABLE "+
            MyProject.MyCart.TABLE_NAME+" ( " +
            MyProject.MyCart._ID +" INTEGER, "+
            MyProject.MyCart.ITEM_ID +" INTEGER, "+
            MyProject.MyCart.CUST_ID +" INTEGER, "+
            MyProject.MyCart.QUANTITY +" INTEGER, "+
            " FOREIGN KEY ( "+ MyProject.MyCart.CUST_ID +" ) REFERENCES "+ MyProject.MyCustomer.TABLE_NAME+" ( "+ MyProject.MyCustomer._ID +" ), "+
            " FOREIGN KEY ( "+ MyProject.MyCart.ITEM_ID +" ) REFERENCES "+ MyProject.MyProducts.TABLE_NAME+" ( "+ MyProject.MyProducts._ID +" ) "+
            " );";

    public static String DROPE_CART_TABLE= "DROP TABLE IF EXISTS "+
            MyProject.MyCart.TABLE_NAME;

    public static String CREATE_ORDER_TABLE="CREATE TABLE "+
            MyProject.MyOrder.TABLE_NAME+" ( " +
            MyProject.MyOrder._ID +" INTEGER PRIMARY KEY, "+
            MyProject.MyOrder._CUST_ID +" INTEGER, "+
            MyProject.MyOrder._DATETIME +" TEXT, "+
            MyProject.MyOrder._TAX +" DOUBLE, "+
            MyProject.MyOrder._STATUS +" TEXT, "+
            " FOREIGN KEY ( "+ MyProject.MyOrder._CUST_ID +" ) REFERENCES "+ MyProject.MyCustomer.TABLE_NAME+" ( "+ MyProject.MyCustomer._ID +" ) "+
            " );";

    public static String DROPE_ORDER_TABLE= "DROP TABLE IF EXISTS "+
            MyProject.MyOrder.TABLE_NAME;

    public static String CREATE_ORDERITEM_TABLE="CREATE TABLE "+
            MyProject.MyOrderItem.TABLE_NAME+" ( " +
            MyProject.MyOrderItem._ID +" INTEGER, "+
            MyProject.MyOrderItem.ITEM_ID +" INTEGER, "+
            MyProject.MyOrderItem.ORDER_ID +" INTEGER, "+
            MyProject.MyOrderItem._QUANTITY +" INTEGER, "+
            MyProject.MyOrderItem._PRICE +" INTEGER, "+
            " FOREIGN KEY ( "+ MyProject.MyOrderItem.ORDER_ID +" ) REFERENCES "+ MyProject.MyOrder.TABLE_NAME+" ( "+ MyProject.MyOrder._ID +" ), "+
            " FOREIGN KEY ( "+ MyProject.MyOrderItem.ITEM_ID +" ) REFERENCES "+ MyProject.MyProducts.TABLE_NAME+" ( "+ MyProject.MyProducts._ID +" ) "+
            " );";


    public static String DROPE_ORDERITEM_TABLE= "DROP TABLE IF EXISTS "+
            MyProject.MyOrderItem.TABLE_NAME;

    public MyDBHelper(@Nullable Context context) {
        super(context, DBNAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PRODUCTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_CUSTOMERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_CART_TABLE);
        sqLiteDatabase.execSQL(CREATE_ORDER_TABLE);
        sqLiteDatabase.execSQL(CREATE_ORDERITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROPE_ORDERITEM_TABLE);
        sqLiteDatabase.execSQL(DROPE_ORDER_TABLE);
        sqLiteDatabase.execSQL(DROPE_CART_TABLE);
        sqLiteDatabase.execSQL(DROPE_CUSTOMERS_TABLE);
        sqLiteDatabase.execSQL(DROPE_PRODUCTS_TABLE);
        onCreate(sqLiteDatabase);
    }
}

