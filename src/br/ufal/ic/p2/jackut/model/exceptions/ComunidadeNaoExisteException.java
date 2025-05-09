package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção personalizada que é lançada quando uma tentativa de acessar ou manipular uma comunidade
 * que não existe no sistema é realizada. Esta exceção herda de {@link RuntimeException}.
 *
 * A exceção é utilizada para indicar que a comunidade solicitada não foi encontrada no sistema.
 */
public class ComunidadeNaoExisteException extends RuntimeException {

    /**
     * Constrói uma nova exceção {@link ComunidadeNaoExisteException} com uma mensagem padrão.
     * A mensagem padrão indica que a comunidade solicitada não existe.
     */
    public ComunidadeNaoExisteException() {
        super("Comunidade não existe.");
    }
}

