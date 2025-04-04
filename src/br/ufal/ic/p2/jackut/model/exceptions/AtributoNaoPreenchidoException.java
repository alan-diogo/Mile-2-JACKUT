package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção lançada quando um atributo não é preenchido corretamente.
 */
public class AtributoNaoPreenchidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construtor com mensagem padrão indicando que um atributo não foi preenchido.
     */
    public AtributoNaoPreenchidoException() {
        super("Atributo não preenchido.");
    }
}
