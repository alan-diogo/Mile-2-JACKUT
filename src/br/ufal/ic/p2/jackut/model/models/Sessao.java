package br.ufal.ic.p2.jackut.model.models;

import java.io.Serializable;

/**
 * Representa uma sessão de usuário.
 * Mantém o estado de uma sessão ativa, vinculando um ID único ao usuário logado.
 */
public class Sessao implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID único da sessão, gerado aleatoriamente
     */
    private String id;

    /**
     * Referência ao usuário associado a esta sessão
     */
    private Usuario usuario;

    /**
     * Cria uma nova sessão para um usuário.
     * @param id Identificador único da sessão
     * @param usuario Usuário que está logado nesta sessão
     */
    public Sessao(String id, Usuario usuario) {
        this.id = id;
        this.usuario = usuario;
    }

    /**
     * Retorna o ID da sessão.
     * @return String com o ID único da sessão
     */
    public String getId() { return id; }

    /**
     * Retorna o usuário associado a esta sessão.
     * @return Objeto Usuario da sessão
     */
    public Usuario getUsuario() { return usuario; }
}