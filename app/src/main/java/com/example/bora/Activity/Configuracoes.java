package com.example.bora.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bora.Classes.Usuario;
import com.example.bora.DAO.ConfiguraçãoFirebase;
import com.example.bora.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.net.URI;

public class Configuracoes extends AppCompatActivity {
    private Button btn_Atualiza;
    private EditText ed_NomeConfig, ed_EmailConfig;
    private ImageView img_profile;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private Usuario usuario;
    private String usuarioLogado, usuarioCode, email, dataNasc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Chat");     //Titulo para ser exibido
        setContentView(R.layout.activity_configuracoes);

        mAuth = ConfiguraçãoFirebase.getFirebaseAuth();
        reference = ConfiguraçãoFirebase.getFirebase();
        email = mAuth.getCurrentUser().getEmail();
        usuarioLogado = mAuth.getCurrentUser().getUid();
        storageReference = ConfiguraçãoFirebase.getFirebaseStorageReference();

        carregaImagemPadrao("gs://bora-103cc.appspot.com/fotoPerfilUsuario/" + usuarioLogado + ".jpg");


        ed_NomeConfig = findViewById(R.id.ed_NomeConfig);
        ed_EmailConfig = findViewById(R.id.ed_EmailConfig);
        img_profile = findViewById(R.id.img_profile);
        btn_Atualiza = findViewById(R.id.btn_Atualiza);
        ed_EmailConfig.setText(email);

        Usuario usuario = new Usuario();
        editaUsuario();

        btn_Atualiza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastraFoto();
                String nome = ed_NomeConfig.getText().toString();
                String email = ed_EmailConfig.getText().toString();
                usuario.setNome(nome);
                usuario.setEmail(email);
                usuario.setNascimento(dataNasc);
                atualizaDados(usuario);
            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), 123);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final int height = 250;
        final int width = 250;

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 123) {
                Uri imagemSelecionada = data.getData();
                Picasso.with(this).load(imagemSelecionada.toString()).resize(width, height).centerCrop().into(img_profile);
            }
        }
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

    public void sair(View v){
        // Desconecta o usuário
        mAuth = ConfiguraçãoFirebase.getFirebaseAuth();
        mAuth.signOut();
        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
    }


    private boolean cadastraFoto (){
        StorageReference montaImgRef = storageReference.child("fotoPerfilUsuario/" + usuarioLogado + ".jpg");
        img_profile.setDrawingCacheEnabled(true);
        img_profile.buildDrawingCache();

        // Pega a foto e transforma em um bitmap
        Bitmap bitmap = img_profile.getDrawingCache();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

        // Define qualidade e tipo da imagem
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
        byte [] data = byteArray.toByteArray();

        UploadTask uploadTask = montaImgRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> donwloadUrl = taskSnapshot.getStorage().getDownloadUrl();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Configuracoes.this, "Erro ao gravar foto :(", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }


    private void carregaImagemPadrao(String img) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReferenceFromUrl(img);

        final int height = 250;
        final int width = 250;

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(Configuracoes.this).load(uri.toString()).resize(width, height).centerCrop().into(img_profile);
            }
        });
    }

    private void editaUsuario(){
        reference.child("usuarios").orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    usuario = postSnapshot.getValue(Usuario.class);
                    ed_NomeConfig.setText(usuario.getNome());
                    dataNasc = usuario.getNascimento();
                    usuarioCode = postSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean atualizaDados(final Usuario usuario) {
        btn_Atualiza.setEnabled(false);

        try{
            reference = reference.child("usuarios");
            mAuth.getCurrentUser().updateEmail(usuario.getEmail()); // Atualiza o e-mail
            reference.child(usuarioCode).setValue(usuario); // Atualiza o usuário

            Toast.makeText(this, "Seus dados foram alterados :)", Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}