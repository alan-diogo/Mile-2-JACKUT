package br.ufal.ic.p2.jackut.model.exceptions;

/**
 * Exce��o personalizada que � lan�ada quando um usu�rio tenta enviar um recado para si mesmo.
 * Esta exce��o herda de {@link RuntimeException}, e � usada para indicar que uma opera��o
 * inv�lida foi tentada, ou seja, quando um usu�rio tenta enviar um recado para si mesmo.
 */
public class BloqueioAutoRecadoException extends RuntimeException {

    /**
     * Constr�i uma nova exce��o {@link BloqueioAutoRecadoException} com uma mensagem padr�o.
     * A mensagem padr�o indica que o usu�rio n�o pode enviar recado para si mesmo.
     */
    public BloqueioAutoRecadoException() {
        super("Usu�rio n�o pode enviar recado para si mesmo.");
    }
}
