package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exce��o personalizada que � lan�ada quando uma tentativa de acessar ou manipular uma comunidade
 * que n�o existe no sistema � realizada. Esta exce��o herda de {@link RuntimeException}.
 *
 * A exce��o � utilizada para indicar que a comunidade solicitada n�o foi encontrada no sistema.
 */
public class ComunidadeNaoExisteException extends RuntimeException {

    /**
     * Constr�i uma nova exce��o {@link ComunidadeNaoExisteException} com uma mensagem padr�o.
     * A mensagem padr�o indica que a comunidade solicitada n�o existe.
     */
    public ComunidadeNaoExisteException() {
        super("Comunidade n�o existe.");
    }
}

