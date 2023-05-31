package br.com.java_mongodb.mongodbSpring.model;

import org.bson.types.ObjectId;

public class Especie {
    private ObjectId id;
    private String nome;

    public Especie() {
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
}
