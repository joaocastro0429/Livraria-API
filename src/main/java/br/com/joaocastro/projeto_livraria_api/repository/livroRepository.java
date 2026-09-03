package br.com.joaocastro.projeto_livraria_api.repository;
import br.com.joaocastro.projeto_livraria_api.entity.LivroEntity;

import org.springframework.data.jpa.repository.JpaRepository;


public interface LivroRepository extends JpaRepository<LivroEntity, Long> {

}