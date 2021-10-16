package com.example.basicbankingapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.basicbankingapp.banking.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transactions extends Database{

    private static String dbName = "BANKING";
    private static int dbVersion = 1;
    private static String tableName = TRANSACTION_TABLE;

    public Transactions(Context context) {
        super(context, dbName, dbVersion);
    }

    public boolean save(Transaction transaction){
        boolean success = false;
        try{
            database = getWritableDatabase();
            Date dateTimeNow = new Date();
            long timeInMilli = dateTimeNow.getTime();
            database.execSQL("insert into " + tableName + " ("+ transactionId + ", " + senderAcc + ", " + receiverAcc + ", " + time + ", " + amount + ") values(" + transaction.getTransactionId() + "," + transaction.getSenderAcc() + "," + transaction.getReceiverAcc() + "," + timeInMilli + "," + transaction.getAmount() + ");");
            success = true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return success;
    }

    public List<Transaction> allTransactions(){
        database = getReadableDatabase();
        List<Transaction> transactionList = new ArrayList<>();
        try {
            try (Cursor cursor = database.rawQuery("select * from " + tableName + ";", null)) {
                while (cursor.moveToNext()) {
                    transactionList.add(new Transaction(cursor.getString(0), cursor.getLong(1), cursor.getLong(2), cursor.getLong(3), cursor.getDouble(4)));
                    Log.e("t", cursor.getLong(2) + "  " + cursor.getDouble(3) + "");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return transactionList;
    }

    public Transaction detailOfTransaction(long transactionID){
        database = getReadableDatabase();
        Transaction transaction = new Transaction();
        try {
            try (Cursor cursor = database.rawQuery("select * from " + tableName + " where " + Transactions.transactionId + " = " + transactionID + ";", null)) {
                cursor.moveToFirst();
                transaction = new Transaction(cursor.getString(0), cursor.getLong(1), cursor.getLong(2), cursor.getLong(3), cursor.getDouble(4));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return transaction;
    }

    public static long count(Context context) {
        long count = 0;
        try {
            Transactions customers = new Transactions(context);
            customers.getWritableDatabase();
            SQLiteDatabase database = customers.getReadableDatabase();
            try (Cursor cursor = database.rawQuery("select count(*) from " + tableName + ";", null)) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
