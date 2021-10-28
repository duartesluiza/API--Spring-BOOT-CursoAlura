package br.com.alura.forum.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.com.alura.forum.modelo.Topico;

//Nessa classe teremos apenas os tipos "primitivos" do java
public class TopicoDto {
	
	private Long id;
	private String titulo;
	private String mensagem;
	private LocalDateTime dataCriacao;
	
	//Se só tenho os getter preciso definir de onde ele vai puxar os dados. Entao vou utilizar um construtor!
	
	//toda vez que eu der "new" no topico DTO, eu passo como parametro um OBJETO do tipo TOPICO:
	public TopicoDto(Topico topico) {
		this.id = topico.getId();
		this.titulo = topico.getTitulo();
		this.mensagem = topico.getMensagem();
		this.dataCriacao = topico.getDataCriacao();
	}
	
	
	
	public Long getId() {
		return id;
	}
	public String getTitulo() {
		return titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}


	//Agora esse metodo, recebe a lista de topicos e eu preciso devolver uma lista de TopicosDto(convercao)
	public static List<TopicoDto> converter(List<Topico> topicos) {
		//TopicoDto::new -> vai chamar o construtor que recebe o proprio topico como parametro
		// Collectors.toList(): transforma em uma lista
		return topicos.stream().map(TopicoDto::new).collect(Collectors.toList());   //sintaxe do java 8
	}
	
}
