package br.com.java_mongodb.mongodbSpring.codec;

import br.com.java_mongodb.mongodbSpring.model.Animal;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AnimalCodec implements CollectibleCodec<Animal> {

    private Codec<Document> codec;

    public AnimalCodec(Codec<Document> codec) {
        this.codec = codec;
    }

    @Override
    public Class<Animal> getEncoderClass() {
        return Animal.class;
    }

    @Override
    public boolean documentHasId(Animal animal) {
        return animal.getId() == null;
    }
    @Override
    public BsonValue getDocumentId(Animal animal) {
        if (!documentHasId(animal))//Verifica se o ID foi criado
        {
            throw new IllegalStateException("Esse documento não tem ID");
        } else//Para que o ID possa ser lido é preciso converter para a base hexadecimal
        {
            return new BsonString(animal.getId().toHexString());
        }
    }

    @Override
    public Animal generateIdIfAbsentFromDocument(Animal animal) {
        return documentHasId(animal) ? animal.criaId() : animal;
    }

    @Override
    public void encode(BsonWriter writer, Animal animal, EncoderContext ec) {
        ObjectId id = animal.getId();
        String nome = animal.getNome();
        String nomeCientifico = animal.getNomeCientifico();

        Document doc = new Document();
        doc.put("_id", id);
        doc.put("nome", nome);
        doc.put("nomeCientifico", nomeCientifico);

        codec.encode(writer, doc, ec);
    }

    @Override
    public Animal decode(BsonReader reader, DecoderContext dc) {
        Document doc = codec.decode(reader, dc);
        Animal animal = new Animal();
        animal.setId(doc.getObjectId("_id"));
        animal.setNome(doc.getString("nome"));
        animal.setNomeCientifico(doc.getString("nomeCientifico"));

        return animal;
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
