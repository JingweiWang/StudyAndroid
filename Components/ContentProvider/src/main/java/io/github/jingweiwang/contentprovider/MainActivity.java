package io.github.jingweiwang.contentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_init_db).setOnClickListener(v -> {
            SQLiteDatabase mDb = new DbOpenHelper(MainActivity.this).getWritableDatabase();
            mDb.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
            mDb.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME);
            mDb.execSQL("insert into book values (3,'Android');");
            mDb.execSQL("insert into book values (4,'iOS');");
            mDb.execSQL("insert into book values (5,'Html5');");
            mDb.execSQL("insert into user values (1,'Jack',1);");
            mDb.execSQL("insert into user values (2,'Jasmine',0);");
        });

        findViewById(R.id.btn_select_all_db).setOnClickListener(v -> {
            selectBook();
            selectUser();
        });

        findViewById(R.id.btn_select_book_db).setOnClickListener(v -> {
            selectBook();
        });

        findViewById(R.id.btn_select_user_db).setOnClickListener(v -> {
            selectUser();
        });

        findViewById(R.id.btn_insert_book_db).setOnClickListener(v -> {
            Uri bookUri = BookProvider.BOOK_CONTENT_URI;
            ContentValues values = new ContentValues();
            values.put("_id", 6);
            values.put("name", "程序设计的艺术");
            getContentResolver().insert(bookUri, values);
        });

        findViewById(R.id.btn_insert_user_db).setOnClickListener(v -> {
            Uri userUri = BookProvider.USER_CONTENT_URI;
            ContentValues values = new ContentValues();
            values.put("_id", 3);
            values.put("name", "JingweiWang");
            values.put("sex", 1);
            getContentResolver().insert(userUri, values);
        });

        findViewById(R.id.btn_delete_user_db).setOnClickListener(v -> {
            Uri userUri = BookProvider.USER_CONTENT_URI;
            getContentResolver().delete(userUri, "_id = 1", null);
        });

        findViewById(R.id.btn_update_user_db).setOnClickListener(v -> {
            Uri userUri = BookProvider.USER_CONTENT_URI;
            ContentValues values = new ContentValues();
            values.put("name", "Jessie");
            values.put("sex", 0);
            getContentResolver().update(userUri, values, "_id = 2", null);
        });
    }

    private void selectBook() {
        Uri bookUri = BookProvider.BOOK_CONTENT_URI;
        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
        while (bookCursor.moveToNext()) {
            Book book = new Book();
            book.setBookId(bookCursor.getInt(0));
            book.setBookName(bookCursor.getString(1));
            Log.e(TAG, "query book: " + book.toString());
        }
        bookCursor.close();
    }

    private void selectUser() {
        Uri userUri = BookProvider.USER_CONTENT_URI;
        Cursor userCursor = getContentResolver().query(userUri, new String[]{"_id", "name", "sex"}, null, null, null);
        while (userCursor.moveToNext()) {
            User user = new User();
            user.setUserId(userCursor.getInt(0));
            user.setUserName(userCursor.getString(1));
            user.setMale(userCursor.getInt(2) == 1);
            Log.e(TAG, "query user: " + user.toString());
        }
        userCursor.close();
    }
}
