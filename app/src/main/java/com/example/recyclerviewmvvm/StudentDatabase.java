package com.example.recyclerviewmvvm;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Student.class},version = 2)
public abstract class StudentDatabase extends RoomDatabase {

    public static StudentDatabase instance;

    public abstract StudentDao studentDao();


    public static synchronized StudentDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),StudentDatabase.class,"/storage/emulated/0/folder/app_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Log.d("DbPath",db.getPath());
                            new PopulateDbAsyncTask(instance).execute();
                            Log.d("ONCREATE","Database has been created.");
                        }

                        @Override
                        public void onOpen(@NonNull SupportSQLiteDatabase db) {
                            super.onOpen(db);
                            Log.d("ONOPEN","Database has been opened.");
                            Log.d("DbPath",db.getPath());
                        }
                    })
                    .build();

        }
        return instance;
    }



    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{

        private StudentDao studentDao;

        public PopulateDbAsyncTask(StudentDatabase db) {
            studentDao = db.studentDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            studentDao.insert(new Student("Đinh Công Long","Hello World",22));
            studentDao.insert(new Student("Trần Ngọc Hà","Hello World ",29));
            studentDao.insert(new Student("Trần Tùng","Hello World",19));
            return null;
        }
    }
}
