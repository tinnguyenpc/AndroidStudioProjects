package ga.tindemo.grsc_inout;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tin on 2018-02-07.
 */

public class Database extends SQLiteOpenHelper {
    public Database(Context context) {
        super(context, "DB.sqlite", null, 1);
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


    public void create_table(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        String sql="Create table if not exists "+tableName+" (ID INTEGER PRIMARY KEY AUTOINCREMENT)";
        db.execSQL(sql);
    }

    public void add_col(String tableName, String colName, String type) {
        SQLiteDatabase db = getWritableDatabase();
//        type: text; integer,....
        if(isFieldExist(tableName,colName)==false){
            String sql="ALTER TABLE '"+tableName+"' ADD COLUMN '"+colName+"' "+type;
            db.execSQL(sql);
        } else return;

    }

    public void insterdata(String tableName, String svalue) {
        SQLiteDatabase db = getWritableDatabase();
//        svalue: null,val01,val02,....
        String sql="INSERT INTO "+tableName+" VALUES (null,"+svalue+")";
        db.execSQL(sql);
    }

    public void updatedata(String tableName, String colval, String condition) {
        SQLiteDatabase db = getWritableDatabase();
//        colval: col1=val1,col2=val2,....
        String sql="UPDATE "+tableName+" SET "+colval+" WHERE "+condition;
        db.execSQL(sql);
        return;

    }

    public Cursor loaddata(String tableName, String[] cols, String condition) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder str_cmd = new StringBuilder();
        str_cmd.append("select ");
        if(cols==null){
            str_cmd.append("*");
        } else {
            for(String col : cols)
                str_cmd.append("`" + col + "`,");
            str_cmd.deleteCharAt(str_cmd.length() - 1);
        }
        str_cmd.append(" from " + tableName);

        if(condition != null){
            str_cmd.append(" where " +  condition);
        }

        return db.rawQuery( str_cmd.toString(),null);
    }

    public void delete_DB(String tableName, String condition) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(tableName, condition, null);
    }

//    public void delete_col(String tableName, String col) {
//    }



    // This method will return if your table exist a field or not
    public boolean isFieldExist(String tableName, String fieldName)
    {
        boolean isExist = true;
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor res = db.rawQuery("PRAGMA table_info('"+tableName+"')",null);
        Cursor res = db.rawQuery("SELECT * FROM '" + tableName +"' WHERE 1 = 2",null);
        int value = res.getColumnIndex(fieldName);

        if(value == -1)
        {
            isExist = false;
//            Log.i("DB",fieldName+" is ready");
        }
        return isExist;
    }
}
