package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção personalizada que é lançada quando um usuário tenta adicionar alguém como ídolo
 * que já foi previamente adicionado como tal. Esta exceção herda de {@link RuntimeException}.
 *
 * A exceção é utilizada para indicar que o usuário não pode ser adicionado novamente como ídolo,
 * pois já está registrado como tal.
 */
public class UsuarioAddIdolException extends RuntimeException {

    /**
     * Constrói uma nova exceção {@link UsuarioAddIdolException} com uma mensagem padrão.
     * A mensagem padrão indica que o usuário já foi adicionado como ídolo.
     */
    public UsuarioAddIdolException() {
        super("Usuário já está adicionado como ídolo.");
    }
}

