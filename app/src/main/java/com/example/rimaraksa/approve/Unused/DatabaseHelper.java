package com.example.rimaraksa.approve.Unused;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rimaraksa.approve.Model.Account;
import com.example.rimaraksa.approve.Model.Contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by rimaraksa on 26/5/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contractManager.db";

    private static final String TABLE_ACCOUNT = "accounts";
    private static final String TABLE_CONTRACT = "contracts";
    private static final String TABLE_ACCOUNT_CONTRACT = "account_contract";

    //ACCOUNT TABLE
    private static final String COLUMN_KEY_ACCOUNT = "account_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NRIC = "nric";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    //CONTRACT TABLE
    private static final String COLUMN_KEY_CONTRACT = "contract_id";
    private static final String COLUMN_SUBJECT = "subject";
    private static final String COLUMN_BODY = "body";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_DATE_REQUEST = "date_request";
    private static final String COLUMN_DATE_APP_OR_REJECT = "date_app_or_reject";

    //ACCOUNT - CONTRACT TABLE
    private static final String COLUMN_KEY_ACCOUNT_CONTRACT = "account_contract_id";
    private static final String COLUMN_SENDER_ID = "sender_id";
    private static final String COLUMN_RECEIVER_ID = "receiver_id";
    private static final String COLUMN_CONTRACT_ID = "contract_id";


    SQLiteDatabase db;

    private static final String TABLE_ACCOUNT_CREATE = "create table accounts (account_id integer primary key not null, " +
            "name text not null, nric text not null, phone text not null, email text not null, " +
            "username text not null, password text not null);";

    private static final String TABLE_CONTRACT_CREATE = "create table contracts (contract_id integer primary key not null, " +
            "subject text not null, body text not null, location text, status text not null, " +
            "date_request datetime not null, date_app_or_reject datetime);";

    private static final String TABLE_ACCOUNT_CONTRACT_CREATE = "create table account_contract (account_contract_id integer primary key not null, " +
            "sender_id integer not null, receiver_id integer not null, contract_id integer not null);";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating required database
        db.execSQL(TABLE_ACCOUNT_CREATE);
        db.execSQL(TABLE_CONTRACT_CREATE);
        db.execSQL(TABLE_ACCOUNT_CONTRACT_CREATE);

        this.db = db;

    }

    public void insertAccount(Account a){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from accounts";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_KEY_ACCOUNT, count);
        values.put(COLUMN_NAME, a.getName());
        values.put(COLUMN_NRIC, a.getNric());
        values.put(COLUMN_PHONE, a.getPhone());
        values.put(COLUMN_USERNAME, a.getUsername());
        values.put(COLUMN_PASSWORD, a.getPassword());

        db.insert(TABLE_ACCOUNT, null, values);
        db.close();

    }

    public int insertContract(Contract c){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from contracts";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_KEY_CONTRACT, count);
        values.put(COLUMN_SUBJECT, c.getSubject());
        values.put(COLUMN_BODY, c.getBody());
        values.put(COLUMN_LOCATION, c.getLocation());
        values.put(COLUMN_STATUS, c.getStatus());
        values.put(COLUMN_DATE_REQUEST, c.getDateRequest());
        values.put(COLUMN_DATE_APP_OR_REJECT, c.getDateAppOrReject());

        db.insert(TABLE_CONTRACT, null, values);
        db.close();

        return count;

    }

    public void insertAccountContract(int sender_id, int receiver_id, int contract_id){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from account_contract";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_KEY_ACCOUNT_CONTRACT, count);
        values.put(COLUMN_SENDER_ID, sender_id);
        values.put(COLUMN_RECEIVER_ID, receiver_id);
        values.put(COLUMN_CONTRACT_ID, contract_id);

        db.insert(TABLE_ACCOUNT_CONTRACT, null, values);
        db.close();

    }

    public List<Contract> getContractsBySender(int sender_id, String status){
        List<Contract> contracts = new ArrayList<Contract>();

        String query = "select * from accounts a, contracts c, account_contract ac " +
                "where a.account_id = ac.sender_id and c.contract_id = ac.contract_id and c.status = " + "'" + status + "' " +
                "and a.account_id = " + sender_id;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
//                Contract c = new Contract();
//                c.setSender(cursor.getString(cursor.getColumnIndex(COLUMN_SENDER_ID)));
//                c.setReceiver(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVER_ID)));
//                c.setSubject(cursor.getString(cursor.getColumnIndex(COLUMN_SUBJECT)));
//                c.setBody(cursor.getString(cursor.getColumnIndex(COLUMN_BODY)));
//                c.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)));
//                c.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
//
//                contracts.add(c);
            } while (cursor.moveToNext());
        }

        return contracts;
    }

    public List<Contract> getContractsByReceiver(int receiver_id, String status){
        List<Contract> contracts = new ArrayList<Contract>();

        String query = "select * from accounts a, contracts c, account_contract ac " +
                "where a.account_id = ac.receiver_id AND c.contract_id = ac.contract_id and c.status = " + "'" + status + "' " +
                "and a.account_id = " + receiver_id;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
//                Contract c = new Contract();
////                String temp = searchUsername(COLUMN_SENDER_ID);
//                c.setSender(cursor.getString(cursor.getColumnIndex(COLUMN_SENDER_ID)));
////                temp = searchUsername(COLUMN_RECEIVER_ID);
//                c.setReceiver(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVER_ID)));
//                c.setSubject(cursor.getString(cursor.getColumnIndex(COLUMN_SUBJECT)));
//                c.setBody(cursor.getString(cursor.getColumnIndex(COLUMN_BODY)));
//                c.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)));
//                c.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
//
//                contracts.add(c);
            } while (cursor.moveToNext());
        }

        return contracts;
    }

    public String searchPassword(String username){
        db = this.getReadableDatabase();
        String query = "select username, password from " + TABLE_ACCOUNT;
        Cursor cursor = db.rawQuery(query, null);

        String a, b;
        b = "not found";

        if(cursor.moveToFirst()){
            do{
                a = cursor.getString(0);


                if(a.equals(username)){
                    b = cursor.getString(1);
                    break;
                }
            } while(cursor.moveToNext());
        }

        return b;

    }

    public String searchName(String username){
        db = this.getReadableDatabase();
        String query = "select username, name from " + TABLE_ACCOUNT;
        Cursor cursor = db.rawQuery(query, null);

        String a, b;
        b = "not found";

        if(cursor.moveToFirst()){
            do{
                a = cursor.getString(0);


                if(a.equals(username)){
                    b = cursor.getString(1);
                    break;
                }
            } while(cursor.moveToNext());
        }

        return b;

    }

    public String searchUsername(String id){
        db = this.getReadableDatabase();
        String query = "select account_id, username from " + TABLE_ACCOUNT;
        Cursor cursor = db.rawQuery(query, null);

        String a, b;
        b = "not found";

        if(cursor.moveToFirst()){
            do{
                a = cursor.getString(0);


                if(a.equals(id)){
                    b = cursor.getString(1);
                    break;
                }
            } while(cursor.moveToNext());
        }

        return b;

    }

    public int searchId(String username){
        db = this.getReadableDatabase();
        String query = "select username, account_id from " + TABLE_ACCOUNT;
        Cursor cursor = db.rawQuery(query, null);

        String a, b;
        b = "-1";

        if(cursor.moveToFirst()){
            do{
                a = cursor.getString(0);


                if(a.equals(username)){
                    b = cursor.getString(1);
                    break;
                }
            } while(cursor.moveToNext());
        }

        return Integer.parseInt(b);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query1 = "DROP TABLE IF EXISTS " + TABLE_ACCOUNT;
        String query2 = "DROP TABLE IF EXISTS " + TABLE_CONTRACT;
        String query3 = "DROP TABLE IF EXISTS " + TABLE_ACCOUNT_CONTRACT;

        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);

        this.onCreate(db);


    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
