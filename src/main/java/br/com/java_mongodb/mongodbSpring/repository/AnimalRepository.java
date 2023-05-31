package br.com.java_mongodb.mongodbSpring.repository;

import br.com.java_mongodb.mongodbSpring.codec.AnimalCodec;
import br.com.java_mongodb.mongodbSpring.model.Animal;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AnimalRepository {

    private MongoClient cliente;
    private MongoDatabase db;

    public void conecta() {
        //Instaciar um codec
        Codec<Document> codec = MongoClient.getDefaultCodecRegistry()
                .get(Document.class);

        //Qual classe ira sofrer o encode/decode
        AnimalCodec animalCodec = new AnimalCodec(codec);

        //Instanciar um registro para o codec
        CodecRegistry registro = CodecRegistries
                .fromRegistries(MongoClient.getDefaultCodecRegistry(),
                        CodecRegistries.fromCodecs(animalCodec));

        //Dar um build no registro
        MongoClientOptions op = MongoClientOptions.builder().
                codecRegistry(registro).build();

        this.cliente = new MongoClient("localhost:27017", op);
        this.db = cliente.getDatabase("Zoologico");
    }

    public void salvar(Animal animal) {
        conecta();
        MongoCollection<Animal> animais = db.getCollection("animais", Animal.class);
        if(animal.getId() == null){//se não tiver um aluno, crio uma aluno
            animais.insertOne(animal);
        }else{//se o aluno já existir salva somente as alterações
            animais.updateOne(Filters.eq("_id", animal.getId()), new Document("$set",animal));
        }        
        cliente.close();
    }

    public List<Animal> listarTodos() {
        conecta();
        MongoCollection<Animal> animais = db.getCollection("animais", Animal.class);
        MongoCursor<Animal> resultado = animais.find().iterator();
        List<Animal> animalLista = new ArrayList<>();
        
        while(resultado.hasNext()){
            Animal animal = resultado.next();
            animalLista.add(animal);
        }
        cliente.close();
        return animalLista;
    }
    
    public Animal obterId(String id){
        conecta();
        MongoCollection<Animal> animais = db.getCollection("animais", Animal.class);
        Animal animal = animais.find(Filters.eq("_id", new ObjectId(id))).first();
        return animal;
    }

    public void excluir(String id){
        conecta();
        MongoCollection<Animal> animais = db.getCollection("animais", Animal.class);
        animais.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }
}