package org.poornima.aarohan.aarohan_2018forcoorfinators.Table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class AccomodationStudentTable {
    public final static String TABLE_NAME = "AccommodationStudentList";
    public final static String Stu_name = "stu_name";
    public final static String Stu_reg_no = "stu_reg_no";
    public final static String Room = "room";
    public final static String Rs_payment_status = "rs_payment_status";
    public final static String Rc_check_in = "rc_check_in";
    public final static String Rc_check_out = "rc_check_out";

    private static final String createTable = "CREATE TABLE `AccommodationStudentList` ( `stu_name` TEXT NOT NULL, `stu_reg_no` TEXT NOT NULL," +
            " `room` TEXT, `rs_payment_status` TEXT NOT NULL, `rc_check_in` TEXT DEFAULT NULL," +
            " `rc_check_out` TEXT DEFAULT NULL, PRIMARY KEY(`stu_reg_no`) )";

    public static void createTable(SQLiteDatabase db)
    {

        db.execSQL(createTable);
        Log.d("Debug","Table Create "+TABLE_NAME);
    }


    public static void clearCoordinatorDetail(SQLiteDatabase db,String query){
        db.execSQL(query);
    }

    public static void deleteTableData(SQLiteDatabase db, String query) {
        db.execSQL(query);
    }

    public static long insert(SQLiteDatabase db, ContentValues cv) {

        return db.insert(AccomodationStudentTable.TABLE_NAME, null, cv);

    }
    public static int update(SQLiteDatabase db, ContentValues cv,String regNO)
    {
        return db.update(AccomodationStudentTable.TABLE_NAME, cv,Stu_reg_no +" = "+"\""+regNO+"\"",null);
    }

}
