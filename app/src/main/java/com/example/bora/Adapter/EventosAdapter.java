package com.example.bora.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bora.Activity.Chat;
import com.example.bora.Activity.Conversas;
import com.example.bora.Classes.ConversasClasse;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.ViewHolder> {
    private List<Eventos> mEventList;
    private Context context;
    private DatabaseReference reference;
    private Usuario usuario;
    private String nomeUsu;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Boolean alterado;
    private List<Eventos> eventosArray;

    public EventosAdapter(List<Eventos> l, Context c){
        context = c;
        mEventList = l;
    }

    @NonNull
    @Override
    public EventosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feed_item, viewGroup, false);
        return new EventosAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventosAdapter.ViewHolder holder, int position) {
        final Eventos item = mEventList.get(position);

        reference = ConfiguraçãoFirebase.getFirebase();
        Usuario usuario = new Usuario();
        reference.child("usuarios").orderByChild("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    Usuario usuario = postSnapshot.getValue(Usuario.class);
                    nomeUsu = usuario.getNome();

                    holder.txt_username.setText(nomeUsu);
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

        holder.txt_conteudo.setText(item.getDescricao());
        holder.txt_dataEvento.setText(item.getData());
        holder.txt_local.setText(item.getLocal());
        holder.txt_time.setText(item.getDataCadastro());
        holder.btn_convida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                String origem = user.getEmail();
                String destino = item.getEmail();
                ConversasClasse conversas = new ConversasClasse();
                conversas.setOrigem(origem);
                conversas.setDestino(destino);
                holder.btn_convida.setEnabled(false);
                reference = ConfiguraçãoFirebase.getFirebase().child("conversas");
                reference.push().setValue(conversas).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        context.startActivity(new Intent(context, Conversas.class)); // Chama a tela do CHAT
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEventList.size(); // Retorna o tamanho da lista de Eventos
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView txt_username, txt_dataEvento, txt_local, txt_conteudo, txt_time;
        protected ImageView img_user;
        protected Button btn_convida;
        public ViewHolder (View itemView){
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_dataEvento = itemView.findViewById(R.id.txt_dataEvento);
            txt_local = itemView.findViewById(R.id.txt_local);
            txt_conteudo = itemView.findViewById(R.id.txt_conteudo);
            txt_time = itemView.findViewById(R.id.txt_time);
            img_user = itemView.findViewById(R.id.img_user);
            btn_convida= itemView.findViewById(R.id.btn_convida);
        }
    }

    public void myFilter(@NonNull String desc){
        String descricao = new String();
        descricao = desc;
        List<Eventos> listaTemp = new ArrayList<>();
        alterado = false;
        if(descricao.length() == 0){
            Toast.makeText(context, "ENTROU VAZIO", Toast.LENGTH_SHORT).show();
            mEventList.addAll(eventosArray);
        }else{
            //mEventList.clear();
            String finalNome = descricao;
            mEventList.forEach((n) -> {
                if(n.getDescricao().toLowerCase().startsWith(finalNome)){
                    listaTemp.add(n);
                    alterado = true;
                }
            });

            notifyDataSetChanged();
        }
        if(alterado == true){
            mEventList.clear();
            mEventList.addAll(listaTemp);
        }
    }
}
