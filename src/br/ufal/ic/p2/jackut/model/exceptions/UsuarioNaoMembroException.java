package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção personalizada que é lançada quando um usuário tenta realizar uma ação em uma comunidade
 * da qual não é membro. Esta exceção herda de {@link RuntimeException}.
 *
 * A exceção é utilizada para indicar que o usuário não pode realizar a ação solicitada, pois
 * ele não é membro da comunidade.
 */
public class UsuarioNaoMembroException extends RuntimeException {

    /**
     * Constrói uma nova exceção {@link UsuarioNaoMembroException} com uma mensagem padrão.
     * A mensagem padrão indica que o usuário não é membro da comunidade.
     */
    public UsuarioNaoMembroException() {
        super("Usuário não é membro da comunidade.");
    }
}

