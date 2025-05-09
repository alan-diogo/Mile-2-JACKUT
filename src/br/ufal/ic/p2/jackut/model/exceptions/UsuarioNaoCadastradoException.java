package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta acessar um usu�rio n�o cadastrado no sistema.
 */public class UsuarioNaoCadastradoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constr�i uma exce��o com mensagem padr�o.
     */
    public UsuarioNaoCadastradoException() {
        super("Usu�rio n�o cadastrado.");
    }
}