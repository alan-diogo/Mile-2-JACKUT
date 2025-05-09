package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exce��o personalizada que � lan�ada quando um usu�rio tenta adicionar um �dolo que j� � f�.
 * Esta exce��o herda de {@link RuntimeException}.
 *
 * A exce��o � utilizada para indicar que o usu�rio n�o pode adicionar um �dolo se j� for f� dele.
 */
public class JaEhFaException extends RuntimeException {

    /**
     * Constr�i uma nova exce��o {@link JaEhFaException} com uma mensagem padr�o.
     * A mensagem padr�o indica que o usu�rio n�o pode adicionar como �dolo algu�m que j� � f�.
     */
    public JaEhFaException() {
        super("Usu�rio n�o pode adicionar como �dolo, pois j� � f�.");
    }
}

