package br.ufal.ic.p2.jackut.model.models;

import br.ufal.ic.p2.jackut.model.exceptions.*;
import java.io.Serializable;
import java.util.*;

/**
 * Representa um usu�rio do sistema, contendo informa��es pessoais,
 * relacionamentos e mensagens.
 */
public class Usuario implements Serializable {
    /** Login �nico do usu�rio */
    private String login;

    /** Senha de acesso */
    private String senha;

    /** Nome de exibi��o */
    private String nome;

    /** Atributos din�micos do perfil */
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
     * Constr�i um novo usu�rio com os dados b�sicos.
     * @param login Identificador �nico do usu�rio
     * @param senha Senha de acesso
     * @param nome Nome de exibi��o do usu�rio
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

    // Getters b�sicos

    /**
     * Retorna o login do usu�rio.
     * @return Login do usu�rio
     */
    public String getLogin() { return login; }

    /**
     * Retorna a senha do usu�rio.
     * @return Senha do usu�rio
     */
    public String getSenha() { return senha; }

    /**
     * Retorna o nome do usu�rio.
     * @return Nome do usu�rio
     */
    public String getNome() { return nome; }

    // Gest�o de amizades

    /**
     * Envia um convite de amizade para outro usu�rio.
     * @param amigo Login do usu�rio que receber� o convite
     * @throws IllegalArgumentException Se j� for amigo ou j� tiver enviado convite
     */
    public void enviarConvite(String amigo) {
        if (amigos.contains(amigo)) {
            throw new IllegalArgumentException("Usu�rio j� est� adicionado como amigo.");
        }
        if (convitesEnviados.contains(amigo)) {
            throw new IllegalArgumentException("Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite.");
        }
        convitesEnviados.add(amigo);
    }

    /**
     * Recebe um convite de amizade de outro usu�rio.
     * @param amigo Login do usu�rio que enviou o convite
     */
    public void receberConvite(String amigo) {
        convitesRecebidos.add(amigo);
    }

    /**
     * Confirma uma amizade ap�s aceita��o de convite.
     * @param amigo Login do usu�rio que ser� adicionado como amigo
     * @throws IllegalArgumentException Se n�o houver convite pendente
     */
    public void confirmarAmizade(String amigo) {
        if (!convitesRecebidos.contains(amigo)) {
            throw new IllegalArgumentException("Convite n�o encontrado.");
        }
        convitesRecebidos.remove(amigo);
        amigos.add(amigo);
        convitesEnviados.remove(amigo);
    }

    /**
     * Verifica se um usu�rio � amigo.
     * @param amigo Login do usu�rio a verificar
     * @return true se forem amigos, false caso contr�rio
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

    // Gest�o de perfil

    /**
     * Adiciona ou edita um atributo no perfil do usu�rio.
     * @param atributo Nome do atributo
     * @param valor Valor do atributo
     */
    public void editarAtributo(String atributo, String valor) {
        perfil.put(atributo, valor);
    }

    /**
     * Obt�m o valor de um atributo do perfil.
     * @param atributo Nome do atributo
     * @return Valor do atributo
     * @throws AtributoNaoPreenchidoException Se o atributo n�o existir
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

    // Gest�o de recados

    /**
     * Adiciona um novo recado � fila do usu�rio.
     * @param mensagem Conte�do do recado
     */
    public void receberRecado(String mensagem) {
        recados.add(mensagem);
    }

    /**
     * L� e remove o pr�ximo recado da fila.
     * @return Conte�do do recado
     * @throws IllegalStateException Se n�o houver recados
     */
    public String lerRecado() {
        if (recados.isEmpty()) {
            throw new IllegalStateException("N�o h� recados.");
        }
        return recados.poll();
    }

    /**
     * Verifica se existe convite pendente enviado para um usu�rio.
     * @param amigo Login do usu�rio a verificar
     * @return true se houver convite pendente, false caso contr�rio
     */
    public boolean possuiConvitePara(String amigo) {
        return convitesEnviados.contains(amigo);
    }

    /**
     * Verifica se existe convite pendente recebido de um usu�rio.
     * @param amigo Login do usu�rio a verificar
     * @return true se houver convite pendente, false caso contr�rio
     */
    public boolean possuiConviteDe(String amigo) {
        return convitesRecebidos.contains(amigo);
    }
}