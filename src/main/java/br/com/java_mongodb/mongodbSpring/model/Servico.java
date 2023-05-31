package br.com.java_mongodb.mongodbSpring.model;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Servico {
    private ObjectId id;
    private String descricao;
    private Animal animal;
    private Profissional profissional;

    public Servico() {
        setDataCadastro(new Date());
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataCadastro;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataEfetuado;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Date getDataEfetuado() {
        return dataEfetuado;
    }

    public void setDataEfetuado(Date dataEfetuado) {
        this.dataEfetuado = dataEfetuado;
    }

    public Servico criaId() {
        setId(new ObjectId());
        return this;
    }

}
