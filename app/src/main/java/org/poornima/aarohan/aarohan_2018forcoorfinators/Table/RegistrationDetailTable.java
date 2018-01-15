package org.poornima.aarohan.aarohan_2018forcoorfinators.Table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
/**
 * Created by Bhoomika on 13-01-2018.
 */
public class RegistrationDetailTable {
    public final static String TABLE_NAME = "RegistrationDetail";
    public final static String stu_name = "stu_name";
    public final static String registrationId = "registrationId";
    private static final String createTable ="CREATE TABLE `RegistrationDetail` (\n" +
            "\t`stu_name`\tTEXT,\n" +
            "\t`registrationId`\tTEXT,\n" +
            "\tPRIMARY KEY(`registrationId`)\n" +
            ");";
    public static void createTable(SQLiteDatabase db) {
        db.execSQL(createTable);
        Log.d("Debug", "Table Created");
    }
    public static void deleteTableData(SQLiteDatabase db, String query) {
        db.execSQL(query);
    }

    public static long insert(SQLiteDatabase db, ContentValues cv) {
        return db.insert(RegistrationDetailTable.TABLE_NAME, null, cv);
    }

}
