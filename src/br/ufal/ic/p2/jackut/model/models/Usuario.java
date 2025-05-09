package br.ufal.ic.p2.jackut.model.models;

import br.ufal.ic.p2.jackut.model.exceptions.*;
import java.io.Serializable;
import java.util.*;

/**
 * Representa um usuário do sistema Jackut, armazenando informações pessoais,
 * relacionamentos (amigos, ídolos, paqueras, inimigos), comunidades e mensagens.
 * Gerencia operações como edição de perfil, envio de recados, e interações sociais.
 */
public class Usuario implements Serializable {
    /** Login único do usuário (chave primária no sistema) */
    private String login;

    /** Senha de autenticação do usuário */
    private String senha;

    /** Nome público para exibição */
    private String nome;

    /** Atributos dinâmicos do perfil (ex: idade, cidade) */
    private Map<String, String> perfil;

    /** Conjunto de amigos confirmados (logins) */
    private Set<String> amigos;

    /** Convites de amizade pendentes enviados */
    private Set<String> convitesEnviados;

    /** Convites de amizade pendentes recebidos */
    private Set<String> convitesRecebidos;

    /** Fila de recados recebidos de outros usuários */
    private Queue<String> recados;
    /** Fila de mensagens recebidas de comunidades */
    private Queue<String> mensagensComunidade = new LinkedList<>();
    /** Usuários que este usuário admira (relação unidirecional) */
    private Set<String> idolos = new HashSet<>();
    /** Usuários que admiram este usuário */
    private Set<String> fas = new HashSet<>();
    /** Paqueras adicionadas pelo usuário */
    private Set<String> paqueras = new HashSet<>();
    /** Inimigos declarados pelo usuário */
    private Set<String> inimigos = new HashSet<>();
    /** Comunidades das quais o usuário é membro */
    private Set<String> comunidades = new LinkedHashSet<>();
    // Construtor
    /**
     * Cria um novo usuário com dados básicos e inicializa estruturas internas.
     *
     * @param login Identificador único (não pode ser nulo ou vazio)
     * @param senha Senha de autenticação (não pode ser nula ou vazia)
     * @param nome Nome público para exibição
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
     * Retorna o login único do usuário.
     * @return String com o login (ex: "joao123")
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
        return recados.poll();  // Retorna e remove o recado da fila
    }

    public String lerMensagem() {
        if (mensagensComunidade.isEmpty()) { // Nova fila para mensagens de comunidades
            throw new IllegalStateException("Não há mensagens.");
        }
        return mensagensComunidade.poll();
    }

    /**
     * Verifica se existe convite pendente enviado para um usuário.
     * @param amigo Login do usuário a verificar
     * @return true se houver convite pendente, false caso contrário
     */
    public boolean possuiConvitePara(String amigo) {
        return convitesEnviados.contains(amigo);
    }
    // Gestão de comunidades
    /**
     * Adiciona o usuário a uma comunidade.
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
        mensagensComunidade.add(mensagem); // Armazena na fila específica
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    // Métodos para ídolos/fãs:

    /**
     * Adiciona um ídolo (usuário admirado).
     * @param idolo Login do ídolo
     * @throws IllegalArgumentException Se for auto-adicionamento ou duplicado
     */
    public void adicionarIdolo(String idolo) {
        if (idolos.contains(idolo)) {
            throw new IllegalArgumentException("Usuário já está adicionado como ídolo.");
        }
        if (login.equals(idolo)) {
            throw new IllegalArgumentException("Usuário não pode ser ídolo de si mesmo.");
        }
        idolos.add(idolo); // Adiciona o ídolo
    }

    public boolean ehFa(String idolo) {
        return idolos.contains(idolo);
    }

    public Set<String> getFas() {
        return new HashSet<>(fas);
    }

    public void adicionarFa(String fa) {
        if (login.equals(fa)) {
            throw new IllegalArgumentException("Usuário não pode ser fã de si mesmo.");
        }
        fas.add(fa);
    }

    /**
     * Adiciona uma paquera (interesse romântico).
     * @param paquera Login da paquera
     * @throws IllegalArgumentException Se for auto-adicionamento ou duplicado
     */
    public void adicionarPaquera(String paquera) {
        if (paqueras.contains(paquera)) {
            throw new IllegalArgumentException("Usuário já está adicionado como paquera.");
        }
        if (login.equals(paquera)) {
            throw new IllegalArgumentException("Usuário não pode ser paquera de si mesmo.");
        }
        paqueras.add(paquera); // Adiciona a paquera
    }

    public boolean ehPaquera(String paquera) {
        return paqueras.contains(paquera);
    }

    public Set<String> getPaqueras() {
        return new HashSet<>(paqueras);
    }

    // Métodos para inimigos:
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
