package com.example.myapplication101;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dianming extends AppCompatActivity {


    private int proetion=1;

    private CheckBox checkBox;

    private ListView lv2;
    private StudentDao dao;

    public String id = "1", password = "1";

    //定义返回值，判断图片的来源
    private int Photo_ALBUM = 1, CAMERA = 2;
    //定义图片的路径
    private String ImagePath = null;
    //定义图片的uri
    private Uri imageUri;

    int FLAG = 0;
    private Bitmap bp = null;
    private AlertDialog alert;

    //加载框
    LoadView loadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_dianming);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        lv2 = (ListView) findViewById(R.id.lv_dianming);
        dao = new StudentDao(dianming.this);
        lv2.setAdapter(new dianming.MyAdapter());

        View view = View.inflate(dianming.this, R.layout.item_3, null);
        final LayoutInflater inflater = dianming.this.getLayoutInflater();
        final View view1 = inflater.inflate(R.layout.yanzheng_page, null, false);

        Context mContext = dianming.this;

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view1);
        builder.setCancelable(false);
        alert = builder.create();

        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long ide) {
                final Map<String, String> de = dao.getStudentInfo(position);
                checkBox = findViewById(R.id.falt);
                proetion=position;
                TextView stu_id = (TextView) view1.findViewById(R.id.student_id);
                TextView stu_name = (TextView) view1.findViewById(R.id.student_name);
                TextView stu_sex = (TextView) view1.findViewById(R.id.student_sex);
                TextView major_na = (TextView) view1.findViewById(R.id.student_major_name);
                TextView college_na = (TextView) view1.findViewById(R.id.student_college_name);
                TextView tel_number = (TextView) view1.findViewById(R.id.student_tel);
                TextView banji = (TextView) view1.findViewById(R.id.student_banji);
                TextView age = (TextView) view1.findViewById(R.id.student_age);
                ImageView image_title = (ImageView) view1.findViewById(R.id.student_image);
                if (de.get("touxiang_id") != null && !de.get("touxiang_id").equals("1")) {
                    image_title.setImageBitmap(getimage(de.get("touxiang_id")));
                }
                stu_id.setText(de.get("studentid"));

                stu_name.setText(de.get("name"));
                stu_sex.setText(de.get("sex"));
                age.setText(de.get("age"));
                banji.setText(de.get("banji"));
                major_na.setText(de.get("major_na"));
                college_na.setText(de.get("college_na"));
                tel_number.setText(de.get("phone"));
                banji.setText(de.get("banji"));
                alert.show();
            }
        });
        view1.findViewById(R.id.tuichu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(dianming.this, "签到失败！", Toast.LENGTH_LONG).show();
                alert.dismiss();
            }
        });
        view1.findViewById(R.id.qiandao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"信息框已经关闭",Toast.LENGTH_LONG).show();
//                alert.dismiss();
                id = ((TextView) view1.findViewById(R.id.student_id)).getText().toString().trim();  //给id 值 去判断人脸
                checkBox=lv2.getChildAt(proetion).findViewById(R.id.falt);

                new AlertDialog.Builder(dianming.this)
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
                                        + File.separator + id + ".jpg");
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
                                ImagePath = outputImage.getAbsolutePath();
                                Log.i("拍照的路径", ImagePath);

                                //跳转相机 进行拍照
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //相片输出路径
                                //指定
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                //返回照片路径
                                startActivityForResult(intent, CAMERA);
                            }
                        }).show();  //显示的对话框
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //生成加载框
        loadView = new LoadView();
        loadView.buildProgressDialog("识别中...", dianming.this);

        // 相册选择图片
        if (requestCode == Photo_ALBUM) {
            if (data != null) {       //开启了相册，但是没有选照片
                Uri uri = data.getData();
                //从uri获取内容的cursor
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToNext();
                ImagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));   //获得图片的绝对路径
                cursor.close();
                Log.i("图片路径", ImagePath);
                bp = getimage(ImagePath);
                //  iv_picture.setImageBitmap(bp);
                runthread();      //开启线程，传入图片
            }
        } else if (requestCode == CAMERA) {

            bp = getimage(ImagePath);
            //  iv_picture.setImageBitmap(bp);
            runthread();  //开启线程，传入图片
        }
    }


    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dao.getTotalCount();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(dianming.this, R.layout.item_3, null);
            TextView tv_item_id = (TextView) view.findViewById(R.id.tv_item_id_3);
            TextView tv_item_name = (TextView) view.findViewById(R.id.tv_item_name_3);
            TextView tv_item_phone = (TextView) view.findViewById(R.id.tv_item_phone_3);
            ImageView touxiang_image = (ImageView) view.findViewById(R.id.student_image_3);
            final Map<String, String> map = dao.getStudentInfo(position);

            //界面只显示三列即可
            tv_item_id.setText(String.valueOf(map.get("studentid")));
            tv_item_name.setText(map.get("name"));
            tv_item_phone.setText(map.get("phone"));

            //此处image 初始使用同一个默认图片
            if (map.get("touxiang_id") == null || map.get("touxiang_id").equals("1")) {
                touxiang_image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.jjy1));
            } else {
                touxiang_image.setImageBitmap(getimage(map.get("touxiang_id")));
            }
            return view;
        }

        @Override
        public StudentInfo getItem(int position) {
//            return list.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }


    //缩放
    public Bitmap getimage(String srcPath) {
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
    public Bitmap compressImage(Bitmap image) {
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

    public byte[] getBytesByBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bitmap.getByteCount());
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    // 调用api  进行人脸识别 不能用不是人脸的头像
    void runthread() {
        // 进行人脸了
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://aip.baidubce.com/rest/2.0/face/v3/search";
                try {
                    byte[] byte1 = getBytesByBitmap(bp);
                    String image1 = Base64Util.encode(byte1);

                    Map<String, Object> map = new HashMap<>();

                    map.put("image", image1);
                    map.put("liveness_control", "NORMAL");
                    map.put("group_id_list", "face");
                    //Log.i(map.get("group_id_list").toString(),"msg");
                    map.put("image_type", "BASE64");
                    map.put("quality_control", "LOW");

                    //转为json 数据
                    String param = GsonUtils.toJson(map);

                    String clientId = "vKM79jsgy0VqG2mpkiBZ3GL4";

                    // 官网获取的 Secret Key 更新为你注册的
                    String clientSecret = "kAKddEXfO60iRckxSS0ermsNGGxh98t5";
                    String accessToken = getAuth(clientId, clientSecret);


                    String result = HttpUtil.post(url, accessToken, "application/json", param);

                    Gson gson = new Gson();
                    Search_result_bean Result_bean = gson.fromJson(result, Search_result_bean.class);

                    int Error_code = Result_bean.getError_code();
                    if (Error_code == 0) {        // 误差结果返回的是0 即为签到成功！！！
                        double score = Result_bean.getResult().getUser_list().get(0).getScore();   //一层层进入，获取到score
                        String user = Result_bean.getResult().getUser_list().get(0).getUser_id();   //获取用户名

                        if (score >= 78.0) {                                  //分数大于78.0分，判断为同一个人，提示打卡成功
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            BackTool.SendHttpInsert(user, df.format(new Date()), 2);
                            Looper.prepare();
                            //关闭对话框 并且添加勾选
                            //Toast.makeText(dianming.this, "签到成功！", Toast.LENGTH_LONG).show();
                            loadView.cancelProgressDialog();
                            alert.dismiss();
                            checkBox.setChecked(true);
                            loadView.cancelProgressDialog();//关闭加载框

                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(dianming.this, "签到失败！照片不在人脸库", Toast.LENGTH_LONG).show();
                            loadView.cancelProgressDialog();
                            alert.dismiss();
                            loadView.cancelProgressDialog();//关闭加载框
                            Looper.loop();
                        }


                    } else {
                        String error_message = "签到失败：" + Result_bean.getError_msg();
                        Looper.prepare();
                        Toast.makeText(dianming.this, error_message, Toast.LENGTH_LONG).show();
                        loadView.cancelProgressDialog();
                        alert.dismiss();
                        loadView.cancelProgressDialog();//关闭加载框
                        Looper.loop();
                    }

                } catch (Exception e) {
                    Log.i("错误", "ai");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static String getAuth(String ak, String sk) {
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
