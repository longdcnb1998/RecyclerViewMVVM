package com.example.recyclerviewmvvm;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_students")
public class Student {

    @PrimaryKey(autoGenerate = true)
    public int id;
//    @ColumnInfo(name = "name_column")
    public String name;
//    @ColumnInfo(name = "des_column")
    public String description;
//    @ColumnInfo(name = "age_column")
    public int age;


    public Student(String name, String description, int age) {
        this.name = name;
        this.description = description;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
