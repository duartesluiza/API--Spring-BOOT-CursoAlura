package br.com.alura.forum.config.validacao;
//Essa classe vai representar um ERRO DE VALIDAÇÃO!! Representa o erro de algum CAMPO!
//O JSON que sera devolvido p/ o cliente nao sera mais o JSON "maluco"

public class ErroDeFormularioDto {
	
	private String campo;
	private String erro;
	
	
	public ErroDeFormularioDto(String campo, String erro) {
		
		this.campo = campo;
		this.erro = erro;
	}


	public String getCampo() {
		return campo;
	}


	public String getErro() {
		return erro;
	}
	
	
	

}
