package org.poornima.aarohan.aarohan_2018forcoorfinators.Table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Bhoomika on 11-01-2018.
 */

public class EventCoordinatorDetailsTable {
    public final static String TABLE_NAME = "EventCoordinatorDetails";
    public final static String Event_id = "event_id";
    public final static String Event_name = "event_name";
    public final static String Event_category = "event_category";
    public final static String Event_participation_category = "event_participation_category";
    public final static String Event_type = "event_type";
    public final static String Event_detail = "event_detail";
    public final static String Event_location = "event_location";
    public final static String Event_date = "event_date";
    public final static String Event_time = "event_time";
    public final static String Co_name = "co_name";
    public final static String Event_map_coordinates_long = "event_map_coordinates_long";
    public final static String Event_map_coordinates_latt = "event_map_coordinates_latt";
    private static final String createTable = "CREATE TABLE `EventCoordinatorDetails` ( `event_id` TEXT, `event_name` INTEGER NOT NULL," +
            " `event_category` INTEGER, `event_participation_category` INTEGER, `event_type` INTEGER, `event_detail` TEXT, " +
            "`event_location` TEXT, `event_date` TEXT, `event_time` TEXT, `co_name` TEXT NOT NULL, `event_map_coordinates_long` TEXT," +
            " `event_map_coordinates_latt` TEXT, PRIMARY KEY(`event_id`) )";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(createTable);
        Log.d("DATABASE", "Table Created");
    }

    public static void deleteTableData(SQLiteDatabase db, String query) {
        db.execSQL(query);
    }

    public static long insert(SQLiteDatabase db, ContentValues cv) {
        return db.insert(EventCoordinatorDetailsTable.TABLE_NAME, null, cv);
    }
}
