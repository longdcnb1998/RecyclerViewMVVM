package com.example.recyclerviewmvvm;


import android.app.Application;
import android.content.ClipData;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StudentViewModel extends AndroidViewModel{

    private StudentRepository repository;
    private LiveData<List<Student>> data;
//
    public StudentViewModel(Application context) {
        super(context);
        repository = new StudentRepository(context);
        data = repository.getAllStudents();
    }
//
    public void insert(Student student){
        repository.insert(student);
    }

    public void update(Student student){
        repository.update(student);
    }

    public void delete(Student student){
        repository.delete(student);
    }

    public void deleteAll(){}
//
    public LiveData<List<Student>> getAllStudents(){
        return data;
    }

//    private MediatorLiveData<List<Student>> liveStudent;
//
//    public MediatorLiveData<List<Student>> getLiveStudent(){
//        if (liveStudent==null){
//            liveStudent = new MediatorLiveData<>();
//        }
//        return liveStudent;
//    }
//
//    public StudentViewModel() {
//        super();
//    }
//
//    public void loadData( final  Context context){
//        repository = new StudentRepository(context);
//        data = (LiveData<List<Student>>) repository.getAllStudents();
//        if (data != null){
//            liveStudent.postValue((List<Student>) data);
//        }
//    }
}
