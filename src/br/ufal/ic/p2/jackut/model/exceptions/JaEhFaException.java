package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção personalizada que é lançada quando um usuário tenta adicionar um ídolo que já é fã.
 * Esta exceção herda de {@link RuntimeException}.
 *
 * A exceção é utilizada para indicar que o usuário não pode adicionar um ídolo se já for fã dele.
 */
public class JaEhFaException extends RuntimeException {

    /**
     * Constrói uma nova exceção {@link JaEhFaException} com uma mensagem padrão.
     * A mensagem padrão indica que o usuário não pode adicionar como ídolo alguém que já é fã.
     */
    public JaEhFaException() {
        super("Usuário não pode adicionar como ídolo, pois já é fã.");
    }
}

