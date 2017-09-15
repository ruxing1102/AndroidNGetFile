package com.example.android.androidngetfile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int REQUEST_CAREMA = 1;
    private final static int REQUEST_GET_PERMISSION = 2;

    private Button btn_carema;


    public static String SAVED_IMAGE_DIR_PATH = Environment.getExternalStorageDirectory().getPath() + "/ImageCut/camera/";// 拍照路径
    String cameraPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_carema = (Button) findViewById(R.id.btn_carema);
        btn_carema.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_carema:
                //获取相机权限，如果不开启会报错
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_GET_PERMISSION);
                }
                takePhotoIn7();
                break;
            default:
                break;
        }
    }


    private void takePhotoIn7() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //图片名称
            String cameraPath = SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".png";
            String out_file_path = SAVED_IMAGE_DIR_PATH;
            //创建存放图片的文件夹
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= 24) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(MainActivity.this,
                                "com.example.android.androidngetfile.fileprovide", new File(cameraPath)));
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cameraPath)));
            }
            startActivityForResult(intent, REQUEST_CAREMA);
        } else {
            Toast.makeText(MainActivity.this, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAREMA://相机
                    Log.i("ruxing", "拍照成功！carema path=" + cameraPath);
                    break;
            }
        }
    }

}
