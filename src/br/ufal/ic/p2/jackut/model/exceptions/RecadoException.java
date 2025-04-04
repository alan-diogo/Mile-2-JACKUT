package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção lançada quando não há recados ou quando ocorre um erro relacionado a recados.
 */
public class RecadoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construtor com mensagem padrão indicando que não há recados.
     */
    public RecadoException() {
        super("Não há recados.");
    }

    /**
     * Construtor que permite especificar uma mensagem personalizada para a exceção.
     *
     * @param mensagem A mensagem de erro a ser exibida.
     */
    public RecadoException(String mensagem) {
        super(mensagem);
    }
}
