package com.msale.educatorhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {
    private String groupId;
    private String profId;
    RecyclerView recyclerView;
    ArrayList<Student> studentArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Intent intent = getIntent();
        String GROUPID = "GROUPID";
        TextView tv = findViewById(R.id.groupNameTV);
        groupId = intent.getStringExtra(GROUPID);
        profId = intent.getStringExtra("PROFID");
        Toolbar toolbar = findViewById(R.id.groupTB);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv.setText(groupId);
        dialog = new Dialog(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        parseData();
    }

    private void parseData() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        studentArrayList = new ArrayList<>();
        db.collection("students")
                .whereEqualTo("group", groupId)
                //.orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                Map<String, Map<Object, String>> m = (Map<String, Map<Object, String>>) document.get("marks");
                                String id = document.getId();
                                String m1 = m.get(profId).get("m1");
                                String m2 = m.get(profId).get("m2");
                                studentArrayList.add(new Student(name, m1, m2, id));
                                Comparator<Student> comparator = new Comparator<Student>() {
                                    @Override
                                    public int compare(Student o1, Student o2) {
                                        return o1.getName().compareTo(o2.getName());
                                    }
                                };
                                Collections.sort(studentArrayList, comparator);
                                myAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(GroupActivity.this, "Не удалось получить спиоск студентов", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        MyAdapter.onStudentClickListener studentClickListener = new MyAdapter.onStudentClickListener() {
            @Override
            public void onStudentClick(Student student, int position) {
                openDialog(student.name, student.m1, student.m2, student.id, position);
            }
        };
        myAdapter = new MyAdapter(GroupActivity.this, studentArrayList, studentClickListener);
        recyclerView.setAdapter(myAdapter);
    }

    private void openDialog(String name, String m1, String m2, String id, int position) {
        dialog.setContentView(R.layout.student_layout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText editTextM1 = dialog.findViewById(R.id.m1ET);
        if (!m1.equals("_")) editTextM1.setText(m1);
        //editTextM1.setBackground(getDrawable(R.drawable.custom_input));

        EditText editTextM2 = dialog.findViewById(R.id.m2ET);
        if (!m2.equals("_")) editTextM2.setText(m2);

        TextView textViewStudentName = dialog.findViewById(R.id.nameTV);
        textViewStudentName.setText(name);

        Button cancelBtn = dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button saveBtn = dialog.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isError = false;
                String fm = editTextM1.getText().toString();
                String sm = editTextM2.getText().toString();
                if(!fm.equals("") && (!fm.matches("\\d+") ||
                Integer.parseInt(fm) < 25 ||
                Integer.parseInt(fm) > 54)){
                    Toast.makeText(GroupActivity.this, "некорректное значение первого модуля!", Toast.LENGTH_SHORT).show();
                    editTextM1.setBackground(getDrawable(R.drawable.error_input));
                    isError = true;
                }
                if(!sm.equals("") && (!sm.matches("\\d+") ||
                        Integer.parseInt(sm) < 25 ||
                        Integer.parseInt(sm) > 54)){
                    Toast.makeText(GroupActivity.this, "некорректное значение второго модуля!", Toast.LENGTH_SHORT).show();
                    editTextM2.setBackground(getDrawable(R.drawable.error_input));
                    isError = true;
                }
                if(isError) return;
                db = FirebaseFirestore.getInstance();
                db.collection("students").document(id)
                        .update(
                                "marks." + profId + ".m1", fm,
                                "marks." + profId + ".m2", sm
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(GroupActivity.this, "Данные сохранены", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(GroupActivity.this, "Ошибка. Не удалось сохранить данные", Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog.dismiss();
                parseData();
            }
        });


        dialog.show();


    }


    @Override
    protected void onPause() {
        super.onPause();
    }
}