package com.example.myapplication101;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication101.StudentADB.StudentDao;
import com.example.myapplication101.StudentADB.StudentInfo;

import java.util.HashMap;
import java.util.Map;

public class findstudent extends AppCompatActivity {

    private Map<String, String> select_student;
    private Button queding,tuichu;
    private TextView Select_id;
    private ListView lv;
    private StudentDao dao;
    private Context mContext;
    private AlertDialog alert=null;
    private AlertDialog.Builder builder=null;
    private View view_bu;

    private TextView stu_id,stu_name,stu_sex,stu_age,stu_banji,smajor_na,scollege_na,tel_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_findstudent);
        initView(); //初始化
        // 隐藏自带栏目
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        Select_id=(TextView) findViewById(R.id.find_student_id);
        queding=(Button) findViewById(R.id.find_true);
        tuichu=(Button) findViewById(R.id.find_tuichu);

        mContext=findstudent.this;
        builder=new AlertDialog.Builder(mContext);

        //加载View
        final LayoutInflater inflater=findstudent.this.getLayoutInflater();
        view_bu=inflater.inflate(R.layout.authorize_dialog,null,false);

        //设置View绑定
        builder.setView(view_bu);
        builder.setCancelable(false);
        alert=builder.create();

        // diag窗口控件 初始化
        stu_id=(TextView) view_bu.findViewById(R.id.student_id);
        stu_name=view_bu.findViewById(R.id.student_name);
        stu_sex=view_bu.findViewById(R.id.student_sex);
        stu_age=view_bu.findViewById(R.id.student_age);
        stu_banji=view_bu.findViewById(R.id.student_banji);

        smajor_na=view_bu.findViewById(R.id.student_major_name);
        scollege_na=view_bu.findViewById(R.id.student_college_name);
        tel_number=view_bu.findViewById(R.id.student_tel);


        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查询该学生的信息并赋值给上面几个
                if (TextUtils.isEmpty(Select_id.getText().toString())){
                    Toast.makeText(getApplicationContext(),"请输入需要查询的id",Toast.LENGTH_SHORT).show();
                }
                else {
                    String stu_select_id = Select_id.getText().toString();
                    select_student = dao.select_stuid(stu_select_id);
                    if (select_student.get("student_id") == null||select_student==null) {
                        Toast.makeText(getApplicationContext(), "未查询到该学生信息，请核实", Toast.LENGTH_SHORT).show();
                    } else {
                        String student_id = select_student.get("student_id");
                        String student_name = select_student.get("student_name");
                        String student_phone = select_student.get("student_phone");
                        String student_sex = select_student.get("student_sex");
                        String student_banji = select_student.get("student_banji");
                        String major_na = select_student.get("student_major_na");
                        String college_na = select_student.get("student_college_na");
                        String student_age=select_student.get("student_age");

                        stu_id.setText(student_id);
                        stu_name.setText(student_name);
                        stu_sex.setText(student_sex);
                        stu_banji.setText(student_banji);
                        tel_number.setText(student_phone);
                        smajor_na.setText(major_na);
                        scollege_na.setText(college_na);
                        stu_age.setText(student_age);
                        alert.show();
                    }
                }
            }
        });
        view_bu.findViewById(R.id.queding2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"查询信息框已经关闭",Toast.LENGTH_LONG).show();
                alert.dismiss();
            }
        });
        tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(findstudent.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_findstudent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login3:
                Toast.makeText(this,"注销成功", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent();
                intent1.setClass(findstudent.this, loginPage.class);
                startActivity(intent1);
                findstudent.this.finish();
                break;
            case R.id.quit3:
                findstudent.this.finish();
                break;
            case R.id.about3:
                Intent intent2 = new Intent();
                intent2.setClass(findstudent.this, aboutPage.class);
                startActivity(intent2);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initView() {
        setContentView(R.layout.activity_findstudent);
        lv = (ListView) findViewById(R.id.lv);
        dao = new StudentDao(this);
        lv.setAdapter(new MyAdapter());
    }

    private class MyAdapter extends BaseAdapter {
        // @Override
        public int getCount() {
            return dao.getTotalCount();
        }
        //@Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = View.inflate(findstudent.this,R.layout.item_2,null);
            TextView tv_item_id = view.findViewById(R.id.tv_item_id_2);
            TextView tv_item_name = view.findViewById(R.id.tv_item_name_2);
            TextView tv_item_phone = view.findViewById(R.id.tv_item_phone_2);
            final Map<String, String> map = dao.getStudentInfo(position);
            tv_item_id.setText(String.valueOf(map.get("studentid")));
            tv_item_name.setText(map.get("name"));
            tv_item_phone.setText(map.get("phone"));

            return view;
        }

        @Override
        public StudentInfo getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
