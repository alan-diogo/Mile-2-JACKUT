package br.ufal.ic.p2.jackut.model.models;

import java.io.Serializable;

/**
 * Representa uma sess�o ativa de usu�rio no sistema Jackut.
 * Armazena um ID �nico associado a um usu�rio autenticado, permitindo o gerenciamento de sess�es.
 */
public class Sessao implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID �nico gerado aleatoriamente para identificar a sess�o.
     */
    private String id;

    /**
     * Usu�rio autenticado vinculado a esta sess�o.
     */
    private Usuario usuario;

    /**
     * Cria uma nova sess�o ativa para um usu�rio.
     *
     * @param id Identificador �nico gerado para a sess�o (normalmente via {@link UUID}).
     * @param usuario Inst�ncia do usu�rio autenticado.
     */
    public Sessao(String id, Usuario usuario) {
        this.id = id;
        this.usuario = usuario;
    }

    /**
     * Retorna o identificador �nico desta sess�o.
     * @return String no formato UUID que representa a sess�o.
     */
    public String getId() { return id; }

    /**
     * Retorna o usu�rio logado associado a esta sess�o.
     * @return Objeto {@link Usuario} contendo dados do usu�rio autenticado.
     */
    public Usuario getUsuario() { return usuario; }
}