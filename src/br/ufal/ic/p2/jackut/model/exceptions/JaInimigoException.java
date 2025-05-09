package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção personalizada que é lançada quando um usuário tenta adicionar alguém como inimigo
 * que já foi previamente adicionado como tal. Esta exceção herda de {@link RuntimeException}.
 *
 * A exceção é utilizada para indicar que o usuário não pode ser adicionado novamente como inimigo,
 * pois já está registrado como inimigo.
 */
public class JaInimigoException extends RuntimeException {

    /**
     * Constrói uma nova exceção {@link JaInimigoException} com uma mensagem padrão.
     * A mensagem padrão indica que o usuário já foi adicionado como inimigo.
     */
    public JaInimigoException() {
        super("Usuário já está adicionado como inimigo.");
    }
}
