package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exce��o lan�ada quando uma tentativa � feita de adicionar um usu�rio a uma comunidade
 * da qual ele j� � membro.
 * <p>
 * Esta � uma exce��o n�o verificada (unchecked), pois herda de {@link RuntimeException}.
 * Ela � usada para sinalizar que a opera��o de adi��o de um membro falhou devido ao fato
 * de o usu�rio j� estar presente na comunidade.
 * </p>

 */
public class UsuarioJaMembroException extends RuntimeException {

    /**
     * Constr�i uma nova inst�ncia de {@code UsuarioJaMembroException} com a mensagem padr�o
     * "Usu�rio j� faz parte dessa comunidade.".
     */
    public UsuarioJaMembroException() {
        super("Usuario j� faz parte dessa comunidade.");
    }
}
