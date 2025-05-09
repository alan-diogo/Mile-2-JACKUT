package br.ufal.ic.p2.jackut.model.models;

import br.ufal.ic.p2.jackut.model.exceptions.*;
import java.io.Serializable;
import java.util.Set;
import java.util.LinkedHashSet;

/**
 * Classe que representa uma comunidade no sistema Jackut.
 * Gerencia informações como nome, descrição, dono e membros da comunidade.
 * Permite adicionar/remover membros e garante a integridade dos dados.
 */

public class Comunidade implements Serializable {
    private String nome;
    private String descricao;
    private String dono;
    private Set<String> membros = new LinkedHashSet<>();

    /**
     * Constrói uma nova comunidade com nome, descrição e dono especificados.
     * O dono é automaticamente adicionado como primeiro membro.
     *
     * @param nome Nome único da comunidade.
     * @param descricao Descrição da comunidade.
     * @param dono Login do usuário criador da comunidade.
     */
    public Comunidade(String nome, String descricao, String dono) {
        this.nome = nome;
        this.descricao = descricao;
        this.dono = dono;
        this.membros.add(dono); // Adiciona o dono como membro
    }


    // Getters

    /**
     * Retorna o nome da comunidade.
     * @return Nome da comunidade.
     */
    public String getNome() { return nome; }
    /**
     * Retorna a descrição da comunidade.
     * @return Descrição textual da comunidade.
     */
    public String getDescricao() { return descricao; }
    /**
     * Retorna o login do dono da comunidade.
     * @return Login do usuário proprietário.
     */
    public String getDono() { return dono; }
    /**
     * Retorna uma cópia imutável da lista de membros.
     * @return Conjunto de logins dos membros em ordem de inserção.
     */
    public Set<String> getMembros() {
        return new LinkedHashSet<>(membros); // Garante imutabilidade
    }
    /**
     * Remove um membro da comunidade.
     * @param login Login do usuário a ser removido.
     */
    public void removerMembro(String login) {
        membros.remove(login);
    }

    /**
     * Adiciona um novo membro à comunidade.
     * @param login Login do usuário a ser adicionado.
     * @throws UsuarioJaMembroException Se o usuário já for membro da comunidade.
     */
    public void adicionarMembro(String login) {
        if (membros.contains(login)) {
            throw new UsuarioJaMembroException();
        }
        membros.add(login);
    }

}
