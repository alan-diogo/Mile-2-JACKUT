package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção personalizada que é lançada quando um usuário tenta enviar um recado para si mesmo.
 * Esta exceção herda de {@link RuntimeException}, e é usada para indicar que uma operação
 * inválida foi tentada, ou seja, quando um usuário tenta enviar um recado para si mesmo.
 */
public class BloqueioAutoRecadoException extends RuntimeException {

    /**
     * Constrói uma nova exceção {@link BloqueioAutoRecadoException} com uma mensagem padrão.
     * A mensagem padrão indica que o usuário não pode enviar recado para si mesmo.
     */
    public BloqueioAutoRecadoException() {
        super("Usuário não pode enviar recado para si mesmo.");
    }
}
