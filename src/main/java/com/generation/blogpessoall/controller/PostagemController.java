package com.generation.blogpessoall.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoall.repository.PostagemRepository;

import jakarta.validation.Valid;

import com.generation.blogpessoall.model.Postagem;

@RestController // Diz que essa classe é um "controlador" da API
@RequestMapping("/postagens") // Caminho da API: /postagens
@CrossOrigin(origins = "*", allowedHeaders = "*") // Permite acesso de qualquer lugar
public class PostagemController {
    
    @Autowired // Pede para o Spring criar o objeto postagemRepository
    private PostagemRepository postagemRepository;

    PostagemController(PostagemRepository postagemRepository) {
        this.postagemRepository = postagemRepository;
    }
    
    @GetMapping // Quando alguém chama GET /postagens
    public ResponseEntity<List<Postagem>> getAll() {
        // Busca todas as postagens no banco e devolve
        return ResponseEntity.ok(postagemRepository.findAll());
		// SELECT * FROM tb_postagens;  (SQL) <- Isso é o que o método findAll() faz, ele retorna todos os registros da tabela tb_postagens
        
        
	}
    
    @GetMapping("/{id}") // Quando alguém chama GET /postagens/{id}
    public ResponseEntity<Postagem> getById(@PathVariable Long id) {
		// Busca a postagem pelo id no banco e devolve
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta)) // Se encontrar, devolve 200 OK com a postagem
				.orElse(ResponseEntity.notFound().build()); // Se não encontrar, devolve 404 Not Found
		// SELECT * FROM tb_postagens WHERE id = {id};  (SQL) <- Isso é o que o método findById() faz, ele retorna o registro da tabela tb_postagens com o id informado
		
	}
    
    @GetMapping("/titulo/{titulo}") // Quando alguém chama GET /postagens/titulo/{titulo}
    public ResponseEntity<List<Postagem>> getAllByTitulo(@PathVariable String titulo) {
    			// Busca todas as postagens cujo título contenha o texto informado, ignorando se está em maiúsculas ou minúsculas
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
		// SELECT * FROM tb_postagens WHERE titulo LIKE '%{titulo}%';  (SQL) <- Isso é o que o método findAllByTituloContainingIgnoreCase() faz, ele retorna todos os registros da tabela tb_postagens cujo título contenha o texto informado
		
		}
    
    @PostMapping // Quando alguém chama POST /postagens
    public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {
		// Salva a postagem no banco e devolve
		return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
		// INSERT INTO tb_postagens (titulo, texto) VALUES ({titulo}, {texto});  (SQL) <- Isso é o que o método save() faz, ele insere um novo registro na tabela tb_postagens com os valores informados
    }
    
    @PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {

		// Verifica se já existe uma postagem com o mesmo ID no banco
		if (postagemRepository.existsById(postagem.getId()))

			// Se existir, salva os novos dados e retorna status 200 OK com a postagem
			// atualizada
			return ResponseEntity.ok(postagemRepository.save(postagem));

		// Comparação com SQL = UPDATE tb_postagens SET titulo = ?, texto = ? WHERE id =
		// ?

		// Se não encontrar a postagem, retorna status 404 Not Found
		return ResponseEntity.notFound().build();
	}
    
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 No Content se a postagem for deletada com sucesso	
    @DeleteMapping("/{id}") // Quando alguém chama DELETE /postagens/{id}
    public void delete(@PathVariable Long id) {
    	// Deleta a postagem no banco
    	Optional<Postagem> postagem = postagemRepository.findById(id); // Busca a postagem pelo id no banco
    	if(postagem.isEmpty()) // Se não encontrar, devolve 404 Not Found
			throw new ResponseStatusException(HttpStatus.NOT_FOUND); // Lança exceção para devolver 404 Not Found
		postagemRepository.deleteById(id); // DELETE FROM tb_postagens WHERE id = {id};  (SQL) <- Isso é o que o método deleteById() faz, ele deleta o registro da tabela tb_postagens com o id informado
    }


    
}

