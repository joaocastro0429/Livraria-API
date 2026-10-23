package br.com.joaocastro.projeto_livraria_api.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import br.com.joaocastro.projeto_livraria_api.dto.LivroDto;
import br.com.joaocastro.projeto_livraria_api.dto.LivroResponse;
import br.com.joaocastro.projeto_livraria_api.entity.LivroEntity;
import br.com.joaocastro.projeto_livraria_api.repository.LivroRepository;

@Service
@Transactional(readOnly = true)
public class LivroService {
    private final LivroRepository livroRepository;
    public LivroService(LivroRepository livroRepository) { this.livroRepository = livroRepository; }

    @Transactional
    public LivroResponse createLivro(LivroDto dto) {
        LivroEntity livro = new LivroEntity();
        preencher(livro, dto);
        return LivroResponse.from(livroRepository.save(livro));
    }

    public List<LivroResponse> buscarLivro() {
        return livroRepository.findAll().stream().map(LivroResponse::from).toList();
    }

    public LivroResponse buscarLivroPorId(Long id) { return LivroResponse.from(encontrar(id)); }

    @Transactional
    public LivroResponse atualizarLivro(Long id, LivroDto dto) {
        LivroEntity livro = encontrar(id);
        preencher(livro, dto);
        return LivroResponse.from(livroRepository.save(livro));
    }

    @Transactional
    public void excluirLivro(Long id) { livroRepository.delete(encontrar(id)); }

    private LivroEntity encontrar(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
    }

    private void preencher(LivroEntity livro, LivroDto dto) {
        livro.setName(dto.name().trim());
        livro.setDescription(dto.description());
        livro.setAuthor(dto.author().trim());
        livro.setIsbn(dto.isbn());
        livro.setPublicationDate(dto.publicationDate());
    }
}
