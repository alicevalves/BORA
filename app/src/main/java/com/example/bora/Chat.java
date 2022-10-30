package com.example.bora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class Chat extends AppCompatActivity {
    //private GroupAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Chat");     //Titulo para ser exibido
        setContentView(R.layout.activity_chat);

      //  adapter = new GroupAdapter();

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
/*
    private class MessageItem extends Item<RecyclerView.ViewHolder>{
        private final boolean isLeft;

        private  MessageItem(boolean isLeft){
            this.isLeft = isLeft;
        }
/*
        @Override
        public void bind(@NonNull RecyclerView.ViewHolder viewm, int position){

        }

        @Override
        public int getLayout(){
            return isLeft ? R.layout.inf_from_chat : R.layout.inf_chat;
        }
    }
*/
}