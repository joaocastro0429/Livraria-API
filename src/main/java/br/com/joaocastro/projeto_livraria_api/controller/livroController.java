package br.com.joaocastro.projeto_livraria_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.joaocastro.projeto_livraria_api.dto.LivroDto;
import br.com.joaocastro.projeto_livraria_api.entity.LivroEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.joaocastro.projeto_livraria_api.service.LivroService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/livro")
@Tag(name = "Livros", description = "Operações para cadastrar, consultar, atualizar e excluir livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar livro", description = "Cadastra um novo livro")
    @ApiResponse(responseCode = "200", description = "Livro cadastrado com sucesso")
    public LivroDto createLivro(@RequestBody LivroDto dto) {
        return livroService.createLivro(dto);
    }

    @GetMapping
    @Operation(summary = "Listar livros", description = "Retorna todos os livros cadastrados")
    @ApiResponse(responseCode = "200", description = "Livros encontrados")
    public List<LivroEntity> buscarLivro() {
        return livroService.buscarLivro();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID", description = "Retorna o livro correspondente ao ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro encontrado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public LivroEntity buscarLivroPorId(@PathVariable Long id) {
        return livroService.buscarLivroPorId(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro", description = "Atualiza os dados do livro correspondente ao ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public LivroEntity atualizarLivro(@PathVariable Long id, @RequestBody LivroDto dto) {
        return livroService.atualizarLivro(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir livro", description = "Exclui o livro correspondente ao ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public void excluirLivro(@PathVariable Long id) {
        livroService.excluirLivro(id);
    }
}
    
    
