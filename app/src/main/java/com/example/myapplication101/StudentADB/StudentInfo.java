package com.example.myapplication101.StudentADB;

/**
 * 学生信息的实体类
 */
public class StudentInfo {
    private Long id;
    private String name="";
    private String phone="";
    private String sex="";
    private int age=0;
    private String major_na="";
    private String college_na="";
    private String banji="";
    private String touxiang_id="1";

    public void setTouxiang_id(String touxiang_id) {
        this.touxiang_id = touxiang_id;
    }

    public String getTouxiang_id() {
        return touxiang_id;
    }

    public void setBanji(String banji) {
        this.banji = banji;
    }

    public String getBanji() {
        return banji;
    }

    public void setCollege_na(String college_na) {
        this.college_na = college_na;
    }

    public String getCollege_na() {
        return college_na;
    }

    public String getMajor_na() {
        return major_na;
    }

    public void setMajor_na(String major_na) {
        this.major_na = major_na;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    @Override
    public String toString() {
        return "StudentInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", sex='"+sex+'\''+
                "，age='"+age+'\''+
                ", banji='"+banji+'\''+
                ", major_na='"+major_na+'\''+
                ", college_na='"+college_na+'\''+
                ",touxiang_id='"+touxiang_id+'\''+
                '}';
    }
}
