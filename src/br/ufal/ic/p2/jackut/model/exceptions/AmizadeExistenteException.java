package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção lançada quando um usuário já é amigo ou a amizade está pendente.
 */
public class AmizadeExistenteException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construtor padrão com mensagem indicando que o usuário já é amigo.
     */
    public AmizadeExistenteException() {
        super("Usuário já está adicionado como amigo.");
    }

    /**
     * Construtor com mensagem indicando que a amizade está pendente de aceitação.
     *
     * @param pendente Se a amizade está pendente.
     */
    public AmizadeExistenteException(boolean pendente) {
        super("Usuário já está adicionado como amigo, esperando aceitação do convite.");
    }
}
