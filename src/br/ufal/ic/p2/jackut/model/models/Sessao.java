package br.ufal.ic.p2.jackut.model.models;

import java.io.Serializable;

/**
 * Representa uma sess�o de usu�rio.
 * Mant�m o estado de uma sess�o ativa, vinculando um ID �nico ao usu�rio logado.
 */
public class Sessao implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID �nico da sess�o, gerado aleatoriamente
     */
    private String id;

    /**
     * Refer�ncia ao usu�rio associado a esta sess�o
     */
    private Usuario usuario;

    /**
     * Cria uma nova sess�o para um usu�rio.
     * @param id Identificador �nico da sess�o
     * @param usuario Usu�rio que est� logado nesta sess�o
     */
    public Sessao(String id, Usuario usuario) {
        this.id = id;
        this.usuario = usuario;
    }

    /**
     * Retorna o ID da sess�o.
     * @return String com o ID �nico da sess�o
     */
    public String getId() { return id; }

    /**
     * Retorna o usu�rio associado a esta sess�o.
     * @return Objeto Usuario da sess�o
     */
    public Usuario getUsuario() { return usuario; }
}