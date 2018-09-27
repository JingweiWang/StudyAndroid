package io.github.jingweiwang.CircularPictureUpload;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_IMG_PICK = 10002;
    private static final int RESULT_IMG_CROP = 10003;
    private static final String APP_Folder_name = "CircularPictureUpload";

    private File EXTRA_OUTPUT = null;
    private File f;

    private Button bn_selectimg, bn_submit;
    private RoundImageView roundImage_network, roundImage_one_border, roundImage_two_border;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bn_selectimg = findViewById(R.id.bn_selectimg);
        bn_submit = findViewById(R.id.bn_submit);
        roundImage_network = findViewById(R.id.roundImage_network);
        roundImage_one_border = findViewById(R.id.roundImage_one_border);
        roundImage_two_border = findViewById(R.id.roundImage_two_border);

        File file = new File(Environment.getExternalStorageDirectory(), File.separator + APP_Folder_name);
        if (!file.exists()) {
            file.mkdirs();
        }

        bn_selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(Uri.parse("content://media/external/images/media"));

                startActivityForResult(intent, RESULT_IMG_PICK);
            }
        });
        bn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Upload upload = new Upload();
                upload.execute(f);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_IMG_PICK:
                if (data == null) {
                    return;
                }
                EXTRA_OUTPUT = new File(Environment.getExternalStorageDirectory(),
                        File.separator + APP_Folder_name + File.separator + "my_icon");
                Uri uri = data.getData();
                Intent intentCrop = new Intent("com.android.camera.action.CROP");
                intentCrop.setDataAndType(uri, "image/*");
                intentCrop.putExtra("aspectX", 1);
                intentCrop.putExtra("aspectY", 1);
                intentCrop.putExtra("scale", true);
                intentCrop.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(EXTRA_OUTPUT));
                intentCrop.putExtra("return-data", false);
                startActivityForResult(intentCrop, RESULT_IMG_CROP);
                break;
            case RESULT_IMG_CROP:
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                if (EXTRA_OUTPUT.exists()) {
                    Bitmap bitmap;
                    String fileName = "my_icon_up.jpg";
                    f = new File(Environment.getExternalStorageDirectory(),
                            File.separator + APP_Folder_name + File.separator + fileName);

                    bitmap = ratio(EXTRA_OUTPUT.getAbsolutePath(), 200, 200);
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(f);
                        if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                            out.flush();
                            out.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (EXTRA_OUTPUT.exists()) {
                        EXTRA_OUTPUT.delete();
                        Log.i("EXTRA_OUTPUT", "delete");
                    }
                    bitmap = BitmapFactory.decodeFile(f.toString());
                    roundImage_network.setImageBitmap(bitmap);
                    roundImage_one_border.setImageBitmap(bitmap);
                    roundImage_two_border.setImageBitmap(bitmap);
                }

                Log.i("ok", "ok");
                break;
        }

    }

    private Bitmap ratio(String imgPath, float pixelW, float pixelH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        Log.i("wh", w + " " + h);
        // 想要缩放的目标尺寸
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        Log.i("wwhh", ww + " " + hh);

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > ww) {
            be = (int) (w / ww);
        } else {
            be = 1;
        }

        Log.i("be", be + "");
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;//设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        // 压缩好比例大小后再进行质量压缩
//        return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }

    private class Upload extends AsyncTask<File, Void, String> {
        @Override
        protected String doInBackground(File... f) {
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), f[0]);
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                MultipartBody multipartBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("MAX_FILE_SIZE", "220000")
                        .addFormDataPart("userfile", "image.jpg", fileBody)
                        .build();

                Request request = new Request.Builder()
                        .url("http://127.0.0.1:7888/account/uploadimg.php")
                        .post(multipartBody)
                        .build();
                Response response = client.newCall(request).execute();
                Log.i("", response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
