package com.example.recyclerviewmvvm;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class StudentRepository {
    private StudentDao studentDao;
    private LiveData<List<Student>> allStudents;

    public StudentRepository(Context context) {
        StudentDatabase database = StudentDatabase.getInstance(context);

        studentDao = database.studentDao();
        allStudents = studentDao.getALLStudents();
    }

    public void insert(Student student){
        new InsertStudentAsyncTask(studentDao).execute(student);
    }

    public void update(Student student){
        new UpdateStudentAsyncTask(studentDao).execute(student);
    }

    public void delete(Student student){
        new DeleteStudentAsyncTask(studentDao).execute(student);
    }

    public void deleteAllStudents(){
        new DeleteAllStudentAsyncTask(studentDao).execute();
    }

    public LiveData<List<Student>> getAllStudents(){
        return allStudents;
    }

    private static class InsertStudentAsyncTask extends AsyncTask<Student,Void,Void>{

        private StudentDao studentDao;

        public InsertStudentAsyncTask(StudentDao studentDao) {
            this.studentDao =studentDao;
        }

        @Override
        protected Void doInBackground(Student... students) {
            studentDao.insert(students[0]);
            return null;
        }
    }

    private static class UpdateStudentAsyncTask extends AsyncTask<Student,Void,Void>{

        private StudentDao studentDao;

        public UpdateStudentAsyncTask(StudentDao studentDao) {
            this.studentDao =studentDao;
        }

        @Override
        protected Void doInBackground(Student... students) {
            studentDao.update(students[0]);
            return null;
        }
    }
    private static class DeleteStudentAsyncTask extends AsyncTask<Student,Void,Void>{

        private StudentDao studentDao;

        public DeleteStudentAsyncTask(StudentDao studentDao) {
            this.studentDao =studentDao;
        }

        @Override
        protected Void doInBackground(Student... students) {
            studentDao.delete(students[0]);
            return null;
        }
    }
    private static class DeleteAllStudentAsyncTask extends AsyncTask<Void,Void,Void>{

        private StudentDao studentDao;

        public DeleteAllStudentAsyncTask(StudentDao studentDao) {
            this.studentDao =studentDao;
        }

        @Override
        protected Void doInBackground(Void... Void) {
            studentDao.deleteAll();
            return null;
        }
    }



}
