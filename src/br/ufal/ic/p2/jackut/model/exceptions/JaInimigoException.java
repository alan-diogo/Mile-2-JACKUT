package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exce��o personalizada que � lan�ada quando um usu�rio tenta adicionar algu�m como inimigo
 * que j� foi previamente adicionado como tal. Esta exce��o herda de {@link RuntimeException}.
 *
 * A exce��o � utilizada para indicar que o usu�rio n�o pode ser adicionado novamente como inimigo,
 * pois j� est� registrado como inimigo.
 */
public class JaInimigoException extends RuntimeException {

    /**
     * Constr�i uma nova exce��o {@link JaInimigoException} com uma mensagem padr�o.
     * A mensagem padr�o indica que o usu�rio j� foi adicionado como inimigo.
     */
    public JaInimigoException() {
        super("Usu�rio j� est� adicionado como inimigo.");
    }
}
