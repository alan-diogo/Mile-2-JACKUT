package br.ufal.ic.p2.jackut.model.models;

import java.io.Serializable;

/**
 * Representa uma sessão ativa de usuário no sistema Jackut.
 * Armazena um ID único associado a um usuário autenticado, permitindo o gerenciamento de sessões.
 */
public class Sessao implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID único gerado aleatoriamente para identificar a sessão.
     */
    private String id;

    /**
     * Usuário autenticado vinculado a esta sessão.
     */
    private Usuario usuario;

    /**
     * Cria uma nova sessão ativa para um usuário.
     *
     * @param id Identificador único gerado para a sessão (normalmente via {@link UUID}).
     * @param usuario Instância do usuário autenticado.
     */
    public Sessao(String id, Usuario usuario) {
        this.id = id;
        this.usuario = usuario;
    }

    /**
     * Retorna o identificador único desta sessão.
     * @return String no formato UUID que representa a sessão.
     */
    public String getId() { return id; }

    /**
     * Retorna o usuário logado associado a esta sessão.
     * @return Objeto {@link Usuario} contendo dados do usuário autenticado.
     */
    public Usuario getUsuario() { return usuario; }
}