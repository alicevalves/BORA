package com.example.bora.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bora.Classes.ChatPessoal;
import com.example.bora.Classes.Eventos;
import com.example.bora.Classes.Usuario;
import com.example.bora.DAO.ConfiguraçãoFirebase;
import com.example.bora.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>  {
    private Context context;
    private ArrayList<ChatPessoal> eventos;
    private List<ChatPessoal> mChat;
    private DatabaseReference reference;
    private String nomeUsu;

    public ChatAdapter(List<ChatPessoal> l, Context c){
        context = c;
        mChat = l;
    }

    public void addMessage(ChatPessoal msg){
        eventos.add(0,msg);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inf_chat, viewGroup, false);
        return new ChatAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        final ChatPessoal item = mChat.get(position);

        reference = ConfiguraçãoFirebase.getFirebase();
        Usuario usuario = new Usuario();
        reference.child("usuarios").orderByChild("email").equalTo(item.getEmailDestino()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    Usuario usuario = postSnapshot.getValue(Usuario.class);
                    nomeUsu = usuario.getNome();

                    holder.username.setText(nomeUsu);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReferenceFromUrl("gs://bora-103cc.appspot.com/fotoPerfilUsuario/" + item.getIdUsu() + ".jpg");

        final int height = 250;
        final int width = 250;

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri.toString()).resize(width, height).centerCrop().into(holder.img_user);
            }
        });
        holder.message.setText(item.getMensagem());
        holder.dateTime.setText(item.getDataTime());

       /* String dayAtual = new SimpleDateFormat("dd/MM/yy").format(Calendar.getInstance().getTime());
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        OffsetDateTime dateTime = OffsetDateTime.parse(data);
        String dayBanco = dateTime.format(dayFormatter);

        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm a");
        OffsetDateTime hora = OffsetDateTime.parse(data);
        String horaBanco = hora.format(horaFormatter);*/
       /* if(dayAtual.equals(dayBanco)){
            holder.dateTime.setText(horaBanco);
        }else {
            holder.dateTime.setText(dayBanco);
        }*/
    }

    @Override
    public int getItemCount() {
        return mChat.size(); // Retorna o tamanho da lista do CHAT
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, message, dateTime;
        ImageView img_user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.txt_mensagem);
            username = itemView.findViewById(R.id.txt_username);
            dateTime = itemView.findViewById(R.id.txt_data);
            img_user = itemView.findViewById(R.id.img_user);
        }
    }
}
