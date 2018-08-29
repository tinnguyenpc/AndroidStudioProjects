package com.example.hieul.hismart;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hieul on 27-Oct-17.
 */

public class Db extends SQLiteOpenHelper {
    public Db(Context context) {
        super(context, "Db.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    public Cursor getdata(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public void querydata(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public void delete_table(String table_name) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + table_name);
    }

    public void delete_row(String tenbang, String tencot, String giatri) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(tenbang, tencot + "=" + giatri, null);
    }

}