package com.king.holymary.database_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.king.holymary.data_handler.DB_HandlerAwakw_1;

import java.sql.SQLException;

/**
 * Created by Arvindo on 31-03-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class DataBaseAwake_1 extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WIDE_AWAKE_DB_1";
    private static final int DATABASE_VERSION = 1;

    private static final String REGISTRATION_TABLE = "REGISTRATION_TABLE";
    private static final String USER_DETAILS_TABLE = "USER_DETAILS_TABLE";

    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_USER_TYPE = "USER_TYPE";
    private static final String KEY_REGISTRATION_OK = "REGISTRATION_OK";
    private static final String KEY_PRIMARY_ID = "PRIMARY_ID";
    private static final String KEY_STATUS = "USER_STATUS";


    private static final String KEY_USER_NAME = "USER_NAME";
    private static final String KEY_EMAIL = "USER_EMAIL";
    private static final String KEY_COLLEGE_NAME = "USER_COLLEGE_NAME";
    private static final String KEY_DEPARTMENT_NAME = "USER_DEPARTMENT_NAME";
    private static final String KEY_BRANCH_NAME = "USER_BRANCH_NAME";

    public DataBaseAwake_1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REGISTRATION_TB = "CREATE TABLE " + REGISTRATION_TABLE + "(" +
                KEY_PRIMARY_ID +" INTEGER PRIMARY KEY," +
                KEY_USER_ID + " VARCHAR(20)," +
                KEY_USER_TYPE + " VARCHAR(1)," +
                KEY_STATUS + " VARCHAR(10), " +
                KEY_REGISTRATION_OK + " VARCHAR(10) " + ")";
        db.execSQL(CREATE_REGISTRATION_TB);

        String CREATE_USER_DETAILS_TB = "CREATE TABLE " + USER_DETAILS_TABLE + "(" +
                KEY_PRIMARY_ID +" INTEGER PRIMARY KEY," +
                KEY_USER_ID + " VARCHAR(20)," +
                KEY_USER_NAME + " VARCHAR(20)," +
                KEY_EMAIL + " VARCHAR(20)," +
                KEY_COLLEGE_NAME + " VARCHAR(100)," +
                KEY_DEPARTMENT_NAME + " VARCHAR(50), " +
                KEY_BRANCH_NAME + " VARCHAR(50) " + ")";
        db.execSQL(CREATE_USER_DETAILS_TB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }

    public void insertRegistrationTB(DB_HandlerAwakw_1 handler) throws SQLException{
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, handler.getUser_id_());
        values.put(KEY_USER_TYPE, handler.getUser_reg_type_());
        values.put(KEY_STATUS, handler.getUser_status());
        values.put(KEY_REGISTRATION_OK, handler.getRegistration_ok());
        db.insert(REGISTRATION_TABLE, null, values);
        db.close();
    }

    public void insertUserDetailsTB(DB_HandlerAwakw_1 handler) throws SQLException{
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, handler.getUser_id_());
        values.put(KEY_USER_NAME, handler.getUser_id_());
        values.put(KEY_EMAIL, handler.getUser_id_());
        values.put(KEY_COLLEGE_NAME, handler.getClgName());
        values.put(KEY_DEPARTMENT_NAME, handler.getDeptName());
        values.put(KEY_BRANCH_NAME, handler.getBranchName());
        db.insert(USER_DETAILS_TABLE, null, values);
        db.close();
    }

    public boolean checkStatus() throws SQLException{
        boolean status = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_STATUS  + ", " + KEY_REGISTRATION_OK + " from " + REGISTRATION_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc LIMIT 1";
        Cursor c = db.rawQuery(query, null);
        if(c != null && c.moveToFirst()){
            String s = c.getString(0);
            String s1 = c.getString(1);
            if(s.equals("true") && s1.equals("ok")){
                status = true;
            }
            c.close();
        }

        return status;
    }

    public String userType() throws SQLException{
        String  type = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_USER_TYPE + " from " + REGISTRATION_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc LIMIT 1";
        Cursor c = db.rawQuery(query, null);
        if(c != null && c.moveToFirst()){
            type = c.getString(0);
            c.close();
        }

        return type;
    }

    public String getLastUserId() throws SQLException{
        String id = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_USER_ID + " from " + REGISTRATION_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc LIMIT 1";
        Cursor c = db.rawQuery(query, null);
        if(c != null && c.moveToFirst()){
            id = c.getString(0);
            c.close();
        }

        return id;
    }

    public String getOnlyRegisteredUserID() throws SQLException{
        String id = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_USER_ID  + ", " + KEY_STATUS + ", " + KEY_REGISTRATION_OK +
                " from " + REGISTRATION_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc LIMIT 1";
        Cursor c = db.rawQuery(query, null);
        if(c != null && c.moveToFirst()){
            if(c.getString(1).equals("false") && c.getString(2).equals("ok")) {
                id = c.getString(0);
            }
            c.close();
        }

        return id;
    }

    public String getTrueUserId() throws SQLException{
        String id = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_USER_ID  + ", " + KEY_STATUS + ", " + KEY_REGISTRATION_OK +
                " from " + REGISTRATION_TABLE +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc LIMIT 1";
        Cursor c = db.rawQuery(query, null);
        if(c != null && c.moveToFirst()){
            if(c.getString(1).equals("true") && c.getString(2).equals("ok")) {
                id = c.getString(0);
            }
            c.close();
        }

        return id;
    }

    public String[] getUserDetails(String userId) throws SQLException{
        String[] arr = new String[4];

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + KEY_COLLEGE_NAME + ", " + KEY_DEPARTMENT_NAME +
                ", " + KEY_BRANCH_NAME + " " +
                " from " + USER_DETAILS_TABLE +
                " where " + KEY_USER_ID + " = " + userId +
                " ORDER BY " +  KEY_PRIMARY_ID + " desc LIMIT 1";
        Cursor c = db.rawQuery(query, null);
        if(c != null && c.moveToFirst()){
            arr[3] = "1";
            arr[0] = c.getString(0);
            arr[1] = c.getString(1);
            arr[2] = c.getString(2);
            c.close();
        }
        else{
            arr[3] = "2";
        }

        return arr;
    }


}
