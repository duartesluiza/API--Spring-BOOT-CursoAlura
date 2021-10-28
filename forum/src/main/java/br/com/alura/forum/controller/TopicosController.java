package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController // SPRING: ESSE CONTROLLER É UM RESTCONTROLLER. Usando ele nao precisamos mais
				// ficar colando @ResponseBody em todo os metodos
@RequestMapping("/topicos") // SPRING: esse controller ele responde as requisições que começam com
							// "/topicos"
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	// Aqui a logica será a seguinte: tenho que carregar a lista com todos os
	// topicos e devolver ela pra quem tiver feito essa chamada
	@GetMapping
	public List<TopicoDto> lista(String nomeCurso) { // peguei a lista de topicos e converti para topicosDto
		if (nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.converter(topicos);
		} else {
			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
			return TopicoDto.converter(topicos);
		}

	}

	// Boa pratica pra devolver o codigo 201 no controler do SPRING: com
	// ResponseEntity
	// SPRING: esse paramentro "TopicoForm eh pra vc pegar do corpo da
	// requisiçao(@RequestBody) e não das URL
	
	//************** METODO PARA SALVAR!  **************************
	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {

		// aqui eu vou receber um form e converter pra um topico:
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);

		// Devolve pra mim o codigo 201(Usando o created): quando crio o metodo created,
		// ele precisa dessa uri pra adicionar no cabeçalho location
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri(); // criando o URI
		return ResponseEntity.created(uri).body(new TopicoDto(topico));

	}

	// Detalhamento de um topico especifico: a ideia é: chega uma requisicao pra URL, passando um topico especifico e quero devolver os detalhes apenas desse topico:
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);  //Optional: opcional, pode ser q tenha um registro, pode ser que nao tenha
		if (topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
		}  //se entrou no if vai devolver um DTO, se nao:
		// se chegou ate aqui preciso devolver um erro 404:
		
		return ResponseEntity.notFound().build();
		
		
	}

	//****************     METODO RESPOSNAVEL POR ATUALIZAR UM METODO:   ******************
	@PutMapping("/{id}")   //quero sobreescrever um recurso. Ouseja, atualizar todas as informações
	@Transactional //!!!!Avisar pro SPRING que eh pra COMMITAR a transacao no final desse metodo!!!!!!!
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> optional = topicoRepository.findById(id);  
		if (optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository); //quando chegar nessa linha, ele ja atualizou em memoria
			return ResponseEntity.ok(new TopicoDto(topico));  // esse parametro do"ok"->eh o corpo que vai ser devolvido como resposta pelo servidor
		}  
		
		return ResponseEntity.notFound().build();	
		
	}
	
	// ************   METODO PARA EXCLUIR UM DETERMINADO TOPICO!!!   *******************
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
			
		}
		//Vou devolver um erro 404
		return ResponseEntity.notFound().build();
	}

}
