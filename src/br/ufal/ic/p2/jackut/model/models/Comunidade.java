package br.ufal.ic.p2.jackut.model.models;

import br.ufal.ic.p2.jackut.model.exceptions.*;
import java.io.Serializable;
import java.util.Set;
import java.util.LinkedHashSet;

/**
 * Classe que representa uma comunidade no sistema Jackut.
 * Gerencia informa��es como nome, descri��o, dono e membros da comunidade.
 * Permite adicionar/remover membros e garante a integridade dos dados.
 */

public class Comunidade implements Serializable {
    private String nome;
    private String descricao;
    private String dono;
    private Set<String> membros = new LinkedHashSet<>();

    /**
     * Constr�i uma nova comunidade com nome, descri��o e dono especificados.
     * O dono � automaticamente adicionado como primeiro membro.
     *
     * @param nome Nome �nico da comunidade.
     * @param descricao Descri��o da comunidade.
     * @param dono Login do usu�rio criador da comunidade.
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
     * Retorna a descri��o da comunidade.
     * @return Descri��o textual da comunidade.
     */
    public String getDescricao() { return descricao; }
    /**
     * Retorna o login do dono da comunidade.
     * @return Login do usu�rio propriet�rio.
     */
    public String getDono() { return dono; }
    /**
     * Retorna uma c�pia imut�vel da lista de membros.
     * @return Conjunto de logins dos membros em ordem de inser��o.
     */
    public Set<String> getMembros() {
        return new LinkedHashSet<>(membros); // Garante imutabilidade
    }
    /**
     * Remove um membro da comunidade.
     * @param login Login do usu�rio a ser removido.
     */
    public void removerMembro(String login) {
        membros.remove(login);
    }

    /**
     * Adiciona um novo membro � comunidade.
     * @param login Login do usu�rio a ser adicionado.
     * @throws UsuarioJaMembroException Se o usu�rio j� for membro da comunidade.
     */
    public void adicionarMembro(String login) {
        if (membros.contains(login)) {
            throw new UsuarioJaMembroException();
        }
        membros.add(login);
    }

}
