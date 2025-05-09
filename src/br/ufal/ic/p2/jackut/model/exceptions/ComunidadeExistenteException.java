package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção personalizada que é lançada quando uma tentativa de criar uma comunidade com um nome
 * que já existe no sistema é realizada. Esta exceção herda de {@link RuntimeException}.
 *
 * A exceção é utilizada para indicar que o nome da comunidade a ser criada já está em uso.
 */
public class ComunidadeExistenteException extends RuntimeException {

  /**
   * Constrói uma nova exceção {@link ComunidadeExistenteException} com uma mensagem padrão.
   * A mensagem padrão indica que já existe uma comunidade com o nome fornecido.
   */
  public ComunidadeExistenteException() {
    super("Comunidade com esse nome já existe.");
  }
}

