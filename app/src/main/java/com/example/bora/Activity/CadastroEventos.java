package com.example.bora.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bora.Classes.Eventos;
import com.example.bora.DAO.ConfiguraçãoFirebase;
import com.example.bora.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroEventos extends AppCompatActivity {
    private EditText ed_descricao, ed_dataEvento, ed_local;
    private Button btn_NovoEvento;
    private DatabaseReference db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private Eventos eventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        setContentView(R.layout.activity_cadastro_eventos);

        ed_descricao = findViewById(R.id.ed_descricao);
        ed_dataEvento = findViewById(R.id.ed_dataEvento);
        ed_local = findViewById(R.id.ed_local);
        btn_NovoEvento = findViewById(R.id.btn_NovoEvento);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        String idUsu = user.getUid();

        btn_NovoEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descricao = ed_descricao.getText().toString();
                String data = ed_dataEvento.getText().toString();
                String local = ed_local.getText().toString();

                eventos = new Eventos();
                eventos.setDescricao(descricao);
                eventos.setData(data);
                eventos.setLocal(local);
                eventos.setIdUsu(idUsu);
                try{
                    reference = ConfiguraçãoFirebase.getFirebase().child("eventos");
                    reference.push().setValue(eventos);

                    voltar();
                } catch (Exception ex){
                    Toast.makeText(CadastroEventos.this, "Erro ao gravar :(", Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, TelaPrincipal.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }
    public void voltar(){
        Intent in = new Intent(this, TelaPrincipal.class);
        startActivity(in);
        finish();
    }
}