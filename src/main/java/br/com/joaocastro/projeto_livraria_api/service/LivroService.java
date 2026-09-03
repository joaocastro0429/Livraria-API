package br.com.joaocastro.projeto_livraria_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.joaocastro.projeto_livraria_api.dto.LivroDto;
import br.com.joaocastro.projeto_livraria_api.entity.LivroEntity;
import br.com.joaocastro.projeto_livraria_api.repository.LivroRepository;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public LivroDto createLivro(LivroDto dto) {
        LivroEntity livro = new LivroEntity(
                null,
                dto.getName(),
                dto.getDescription(),
                dto.getDuration(),
                dto.getStartDate()

        );

        LivroEntity livroSalvo = livroRepository.save(livro);

        return new LivroDto(
                livroSalvo.getName(),
                livroSalvo.getDescription(),
                livroSalvo.getDuration(),
                livroSalvo.getStartDate());

    }
    
    public List<LivroEntity> buscarLivro() {
        return livroRepository.findAll();
    }

    public LivroEntity buscarLivroPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
    }

    public LivroEntity atualizarLivro(Long id, LivroDto dto) {
        LivroEntity livro = buscarLivroPorId(id);

        livro.setName(dto.getName());
        livro.setDescription(dto.getDescription());
        livro.setDuration(dto.getDuration());
        livro.setStartDate(dto.getStartDate());

        return livroRepository.save(livro);
    }

    public void excluirLivro(Long id) {
        LivroEntity livro = buscarLivroPorId(id);
        livroRepository.delete(livro);
    }
}
