package org.poornima.aarohan.aarohan_2018forcoorfinators.Table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class EventStudentsTable {
    public final static String TABLE_NAME = "EventStudentList";
    public final static String Stu_name = "stu_name";
    public final static String Stu_reg_no = "stu_reg_no";
    public final static String Stu_college = "stu_college";
    public final static String Stu_email = "stu_email";
    public final static String Stu_contact = "stu_contact";
    public final static String Ev_event_att = "ev_event_att";
    private static final String createTable = "CREATE TABLE `EventStudentList` ( `stu_name` TEXT NOT NULL, `stu_reg_no` TEXT NOT NULL," +
            " `stu_college` TEXT, `stu_email` TEXT NOT NULL, `stu_contact` TEXT," +
            " `ev_event_att` TEXT NOT NULL, PRIMARY KEY(`stu_reg_no`) )";

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

        return db.insert(EventStudentsTable.TABLE_NAME, null, cv);

    }
    public static int update(SQLiteDatabase db, ContentValues cv,String regNO)
    {
        return db.update(EventStudentsTable.TABLE_NAME, cv,Stu_reg_no +" = "+"\""+regNO+"\"",null);
    }


}
