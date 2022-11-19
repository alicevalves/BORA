package com.example.bora.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.bora.Adapter.ChatAdapter;
import com.example.bora.Adapter.ConversasAdapter;
import com.example.bora.Adapter.EventosAdapter;
import com.example.bora.Classes.ConversasClasse;
import com.example.bora.Classes.Eventos;
import com.example.bora.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Conversas extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManagerTodos;
    private List<ConversasClasse> conversas;
    private FirebaseUser user;
    private ConversasClasse todasConversas;
    private ConversasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Conversas");     //Titulo para ser exibido
        setContentView(R.layout.activity_conversas);

        mAuth = FirebaseAuth.getInstance();
        mRecyclerView = findViewById(R.id.rw_conversas);
        consultaConversas();
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


    private void consultaConversas(){
        mRecyclerView.setHasFixedSize(true);
        mLayoutManagerTodos = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManagerTodos);
        conversas = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        // Pega o valor de Origem (Usuário Conectado)
        String origem = user.getUid();
        reference.child("conversas").orderByChild("destino").equalTo(origem).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    todasConversas = postSnapshot.getValue(ConversasClasse.class);
                    conversas.add(todasConversas);
                }
                adapter.notifyDataSetChanged(); //Notifica quando o dado é notificado
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("conversas").orderByChild("origem").equalTo(origem).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    todasConversas = postSnapshot.getValue(ConversasClasse.class);
                    conversas.add(todasConversas);
                }
                adapter.notifyDataSetChanged(); //Notifica quando o dado é notificado
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new ConversasAdapter(conversas, this);
        mRecyclerView.setAdapter(adapter); // Preenche os itens
    }

}