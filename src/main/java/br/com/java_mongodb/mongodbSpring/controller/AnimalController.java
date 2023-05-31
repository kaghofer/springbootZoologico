package br.com.java_mongodb.mongodbSpring.controller;

import br.com.java_mongodb.mongodbSpring.model.Animal;
import br.com.java_mongodb.mongodbSpring.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AnimalController {

    @Autowired
    AnimalRepository repository;

    @GetMapping("/animal/cadastrar")
    public String cadastrar(Model model) {
        model.addAttribute("animal", new Animal());
        return "animal/cadastrar";
    }

    @PostMapping("/animal/salvar")
    public String salvar(@ModelAttribute Animal animal) {
        repository.salvar(animal);
        return "redirect:/";
    }

    @GetMapping("/animal/listar")
    public String listar(Model model) {
        List<Animal> animais = repository.listarTodos();
        model.addAttribute("animais", animais);
        return "animal/listar";
    }

    @GetMapping("/animal/visualizar/{id}")
    public String visualizar(@PathVariable String id, Model model) {
        Animal animal = repository.obterId(id);
        model.addAttribute("animal", animal);
        return "animal/visualizar";
    }

    @GetMapping("/animal/excluir/{id}")
    public String excluir(@PathVariable String id) {
        repository.excluir(id);
        return "redirect:/animal/listar";
    }

    @GetMapping("animal/atualizar/{id}")
    public String alterar(@PathVariable String id, Model model){
        Animal animal = repository.obterId(id);
        model.addAttribute("animal", animal);
        return "animal/atualizar";
    }
    @PostMapping("/animal/editar/{id}")
    public String editar(@ModelAttribute Animal animal) {
        repository.salvar(animal);
        return "redirect:/animal/listar";
    }
}
