package com.msale.educatorhelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Student> studentArrayList;
    int row_index = -1;

    interface onStudentClickListener{
        void onStudentClick(Student student, int position);
    }

    private final onStudentClickListener onClickListener;

    public MyAdapter(Context context, ArrayList<Student> studentArrayList, onStudentClickListener onClickListener) {
        this.context = context;
        this.studentArrayList = studentArrayList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Student student = studentArrayList.get(position);
        holder.name.setText(student.name);
        holder.m1.setText(student.m1);
        holder.m2.setText(student.m2);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onStudentClick(student, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, m1, m2;
        LinearLayout linearLayout;
        CardView cv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nametv);
            m1 = itemView.findViewById(R.id.m1tv);
            m2 = itemView.findViewById(R.id.m2tv);
            linearLayout = itemView.findViewById(R.id.itemLL);
            cv = itemView.findViewById(R.id.itemCV);
        }
    }
}
