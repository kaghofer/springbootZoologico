package br.com.java_mongodb.mongodbSpring.codec;

import br.com.java_mongodb.mongodbSpring.model.*;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServicoCodec implements CollectibleCodec<Servico> {

    private Codec<Document> codec;

    public ServicoCodec(Codec<Document> codec) {
        this.codec = codec;
    }

    @Override
    public Class<Servico> getEncoderClass() {
        return Servico.class;
    }//Esse metodo diz qual classe será codificada

    @Override
    public boolean documentHasId(Servico servico) {
        return servico.getId() == null;
    }//Esse metodo só verifica se o objeto chamado tem ID

    @Override
    public BsonValue getDocumentId(Servico servico) {
        if (!documentHasId(servico))//Verifica se o ID foi criado
        {
            throw new IllegalStateException("Esse documento não tem ID");
        } else//Para que o ID possa ser lido é preciso converter para a base hexadecimal
        {
            return new BsonString(servico.getId().toHexString());
        }
    }

    @Override
    public Servico generateIdIfAbsentFromDocument(Servico servico) {
        return documentHasId(servico) ? servico.criaId() : servico;
    }

    @Override
    public void encode(BsonWriter writer, Servico servico, EncoderContext ec) {
        /*Esse metodo pega um Objeto e o envia para o Mongodb, um bom exemplo
        seria dizer para o mongodb qual a receita ele deve seguir para poder
        salvar o Objeto ALUNO em sua base de dados*/

        ObjectId id = servico.getId();
        String descricao = servico.getDescricao();
        Date dtCadastro =  servico.getDataCadastro();
        Date dtEfetuado = servico.getDataEfetuado();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDtCadastro = dateFormat.format(dtCadastro);
        String formattedDtEfetuado;
        if(dtEfetuado==null){
            formattedDtEfetuado = null;
        }else {
            formattedDtEfetuado = dateFormat.format(dtEfetuado);
        }


        Animal animal = servico.getAnimal();
        Profissional profissional = servico.getProfissional();


        Document doc = new Document();
        Document animaldoc = new Document();
        Document profissionaldoc = new Document();

        if (animal!=null){
            animaldoc.append("_id", animal.getId()).append("nome", animal.getNome()).append("nomeCientifico", animal.getNomeCientifico());
        }
        if (profissional != null){
            profissionaldoc.append("_id", profissional.getId()).append("nome",profissional.getNome()).append("cargo",profissional.getCargo());
        }

        doc.put("_id", id);
        doc.put("descricao", descricao);
        doc.put("dtCadastro", formattedDtCadastro);
        doc.put("dtEfetuado", formattedDtEfetuado);
        doc.append("animal", animaldoc);
        doc.append("profissional", profissionaldoc);

        codec.encode(writer, doc, ec);
        //Essa função é quem traduz o que escrevemos na VIEW
    }

    @Override
    public Servico decode(BsonReader reader, DecoderContext dc) {
        Document doc = codec.decode(reader, dc);
        Servico servico = new Servico();
        Animal a;
        Profissional p;
        servico.setId(doc.getObjectId("_id"));
        servico.setDescricao(doc.getString("descricao"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date dtEfetuado;
            if (doc.getString("dtEfetuado") == null){
                dtEfetuado = null;
            }else {
                dtEfetuado = dateFormat.parse(doc.getString("dtEfetuado"));
            }

            Date dtCadastro = dateFormat.parse(doc.getString("dtCadastro"));
            servico.setDataEfetuado(dtEfetuado);
            servico.setDataCadastro(dtCadastro);
        } catch (ParseException e) {
            // Handle parsing exception
            e.printStackTrace();
        }

//        servico.setDataEfetuado(doc.getDate("dtEfetuado"));
//        servico.setDataCadastro(doc.getDate("dtCadastro"));

        Document animaldoc = (Document) doc.get("animal");
        Document profissionaldoc = (Document) doc.get("profissional");

        if(animaldoc != null){
            a = new Animal(animaldoc.getObjectId("_id"),animaldoc.getString("nome"),animaldoc.getString("nomeCientifico"));
            servico.setAnimal(a);
        }
        if(profissionaldoc != null){
            p = new Profissional(profissionaldoc.getObjectId("_id"), profissionaldoc.getString("nome"), profissionaldoc.getString("cargo"));
            servico.setProfissional(p);
        }
        return servico;
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
