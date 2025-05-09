package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção personalizada que é lançada quando um usuário tenta se tornar paquera de si mesmo.
 * Esta exceção herda de {@link RuntimeException}, e é usada para indicar que uma operação
 * inválida foi tentada, ou seja, quando um usuário tenta adicionar a si mesmo como paquera.
 */
public class BloqueioAutoPaqueraException extends RuntimeException {

    /**
     * Constrói uma nova exceção {@link BloqueioAutoPaqueraException} com uma mensagem padrão.
     * A mensagem padrão indica que o usuário não pode ser paquera de si mesmo.
     */
    public BloqueioAutoPaqueraException() {
        super("Usuário não pode ser paquera de si mesmo.");
    }
}