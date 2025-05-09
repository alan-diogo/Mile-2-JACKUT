package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção lançada quando uma operação tenta acessar um usuário não cadastrado no sistema.
 */public class UsuarioNaoCadastradoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constrói uma exceção com mensagem padrão.
     */
    public UsuarioNaoCadastradoException() {
        super("Usuário não cadastrado.");
    }
}