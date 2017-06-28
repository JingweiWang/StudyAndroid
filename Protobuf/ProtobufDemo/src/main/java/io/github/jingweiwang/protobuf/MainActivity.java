package io.github.jingweiwang.protobuf;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.protobuf.Any;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.github.jingweiwang.protobuf.bean.BookOuterClass;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BookOuterClass.Cook cook = BookOuterClass.Cook.newBuilder()
                .setId(2)
                .build();
        BookOuterClass.Book book = BookOuterClass.Book.newBuilder()
                .setId(1)
                .setName("Hello")
                .setDesc("Code Book")
                .setBody(Any.pack(cook))
                .build();
        save(book);

        read();
    }

    void save(BookOuterClass.Book book) {
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, "book");
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            book.writeTo(outputStream);
            outputStream.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    void read() {
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, "book");
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(data)) != -1) {
                out.write(data, 0, len);
                out.flush();
            }
            BookOuterClass.Book book = BookOuterClass.Book.parseFrom(out.toByteArray());
            out.close();
            BookOuterClass.Cook cookUnpack = book.getBody().unpack(BookOuterClass.Cook.class);
            Log.e(TAG, "id: " + book.getId() + " name: " + book.getName() + " desc: " + book.getDesc() + " any: " + book.getBody().getTypeUrl() + " : " + cookUnpack.getId() + "\n");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
