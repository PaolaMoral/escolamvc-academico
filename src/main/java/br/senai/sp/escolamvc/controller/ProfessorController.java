package br.senai.sp.escolamvc.controller;

import br.senai.sp.escolamvc.model.Aluno;
import br.senai.sp.escolamvc.model.Professor;
import br.senai.sp.escolamvc.repository.ProfessorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;

    @PostMapping ("/buscar")
    public String buscar (@Param("nome")String nome, Model model){
        if (nome == null){
            model.addAttribute("professores", professorRepository.findAll());
            return "professores/listagem";
        }
        List<Professor> professorsBuscadosNoBanco = professorRepository.findProfessorByNomeContaining(nome);
        model.addAttribute("professores", professorsBuscadosNoBanco);

        return "professores/listagem";
    }

    @GetMapping
    public String listagem(Model model) {

        // Busca a lista de professores no banco de dados
        List<Professor> listaProfessores = professorRepository.findAll();

        // Adiciona a lista de professores no objeto model para ser carregado no template
        model.addAttribute("professores", listaProfessores);

        // Retorna o template professores/listagem.html
        return "professor/listagem";
    }

    @GetMapping("/form-inserir")
    public String formInserir(Model model) {
        model.addAttribute("professor", new Professor());
        // templates/professor/inserir.html
        return "professor/inserir";
    }

    @PostMapping("/salvar")
    public String salvarProfessor(@Valid Professor professor, BindingResult result,
                                  RedirectAttributes attributes) {

        // Se houver erro de validação, retorna para o template professor/inserir.html
        if (result.hasErrors()) {
            return "professor/inserir";
        }

        // Salva o professor no banco de dados
        professorRepository.save(professor);

        // Adiciona uma mensagem que será exibida no template
        attributes.addFlashAttribute("mensagem", "Professor salvo com sucesso!");

        // Redireciona para a página de listagem de professores
        return "redirect:/professor";
    }

    /*
     * Método que direciona para templates/alunos/alterar.html
     */
    @GetMapping("/alterar/{id}")
    public String alterar(@PathVariable("id") Long id, Model model) {

        // Busca o aluno no banco de dados
        Professor professor = professorRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("ID inválido"));

        // Adiciona o professor no objeto model para ser carregado no formulário
        model.addAttribute("professor", professor);

        // Retorna o template aluno/alterar.html
        return "professor/alterar";
    }

    @PostMapping("/alterar/{id}")
    public String alterar(@PathVariable("id") Long id, @Valid Professor professor,
                          BindingResult result, RedirectAttributes attributes) {

        // Se houver erro de validação, retorna para o template professores/alterar.html
        if (result.hasErrors()) {
            return "professor/alterar";
        }

        // Busca o professor no banco de dados
        Professor professorAtualizado = professorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido"));

        // Seta os dados do aluno
        professorAtualizado.setNome(professor.getNome());
        professorAtualizado.setEmail(professor.getEmail());
        professorAtualizado.setCpf(professor.getCpf());
        professorAtualizado.setDataNascimento(professor.getDataNascimento());


        // Salva o aluno no banco de dados
        professorRepository.save(professorAtualizado);

        // Adiciona uma mensagem que será exibida no template
        attributes.addFlashAttribute("mensagem",
                "Professor atualizado com sucesso!");

        // Redireciona para a página de listagem de professores
        return "redirect:/professor";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attributes) {

        // Busca o professor no banco de dados
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido"));

        // Exclui o professor do banco de dados
        professorRepository.delete(professor);

        // Adiciona uma mensagem que será exibida no template
        attributes.addFlashAttribute("mensagem", "Professor excluído com sucesso!");

        // Redireciona para a página de listagem de professores
        return "redirect:/professor";
    }
}
