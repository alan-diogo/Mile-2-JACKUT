package br.ufal.ic.p2.jackut.model.models;

import br.ufal.ic.p2.jackut.model.exceptions.*;
import java.io.*;
import java.util.*;

/**
 * Classe que representa o sistema principal, ela gerencia usu�rios,
 * sess�es e opera��es do sistema.
 */
public class Sistema implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String ARQUIVO_DADOS = "dados.ser";
    /** Mapa de usu�rios cadastrados (chave = login) */
    private Map<String, Usuario> usuarios;

    /** Mapa de sess�es ativas (chave = ID da sess�o) */
    private Map<String, Sessao> sessoes;

    /**
     * Construtor padr�o que inicializa as estruturas de dados do sistema.
     */
    public Sistema() {
        this.usuarios = new HashMap<>();
        this.sessoes = new HashMap<>();
    }

    /**
     * Carrega os dados do sistema a partir de um arquivo de persist�ncia.
     * @return Inst�ncia do Sistema com os dados carregados ou nova inst�ncia se o arquivo n�o existir.
     * @throws RuntimeException Se ocorrer erro durante o carregamento.
     */
    public static Sistema carregarDados() {
        File arquivo = new File(ARQUIVO_DADOS);
        if (!arquivo.exists()) {
            return new Sistema();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            return (Sistema) in.readObject();
        } catch (InvalidClassException e) {
            System.err.println("Aviso: Dados antigos incompat�veis. Criando novo sistema.");
            return new Sistema();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar dados: " + e.getMessage(), e);
        }
    }

    /**
     * Salva os dados do sistema em um arquivo para persist�ncia.
     * @throws RuntimeException Se ocorrer erro durante o salvamento.
     */
    public void salvarDados() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            out.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar dados: " + e.getMessage(), e);
        }
    }

    /**
     * Remove todos os dados do sistema, reiniciando-o para o estado inicial.
     */
    public void zerarSistema() {
        usuarios.clear();
        sessoes.clear();
    }

    /**
     * Cria um novo usu�rio no sistema.
     * @param login Login �nico do usu�rio.
     * @param senha Senha do usu�rio.
     * @param nome Nome de exibi��o do usu�rio.
     * @throws IllegalArgumentException Se login ou senha forem inv�lidos, ou se o login j� existir.
     */
    public void criarUsuario(String login, String senha, String nome) {
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("Login inv�lido.");
        }
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("Senha inv�lida.");
        }
        if (usuarios.containsKey(login)) {
            throw new IllegalArgumentException("Conta com esse nome j� existe.");
        }
        usuarios.put(login, new Usuario(login, senha, nome));
    }

    /**
     * Abre uma nova sess�o para um usu�rio.
     * @param login Login do usu�rio.
     * @param senha Senha do usu�rio.
     * @return ID da sess�o criada.
     * @throws IllegalArgumentException Se login ou senha forem inv�lidos.
     */
    public String abrirSessao(String login, String senha) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null || !usuario.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Login ou senha inv�lidos.");
        }
        String idSessao = UUID.randomUUID().toString();
        sessoes.put(idSessao, new Sessao(idSessao, usuario));
        return idSessao;
    }

    /**
     * Obt�m o valor de um atributo do perfil de um usu�rio.
     * @param login Login do usu�rio.
     * @param atributo Nome do atributo.
     * @return Valor do atributo.
     * @throws IllegalArgumentException Se o usu�rio n�o existir.
     */
    public String getAtributoUsuario(String login, String atributo) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new IllegalArgumentException("Usu�rio n�o cadastrado.");
        }
        return usuario.getAtributo(atributo);
    }

    /**
     * Edita um atributo do perfil do usu�rio da sess�o atual.
     * @param idSessao ID da sess�o.
     * @param atributo Nome do atributo.
     * @param valor Novo valor do atributo.
     * @throws IllegalArgumentException Se a sess�o for inv�lida.
     */
    public void editarPerfil(String idSessao, String atributo, String valor) {
        if (idSessao == null || idSessao.isEmpty()) {
            throw new IllegalArgumentException("Sess�o inv�lida.");
        }
        Sessao sessao = sessoes.get(idSessao);
        if (sessao == null) {
            throw new IllegalArgumentException("Sess�o inv�lida.");
        }
        sessao.getUsuario().editarAtributo(atributo, valor);
    }

    /**
     * Adiciona um amigo para o usu�rio da sess�o atual.
     * @param idSessao ID da sess�o.
     * @param amigo Login do amigo a ser adicionado.
     * @throws UsuarioNaoCadastradoException Se o usu�rio ou amigo n�o existirem.
     * @throws SessaoInvalidaException Se a sess�o for inv�lida.
     * @throws IllegalArgumentException Se o usu�rio tentar adicionar a si mesmo.
     */
    public void adicionarAmigo(String idSessao, String amigo) {
        if (idSessao == null || idSessao.isEmpty()) {
            throw new UsuarioNaoCadastradoException();
        }

        Sessao sessao = sessoes.get(idSessao);
        if (sessao == null) {
            throw new SessaoInvalidaException();
        }
        if (!usuarios.containsKey(amigo)) {
            throw new UsuarioNaoCadastradoException();
        }
        if (sessao.getUsuario().getLogin().equals(amigo)) {
            throw new IllegalArgumentException("Usu�rio n�o pode adicionar a si mesmo como amigo.");
        }

        Usuario usuarioAtual = sessao.getUsuario();
        Usuario usuarioAlvo = usuarios.get(amigo);

        try {
            usuarioAtual.enviarConvite(amigo);
            usuarioAlvo.receberConvite(usuarioAtual.getLogin());

            if (usuarioAlvo.possuiConvitePara(usuarioAtual.getLogin())) {
                usuarioAtual.confirmarAmizade(amigo);
                usuarioAlvo.confirmarAmizade(usuarioAtual.getLogin());
            }
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Verifica se dois usu�rios s�o amigos.
     * @param login Login do primeiro usu�rio.
     * @param amigo Login do segundo usu�rio.
     * @return true se forem amigos, false caso contr�rio.
     * @throws IllegalArgumentException Se algum dos usu�rios n�o existir.
     */
    public boolean ehAmigo(String login, String amigo) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null || !usuarios.containsKey(amigo)) {
            throw new IllegalArgumentException("Usu�rio n�o cadastrado.");
        }
        return usuario.ehAmigo(amigo);
    }

    /**
     * Obt�m a lista de amigos de um usu�rio em formato de string.
     * @param login Login do usu�rio.
     * @return String formatada com a lista de amigos.
     * @throws IllegalArgumentException Se o usu�rio n�o existir.
     */
    public String getAmigos(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new IllegalArgumentException("Usu�rio n�o cadastrado.");
        }
        return usuario.getAmigosString();
    }

    /**
     * Envia um recado para outro usu�rio.
     * @param idSessao ID da sess�o do remetente.
     * @param destinatario Login do destinat�rio.
     * @param mensagem Conte�do do recado.
     * @throws IllegalArgumentException Se a sess�o for inv�lida, destinat�rio n�o existir ou for o mesmo que o remetente.
     */
    public void enviarRecado(String idSessao, String destinatario, String mensagem) {
        Sessao sessao = sessoes.get(idSessao);
        if (sessao == null) {
            throw new IllegalArgumentException("Sess�o inv�lida.");
        }
        if (!usuarios.containsKey(destinatario)) {
            throw new IllegalArgumentException("Usu�rio n�o cadastrado.");
        }
        if (sessao.getUsuario().getLogin().equals(destinatario)) {
            throw new IllegalArgumentException("Usu�rio n�o pode enviar recado para si mesmo.");
        }
        usuarios.get(destinatario).receberRecado(mensagem);
    }

    /**
     * L� o pr�ximo recado da fila do usu�rio da sess�o atual.
     * @param idSessao ID da sess�o.
     * @return Conte�do do recado.
     * @throws IllegalArgumentException Se a sess�o for inv�lida.
     */
    public String lerRecado(String idSessao) {
        Sessao sessao = sessoes.get(idSessao);
        if (sessao == null) {
            throw new IllegalArgumentException("Sess�o inv�lida.");
        }
        return sessao.getUsuario().lerRecado();
    }
}