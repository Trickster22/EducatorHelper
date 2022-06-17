package com.msale.educatorhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EnterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI(currentUser);
        }
    }

    public void onResume() {
        EditText edp = findViewById(R.id.editTextTextPassword);
        EditText edm = findViewById(R.id.editTextTextEmailAddress);
        edp.setText("");
        edm.setText("");
        super.onResume();
    }
    public void signing(View view){
        EditText editTextPassword = findViewById(R.id.editTextTextPassword);
        EditText editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        String password = editTextPassword.getText().toString();
        String email = editTextEmail.getText().toString();

        if (password.equals("") || email.equals("")){
            Toast.makeText(this,"Заполните пустые поля", Toast.LENGTH_SHORT).show();
        }

        else{
            signIn(email, password);
        }
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(EnterActivity.this, "Почта или пароль введены неверно",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("EMAIL", user.getEmail());
        startActivity(intent);

    }



}