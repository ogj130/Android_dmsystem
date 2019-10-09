package com.example.myapplication101;

import android.annotation.TargetApi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication101.StudentADB.StudentDao;
import com.example.myapplication101.StudentADB.StudentInfo;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class shangchuan extends AppCompatActivity {

    public String id="1",password="1";
    EditText user_id;
    EditText user_password;
    EditText wenjian;

    ImageView touxiang;
    Button register;
    //定义返回值，判断图片的来源
    private int Photo_ALBUM = 1, CAMERA = 2;
    //定义图片的路径
    private String imagePath = null;
    //定义图片的uri
    private Uri imageUri;

    int FLAG=0;
    private Bitmap bp = null;


    LoadView loadView;

    //检查登录是否有效
    boolean check(String s) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangchuan);
        // 隐藏自带栏目
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        FLAG = 0;
        user_id=findViewById(R.id.user_id);
        user_password=findViewById(R.id.user_password);
        touxiang=findViewById(R.id.touxiang);
        register=findViewById(R.id.register);
        wenjian=findViewById(R.id.wenjian);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = user_password.getText().toString().trim();
                id = user_id.getText().toString().trim();
                if (id.equals("") || password.equals("")) {
                    Toast.makeText(shangchuan.this, "id和password不能为空", Toast.LENGTH_SHORT).show();
                } else if (check(id) == false) {
                    Toast.makeText(shangchuan.this, "id非法", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(shangchuan.this)
                            .setTitle("系统提示")
                            //设置显示的内容
                            .setMessage("请选择上传方式")
                            //右边的Button
                            .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {//添加返回按钮事件
                                }
                            })
                            //中间的Button
                            .setNeutralButton("从相册上传", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //选择数据
                                    Intent in = new Intent(Intent.ACTION_PICK);

                                    //选择的数据为图片
                                    in.setType("image/*");
                                    startActivityForResult(in, Photo_ALBUM);
                                }
                            })
                            //定义左边的按钮
                            .setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                                @Override
                                public void onClick(
                                        DialogInterface dialogInterface,
                                        int i) {
                                    //7.0 加载拍照功能
                                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                    StrictMode.setVmPolicy(builder.build());
                                    builder.detectFileUriExposure();
                                    //临时照片存储地
                                    File outputImage = new File(Environment.getExternalStorageDirectory()
                                            + File.separator+wenjian.getText().toString()+id+".jpg");
                                    try {
                                        if (outputImage.exists()) {
                                            outputImage.delete();
                                        }
                                        //创建临时地址
                                        outputImage.createNewFile();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    //获取uri
                                    imageUri = Uri.fromFile(outputImage); //把file转化为uri对象
                                    imagePath = outputImage.getAbsolutePath();
                                    Log.i("拍照的路径", imagePath);

                                    //跳转相机 进行拍照
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                    //返回照片路径
                                    startActivityForResult(intent, CAMERA);
                                }
                            }).show();  //显示的对话框
                }
            }
        });
    }

    //在拍完照后的 回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadView = new LoadView();
        loadView.buildProgressDialog("上传中...", shangchuan.this);
        if(requestCode==Photo_ALBUM){ //如果是选择的相册 进行保存
            if(data!=null){
                Uri uri=data.getData();
                Cursor cursor=getContentResolver().query(uri, null, null, null, null);
                cursor.moveToNext();

                //获取图片的绝对路径
                imagePath=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                cursor.close();
                Log.i("图片path:",imagePath);
                bp=getimage(imagePath);
                touxiang.setImageBitmap(bp);
                runthread();
            }
        }
        else if(requestCode==CAMERA){
            bp=getimage(imagePath);
            touxiang.setImageBitmap(bp);
            runthread();
        }
    }
    //缩放
    public Bitmap getimage(String srcPath){
        BitmapFactory.Options newOpts = new BitmapFactory.Options();

        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;

        //此时返回bm为空
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {
            //如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            //如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }

        //设置缩放比例
        newOpts.inSampleSize = be;

        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        //压缩好比例大小后再进行质量压缩
        return compressImage(bitmap);
    }
    //质量压缩
    public Bitmap compressImage(Bitmap image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > 300) {
            //循环判断如果压缩后图片是否大于300kb,大于继续压缩
            baos.reset();

            //重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

            //每次都减少10
            options -= 10;
        }

        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());

        //把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    public byte[] getBytesByBitmap(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bitmap.getByteCount());
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
    // 调用api  进行人脸识别 不能用不是人脸的头像
    void runthread(){
        // 进行人脸了
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
                try {
                    byte[] byte1=getBytesByBitmap(bp);
                    String image1= Base64Util.encode(byte1);

                    Map<String,Object> map=new HashMap<>();


                    map.put("image", image1);
                    map.put("group_id", "face");
                    map.put("user_id", id);
                    map.put("user_info", "abc");
                    map.put("liveness_control", "NORMAL");
                    map.put("image_type", "BASE64");
                    map.put("quality_control", "LOW");

                    //转为json 数据
                    String param = GsonUtils.toJson(map);


                    String clientId = "vKM79jsgy0VqG2mpkiBZ3GL4";

                    // 官网获取的 Secret Key 更新为你注册的
                    String clientSecret = "kAKddEXfO60iRckxSS0ermsNGGxh98t5";
                    String accessToken = getAuth(clientId, clientSecret);

                    //  = "24.470560ecfc8ded10d622b3dd4e258f34.2592000.1563086633.282335-15236904";
                    String result=HttpUtil.post(url,accessToken,"application/json",param);
                    Gson gson=new Gson();
                    add_result Result_bean=gson.fromJson(result,add_result.class);

                    int Error_code = Result_bean.getError_code();
                    if (Error_code == 0) {
                        StudentDao ggg=new StudentDao(shangchuan.this);
                        StudentInfo stuData=new StudentInfo();
                        Map<String,String> st=ggg.select_stuid(id);
                        stuData.setName(st.get("student_name"));
                        stuData.setSex(st.get("student_sex"));
                        stuData.setBanji(st.get("student_banji"));
                        stuData.setAge(Integer.parseInt(st.get("student_age")));
                        stuData.setMajor_na(st.get("student_major_na"));
                        stuData.setCollege_na(st.get("student_college_na"));
                        stuData.setPhone(st.get("student_phone"));
                        stuData.setId(Long.valueOf(st.get("student_id")));
                        stuData.setTouxiang_id(imagePath);
                        if(ggg.adjust(stuData)) {
                            BackTool.SendHttpInsert(id,password,1);
                            /**楼下这段话之后的语句跑不了**/
                            Looper.prepare();
                            Toast.makeText(shangchuan.this, "上传成功", Toast.LENGTH_LONG).show();
                            loadView.cancelProgressDialog();
                            Looper.loop();
                        }else {
                            loadView.cancelProgressDialog();
                            Toast.makeText(shangchuan.this,"上传失败，可能不存在该学生",Toast.LENGTH_SHORT).show();
                        }

//                        MyHelper ggg = new MyHelper(in.this);
//                        db = ggg.getWritableDatabase();
//                        ggg.Insert(db, "name_id", names, IDs);
//                        Date dNow = new Date( );

                    } else {
                        String error_message = "上传失败：" + Result_bean.getError_msg();
                        System.out.println("error_message: " + error_message);
                        Looper.prepare();
                        Toast.makeText(shangchuan.this, error_message, Toast.LENGTH_LONG).show();
                        loadView.cancelProgressDialog();
                        Looper.loop();
                    }


                }catch (Exception e){
                    loadView.cancelProgressDialog();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static String getAuth(String ak,String sk){
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }
}
