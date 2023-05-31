package br.com.java_mongodb.mongodbSpring.controller;

import br.com.java_mongodb.mongodbSpring.model.Animal;
import br.com.java_mongodb.mongodbSpring.model.Profissional;
import br.com.java_mongodb.mongodbSpring.model.Servico;
import br.com.java_mongodb.mongodbSpring.repository.AnimalRepository;
import br.com.java_mongodb.mongodbSpring.repository.ProfissionalRepository;
import br.com.java_mongodb.mongodbSpring.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;

@Controller
public class ServicoController {

    @Autowired
    ServicoRepository repository;
    @Autowired
    AnimalRepository animalRepository;
    @Autowired
    ProfissionalRepository profissionalRepository;

    @GetMapping("/servico/cadastrar")
    public String cadastrar(Model model) {
        model.addAttribute("servico", new Servico());
        List<Animal> animais = animalRepository.listarTodos();
        model.addAttribute("animais", animais);
        List<Profissional> profissionais = profissionalRepository.listarTodos();
        model.addAttribute("profissionais", profissionais);
        return "servico/cadastrar";
    }

    @PostMapping("/servico/salvar")
    public String salvar(@ModelAttribute Servico servico) {
        Animal a = animalRepository.obterId(servico.getAnimal().getId().toString());
        Profissional p = profissionalRepository.obterId(servico.getProfissional().getId().toString());
        servico.setAnimal(a);
        servico.setProfissional(p);
        repository.salvar(servico);
        return "redirect:/";
    }

    @GetMapping("/servico/finalizar/{id}")
    public String finalizar(@PathVariable String id) {
        Servico servico = repository.obterId(id);
        Animal a = animalRepository.obterId(servico.getAnimal().getId().toString());
        Profissional p = profissionalRepository.obterId(servico.getProfissional().getId().toString());
        servico.setAnimal(a);
        servico.setProfissional(p);
        servico.setDataEfetuado(new Date());
        repository.salvar(servico);
        return "redirect:/servico/listar";
    }

    @GetMapping("/servico/listar")
    public String listar(Model model) {
        List<Servico> servicos = repository.listarTodos();
        model.addAttribute("servicos", servicos);
        return "servico/listar";
    }
    @GetMapping("/servico/filtrar/{filtro}")
    public String filtrar(Model model,@PathVariable String filtro) {
        List<Servico> servicos = repository.filtrar(filtro);
        model.addAttribute("servicos", servicos);
        return "servico/listar";
    }

    @GetMapping("/servico/visualizar/{id}")
    public String visualizar(@PathVariable String id, Model model) {
        Servico servico = repository.obterId(id);
        model.addAttribute("servico", servico);
        return "servico/visualizar";
    }

    @GetMapping("/servico/excluir/{id}")
    public String excluir(@PathVariable String id) {
        repository.excluir(id);
        return "redirect:/servico/listar";
    }

    @GetMapping("servico/atualizar/{id}")
    public String alterar(@PathVariable String id, Model model) {
        Servico servico = repository.obterId(id);
        List<Animal> animais = animalRepository.listarTodos();
        model.addAttribute("animais", animais);
        List<Profissional> profissionais = profissionalRepository.listarTodos();
        model.addAttribute("profissionais", profissionais);
        model.addAttribute("servico", servico);

        return "servico/atualizar";
    }

    @PostMapping("/servico/editar/{id}")
    public String editar(@ModelAttribute Servico servico) {
        Animal a = animalRepository.obterId(servico.getAnimal().getId().toString());
        Profissional p = profissionalRepository.obterId(servico.getProfissional().getId().toString());
        servico.setAnimal(a);
        servico.setProfissional(p);
        repository.salvar(servico);
        return "redirect:/";
    }
}
