package com.example.myapplication101;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class paizhao extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 101;
    public Button pz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paizhao);
        pz=findViewById(R.id.paizhao);
        pz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(!checkPermission()){
                        requestPermission();
                    }
                    else{
                        takePhoto();
                    }
                }
            }
        });
    }

    public Boolean checkPermission() {
        boolean haveCameraPermission= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        boolean haveWritePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return haveCameraPermission && haveWritePermission;
    }
    //申请
    private void requestPermission(){
        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION_CODE);
    }

    //申请完后的回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:

                boolean allowAllPermission = false;

                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {//被拒绝授权
                        allowAllPermission = false;
                        break;
                    }
                    allowAllPermission = true;
                }

                if (allowAllPermission) {

                    //takePhotoOrPickPhoto();//开始拍照或从相册选取照片
                } else {
                    Toast.makeText(this, "该功能需要授权方可使用", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private Uri mUri;
    private void takePhoto(){
        //步骤一： 创建存储照片的文件
        String path=getFilesDir()+ File.separator+"images"+File.separator;
        File file=new File(path,"test.jpg");
        if(!file.getParentFile().exists()){
            // 若不存在就创建一个文件
            file.getParentFile().mkdirs();
        }
        //步骤二：Android 7.0及以上获取文件 Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            mUri = FileProvider.getUriForFile(paizhao.this, "com.example.myapplication101", file);
        }
        else {
            //步骤三：获取文件Uri
            mUri = Uri.fromFile(file);
        }
        //步骤四：调取系统拍照功能
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,mUri);
    }
}
