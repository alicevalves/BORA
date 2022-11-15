package com.example.bora;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bora.Classes.ChatPessoal;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Context context;
    private ArrayList<ChatPessoal> list;

    public RecyclerViewAdapter(Context context, ArrayList<ChatPessoal> list) {
        this.context = context;
        this.list = list;
    }

    public void addMessage(ChatPessoal msg){

        list.add(0,msg);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inf_from_chat,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.username.setText(list.get(position).getIdUsu());
        holder.message.setText(list.get(position).getMensagem());
        holder.dateTime.setText(list.get(position).getDataTime());

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username,message,dateTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.ed_Mensagem);
            username = itemView.findViewById(R.id.txt_username);
            dateTime = itemView.findViewById(R.id.txt_data);
        }
    }
}