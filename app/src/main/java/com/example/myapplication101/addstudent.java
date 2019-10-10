package com.example.myapplication101;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication101.StudentADB.StudentDao;
import com.example.myapplication101.StudentADB.StudentInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 添加的注释 github查看
 */
public class addstudent extends AppCompatActivity {

    private String id;
    private String name;
    private String phone;
    private String age;
    private String banji;
    private String major_na;
    private String college_na;
    private String sex="男";


    private EditText et_id;
    private EditText et_name;
    private EditText et_phone;
    private EditText et_major_na;
    private EditText et_college_na;
    private RadioGroup et_sex;

    private Spinner et_banji;
    private Spinner et_age;


    private ListView lv;
    private StudentDao dao;
    private ArrayList<EditText> edts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_addstudent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final LayoutInflater inflater=addstudent.this.getLayoutInflater();
        final View view2=inflater.inflate(R.layout.yanzheng,null,false);
        Context mContext=addstudent.this;
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setView(view2);
        builder.setCancelable(false);
        final AlertDialog alert=builder.create();

        view2.findViewById(R.id.qd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText=view2.findViewById(R.id.mm);
                if(editText.getText().toString().trim().equals("123456")){
                    Toast.makeText(getApplicationContext(),"验证成功!正在删除",Toast.LENGTH_SHORT).show();
                    dao.deleteAll();
                    Toast.makeText(getApplicationContext(),"删除全部数据成功", Toast.LENGTH_SHORT).show();
                    lv.setAdapter(new MyAdapter());
                    alert.dismiss();
                }
                else {
                    Toast.makeText(getApplicationContext(),"验证失败",Toast.LENGTH_SHORT).show();
                    alert.dismiss();
                }
            }
        });
        switch (item.getItemId()) {
            case R.id.item_deleteall:
                alert.show();
                break;
            case R.id.quit:
                addstudent.this.finish();
                break;
            case R.id.about:
                Intent intent2 = new Intent();
                intent2.setClass(addstudent.this, aboutPage.class);
                startActivity(intent2);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private List<String> delist1,delist2;
    private ArrayAdapter<String> arrayAdapter1,arrayAdapter2;
    private void Spinner_init(){
        et_age = (Spinner) findViewById(R.id.spin_age);
        et_banji= (Spinner) findViewById(R.id.spin_banji);
        delist1=new ArrayList<String>();
        delist2=new ArrayList<String>();
        for(int i=1;i<50;i++){
            delist1.add(String.valueOf(i)+"岁");
        }
        for(int i=1;i<4;i++){
            delist2.add(String.valueOf(i)+"班");
        }
        arrayAdapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,delist1);
        arrayAdapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,delist2);

        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        et_age.setAdapter(arrayAdapter1);
        et_banji.setAdapter(arrayAdapter2);
        et_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                age=delist1.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        et_banji.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                banji=delist2.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void initView() {
        setContentView(R.layout.activity_addstudent);
        et_id = (EditText) findViewById(R.id.et_id);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_sex =(RadioGroup) findViewById(R.id.student_sex);
        et_college_na = (EditText) findViewById(R.id.et_college_na);
        et_major_na = (EditText) findViewById(R.id.et_major_na);
        Spinner_init();
        lv = (ListView) findViewById(R.id.lv);

        dao = new StudentDao(this);
        lv.setAdapter(new MyAdapter());

        View view = View.inflate(addstudent.this,R.layout.item,null);
        final LayoutInflater inflater=addstudent.this.getLayoutInflater();
        final View view1=inflater.inflate(R.layout.authorize_dialog,null,false);

        Context mContext=addstudent.this;

        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setView(view1);
        builder.setCancelable(false);
        final AlertDialog alert=builder.create();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Map<String, String> de = dao.getStudentInfo(position);
                EditText stu_id = (EditText) view1.findViewById(R.id.student_id);
                EditText stu_name = (EditText) view1.findViewById(R.id.student_name);
                EditText stu_sex = (EditText) view1.findViewById(R.id.student_sex);
                EditText major_na = (EditText) view1.findViewById(R.id.student_major_name);
                EditText college_na = (EditText) view1.findViewById(R.id.student_college_name);
                EditText tel_number = (EditText) view1.findViewById(R.id.student_tel);
                EditText banji = (EditText) view1.findViewById(R.id.student_banji);
                EditText age=(EditText) view1.findViewById(R.id.student_age);
                edts=new ArrayList<EditText>();
                edts.add(stu_id);
                edts.add(stu_name);
                edts.add(stu_sex);
                edts.add(major_na);
                edts.add(college_na);
                edts.add(tel_number);
                edts.add(banji);
                edts.add(age);
                setEdictale(false);// 无法编译
                ImageView image_title=(ImageView)view1.findViewById(R.id.student_image);
                if(de.get("touxiang_id")!=null&&!de.get("touxiang_id").equals("1")){
                    image_title.setImageBitmap(getimage(de.get("touxiang_id")));
                }
                stu_id.setText(de.get("studentid"));
                stu_name.setText(de.get("name"));
                stu_sex.setText(de.get("sex"));
                System.out.println(de.get("age"));
                age.setText(de.get("age"));
                banji.setText(de.get("banji"));
                major_na.setText(de.get("major_na"));
                college_na.setText(de.get("college_na"));
                tel_number.setText(de.get("phone"));
                banji.setText(de.get("banji"));
                alert.show();
            }
        });
        view1.findViewById(R.id.queding2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"信息框已经关闭",Toast.LENGTH_LONG).show();
                alert.dismiss();
                lv.setAdapter(new MyAdapter());  //刷新
            }
        });
        final Button edit=view1.findViewById(R.id.bianyi);
        view1.findViewById(R.id.bianyi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit.getText().toString().equals("编译")) {
                    Toast.makeText(getApplicationContext(), "开始编译", Toast.LENGTH_SHORT).show();
                    edit.setText("完成");
                    setEdictale(true);
                    Toast.makeText(getApplicationContext(), "完成编译", Toast.LENGTH_SHORT).show();
                }
                else if(edit.getText().toString().equals("完成")){
                    setEdictale(false);
                    edit.setText("编译");
                    //修改数据库的 逻辑代码
                    String id_xiugai=edts.get(0).getText().toString();
                    StudentDao ggg=new StudentDao(addstudent.this);
                    StudentInfo stuData=new StudentInfo();
                    Map<String,String> st=ggg.select_stuid(id_xiugai);
                    EditText stu_name = (EditText) view1.findViewById(R.id.student_name);
                    EditText stu_sex = (EditText) view1.findViewById(R.id.student_sex);
                    EditText major_na = (EditText) view1.findViewById(R.id.student_major_name);
                    EditText college_na = (EditText) view1.findViewById(R.id.student_college_name);
                    EditText tel_number = (EditText) view1.findViewById(R.id.student_tel);
                    EditText banji = (EditText) view1.findViewById(R.id.student_banji);
                    EditText age=(EditText) view1.findViewById(R.id.student_age);
                    stuData.setName(stu_name.getText().toString());
                    stuData.setSex(stu_sex.getText().toString());
                    stuData.setBanji(banji.getText().toString());
                    stuData.setAge(Integer.parseInt(age.getText().toString()));
                    stuData.setMajor_na(major_na.getText().toString());
                    stuData.setCollege_na(college_na.getText().toString());
                    stuData.setPhone(tel_number.getText().toString());
                    stuData.setId(Long.valueOf(st.get("student_id")));
                    stuData.setTouxiang_id(st.get("touxiang_id"));
                    if(ggg.adjust(stuData)){
                        Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"修改失败",Toast.LENGTH_SHORT).show();
                        alert.dismiss();
                    }
                }
            }
        });
    }
    private void setEdictale(boolean edictale){
        edts.get(0).setEnabled(false);
        for (int i = 1; i < edts.size(); i++) {  //id 不能修改
            edts.get(i).setFocusable(edictale);
            edts.get(i).setFocusableInTouchMode(edictale);
            edts.get(i).setEnabled(edictale);
        }
        edts.get(1).setFocusable(true);
        edts.get(1).setFocusableInTouchMode(true);
        edts.get(1).requestFocus();
    }

    /**
     * button的点击事件，用来添加学生信息
     * @param view
     */
    public void addStudent(View view) {
        id =  et_id.getText().toString().trim();
        name =  et_name.getText().toString().trim();
        phone =  et_phone.getText().toString().trim();
        major_na = et_major_na.getText().toString().trim();
        college_na = et_college_na.getText().toString().trim();
        et_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton=(RadioButton) findViewById(checkedId);
                sex= radioButton.getText().toString();
            }
        });
        if(id.equals("")||phone.equals("")||major_na.equals("")||college_na.equals("")||sex.equals("")||name.equals("")){
            Toast.makeText(getApplicationContext(),"不能空余属性",Toast.LENGTH_SHORT).show();
        }
        else {
            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "数据不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else {
                //判断在数据库是否存在该学生
                StudentDao ggg = new StudentDao(addstudent.this);
                Map<String, String> st = ggg.select_stuid(id);
                if (st == null) {
                    // 保存数据到数据库，并且同步显示到界面
                    StudentInfo info = new StudentInfo();
                    info.setId(Long.valueOf(id));
                    info.setName(name);
                    info.setSex(sex);
                    info.setPhone(phone);
                    info.setAge(Integer.valueOf(age.substring(0,age.length()-1)));
                    info.setBanji(banji);
                    info.setCollege_na(college_na);
                    info.setMajor_na(major_na);

                    boolean result = dao.add(info);
                    if (result) {
                        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                        lv.setAdapter(new MyAdapter());
                        et_id.setText("");
                        et_major_na.setText("");
                        et_college_na.setText("");
                        et_name.setText("");
                        et_phone.setText("");

                    } else {
                        Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "已经存在该学生", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dao.getTotalCount();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(addstudent.this,R.layout.item,null);
            TextView tv_item_id = (TextView) view.findViewById(R.id.tv_item_id);
            TextView tv_item_name = (TextView) view.findViewById(R.id.tv_item_name);
            TextView tv_item_phone = (TextView) view.findViewById(R.id.tv_item_phone);
            ImageView touxiang_image=(ImageView) view.findViewById(R.id.student_image);
            ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            final Map<String, String> map = dao.getStudentInfo(position);
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result = dao.delete(map.get("studentid"));
                    if (result) {
                        Toast.makeText(addstudent.this,"删除成功", Toast.LENGTH_SHORT).show();
                        lv.setAdapter(new MyAdapter());
                    }
                }
            });
            //界面只显示三列即可
            tv_item_id.setText(String.valueOf(map.get("studentid")));
            tv_item_name.setText(map.get("name"));
            tv_item_phone.setText(map.get("phone"));

            //此处image 初始使用同一个默认图片
            if(map.get("touxiang_id")==null||map.get("touxiang_id").equals("1")){
                touxiang_image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.jjy1));
            }
            else {
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
}
