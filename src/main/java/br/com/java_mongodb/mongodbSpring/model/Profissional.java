package br.com.java_mongodb.mongodbSpring.model;

import org.bson.types.ObjectId;

import java.util.List;

public class Profissional {
    private ObjectId id;
    private String nome;
    private String cargo;

    public Profissional() {
    }

    public Profissional(ObjectId id, String nome, String cargo) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Profissional criaId() {
        setId(new ObjectId());
        return this;
    }
}
