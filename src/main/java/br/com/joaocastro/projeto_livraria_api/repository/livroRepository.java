package br.com.joaocastro.projeto_livraria_api.repository;
import br.com.joaocastro.projeto_livraria_api.entity.livroEntity;

import org.springframework.data.jpa.repository.JpaRepository;


public interface livroRepository extends JpaRepository<livroEntity, Long> {

}