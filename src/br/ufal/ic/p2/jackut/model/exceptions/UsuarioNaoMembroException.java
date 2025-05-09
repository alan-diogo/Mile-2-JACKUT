package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exce��o personalizada que � lan�ada quando um usu�rio tenta realizar uma a��o em uma comunidade
 * da qual n�o � membro. Esta exce��o herda de {@link RuntimeException}.
 *
 * A exce��o � utilizada para indicar que o usu�rio n�o pode realizar a a��o solicitada, pois
 * ele n�o � membro da comunidade.
 */
public class UsuarioNaoMembroException extends RuntimeException {

    /**
     * Constr�i uma nova exce��o {@link UsuarioNaoMembroException} com uma mensagem padr�o.
     * A mensagem padr�o indica que o usu�rio n�o � membro da comunidade.
     */
    public UsuarioNaoMembroException() {
        super("Usu�rio n�o � membro da comunidade.");
    }
}

