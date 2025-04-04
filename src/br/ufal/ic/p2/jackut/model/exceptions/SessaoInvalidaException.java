package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção lançada quando a sessão é considerada inválida.
 */
public class SessaoInvalidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construtor com mensagem padrão indicando que a sessão é inválida.
     */
    public SessaoInvalidaException() {
        super("Sessão inválida.");
    }
}
