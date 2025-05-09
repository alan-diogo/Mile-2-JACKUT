package br.ufal.ic.p2.jackut.model.models;

import br.ufal.ic.p2.jackut.model.exceptions.*;
import java.io.Serializable;
import java.util.*;

/**
 * Representa um usu�rio do sistema Jackut, armazenando informa��es pessoais,
 * relacionamentos (amigos, �dolos, paqueras, inimigos), comunidades e mensagens.
 * Gerencia opera��es como edi��o de perfil, envio de recados, e intera��es sociais.
 */
public class Usuario implements Serializable {
    /** Login �nico do usu�rio (chave prim�ria no sistema) */
    private String login;

    /** Senha de autentica��o do usu�rio */
    private String senha;

    /** Nome p�blico para exibi��o */
    private String nome;

    /** Atributos din�micos do perfil (ex: idade, cidade) */
    private Map<String, String> perfil;

    /** Conjunto de amigos confirmados (logins) */
    private Set<String> amigos;

    /** Convites de amizade pendentes enviados */
    private Set<String> convitesEnviados;

    /** Convites de amizade pendentes recebidos */
    private Set<String> convitesRecebidos;

    /** Fila de recados recebidos de outros usu�rios */
    private Queue<String> recados;
    /** Fila de mensagens recebidas de comunidades */
    private Queue<String> mensagensComunidade = new LinkedList<>();
    /** Usu�rios que este usu�rio admira (rela��o unidirecional) */
    private Set<String> idolos = new HashSet<>();
    /** Usu�rios que admiram este usu�rio */
    private Set<String> fas = new HashSet<>();
    /** Paqueras adicionadas pelo usu�rio */
    private Set<String> paqueras = new HashSet<>();
    /** Inimigos declarados pelo usu�rio */
    private Set<String> inimigos = new HashSet<>();
    /** Comunidades das quais o usu�rio � membro */
    private Set<String> comunidades = new LinkedHashSet<>();
    // Construtor
    /**
     * Cria um novo usu�rio com dados b�sicos e inicializa estruturas internas.
     *
     * @param login Identificador �nico (n�o pode ser nulo ou vazio)
     * @param senha Senha de autentica��o (n�o pode ser nula ou vazia)
     * @param nome Nome p�blico para exibi��o
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
     * Retorna o login �nico do usu�rio.
     * @return String com o login (ex: "joao123")
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
        return recados.poll();  // Retorna e remove o recado da fila
    }

    public String lerMensagem() {
        if (mensagensComunidade.isEmpty()) { // Nova fila para mensagens de comunidades
            throw new IllegalStateException("N�o h� mensagens.");
        }
        return mensagensComunidade.poll();
    }

    /**
     * Verifica se existe convite pendente enviado para um usu�rio.
     * @param amigo Login do usu�rio a verificar
     * @return true se houver convite pendente, false caso contr�rio
     */
    public boolean possuiConvitePara(String amigo) {
        return convitesEnviados.contains(amigo);
    }
    // Gest�o de comunidades
    /**
     * Adiciona o usu�rio a uma comunidade.
     * @param nomeComunidade Nome da comunidade (ex: "Programadores Java")
     */
    public void adicionarComunidade(String nomeComunidade) {
        comunidades.add(nomeComunidade);
    }

    public Set<String> getComunidades() {
        return comunidades; // Deve ser o Set<String> original
    }
    public Queue<String> getRecados() {
        return recados;
    }

    public void receberMensagemComunidade(String mensagem) {
        mensagensComunidade.add(mensagem); // Armazena na fila espec�fica
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    // M�todos para �dolos/f�s:

    /**
     * Adiciona um �dolo (usu�rio admirado).
     * @param idolo Login do �dolo
     * @throws IllegalArgumentException Se for auto-adicionamento ou duplicado
     */
    public void adicionarIdolo(String idolo) {
        if (idolos.contains(idolo)) {
            throw new IllegalArgumentException("Usu�rio j� est� adicionado como �dolo.");
        }
        if (login.equals(idolo)) {
            throw new IllegalArgumentException("Usu�rio n�o pode ser �dolo de si mesmo.");
        }
        idolos.add(idolo); // Adiciona o �dolo
    }

    public boolean ehFa(String idolo) {
        return idolos.contains(idolo);
    }

    public Set<String> getFas() {
        return new HashSet<>(fas);
    }

    public void adicionarFa(String fa) {
        if (login.equals(fa)) {
            throw new IllegalArgumentException("Usu�rio n�o pode ser f� de si mesmo.");
        }
        fas.add(fa);
    }

    /**
     * Adiciona uma paquera (interesse rom�ntico).
     * @param paquera Login da paquera
     * @throws IllegalArgumentException Se for auto-adicionamento ou duplicado
     */
    public void adicionarPaquera(String paquera) {
        if (paqueras.contains(paquera)) {
            throw new IllegalArgumentException("Usu�rio j� est� adicionado como paquera.");
        }
        if (login.equals(paquera)) {
            throw new IllegalArgumentException("Usu�rio n�o pode ser paquera de si mesmo.");
        }
        paqueras.add(paquera); // Adiciona a paquera
    }

    public boolean ehPaquera(String paquera) {
        return paqueras.contains(paquera);
    }

    public Set<String> getPaqueras() {
        return new HashSet<>(paqueras);
    }

    // M�todos para inimigos:
    public void adicionarInimigo(String inimigo) {
        inimigos.add(inimigo);
    }

    public Set<String> getInimigos() {
        return new HashSet<>(inimigos);
    }
    public void removerAmigo(String amigo) {
        amigos.remove(amigo);
        convitesEnviados.remove(amigo);
        convitesRecebidos.remove(amigo);
    }

    public void removerIdolo(String idolo) {
        idolos.remove(idolo);
    }

    public void removerFa(String fa) {
        fas.remove(fa);
    }

    public void removerPaquera(String paquera) {
        paqueras.remove(paquera);
    }

    public void removerInimigo(String inimigo) {
        inimigos.remove(inimigo);
    }
}
