package com.example.bora.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bora.Adapter.ChatAdapter;
import com.example.bora.Classes.ChatPessoal;
import com.example.bora.DAO.ConfiguraçãoFirebase;
import com.example.bora.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Chat extends AppCompatActivity {
    private EditText ed_Mensagem;
    private RecyclerView mRecyclerView;
    private Button btn_Enviar;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private ChatPessoal chat;
    private LinearLayoutManager mLayoutManagerTodos;
    private ArrayList <ChatPessoal> list;
    private FirebaseUser user;
    private List<ChatPessoal> chatlist;
    private ChatAdapter adapter;
    private String chave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Chat");     //Titulo para ser exibido
        setContentView(R.layout.activity_chat);
        //Pega a intent de outra activity
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        chave = bundle.getString("key");

        ed_Mensagem = findViewById(R.id.ed_Mensagem);
        mRecyclerView = findViewById(R.id.rc_chat);
        btn_Enviar = findViewById(R.id.btn_Enviar);
        list = new ArrayList<>();

        mAuth = ConfiguraçãoFirebase.getFirebaseAuth();
        user = mAuth.getCurrentUser();
        // Pega o valor de Origem (Usuário Conectado)
        String Uid = user.getUid();
        String timeStamp = new SimpleDateFormat("dd/MM/yy HH:mm a").format(Calendar.getInstance().getTime());

        btn_Enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatlist.clear();
                String msg = ed_Mensagem.getText().toString();
                chat = new ChatPessoal();
                chat.setIdUsu(Uid);
                chat.setMensagem(msg);
                chat.setDataTime(timeStamp);
                chat.setChave(chave);
                reference = ConfiguraçãoFirebase.getFirebase().child("chat");
                reference.push().setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ed_Mensagem.setText("");
                    }
                });
            }
        });

        adapter = new ChatAdapter(list, this);
        LinearLayoutManager llm = new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, Conversas.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiveMessages();
    }

    private void receiveMessages(){
        mRecyclerView.setHasFixedSize(true);
        mLayoutManagerTodos = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManagerTodos);
        chatlist = new ArrayList<>();

        reference = ConfiguraçãoFirebase.getFirebase();
        reference.child("chat").orderByChild("chave").equalTo(chave).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    chat = postSnapshot.getValue(ChatPessoal.class);
                    chatlist.add(chat);
                }
                adapter.notifyDataSetChanged(); //Notifica quando o dado é notificado
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new ChatAdapter(chatlist, this);
        mRecyclerView.setAdapter(adapter); // Preenche os itens
    }
}