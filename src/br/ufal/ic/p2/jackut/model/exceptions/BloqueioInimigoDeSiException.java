package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção personalizada que é lançada quando um usuário tenta se tornar inimigo de si mesmo.
 * Esta exceção herda de {@link RuntimeException}, e é usada para indicar que uma operação
 * inválida foi tentada, ou seja, quando um usuário tenta adicionar a si mesmo como inimigo.
 */
public class BloqueioInimigoDeSiException extends RuntimeException {

    /**
     * Constrói uma nova exceção {@link BloqueioInimigoDeSiException} com uma mensagem padrão.
     * A mensagem padrão indica que o usuário não pode ser inimigo de si mesmo.
     */
    public BloqueioInimigoDeSiException() {
        super("Usuário não pode ser inimigo de si mesmo.");
    }
}
