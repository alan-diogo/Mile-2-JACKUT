package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exce��o lan�ada quando n�o h� recados ou quando ocorre um erro relacionado a recados.
 */
public class RecadoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construtor com mensagem padr�o indicando que n�o h� recados.
     */
    public RecadoException() {
        super("N�o h� recados.");
    }

    /**
     * Construtor que permite especificar uma mensagem personalizada para a exce��o.
     *
     * @param mensagem A mensagem de erro a ser exibida.
     */
    public RecadoException(String mensagem) {
        super(mensagem);
    }
}
