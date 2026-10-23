package br.com.joaocastro.projeto_livraria_api.controller;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.joaocastro.projeto_livraria_api.dto.LivroDto;
import br.com.joaocastro.projeto_livraria_api.dto.LivroResponse;
import br.com.joaocastro.projeto_livraria_api.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/livro")
@Tag(name = "Livros", description = "Cadastro, consulta, atualização e exclusão de livros")
public class LivroController {
    private final LivroService livroService;
    public LivroController(LivroService livroService) { this.livroService = livroService; }

    @PostMapping
    @Operation(summary = "Cadastrar livro")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Livro cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")})
    public ResponseEntity<LivroResponse> createLivro(@Valid @RequestBody LivroDto dto) {
        LivroResponse livro = livroService.createLivro(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(livro.id()).toUri();
        return ResponseEntity.created(location).body(livro);
    }

    @GetMapping
    @Operation(summary = "Listar livros")
    public List<LivroResponse> buscarLivro() { return livroService.buscarLivro(); }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Livro encontrado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")})
    public LivroResponse buscarLivroPorId(@PathVariable Long id) { return livroService.buscarLivroPorId(id); }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Livro atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")})
    public LivroResponse atualizarLivro(@PathVariable Long id, @Valid @RequestBody LivroDto dto) {
        return livroService.atualizarLivro(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir livro")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Livro excluído"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")})
    public ResponseEntity<Void> excluirLivro(@PathVariable Long id) {
        livroService.excluirLivro(id);
        return ResponseEntity.noContent().build();
    }
}
