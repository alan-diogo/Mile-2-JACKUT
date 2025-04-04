package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exce��o lan�ada quando um atributo n�o � preenchido corretamente.
 */
public class AtributoNaoPreenchidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construtor com mensagem padr�o indicando que um atributo n�o foi preenchido.
     */
    public AtributoNaoPreenchidoException() {
        super("Atributo n�o preenchido.");
    }
}
