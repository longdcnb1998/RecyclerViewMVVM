package com.example.recyclerviewmvvm;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert
    void insert(Student student);

    @Update
    void update(Student student);

    @Delete
    void delete(Student student);

    @Query("DELETE FROM tbl_students ")
    void deleteAll();

    @Query("SELECT * FROM tbl_students ORDER BY age  DESC")
    LiveData< List<Student>> getALLStudents();
}
