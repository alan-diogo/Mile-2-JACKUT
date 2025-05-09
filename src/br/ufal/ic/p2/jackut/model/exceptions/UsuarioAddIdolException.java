package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exce��o personalizada que � lan�ada quando um usu�rio tenta adicionar algu�m como �dolo
 * que j� foi previamente adicionado como tal. Esta exce��o herda de {@link RuntimeException}.
 *
 * A exce��o � utilizada para indicar que o usu�rio n�o pode ser adicionado novamente como �dolo,
 * pois j� est� registrado como tal.
 */
public class UsuarioAddIdolException extends RuntimeException {

    /**
     * Constr�i uma nova exce��o {@link UsuarioAddIdolException} com uma mensagem padr�o.
     * A mensagem padr�o indica que o usu�rio j� foi adicionado como �dolo.
     */
    public UsuarioAddIdolException() {
        super("Usu�rio j� est� adicionado como �dolo.");
    }
}

