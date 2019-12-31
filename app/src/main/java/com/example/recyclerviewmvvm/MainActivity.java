package com.example.recyclerviewmvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.recyclerviewmvvm.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    private StudentViewModel viewModel;
    private ArrayList<Student> listStudent;
    private StudentAdapter adapter;
    private int ADD_REQUEST_CODE =1;
    private int EDIT_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        listStudent = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        adapter = new StudentAdapter();
        viewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        viewModel.getAllStudents().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(List<Student> students) {
                Log.d("AAA", String.valueOf(students.size()));
                adapter.setStudents(students);
                binding.recyclerView.setAdapter(adapter);
            }
        });

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddActivity.class);

                startActivityForResult(intent,ADD_REQUEST_CODE);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
               viewModel.delete(adapter.getStudent(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(binding.recyclerView);

        adapter.setOnClickListener(new StudentAdapter.Callback() {
            @Override
            public void onItemClick(Student student) {
                Toast.makeText(MainActivity.this, ""+student.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),AddActivity.class);
                intent.putExtra("Id",student.id);
                intent.putExtra("Name",student.name);
                intent.putExtra("Description",student.description);
                intent.putExtra("Age",student.age);
                startActivityForResult(intent,EDIT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("Code_R", String.valueOf(requestCode));

        if (requestCode == ADD_REQUEST_CODE && resultCode == RESULT_OK){
            String name = data.getStringExtra("Name");
            String des = data.getStringExtra("Description");
            int age = data.getIntExtra("Age",1);


            Student student = new Student(name,des,age);

            viewModel.insert(student);
            Toast.makeText(this, "Đã thêm thành công", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK){
            int id = data.getIntExtra("Id",-1);
            if ((id == -1)){
                Toast.makeText(this, "Không thể cập nhập", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = data.getStringExtra("Name");
            String des = data.getStringExtra("Description");
            int age = data.getIntExtra("Age",1);

            Student student = new Student(name,des,age);
            student.setId(id);
            viewModel.update(student);
            Toast.makeText(this, "Đã cập nhập", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteAll_menu :
                viewModel.deleteAll();
                Toast.makeText(this, "Đã Xóa Toàn Bộ Dũ Liệu", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
