package br.com.java_mongodb.mongodbSpring.codec;

import br.com.java_mongodb.mongodbSpring.model.Animal;
import br.com.java_mongodb.mongodbSpring.model.Profissional;
import br.com.java_mongodb.mongodbSpring.model.Servico;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ProfissionalCodec implements CollectibleCodec<Profissional> {

    private Codec<Document> codec;

    public ProfissionalCodec(Codec<Document> codec) {
        this.codec = codec;
    }

    @Override
    public Class<Profissional> getEncoderClass() {
        return Profissional.class;
    }//Esse metodo diz qual classe será codificada

    @Override
    public boolean documentHasId(Profissional profissional) {
        return profissional.getId() == null;
    }//Esse metodo só verifica se o objeto chamado tem ID

    @Override
    public BsonValue getDocumentId(Profissional profissional) {
        if (!documentHasId(profissional))//Verifica se o ID foi criado
        {
            throw new IllegalStateException("Esse documento não tem ID");
        } else//Para que o ID possa ser lido é preciso converter para a base hexadecimal
        {
            return new BsonString(profissional.getId().toHexString());
        }
    }

    @Override
    public Profissional generateIdIfAbsentFromDocument(Profissional profissional) {
        return documentHasId(profissional) ? profissional.criaId() : profissional;
    }

    @Override
    public void encode(BsonWriter writer, Profissional profissional, EncoderContext ec) {
        /*Esse metodo pega um Objeto e o envia para o Mongodb, um bom exemplo
        seria dizer para o mongodb qual a receita ele deve seguir para poder 
        salvar o Objeto ALUNO em sua base de dados*/
        ObjectId id = profissional.getId();
        String nome = profissional.getNome();
        String cargo = profissional.getCargo();


        Document doc = new Document();
        doc.put("_id", id);
        doc.put("nome", nome);
        doc.put("cargo", cargo);

        codec.encode(writer, doc, ec);
        //Essa função é quem traduz o que escrevemos na VIEW
    }

    @Override
    public Profissional decode(BsonReader reader, DecoderContext dc) {
        Document doc = codec.decode(reader, dc);
        Profissional profissional = new Profissional();
        profissional.setId(doc.getObjectId("_id"));
        profissional.setNome(doc.getString("nome"));
        profissional.setCargo(doc.getString("cargo"));
        return profissional;

    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
