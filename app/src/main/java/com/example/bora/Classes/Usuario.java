package com.example.bora.Classes;

public class Usuario {
    private String nome;
    private String nascimento;
    private String email;

    public String getEmail(){ return email; }

    public void setEmail(String email){ this.email = email; }

    public String getNome(){ return nome; }

    public void setNome(String nome){ this.nome = nome; }

    public String getNascimento(){ return nascimento; }

    public void setNascimento(String nascimento){ this.nascimento = nascimento; }
}
