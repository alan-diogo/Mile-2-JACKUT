package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exce��o personalizada que � lan�ada quando um usu�rio tenta se tornar paquera de si mesmo.
 * Esta exce��o herda de {@link RuntimeException}, e � usada para indicar que uma opera��o
 * inv�lida foi tentada, ou seja, quando um usu�rio tenta adicionar a si mesmo como paquera.
 */
public class BloqueioAutoPaqueraException extends RuntimeException {

    /**
     * Constr�i uma nova exce��o {@link BloqueioAutoPaqueraException} com uma mensagem padr�o.
     * A mensagem padr�o indica que o usu�rio n�o pode ser paquera de si mesmo.
     */
    public BloqueioAutoPaqueraException() {
        super("Usu�rio n�o pode ser paquera de si mesmo.");
    }
}