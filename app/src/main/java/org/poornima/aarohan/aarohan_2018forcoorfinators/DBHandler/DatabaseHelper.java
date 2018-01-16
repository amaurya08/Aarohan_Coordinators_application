package org.poornima.aarohan.aarohan_2018forcoorfinators.DBHandler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.EventCoordinatorDetailsTable;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.EventStudentsTable;
import org.poornima.aarohan.aarohan_2018forcoorfinators.Table.RegistrationDetailTable;

/**
 * Created by Bhoomika on 11-01-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
        private Context context;
        public DatabaseHelper(Context context){
            super(context,"aarohanCoordinatorDb.db",null,1);
            Log.d("Debug","Database Created");
            this.context=context;
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            EventCoordinatorDetailsTable.createTable(db);
            EventStudentsTable.createTable(db);
            RegistrationDetailTable.createTable(db);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
        @Override
        public SQLiteDatabase getReadableDatabase() {
            return super.getReadableDatabase();
        }
        @Override
        public SQLiteDatabase getWritableDatabase() {
            return super.getWritableDatabase();
        }
    }

