package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exce��o lan�ada quando um usu�rio j� � amigo ou a amizade est� pendente.
 */
public class AmizadeExistenteException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construtor padr�o com mensagem indicando que o usu�rio j� � amigo.
     */
    public AmizadeExistenteException() {
        super("Usu�rio j� est� adicionado como amigo.");
    }

    /**
     * Construtor com mensagem indicando que a amizade est� pendente de aceita��o.
     *
     * @param pendente Se a amizade est� pendente.
     */
    public AmizadeExistenteException(boolean pendente) {
        super("Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite.");
    }
}
