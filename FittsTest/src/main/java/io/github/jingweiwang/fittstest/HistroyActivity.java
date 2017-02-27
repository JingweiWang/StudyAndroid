package io.github.jingweiwang.fittstest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistroyActivity extends AppCompatActivity {
    // 记录当前的父文件夹
    File currentParent;
    // 记录当前路径下的所有文件的文件数组
    File[] currentFiles;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histroy);

        listView = (ListView) findViewById(R.id.listView);

        //获取目录
        File root = new File(Environment.getDataDirectory() + "/data/io.github.jingweiwang.fittstest/files/");
        //如果存在
        if (root.exists()) {
            currentParent = root;
            currentFiles = root.listFiles();
            // 使用当前目录下的全部文件、文件夹来填充ListView
            inflateListView(currentFiles);
        }
        // 为ListView的列表项的单击事件绑定监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // 用户单击了文件，直接返回，不做任何处理
                if (currentFiles[position].isFile()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HistroyActivity.this)
                            .setTitle(currentFiles[position].getName() + "").setMessage("您是否要继续上传此文件?");
                    builder.setPositiveButton("上传", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                AVFile file = AVFile.withAbsoluteLocalPath(currentFiles[position].getName(), currentFiles[position] + "");
                                file.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e != null) {
                                            //上传失败
                                            Toast.makeText(HistroyActivity.this, "上传失败,请检查您的网络!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //上传成功
                                            Toast.makeText(HistroyActivity.this, "上传成功,感谢您.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new ProgressCallback() {
                                    @Override
                                    public void done(Integer percentDone) {
                                        //打印进度
                                        System.out.println("uploading: " + percentDone);
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).create().show();
                    return;
                }
            }
        });
    }

    private void inflateListView(File[] files) {
        // 创建一个List集合，List集合的元素是Map
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < files.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("fileName", files[i].getName());
            // 添加List项
            listItems.add(listItem);
        }
        // 创建一个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this
                , listItems, R.layout.list_doc
                , new String[]{"fileName"}
                , new int[]{R.id.file_name});
        // 为ListView设置Adapter
        listView.setAdapter(simpleAdapter);
    }
}
