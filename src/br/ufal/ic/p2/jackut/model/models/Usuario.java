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
    /**
     * L� e remove uma mensagem da fila de mensagens da comunidade.
     *
     * @return A mensagem removida da fila de mensagens da comunidade.
     * @throws IllegalStateException Se n�o houver mensagens na fila.
     */
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

    /**
     * Retorna o conjunto de comunidades do usu�rio.
     *
     * @return Um {@code Set} contendo os identificadores das comunidades do usu�rio.
     */
    public Set<String> getComunidades() {
        return comunidades; // Deve ser o Set<String> original
    }
    /**
     * Retorna a fila de recados do usu�rio.
     *
     * @return Uma {@code Queue} contendo os recados do usu�rio.
     */
    public Queue<String> getRecados() {
        return recados;
    }
    /**
     * Recebe uma mensagem e a armazena na fila espec�fica de mensagens da comunidade.
     *
     * @param mensagem A mensagem a ser armazenada na fila de mensagens da comunidade.
     */
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
    /**
     * Verifica se o usu�rio � f� de um determinado �dolo.
     *
     * @param idolo O identificador do �dolo a ser verificado.
     * @return {@code true} se o usu�rio for f� do �dolo, {@code false} caso contr�rio.
     */

    public boolean ehFa(String idolo) {
        return idolos.contains(idolo);
    }
    /**
     * Retorna um conjunto de todos os f�s do usu�rio.
     *
     * @return Um {@code Set} contendo os identificadores dos f�s.
     */

    public Set<String> getFas() {
        return new HashSet<>(fas);
    }
    /**
     * Adiciona um novo f� � lista de f�s do usu�rio.
     *
     * @param fa O identificador do f� a ser adicionado.
     * @throws IllegalArgumentException Se o usu�rio tentar se adicionar como f� de si mesmo.
     */
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

    /**
     * Verifica se o usu�rio est� paquerando uma determinada pessoa.
     *
     * @param paquera O identificador da pessoa a ser verificada.
     * @return {@code true} se o usu�rio estiver paquerando a pessoa, {@code false} caso contr�rio.
     */
    public boolean ehPaquera(String paquera) {
        return paqueras.contains(paquera);
    }
    /**
     * Retorna um conjunto de todas as pessoas que o usu�rio est� paquerando.
     *
     * @return Um {@code Set} contendo os identificadores das pessoas que o usu�rio est� paquerando.
     */
    public Set<String> getPaqueras() {
        return new HashSet<>(paqueras);
    }

    /**
     * Adiciona um novo inimigo � lista de inimigos do usu�rio.
     *
     * @param inimigo O identificador do inimigo a ser adicionado.
     */
    public void adicionarInimigo(String inimigo) {
        inimigos.add(inimigo);
    }
    /**
     * Retorna um conjunto de todos os inimigos do usu�rio.
     *
     * @return Um {@code Set} contendo os identificadores dos inimigos do usu�rio.
     */
    public Set<String> getInimigos() {
        return new HashSet<>(inimigos);
    }
    /**
     * Remove um amigo da lista de amigos e tamb�m os convites enviados e recebidos.
     *
     * @param amigo O identificador do amigo a ser removido.
     */
    public void removerAmigo(String amigo) {
        amigos.remove(amigo);
        convitesEnviados.remove(amigo);
        convitesRecebidos.remove(amigo);
    }
    /**
     * Remove um �dolo da lista de �dolos do usu�rio.
     *
     * @param idolo O identificador do �dolo a ser removido.
     */
    public void removerIdolo(String idolo) {
        idolos.remove(idolo);
    }
    /**
     * Remove um f� da lista de f�s do usu�rio.
     *
     * @param fa O identificador do f� a ser removido.
     */
    public void removerFa(String fa) {
        fas.remove(fa);
    }
    /**
     * Remove uma pessoa da lista de paqueras do usu�rio.
     *
     * @param paquera O identificador da pessoa a ser removida da lista de paqueras.
     */
    public void removerPaquera(String paquera) {
        paqueras.remove(paquera);
    }
    /**
     * Remove um inimigo da lista de inimigos do usu�rio.
     *
     * @param inimigo O identificador do inimigo a ser removido.
     */
    public void removerInimigo(String inimigo) {
        inimigos.remove(inimigo);
    }
}
