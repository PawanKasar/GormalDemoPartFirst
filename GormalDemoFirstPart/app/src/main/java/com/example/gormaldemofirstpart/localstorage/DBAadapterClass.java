package com.example.gormaldemofirstpart.localstorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

public class DBAadapterClass extends SQLiteOpenHelper {

    private static final String DatabaseName = "Gormal.db";
    Context context;
    SQLiteDatabase sqliteDatabase;
    private static final int version = 1;

    String SCRIPT_CREATE_DATABASE= "create table TABLE_PRODUCT_MASTER (id integer primary key autoincrement, productName text, productDescription text, quantity text, price text);";

    public DBAadapterClass(Context context) {
        super(context, DatabaseName, null, version);
        this.context = context;

        /*super(context, Environment.getExternalStorageDirectory() + File.separator + DatabaseName, null, version);
        this.context = context;*/
        /*SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + File.separator + DatabaseName, null);*/
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS TABLE_PRODUCT_MASTER");
    }

    public long insertTagsIntoTagMaster(String productname,String productdescription,String quantity,
                                        String price) {

        long returnV = 0;
        sqliteDatabase = this.getWritableDatabase();
        sqliteDatabase.beginTransaction();
        try {
            ContentValues con2 = new ContentValues();
            con2.put("productName", productname);
            con2.put("productDescription", productdescription);
            con2.put("quantity", quantity);
            con2.put("price", price);
            returnV = sqliteDatabase.insert("TABLE_PRODUCT_MASTER", null, con2);
            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {

            returnV = 0;
        } finally {
            sqliteDatabase.endTransaction();
        }

        return returnV;

    }

    public JSONArray getProductFromAllocationTable() {
        SQLiteDatabase sqliteDatabase = this.getWritableDatabase();
        JSONArray resultSet = new JSONArray();
        JSONObject rowObject = null;
        sqliteDatabase.beginTransaction();
        try {
            Cursor cursor = sqliteDatabase.rawQuery("select * from  TABLE_PRODUCT_MASTER", null);

            cursor.moveToFirst();

            //
            while (cursor.isAfterLast() == false) {
                int totalColumn = cursor.getColumnCount();
                rowObject = new JSONObject();
                for (int i = 0; i < totalColumn; i++) {
                    if (cursor.getColumnName(i) != null) {
                        try {
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } catch (Exception e) {
                            Log.d("TAG", e.getMessage());
                        }
                    }

                }
                resultSet.put(rowObject);
                cursor.moveToNext();
            }

            cursor.close();
            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            rowObject = null;
        } finally {
            sqliteDatabase.endTransaction();
        }

        return resultSet;
    }
}
