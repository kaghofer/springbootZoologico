package br.com.java_mongodb.mongodbSpring.controller;

import br.com.java_mongodb.mongodbSpring.model.Profissional;
import br.com.java_mongodb.mongodbSpring.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProfissionalController {

    @Autowired
    ProfissionalRepository repository;

    @GetMapping("/profissional/cadastrar")
    public String cadastrar(Model model) {
        model.addAttribute("profissional", new Profissional());
        return "profissional/cadastrar";
    }

    @PostMapping("/profissional/salvar")
    public String salvar(@ModelAttribute Profissional profissional) {
        repository.salvar(profissional);
        return "redirect:/";
    }

    @GetMapping("/profissional/listar")
    public String listar(Model model) {
        List<Profissional> profissionais = repository.listarTodos();
        model.addAttribute("profissionais", profissionais);
        return "profissional/listar";
    }

    @GetMapping("/profissional/visualizar/{id}")
    public String visualizar(@PathVariable String id, Model model) {
        Profissional profissional = repository.obterId(id);
        model.addAttribute("profissional", profissional);
        return "profissional/visualizar";
    }

    @GetMapping("/profissional/excluir/{id}")
    public String excluir(@PathVariable String id) {
        repository.excluir(id);
        return "redirect:/profissional/listar";
    }

    @GetMapping("profissional/atualizar/{id}")
    public String alterar(@PathVariable String id, Model model){
        Profissional profissional = repository.obterId(id);
        model.addAttribute("profissional", profissional);
        return "profissional/atualizar";
    }
    @PostMapping("/profissional/editar/{id}")
    public String editar(@ModelAttribute Profissional profissional) {
        repository.salvar(profissional);
        return "redirect:/profissional/listar";
    }
}
