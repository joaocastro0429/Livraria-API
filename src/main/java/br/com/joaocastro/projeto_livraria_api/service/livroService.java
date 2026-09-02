package br.com.joaocastro.projeto_livraria_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.joaocastro.projeto_livraria_api.dto.livroDto;
import br.com.joaocastro.projeto_livraria_api.entity.livroEntity;
import br.com.joaocastro.projeto_livraria_api.repository.livroRepository;

@Service
public class livroService {

    private final livroRepository livroRepository;

    public livroService(livroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public livroDto createLivro(livroDto dto) {
        livroEntity livro = new livroEntity(
                null,
                dto.getName(),
                dto.getDescription(),
                dto.getDuration(),
                dto.getStartDate()

        );

        livroEntity livroSalvo = livroRepository.save(livro);

        return new livroDto(
                livroSalvo.getName(),
                livroSalvo.getDescription(),
                livroSalvo.getDuration(),
                livroSalvo.getStartDate());

    }
    
    public List<livroEntity> buscarLivro() {
        return livroRepository.findAll();
    }

    public livroEntity buscarLivroPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
    }

    public livroEntity atualizarLivro(Long id, livroDto dto) {
        livroEntity livro = buscarLivroPorId(id);

        livro.setName(dto.getName());
        livro.setDescription(dto.getDescription());
        livro.setDuration(dto.getDuration());
        livro.setStartDate(dto.getStartDate());

        return livroRepository.save(livro);
    }

    public void excluirLivro(Long id) {
        livroEntity livro = buscarLivroPorId(id);
        livroRepository.delete(livro);
    }
}
