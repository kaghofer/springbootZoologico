package br.com.java_mongodb.mongodbSpring.model;

import org.bson.types.ObjectId;

public class Animal {
    private ObjectId id;
    private String nome;
    private String nomeCientifico;

    public Animal() {
    }

    public Animal(ObjectId id, String nome, String nomeCientifico) {
        this.id = id;
        this.nome = nome;
        this.nomeCientifico = nomeCientifico;
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

    public String getNomeCientifico() {
        return nomeCientifico;
    }

    public void setNomeCientifico(String nomeCientifico) {
        this.nomeCientifico = nomeCientifico;
    }

    public Animal criaId() {
        setId(new ObjectId());
        return this;
    }

}
