package com.msale.educatorhelper;

public class Student {
    String name, m1, m2, id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Student(){}

    public Student(String name, String m1, String m2, String id) {
        this.name = name;
        if (m1.equals("")) m1 = "_";
        this.m1 = m1;
        if (m2.equals("")) m2 = "_";
        this.m2 = m2;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getM1() {
        return m1;
    }

    public void setM1(String m1) {
        this.m1 = m1;
    }

    public String getM2() {
        return m2;
    }

    public void setM2(String m2) {
        this.m2 = m2;
    }
}
