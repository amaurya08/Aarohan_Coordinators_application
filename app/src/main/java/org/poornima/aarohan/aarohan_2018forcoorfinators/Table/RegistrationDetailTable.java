package org.poornima.aarohan.aarohan_2018forcoorfinators.Table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class RegistrationDetailTable {
    public final static String TABLE_NAME = "RegistrationDetail";
    public final static String stu_name = "stu_name";
    public final static String registrationId = "registrationId";
    public final static String student_college = "student_college";
    public final static String student_profile_image = "student_profile_image";
    public final static String student_aadhar_image = "student_aadhar_image";
    public final static String student_college_id_image = "student_college_id_image";
    private static final String createTable = "CREATE TABLE `RegistrationDetail` (\n" +
            "\t`stu_name`\tTEXT,\n" +
            "\t`registrationId`\tTEXT,\n" +
            "\t`student_college`\tTEXT,\n" +
            "\t`student_profile_image`\tTEXT,\n" +
            "\t`student_aadhar_image`\tTEXT,\n" +
            "\t`student_college_id_image`\tTEXT,\n" +
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
