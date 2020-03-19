package com.example.recyclerviewmvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
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
    private int ADD_REQUEST_CODE = 1;
    private int EDIT_REQUEST_CODE = 2;
    private Paint p = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resetSequenceAction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            verifyStoragePermissions(this);
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        listStudent = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        viewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        viewModel.getAllStudents().observe(this, data -> {
            adapter = new StudentAdapter(this, (ArrayList<Student>) data, new StudentAdapter.Callback() {
                @Override
                public void onItemClick(Student student) {
                    Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                    intent.putExtra("Id", student.id);
                    intent.putExtra("Name", student.name);
                    intent.putExtra("Description", student.description);
                    intent.putExtra("Age", student.age);
                    startActivityForResult(intent, EDIT_REQUEST_CODE);
                }
            });
            binding.recyclerView.setAdapter(adapter);
            try {
                for (int i = 0; i < data.size(); i++) {
                    Student category = data.get(i);
                    Log.d("Cat_AAA " + i, category.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);

                startActivityForResult(intent, ADD_REQUEST_CODE);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//               viewModel.delete(adapter.getStudent(viewHolder.getAdapterPosition()));
                if (direction == ItemTouchHelper.RIGHT) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                float newDx = dX;
                if (newDx >= 100f) {
                    newDx = 100f;
                }
                super.onChildDraw(c, recyclerView, viewHolder, newDx, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(binding.recyclerView);

//        adapter.setOnClickListener(new StudentAdapter.Callback() {
//            @Override
//            public void onItemClick(Student student) {
//                Toast.makeText(MainActivity.this, ""+student.getName(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(),AddActivity.class);
//                intent.putExtra("Id",student.id);
//                intent.putExtra("Name",student.name);
//                intent.putExtra("Description",student.description);
//                intent.putExtra("Age",student.age);
//                startActivityForResult(intent,EDIT_REQUEST_CODE);
//            }
//        });
    }

    private void verifyStoragePermissions(MainActivity activity) {
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        int permission = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void resetSequenceAction() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("Code_R", String.valueOf(requestCode));

        if (requestCode == ADD_REQUEST_CODE && resultCode == RESULT_OK) {
            String name = data.getStringExtra("Name");
            String des = data.getStringExtra("Description");
            int age = data.getIntExtra("Age", 1);


            Student student = new Student(name, des, age);

            viewModel.insert(student);
            Toast.makeText(this, "Đã thêm thành công", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = data.getIntExtra("Id", -1);
            if ((id == -1)) {
                Toast.makeText(this, "Không thể cập nhập", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = data.getStringExtra("Name");
            String des = data.getStringExtra("Description");
            int age = data.getIntExtra("Age", 1);

            Student student = new Student(name, des, age);
            student.setId(id);
            viewModel.update(student);
            Toast.makeText(this, "Đã cập nhập", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAll_menu:
                viewModel.deleteAll();
                Toast.makeText(this, "Đã Xóa Toàn Bộ Dũ Liệu", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
