package com.example.stu.maasrot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class DBManager {
    SQLiteDatabase database;
    Context context;
    private static DBManager dbManager;
    private Handler handler = new Handler();

    public static DBManager getDbManager(Context context) {
        if (dbManager == null){
            dbManager = new DBManager(context);
        }
        return dbManager;
    }

    private DBManager(Context context) {
        this.context = context;
    }

    public synchronized void createTables(final OnThreadFinish finish){
        new Thread(){
            @Override
            public void run() {
                database = context.openOrCreateDatabase("maaser.sqlite", Context.MODE_PRIVATE, null);
                database.execSQL("CREATE TABLE IF NOT EXISTS tables(action_name TEXT, action_sum INTEGER, total_sum INTEGER, plus_action BOOL, date TEXT)");
                database.close();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                   if (finish != null){
                       finish.onFinish();
                   }
                    }
                });
            }
        }.start();
    }

    public synchronized void insertAction(final Action action, final OnThreadFinish finish){
        new Thread(){
            @Override
            public void run() {
                database = context.openOrCreateDatabase("maaser.sqlite", Context.MODE_PRIVATE, null);


                ContentValues cv = new ContentValues();
                cv.put("action_name", action.actionName);
                cv.put("action_sum", action.actionSum);
                cv.put("total_sum", action.totalSum);
                cv.put("plus_action", action.plus);
                cv.put("date", action.date);
                database.insert("tables", null, cv);
                database.close();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (finish != null){
                            finish.onFinish();
                        }

                    }
                });

            }
        }.start();
    }

    public synchronized int getLastAction(){
        int totalSum = 0;
        database = context.openOrCreateDatabase("maaser.sqlite", Context.MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM tables", null);
        if (cursor != null && cursor.moveToLast()){
            totalSum = cursor.getInt(cursor.getColumnIndex("total_sum"));
        }
        return totalSum;
    }

    public synchronized void getHistory(final OnThreadFinish finish){
        new Thread(){
            @Override
            public void run() {
                ArrayList<Action> actions = new ArrayList<>();
                database = context.openOrCreateDatabase("maaser.sqlite", Context.MODE_PRIVATE, null);
                Cursor cursor = database.rawQuery("SELECT * FROM tables", null);
                if (cursor != null && cursor.moveToFirst()){
                    do {
                        String date = cursor.getString(cursor.getColumnIndex("date"));
                        Action action = new Action(cursor.getString(cursor.getColumnIndex("action_name")),cursor.getInt(cursor.getColumnIndex("action_sum")),cursor.getInt(cursor.getColumnIndex("plus_action")) > 0, date);
                        action.totalSum = cursor.getInt(cursor.getColumnIndex("total_sum"));
                        actions.add(action);
                    }while (cursor.moveToNext());
                }
                if (finish != null){
                    finish.onFinish(actions);
                }
            }
        }.start();
    }

}
