package br.com.java_mongodb.mongodbSpring.repository;

import br.com.java_mongodb.mongodbSpring.codec.ServicoCodec;
import br.com.java_mongodb.mongodbSpring.model.Servico;
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
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class ServicoRepository {

    private MongoClient cliente;
    private MongoDatabase db;

    public void conecta() {
        //Instaciar um codec
        Codec<Document> codec = MongoClient.getDefaultCodecRegistry()
                .get(Document.class);

        //Qual classe ira sofrer o encode/decode
        ServicoCodec servicoCodec = new ServicoCodec(codec);

        //Instanciar um registro para o codec
        CodecRegistry registro = CodecRegistries
                .fromRegistries(MongoClient.getDefaultCodecRegistry(),
                        CodecRegistries.fromCodecs(servicoCodec));

        //Dar um build no registro
        MongoClientOptions op = MongoClientOptions.builder().
                codecRegistry(registro).build();

        this.cliente = new MongoClient("localhost:27017", op);
        this.db = cliente.getDatabase("Zoologico");
    }

    public void salvar(Servico servico) {
        conecta();
        MongoCollection<Servico> servicos = db.getCollection("servicos", Servico.class);
        if (servico.getId() == null) {//se não tiver um aluno, crio uma aluno
            servicos.insertOne(servico);
        } else {//se o aluno já existir salva somente as alterações
            servicos.updateOne(Filters.eq("_id", servico.getId()), new Document("$set", servico));
        }
        cliente.close();
    }

    public List<Servico> listarTodos() {
        conecta();
        MongoCollection<Servico> servicos = db.getCollection("servicos", Servico.class);
        MongoCursor<Servico> resultado = servicos.find().iterator();
        List<Servico> servicoLista = new ArrayList<>();

        while (resultado.hasNext()) {
            Servico servico = resultado.next();
            servicoLista.add(servico);
        }
        cliente.close();
        return servicoLista;
    }

    //    public List<Servico> filtrar(String descricao) {
//        conecta();
//        MongoCollection<Servico> servicos = db.getCollection("servicos", Servico.class);
//        MongoCursor<Servico> resultado = servicos.find(Filters.eq("descricao", descricao)).iterator();
//        List<Servico> servicoLista = new ArrayList<>();
//
//        while (resultado.hasNext()) {
//            Servico servico = resultado.next();
//            servicoLista.add(servico);
//        }
//        cliente.close();
//        return servicoLista;
//    }
    public List<Servico> filtrar(String descricao) {
        conecta();
        MongoCollection<Servico> servicos = db.getCollection("servicos", Servico.class);
        Bson filter = Filters.regex("descricao", Pattern.quote(descricao));
        MongoCursor<Servico> resultado = servicos.find(filter).iterator();
        List<Servico> servicoLista = new ArrayList<>();

        while (resultado.hasNext()) {
            Servico servico = resultado.next();
            servicoLista.add(servico);
        }
        cliente.close();
        return servicoLista;
    }

    public Servico obterId(String id) {
        conecta();
        MongoCollection<Servico> servicos = db.getCollection("servicos", Servico.class);
        Servico servico = servicos.find(Filters.eq("_id", new ObjectId(id))).first();
        return servico;
    }

    public void excluir(String id) {
        conecta();
        MongoCollection<Servico> servicos = db.getCollection("servicos", Servico.class);
        servicos.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

}
