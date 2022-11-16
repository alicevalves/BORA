package com.example.bora.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import android.widget.EditText;
import android.widget.Toast;

import com.example.bora.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Button btn_Login, btn_EsqueciSenha;
    private EditText ed_Senha, ed_Email;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        ed_Email = findViewById(R.id.ed_Email);
        ed_Senha = findViewById(R.id.ed_Senha);
        btn_Login = findViewById(R.id.btn_Login);
        btn_EsqueciSenha =  findViewById(R.id.btn_EsqueciSenha);
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = ed_Email.getText().toString();
                String p = ed_Senha.getText().toString();
                mAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(MainActivity.this, TelaPrincipal.class));
                            finish();
                        }else{
                            Toast.makeText(MainActivity.this, "Falha no Login!", Toast.LENGTH_SHORT).show();}
                    }
                });
            }
        });

        btn_EsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = ed_Email.getText().toString();
                mAuth.sendPasswordResetEmail(e).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Verifique sua caixa de entrada/spam", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Falha ao recuperar senha!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    public void cadastrar(View v){
        Intent in = new Intent(this, Cadastro.class);
        startActivity(in);
    }
}