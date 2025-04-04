package br.ufal.ic.p2.jackut.model.models;

import br.ufal.ic.p2.jackut.model.exceptions.*;
import java.io.Serializable;
import java.util.*;

/**
 * Representa um usuário do sistema, contendo informações pessoais,
 * relacionamentos e mensagens.
 */
public class Usuario implements Serializable {
    /** Login único do usuário */
    private String login;

    /** Senha de acesso */
    private String senha;

    /** Nome de exibição */
    private String nome;

    /** Atributos dinâmicos do perfil */
    private Map<String, String> perfil;

    /** Lista de amigos confirmados */
    private Set<String> amigos;

    /** Convites de amizade enviados */
    private Set<String> convitesEnviados;

    /** Convites de amizade recebidos */
    private Set<String> convitesRecebidos;

    /** Fila de recados recebidos */
    private Queue<String> recados;

    /**
     * Constrói um novo usuário com os dados básicos.
     * @param login Identificador único do usuário
     * @param senha Senha de acesso
     * @param nome Nome de exibição do usuário
     */
    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.perfil = new HashMap<>();
        this.amigos = new LinkedHashSet<>();
        this.convitesEnviados = new HashSet<>();
        this.convitesRecebidos = new HashSet<>();
        this.recados = new LinkedList<>();
    }

    // Getters básicos

    /**
     * Retorna o login do usuário.
     * @return Login do usuário
     */
    public String getLogin() { return login; }

    /**
     * Retorna a senha do usuário.
     * @return Senha do usuário
     */
    public String getSenha() { return senha; }

    /**
     * Retorna o nome do usuário.
     * @return Nome do usuário
     */
    public String getNome() { return nome; }

    // Gestão de amizades

    /**
     * Envia um convite de amizade para outro usuário.
     * @param amigo Login do usuário que receberá o convite
     * @throws IllegalArgumentException Se já for amigo ou já tiver enviado convite
     */
    public void enviarConvite(String amigo) {
        if (amigos.contains(amigo)) {
            throw new IllegalArgumentException("Usuário já está adicionado como amigo.");
        }
        if (convitesEnviados.contains(amigo)) {
            throw new IllegalArgumentException("Usuário já está adicionado como amigo, esperando aceitação do convite.");
        }
        convitesEnviados.add(amigo);
    }

    /**
     * Recebe um convite de amizade de outro usuário.
     * @param amigo Login do usuário que enviou o convite
     */
    public void receberConvite(String amigo) {
        convitesRecebidos.add(amigo);
    }

    /**
     * Confirma uma amizade após aceitação de convite.
     * @param amigo Login do usuário que será adicionado como amigo
     * @throws IllegalArgumentException Se não houver convite pendente
     */
    public void confirmarAmizade(String amigo) {
        if (!convitesRecebidos.contains(amigo)) {
            throw new IllegalArgumentException("Convite não encontrado.");
        }
        convitesRecebidos.remove(amigo);
        amigos.add(amigo);
        convitesEnviados.remove(amigo);
    }

    /**
     * Verifica se um usuário é amigo.
     * @param amigo Login do usuário a verificar
     * @return true se forem amigos, false caso contrário
     */
    public boolean ehAmigo(String amigo) {
        return amigos.contains(amigo);
    }

    /**
     * Retorna a lista de amigos como uma string formatada.
     * @return String no formato "{amigo1,amigo2,...}"
     */
    public String getAmigosString() {
        StringBuilder sb = new StringBuilder("{");
        Iterator<String> it = amigos.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    // Gestão de perfil

    /**
     * Adiciona ou edita um atributo no perfil do usuário.
     * @param atributo Nome do atributo
     * @param valor Valor do atributo
     */
    public void editarAtributo(String atributo, String valor) {
        perfil.put(atributo, valor);
    }

    /**
     * Obtém o valor de um atributo do perfil.
     * @param atributo Nome do atributo
     * @return Valor do atributo
     * @throws AtributoNaoPreenchidoException Se o atributo não existir
     */
    public String getAtributo(String atributo) {
        if ("nome".equals(atributo)) {
            return this.nome;
        }
        if (!perfil.containsKey(atributo)) {
            throw new AtributoNaoPreenchidoException();
        }
        return perfil.get(atributo);
    }

    // Gestão de recados

    /**
     * Adiciona um novo recado à fila do usuário.
     * @param mensagem Conteúdo do recado
     */
    public void receberRecado(String mensagem) {
        recados.add(mensagem);
    }

    /**
     * Lê e remove o próximo recado da fila.
     * @return Conteúdo do recado
     * @throws IllegalStateException Se não houver recados
     */
    public String lerRecado() {
        if (recados.isEmpty()) {
            throw new IllegalStateException("Não há recados.");
        }
        return recados.poll();
    }

    /**
     * Verifica se existe convite pendente enviado para um usuário.
     * @param amigo Login do usuário a verificar
     * @return true se houver convite pendente, false caso contrário
     */
    public boolean possuiConvitePara(String amigo) {
        return convitesEnviados.contains(amigo);
    }

    /**
     * Verifica se existe convite pendente recebido de um usuário.
     * @param amigo Login do usuário a verificar
     * @return true se houver convite pendente, false caso contrário
     */
    public boolean possuiConviteDe(String amigo) {
        return convitesRecebidos.contains(amigo);
    }
}