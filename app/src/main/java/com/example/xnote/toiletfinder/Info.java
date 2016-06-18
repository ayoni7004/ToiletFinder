package com.example.xnote.toiletfinder;

/**
 * Created by Xnote on 2016-05-17.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Info extends AppCompatActivity {

    SQLiteDatabase database;
    CustomerDatabaseHelper databaseHelper;
    String tableName = "PRODUCT";
    String databaseName = "memberJoin";
    String Cnum;
    String Cpass;
    String Cname;
    String Cmajor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try

        {
            if (database == null) {
                //  database = openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
                databaseHelper = new CustomerDatabaseHelper(getApplicationContext(), databaseName, null, 1);
                database = databaseHelper.getWritableDatabase();
                Toast.makeText(getApplication(), "DB :" + databaseName + "이 생성되었습니다.", Toast.LENGTH_SHORT).show();
            } else if (database != null) {
                Toast.makeText(getApplication(), "이미 디비열렸음", Toast.LENGTH_SHORT).show();
            }

        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }
        try {
            if (database != null) {
                database.execSQL("CREATE TABLE if not exists " + tableName + "(" +
                        "_id integer PRIMARY KEY autoincrement," +
                        "name text not null," +
                        "pass text not null," +
                        "write text not null," +
                        "num text not null," +
                        "major text not null" +
                        ")");
                Toast.makeText(getApplication(), "Table :" + tableName + "이 생성되었습니다.", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }
    class CustomerDatabaseHelper extends SQLiteOpenHelper {
        CustomerDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onOpen(SQLiteDatabase database) {
            super.onOpen(database);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        }
    }

}
