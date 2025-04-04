package br.ufal.ic.p2.jackut.model.models;

import br.ufal.ic.p2.jackut.model.exceptions.*;
import java.io.*;
import java.util.*;

/**
 * Classe que representa o sistema principal, ela gerencia usuários,
 * sessões e operações do sistema.
 */
public class Sistema implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String ARQUIVO_DADOS = "dados.ser";
    /** Mapa de usuários cadastrados (chave = login) */
    private Map<String, Usuario> usuarios;

    /** Mapa de sessões ativas (chave = ID da sessão) */
    private Map<String, Sessao> sessoes;

    /**
     * Construtor padrão que inicializa as estruturas de dados do sistema.
     */
    public Sistema() {
        this.usuarios = new HashMap<>();
        this.sessoes = new HashMap<>();
    }

    /**
     * Carrega os dados do sistema a partir de um arquivo de persistência.
     * @return Instância do Sistema com os dados carregados ou nova instância se o arquivo não existir.
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
            System.err.println("Aviso: Dados antigos incompatíveis. Criando novo sistema.");
            return new Sistema();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar dados: " + e.getMessage(), e);
        }
    }

    /**
     * Salva os dados do sistema em um arquivo para persistência.
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
     * Cria um novo usuário no sistema.
     * @param login Login único do usuário.
     * @param senha Senha do usuário.
     * @param nome Nome de exibição do usuário.
     * @throws IllegalArgumentException Se login ou senha forem inválidos, ou se o login já existir.
     */
    public void criarUsuario(String login, String senha, String nome) {
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("Login inválido.");
        }
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("Senha inválida.");
        }
        if (usuarios.containsKey(login)) {
            throw new IllegalArgumentException("Conta com esse nome já existe.");
        }
        usuarios.put(login, new Usuario(login, senha, nome));
    }

    /**
     * Abre uma nova sessão para um usuário.
     * @param login Login do usuário.
     * @param senha Senha do usuário.
     * @return ID da sessão criada.
     * @throws IllegalArgumentException Se login ou senha forem inválidos.
     */
    public String abrirSessao(String login, String senha) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null || !usuario.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Login ou senha inválidos.");
        }
        String idSessao = UUID.randomUUID().toString();
        sessoes.put(idSessao, new Sessao(idSessao, usuario));
        return idSessao;
    }

    /**
     * Obtém o valor de um atributo do perfil de um usuário.
     * @param login Login do usuário.
     * @param atributo Nome do atributo.
     * @return Valor do atributo.
     * @throws IllegalArgumentException Se o usuário não existir.
     */
    public String getAtributoUsuario(String login, String atributo) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não cadastrado.");
        }
        return usuario.getAtributo(atributo);
    }

    /**
     * Edita um atributo do perfil do usuário da sessão atual.
     * @param idSessao ID da sessão.
     * @param atributo Nome do atributo.
     * @param valor Novo valor do atributo.
     * @throws IllegalArgumentException Se a sessão for inválida.
     */
    public void editarPerfil(String idSessao, String atributo, String valor) {
        if (idSessao == null || idSessao.isEmpty()) {
            throw new IllegalArgumentException("Sessão inválida.");
        }
        Sessao sessao = sessoes.get(idSessao);
        if (sessao == null) {
            throw new IllegalArgumentException("Sessão inválida.");
        }
        sessao.getUsuario().editarAtributo(atributo, valor);
    }

    /**
     * Adiciona um amigo para o usuário da sessão atual.
     * @param idSessao ID da sessão.
     * @param amigo Login do amigo a ser adicionado.
     * @throws UsuarioNaoCadastradoException Se o usuário ou amigo não existirem.
     * @throws SessaoInvalidaException Se a sessão for inválida.
     * @throws IllegalArgumentException Se o usuário tentar adicionar a si mesmo.
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
            throw new IllegalArgumentException("Usuário não pode adicionar a si mesmo como amigo.");
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
     * Verifica se dois usuários são amigos.
     * @param login Login do primeiro usuário.
     * @param amigo Login do segundo usuário.
     * @return true se forem amigos, false caso contrário.
     * @throws IllegalArgumentException Se algum dos usuários não existir.
     */
    public boolean ehAmigo(String login, String amigo) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null || !usuarios.containsKey(amigo)) {
            throw new IllegalArgumentException("Usuário não cadastrado.");
        }
        return usuario.ehAmigo(amigo);
    }

    /**
     * Obtém a lista de amigos de um usuário em formato de string.
     * @param login Login do usuário.
     * @return String formatada com a lista de amigos.
     * @throws IllegalArgumentException Se o usuário não existir.
     */
    public String getAmigos(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não cadastrado.");
        }
        return usuario.getAmigosString();
    }

    /**
     * Envia um recado para outro usuário.
     * @param idSessao ID da sessão do remetente.
     * @param destinatario Login do destinatário.
     * @param mensagem Conteúdo do recado.
     * @throws IllegalArgumentException Se a sessão for inválida, destinatário não existir ou for o mesmo que o remetente.
     */
    public void enviarRecado(String idSessao, String destinatario, String mensagem) {
        Sessao sessao = sessoes.get(idSessao);
        if (sessao == null) {
            throw new IllegalArgumentException("Sessão inválida.");
        }
        if (!usuarios.containsKey(destinatario)) {
            throw new IllegalArgumentException("Usuário não cadastrado.");
        }
        if (sessao.getUsuario().getLogin().equals(destinatario)) {
            throw new IllegalArgumentException("Usuário não pode enviar recado para si mesmo.");
        }
        usuarios.get(destinatario).receberRecado(mensagem);
    }

    /**
     * Lê o próximo recado da fila do usuário da sessão atual.
     * @param idSessao ID da sessão.
     * @return Conteúdo do recado.
     * @throws IllegalArgumentException Se a sessão for inválida.
     */
    public String lerRecado(String idSessao) {
        Sessao sessao = sessoes.get(idSessao);
        if (sessao == null) {
            throw new IllegalArgumentException("Sessão inválida.");
        }
        return sessao.getUsuario().lerRecado();
    }
}