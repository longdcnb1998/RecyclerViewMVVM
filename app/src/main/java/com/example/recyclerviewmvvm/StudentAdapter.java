package com.example.recyclerviewmvvm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewmvvm.databinding.ItemStudentBinding;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder>  {

    private Context mContext;
    private ArrayList<Student> students;
    private Callback callback;

    public StudentAdapter(Context mContext, ArrayList<Student> students, Callback callback) {
        this.mContext = mContext;
        this.students = students;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStudentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_student,parent,false);
        ViewHolder holder = new ViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewHolder viewHolder = holder;
        viewHolder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }
    public void setStudents(List<Student> students){
        this.students = (ArrayList<Student>) students;
        notifyDataSetChanged();
    }

    public Student getStudent(int position){
        return students.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemStudentBinding binding;

        public ViewHolder(@NonNull ItemStudentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (callback != null && position != RecyclerView.NO_POSITION){
                        callback.onItemClick(students.get(position));
                    }
                }
            });
        }

        public void bindData(int position){
            Student student = students.get(position);
            if (student != null){
                binding.name.setText(student.getName());
                binding.des.setText(student.getDescription());
                binding.age.setText(String.valueOf(student.age));
            }
        }
    }

    public interface Callback{
        void onItemClick(Student  student);
    }

    public void setOnClickListener(Callback callback){
        this.callback = callback;
    }
}
