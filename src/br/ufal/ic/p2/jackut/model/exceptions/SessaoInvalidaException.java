package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exce��o lan�ada quando a sess�o � considerada inv�lida.
 */
public class SessaoInvalidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construtor com mensagem padr�o indicando que a sess�o � inv�lida.
     */
    public SessaoInvalidaException() {
        super("Sess�o inv�lida.");
    }
}
