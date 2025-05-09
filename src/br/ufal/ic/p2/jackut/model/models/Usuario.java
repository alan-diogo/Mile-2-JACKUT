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
    /**
     * Lê e remove uma mensagem da fila de mensagens da comunidade.
     *
     * @return A mensagem removida da fila de mensagens da comunidade.
     * @throws IllegalStateException Se não houver mensagens na fila.
     */
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

    /**
     * Retorna o conjunto de comunidades do usuário.
     *
     * @return Um {@code Set} contendo os identificadores das comunidades do usuário.
     */
    public Set<String> getComunidades() {
        return comunidades; // Deve ser o Set<String> original
    }
    /**
     * Retorna a fila de recados do usuário.
     *
     * @return Uma {@code Queue} contendo os recados do usuário.
     */
    public Queue<String> getRecados() {
        return recados;
    }
    /**
     * Recebe uma mensagem e a armazena na fila específica de mensagens da comunidade.
     *
     * @param mensagem A mensagem a ser armazenada na fila de mensagens da comunidade.
     */
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
    /**
     * Verifica se o usuário é fã de um determinado ídolo.
     *
     * @param idolo O identificador do ídolo a ser verificado.
     * @return {@code true} se o usuário for fã do ídolo, {@code false} caso contrário.
     */

    public boolean ehFa(String idolo) {
        return idolos.contains(idolo);
    }
    /**
     * Retorna um conjunto de todos os fãs do usuário.
     *
     * @return Um {@code Set} contendo os identificadores dos fãs.
     */

    public Set<String> getFas() {
        return new HashSet<>(fas);
    }
    /**
     * Adiciona um novo fã à lista de fãs do usuário.
     *
     * @param fa O identificador do fã a ser adicionado.
     * @throws IllegalArgumentException Se o usuário tentar se adicionar como fã de si mesmo.
     */
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

    /**
     * Verifica se o usuário está paquerando uma determinada pessoa.
     *
     * @param paquera O identificador da pessoa a ser verificada.
     * @return {@code true} se o usuário estiver paquerando a pessoa, {@code false} caso contrário.
     */
    public boolean ehPaquera(String paquera) {
        return paqueras.contains(paquera);
    }
    /**
     * Retorna um conjunto de todas as pessoas que o usuário está paquerando.
     *
     * @return Um {@code Set} contendo os identificadores das pessoas que o usuário está paquerando.
     */
    public Set<String> getPaqueras() {
        return new HashSet<>(paqueras);
    }

    /**
     * Adiciona um novo inimigo à lista de inimigos do usuário.
     *
     * @param inimigo O identificador do inimigo a ser adicionado.
     */
    public void adicionarInimigo(String inimigo) {
        inimigos.add(inimigo);
    }
    /**
     * Retorna um conjunto de todos os inimigos do usuário.
     *
     * @return Um {@code Set} contendo os identificadores dos inimigos do usuário.
     */
    public Set<String> getInimigos() {
        return new HashSet<>(inimigos);
    }
    /**
     * Remove um amigo da lista de amigos e também os convites enviados e recebidos.
     *
     * @param amigo O identificador do amigo a ser removido.
     */
    public void removerAmigo(String amigo) {
        amigos.remove(amigo);
        convitesEnviados.remove(amigo);
        convitesRecebidos.remove(amigo);
    }
    /**
     * Remove um ídolo da lista de ídolos do usuário.
     *
     * @param idolo O identificador do ídolo a ser removido.
     */
    public void removerIdolo(String idolo) {
        idolos.remove(idolo);
    }
    /**
     * Remove um fã da lista de fãs do usuário.
     *
     * @param fa O identificador do fã a ser removido.
     */
    public void removerFa(String fa) {
        fas.remove(fa);
    }
    /**
     * Remove uma pessoa da lista de paqueras do usuário.
     *
     * @param paquera O identificador da pessoa a ser removida da lista de paqueras.
     */
    public void removerPaquera(String paquera) {
        paqueras.remove(paquera);
    }
    /**
     * Remove um inimigo da lista de inimigos do usuário.
     *
     * @param inimigo O identificador do inimigo a ser removido.
     */
    public void removerInimigo(String inimigo) {
        inimigos.remove(inimigo);
    }
}
