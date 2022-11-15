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

import com.example.bora.Classes.ChatPessoal;
import com.example.bora.DAO.ConfiguraçãoFirebase;
import com.example.bora.R;
import com.example.bora.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Chat extends AppCompatActivity {
    private EditText ed_Mensagem;
    private RecyclerView rc_chat;
    private Button btn_Enviar;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private ChatPessoal chat;
    private ArrayList <ChatPessoal> list;
    private FirebaseUser user;
    private RecyclerViewAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Chat");     //Titulo para ser exibido
        setContentView(R.layout.activity_chat);

        ed_Mensagem = findViewById(R.id.ed_Mensagem);
        rc_chat = findViewById(R.id.rc_chat);
        btn_Enviar = findViewById(R.id.btn_Enviar);
        list = new ArrayList<>();

        mAuth = ConfiguraçãoFirebase.getFirebaseAuth();
        user = mAuth.getCurrentUser();
        // Pega o valor de Origem (Usuário Conectado)
        String Uid = user.getUid();
        String timeStamp = new SimpleDateFormat("dd-MM-yy HH:mm a").format(Calendar.getInstance().getTime());

        btn_Enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = ed_Mensagem.getText().toString();
                chat = new ChatPessoal();
                chat.setIdUsu(Uid);
                chat.setMensagem(msg);
                chat.setDataTime(timeStamp);
                chat.setEmailDestino("allytori01@gmail.com");

                reference = ConfiguraçãoFirebase.getFirebase().child("chat");
                reference.push().setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ed_Mensagem.setText("");
                    }
                });
            }
        });

        adapter = new RecyclerViewAdapter(this,list);
        LinearLayoutManager llm = new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        rc_chat.setLayoutManager(llm);
        rc_chat.setAdapter(adapter);
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
        adapter = new RecyclerViewAdapter(this,list);
        reference = ConfiguraçãoFirebase.getFirebase();
        reference.child("chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snap:snapshot.getChildren()){
                    ChatPessoal mensagem = snap.getValue(ChatPessoal.class);
                    adapter.addMessage(mensagem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}