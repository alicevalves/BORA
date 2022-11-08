package com.example.bora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cadastro extends AppCompatActivity {

    private EditText ed_Nome, ed_EmailCad, ed_DataNascimento, ed_SenhaCad, ed_ConfirmSenha;
    private CheckBox chk_termos;
    private Button btn_cadastrar;
    private FirebaseAuth mAuth;

    public Cadastro() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Cadastre sua conta");     //Titulo para ser exibido
        setContentView(R.layout.activity_cadastro);

        mAuth = FirebaseAuth.getInstance();

        ed_Nome = findViewById(R.id.ed_Nome);
        ed_EmailCad = findViewById(R.id.ed_EmailCad);
        ed_DataNascimento = findViewById(R.id.ed_DataNascimento);
        ed_SenhaCad = findViewById(R.id.ed_SenhaCad);
        ed_ConfirmSenha = findViewById(R.id.ed_ConfirmSenha);
        chk_termos = findViewById(R.id.chk_termos);
        btn_cadastrar =  findViewById(R.id.btn_cadastrar);

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String registerNome = ed_Nome.getText().toString();
                String registerEmail = ed_EmailCad.getText().toString();
                String registerNascimento = ed_DataNascimento.getText().toString();
                String registerSenha = ed_SenhaCad.getText().toString();
                String registerSenhaConf = ed_ConfirmSenha.getText().toString();

                if (!chk_termos.isChecked()){
                    Toast.makeText(Cadastro.this, "É necessário aceitar os termos :|", Toast.LENGTH_SHORT).show();
                }else {
                    if(!TextUtils.isEmpty(registerEmail) || !TextUtils.isEmpty(registerSenha)
                            || !TextUtils.isEmpty(registerNascimento) || !TextUtils.isEmpty(registerSenhaConf)
                            || !TextUtils.isEmpty(registerNome)){
                        if(registerSenha.equals(registerSenhaConf)){
                            String senhahash = md5(registerSenha);
                            mAuth.createUserWithEmailAndPassword(registerEmail, senhahash).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        abrirTelaPrincipal();
                                    }else{
                                        String error = task.getException().getMessage();
                                        Toast.makeText(Cadastro.this, "Erro:" + error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Cadastro.this, "As senhas informadas estão diferentes :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void abrirTelaPrincipal() {
        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
        finish();
    }

    public void abrirTermos(View v) {
        Intent in = new Intent(this, Termos.class);
        startActivity(in);
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, MainActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }
}