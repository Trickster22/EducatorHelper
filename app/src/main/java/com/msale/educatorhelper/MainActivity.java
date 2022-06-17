package com.msale.educatorhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String email;
    private String prof_id;
    private Toolbar toolbar;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String EMAIL = "EMAIL";
        email = intent.getStringExtra(EMAIL);
        toolbar = findViewById(R.id.mainTB);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        db = FirebaseFirestore.getInstance();
        ListView groupList = findViewById(R.id.groupList);
        db.collection("professors")
                .whereEqualTo("mail", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayList<String> groups = (ArrayList<String>) document.get("groups");
                                Collections.sort(groups);
                                prof_id = document.getId();
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                                        android.R.layout.simple_list_item_1, groups);
                                groupList.setAdapter(adapter);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Не удалось получить спиоск групп", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> adapterView,
                                            View itemView,
                                            int position,
                                            long id) {
                        Intent intent = new Intent(MainActivity.this,
                                GroupActivity.class);
                        String s = (String) adapterView.getItemAtPosition(position);
                        intent.putExtra("GROUPID", s);
                        intent.putExtra("PROFID", prof_id);
                        startActivity(intent);
                    }
                };
        //Слушатель связывается со списковым представлением
        groupList.setOnItemClickListener(itemClickListener);
    }

    public void logOut(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void onPause(){
        super.onPause();
    }

}