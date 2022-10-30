package com.example.bora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class TelaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){

        Toast.makeText(this, item.getItemId(), Toast.LENGTH_SHORT);

       switch (item.getItemId()){
            case R.id.chat:
                Toast.makeText(this, "você clicou no CHAT", Toast.LENGTH_SHORT);
                Intent in = new Intent(this, Conversas.class);
                Log.i("NumberGenerated", "você clicou no CHAT");
                startActivity(in);
                return super.onOptionsItemSelected(item);
           case R.id.config:
               Log.i("NumberGenerated", "você clicou no search");
               Toast.makeText(this, "você clicou no search", Toast.LENGTH_SHORT);
               Intent en = new Intent(this, Configuracoes.class);
               startActivity(en);
            case R.id.search:
                Toast.makeText(this, "você clicou no search", Toast.LENGTH_SHORT);
        }
        return super.onOptionsItemSelected(item);
    }

    public void novoEvento(View v){
        Intent in = new Intent(this, CadastroEventos.class);
        startActivity(in);
    }
}