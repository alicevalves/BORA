package com.example.bora.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.bora.Activity.CadastroEventos;
import com.example.bora.Activity.Configuracoes;
import com.example.bora.Activity.Conversas;
import com.example.bora.Adapter.EventosAdapter;
import com.example.bora.Classes.Eventos;
import com.example.bora.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TelaPrincipal extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private EventosAdapter adapter;
    private DatabaseReference reference;
    private Eventos todosEventos;
    private LinearLayoutManager mLayoutManagerTodos;
    private List<Eventos> eventos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        mRecyclerView = findViewById(R.id.rw_feed);
    }
    @Override
    public void onResume(){
        carregaTodosEventos();
        super.onResume();
    }

    private void carregaTodosEventos(){
        mRecyclerView.setHasFixedSize(true);
        mLayoutManagerTodos = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManagerTodos);
        eventos = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("eventos").orderByChild("descricao").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    todosEventos = postSnapshot.getValue(Eventos.class);
                    eventos.add(todosEventos);
                }
                adapter.notifyDataSetChanged(); //Notifica quando o dado é notificado
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new EventosAdapter(eventos, this);
        mRecyclerView.setAdapter(adapter); // Preenche os itens
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                EventosAdapter eventosAdapter = (EventosAdapter) mRecyclerView.getAdapter();
                eventosAdapter.myFilter(s.toLowerCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
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