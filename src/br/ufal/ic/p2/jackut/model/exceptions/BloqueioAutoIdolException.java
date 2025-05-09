package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exce��o personalizada que � lan�ada quando um usu�rio tenta se tornar f� de si mesmo.
 * Esta exce��o herda de {@link RuntimeException}, e � usada para indicar que uma opera��o
 * inv�lida foi tentada, ou seja, quando um usu�rio tenta adicionar a si mesmo como f�.
 */
public class BloqueioAutoIdolException extends RuntimeException {

    /**
     * Constr�i uma nova exce��o {@link BloqueioAutoIdolException} com uma mensagem padr�o.
     * A mensagem padr�o indica que o usu�rio n�o pode ser f� de si mesmo.
     */
    public BloqueioAutoIdolException() {
        super("Usu�rio n�o pode ser f� de si mesmo.");
    }
}