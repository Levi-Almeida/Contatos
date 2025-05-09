package com.example.listacontatos.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Contato implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String nome;

    @SerializedName("phone")
    private String telefone;

    @SerializedName("email")
    private String email;

    @SerializedName("favorite")
    private boolean favorito;

    public Contato() {
    }

    public Contato(String nome, String telefone, String email, boolean favorito) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.favorito = favorito;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}