package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção personalizada que é lançada quando um usuário tenta se tornar fã de si mesmo.
 * Esta exceção herda de {@link RuntimeException}, e é usada para indicar que uma operação
 * inválida foi tentada, ou seja, quando um usuário tenta adicionar a si mesmo como fã.
 */
public class BloqueioAutoIdolException extends RuntimeException {

    /**
     * Constrói uma nova exceção {@link BloqueioAutoIdolException} com uma mensagem padrão.
     * A mensagem padrão indica que o usuário não pode ser fã de si mesmo.
     */
    public BloqueioAutoIdolException() {
        super("Usuário não pode ser fã de si mesmo.");
    }
}