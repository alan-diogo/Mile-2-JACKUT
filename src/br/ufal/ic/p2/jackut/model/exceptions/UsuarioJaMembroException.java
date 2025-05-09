package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exceção lançada quando uma tentativa é feita de adicionar um usuário a uma comunidade
 * da qual ele já é membro.
 * <p>
 * Esta é uma exceção não verificada (unchecked), pois herda de {@link RuntimeException}.
 * Ela é usada para sinalizar que a operação de adição de um membro falhou devido ao fato
 * de o usuário já estar presente na comunidade.
 * </p>

 */
public class UsuarioJaMembroException extends RuntimeException {

    /**
     * Constrói uma nova instância de {@code UsuarioJaMembroException} com a mensagem padrão
     * "Usuário já faz parte dessa comunidade.".
     */
    public UsuarioJaMembroException() {
        super("Usuario já faz parte dessa comunidade.");
    }
}
