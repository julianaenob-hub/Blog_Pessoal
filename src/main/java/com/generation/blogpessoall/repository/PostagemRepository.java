package com.generation.blogpessoall.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoall.model.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {
	
	// Busca todas as postagens cujo título contenha o texto informado,
	// ignorando se está em maiúsculas ou minúsculas
	public List<Postagem> findAllByTituloContainingIgnoreCase(String titulo);
}
 