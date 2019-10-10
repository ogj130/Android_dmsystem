package com.example.myapplication101.StudentADB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by musei on 2018/1/1.
 */

public class StudentDao {
    private StudentDBOpenHelper helper;

    public StudentDao(Context context) {
        helper = new StudentDBOpenHelper(context);
    }

    /**
     * 添加一条记录
     *
     * @param Studentid
     *          学生id
     * @param name
     *          学生姓名
     * @param phone
     *          学生电话
     * @return  是否添加成功
     */
    public boolean add(String Studentid, String name, String phone,String sex,String age,String banji,String major_na,String college_na,String touxiang_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("studentid", Studentid);
        values.put("name", name);
        values.put("phone", phone);
        values.put("sex",sex);
        values.put("age",age);
        values.put("banji",banji);
        values.put("major_na",major_na);
        values.put("college_na",college_na);
        values.put("touxiang_id",touxiang_id);
        long row=-1;
        try {
            row = db.insert("info", null, values);
        }
        catch (Exception e){
            System.out.println("以下是error：");
            e.printStackTrace();
        }
        finally {
            db.close();
        }
        return row == -1 ? false : true;
    }

    /**
     * 添加一条记录
     *
     * @param info
     *          student domain
     * @return  是否添加成功
     */
    public boolean add(StudentInfo info) {
        return add(String.valueOf(info.getId()), info.getName(),
                info.getPhone(),info.getSex(),String.valueOf(info.getAge()),
                info.getBanji(),info.getMajor_na(),info.getCollege_na(),info.getTouxiang_id());
    }

    /**
     * 删除一条记录
     *
     * @param Studentid
     *          学生id
     * @return  是否删除成功
     */
    public boolean delete(String Studentid) {
        SQLiteDatabase db = helper.getWritableDatabase();

        int count = db.delete("info", "studentid=?", new String[]{Studentid});
        db.close();
        return count <= 0 ? false : true;
    }

    /**
     * 修改一条记录
     *
     * @param studentInfo
     *
     * @return 是否修改成功
     */
    public boolean adjust(StudentInfo studentInfo){
        SQLiteDatabase db=helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("studentid", studentInfo.getId());
        values.put("name", studentInfo.getName());
        values.put("phone", studentInfo.getPhone());
        values.put("touxiang_id",studentInfo.getTouxiang_id());
        int count=db.update("info",values,"studentid=?",new String[]{(String.valueOf(studentInfo.getId()))});
        db.close();
        return count <= 0 ? false : true;
    }

    public Map<String, String> select_stuid(String stu_id){
        SQLiteDatabase db=helper.getReadableDatabase();
        HashMap<String, String> result = new HashMap<>();
        Cursor cursor= db.query("info", new String[]{"studentid", "name", "phone","sex","age","banji",
                "major_na","college_na","touxiang_id"}, null, null, null, null, null);
        int falt=0;
        if(cursor.moveToFirst()){
            do{
                String id=cursor.getString(cursor.getColumnIndex("studentid"));
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String phone=cursor.getString(cursor.getColumnIndex("phone"));
                String sex=cursor.getString(cursor.getColumnIndex("sex"));
                String banji=cursor.getString(cursor.getColumnIndex("banji"));
                String age=cursor.getString(cursor.getColumnIndex("age"));
                String major_na=cursor.getString(cursor.getColumnIndex("major_na"));
                String college_na=cursor.getString(cursor.getColumnIndex("college_na"));
                String touxiang_id=cursor.getString(cursor.getColumnIndex("touxiang_id"));
                if(id.equals(stu_id)){
                    falt=1;
                    result.put("student_id",id);
                    result.put("student_name",name);
                    result.put("student_phone",phone);
                    result.put("student_sex",sex);
                    result.put("student_banji",banji);
                    result.put("student_age",age);
                    result.put("student_major_na",major_na);
                    result.put("student_college_na",college_na);
                    result.put("touxiang_id",touxiang_id);
                    break;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        if(falt==1)
            return result;
        else
            return null;
    }

    /**
     * 查询一条记录
     *
     * @param position
     *          数据在数据库表里面的位置
     * @return  一条记录信息
     */
    public Map<String, String> getStudentInfo(int position) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("info", new String[]{"studentid", "name", "phone","sex",
                "age", "banji","major_na","college_na","touxiang_id"}, null, null, null, null, null);
        cursor.moveToPosition(position);
        String studentid = cursor.getString(0);
        String name = cursor.getString(1);
        String phone = cursor.getString(2);
        String sex=cursor.getString(3);
        String age=cursor.getString(4);
        String banji=cursor.getString(5);
        String major_na=cursor.getString(6);
        String college_na=cursor.getString(7);
        String touxiang_id=cursor.getString(8);

        cursor.close();
        db.close();
        HashMap<String, String> result = new HashMap<>();
        result.put("studentid", studentid);
        result.put("name", name);
        result.put("phone", phone);
        result.put("sex",sex);
        result.put("age",age);
        result.put("banji",banji);
        result.put("major_na",major_na);
        result.put("college_na",college_na);
        result.put("touxiang_id",touxiang_id);

        return result;
    }

    /**
     * 查询数据库里面一共有多少条记录
     *
     * @return  记录的条数
     */
    public int getTotalCount() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("info", null, null, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    /**
     * 删除所有的数据
     */
    public void deleteAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();// 开启事务

        try {
            Cursor cursor = db.query("info", new String[]{"studentid"}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String studentid = cursor.getString(0);
                db.delete("info", "studentid=?", new String[]{studentid});
            }
            cursor.close();
            db.setTransactionSuccessful();// 设置事务执行成功，必须要这一行代码执行，数据才会被提交
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}