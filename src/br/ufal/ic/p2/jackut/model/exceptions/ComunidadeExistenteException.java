package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exce��o personalizada que � lan�ada quando uma tentativa de criar uma comunidade com um nome
 * que j� existe no sistema � realizada. Esta exce��o herda de {@link RuntimeException}.
 *
 * A exce��o � utilizada para indicar que o nome da comunidade a ser criada j� est� em uso.
 */
public class ComunidadeExistenteException extends RuntimeException {

  /**
   * Constr�i uma nova exce��o {@link ComunidadeExistenteException} com uma mensagem padr�o.
   * A mensagem padr�o indica que j� existe uma comunidade com o nome fornecido.
   */
  public ComunidadeExistenteException() {
    super("Comunidade com esse nome j� existe.");
  }
}

