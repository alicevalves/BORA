package com.example.bora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btn_criarConta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_criarConta = findViewById(R.id.btn_criarConta);


    }

    public void cadastrar(View v){
        Intent in = new Intent(this, Cadastro.class);
        startActivity(in);
    }

    public void entrar(View v){
        Intent in = new Intent(this, TelaPrincipal.class);
        startActivity(in);
    }
}