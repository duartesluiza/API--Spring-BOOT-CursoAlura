package br.com.alura.forum.config.validacao;


//O HANDLER eh um interceptador, onde eu to configurando o SPRING pra que,
// sempre que houver algum erro(Exception) em algum metodo de qualquer controller,
// ele chama automaticamente esse interceptador, passando o erro que ocorreu, ou seja, passando a LISTA com todos os erros que ocorreram
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErroDeValidacaoHandler {
	
	@Autowired
	private MessageSource messageSource;   //essa classe eh do SPRING e ajuda a pegar mensagem de erro

	@ResponseStatus(code = HttpStatus.BAD_REQUEST) // Por mais que eu tenha tratado o erro, nao é pra devolver 200, é
													// pra devolver 400!!!
	// Metodo que vai fazer o tratamento do erro:
	@ExceptionHandler(MethodArgumentNotValidException.class) // agora o SPRING sabe que se acontecer essa exception em
																// qualquer restController ele vai cair aqui nesse
																// metodo
	public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception) {
		List<ErroDeFormularioDto> dto = new ArrayList<>();
		
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		fieldErrors.forEach(e -> {
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(), mensagem);
			dto.add(erro);
		});
		
		return dto;
	}

}
