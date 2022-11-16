package com.example.bora.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bora.Activity.Chat;
import com.example.bora.Activity.Conversas;
import com.example.bora.Classes.ConversasClasse;
import com.example.bora.Classes.Usuario;
import com.example.bora.DAO.ConfiguraçãoFirebase;
import com.example.bora.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.ViewHolder>  {
    private Context context;
    private ArrayList<ConversasClasse> conversas;
    private List<ConversasClasse> mConversas;
    private DatabaseReference reference;
    private String nomeUsu;

    public ConversasAdapter(List<ConversasClasse> l, Context c){
        context = c;
        mConversas = l;
    }

    public void addMessage(ConversasClasse msg){
        conversas.add(0,msg);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConversasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.conversas_itens, viewGroup, false);
        return new ConversasAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversasAdapter.ViewHolder holder, int position) {
        final ConversasClasse item = mConversas.get(position);

        reference = ConfiguraçãoFirebase.getFirebase();
        Usuario usuario = new Usuario();
        reference.child("usuarios").orderByChild("email").equalTo(item.getOrigem()).addValueEventListener(new ValueEventListener() {
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

/*
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
  */
        holder.message.setText("Digite a primeira mensagem");
        holder.cl_conversas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, Chat.class)); // Chama a tela do CHAT
            }
        });
    }

    @Override
    public int getItemCount() {
        return mConversas.size(); // Retorna o tamanho da lista do CHAT
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, message;
        ImageView img_user;
        ConstraintLayout cl_conversas;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.txt_mensagem);
            username = itemView.findViewById(R.id.txt_username);
            img_user = itemView.findViewById(R.id.img_user);
            cl_conversas = itemView.findViewById(R.id.cl_conversas);
        }
    }
}


