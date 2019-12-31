package com.example.recyclerviewmvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.recyclerviewmvvm.databinding.ActivityAddBinding;

public class AddActivity extends AppCompatActivity {


    ActivityAddBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_add);

        binding.picker.setMaxValue(0);
        binding.picker.setMaxValue(100);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();


        int id = intent.getIntExtra("Id",-1);
        if (id != -1){
            setTitle("Edit Student");
            String name  = intent.getStringExtra("Name");
            String des = intent.getStringExtra("Description");
            int age = intent.getIntExtra("Age",1);
            binding.editTextName.setText(name);
            binding.editTextDes.setText(des);
            binding.picker.setValue(age);
        }
        else {
            setTitle("Add Student");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save :
                saveStudent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveStudent() {
        String name = binding.editTextName.getText().toString().trim();
        String des = binding.editTextDes.getText().toString().trim();
        int age = binding.picker.getValue();

        if(name.isEmpty() || des.isEmpty()){
            Toast.makeText(this, "Làm ơn nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
        }

        Intent data = new Intent();
        data.putExtra("Name",name);
        data.putExtra("Description",des);
        data.putExtra("Age",age);

        int id = getIntent().getIntExtra("Id",-1);
        if (id != -1){
            data.putExtra("Id",id);

        }
        setResult(RESULT_OK,data);
        finish();
    }
}
