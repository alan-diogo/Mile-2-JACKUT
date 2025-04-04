package br.ufal.ic.p2.jackut.model;

import br.ufal.ic.p2.jackut.model.exceptions.*;
import br.ufal.ic.p2.jackut.model.models.*;
import java.io.Serializable;

/**
 * Classe Facade que representa a interface principal do sistema Jackut.
 * Centraliza todas as operações do sistema e serve como ponto único de acesso
 * para a camada de apresentação e testes.
 */
public class Facade implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Instância do sistema que contém a lógica de negócio
     */
    private Sistema sistema;

    /**
     * Constrói uma nova fachada, carregando os dados persistentes do sistema.
     */
    public Facade() {
        this.sistema = Sistema.carregarDados();
    }

    /**
     * Reinicia o sistema, removendo todos os dados.
     */
    public void zerarSistema() {
        sistema.zerarSistema();
    }

    /**
     * Cria um novo usuário no sistema.
     * @param login Identificador único do usuário
     * @param senha Senha de acesso
     * @param nome Nome de exibição do usuário
     * @throws IllegalArgumentException Se os parâmetros forem inválidos ou o login já existir
     */
    public void criarUsuario(String login, String senha, String nome) {
        sistema.criarUsuario(login, senha, nome);
    }

    /**
     * Abre uma nova sessão para um usuário.
     * @param login Login do usuário
     * @param senha Senha do usuário
     * @return ID da sessão criada
     * @throws IllegalArgumentException Se o login ou senha forem inválidos
     */
    public String abrirSessao(String login, String senha) {
        return sistema.abrirSessao(login, senha);
    }

    /**
     * Obtém um atributo do perfil de um usuário.
     * @param login Login do usuário
     * @param atributo Nome do atributo
     * @return Valor do atributo
     * @throws IllegalArgumentException Se o usuário não existir
     * @throws AtributoNaoPreenchidoException Se o atributo não estiver definido
     */
    public String getAtributoUsuario(String login, String atributo) {
        return sistema.getAtributoUsuario(login, atributo);
    }

    /**
     * Edita um atributo do perfil do usuário da sessão atual.
     * @param idSessao ID da sessão
     * @param atributo Nome do atributo
     * @param valor Novo valor do atributo
     * @throws IllegalArgumentException Se a sessão for inválida
     */
    public void editarPerfil(String idSessao, String atributo, String valor) {
        sistema.editarPerfil(idSessao, atributo, valor);
    }

    /**
     * Adiciona um amigo para o usuário da sessão atual.
     * @param idSessao ID da sessão
     * @param amigo Login do amigo a ser adicionado
     * @throws UsuarioNaoCadastradoException Se o usuário ou amigo não existirem
     * @throws SessaoInvalidaException Se a sessão for inválida
     * @throws AmizadeExistenteException Se já existir amizade ou convite pendente
     */
    public void adicionarAmigo(String idSessao, String amigo) {
        sistema.adicionarAmigo(idSessao, amigo);
    }

    /**
     * Verifica se dois usuários são amigos.
     * @param login Login do primeiro usuário
     * @param amigo Login do segundo usuário
     * @return true se forem amigos, false caso contrário
     * @throws IllegalArgumentException Se algum dos usuários não existir
     */
    public boolean ehAmigo(String login, String amigo) {
        return sistema.ehAmigo(login, amigo);
    }

    /**
     * Obtém a lista de amigos de um usuário.
     * @param login Login do usuário
     * @return String formatada com a lista de amigos no formato {amigo1,amigo2,...}
     * @throws IllegalArgumentException Se o usuário não existir
     */
    public String getAmigos(String login) {
        return sistema.getAmigos(login);
    }

    /**
     * Envia um recado para outro usuário.
     * @param idSessao ID da sessão do remetente
     * @param destinatario Login do destinatário
     * @param mensagem Conteúdo do recado
     * @throws IllegalArgumentException Se a sessão for inválida, destinatário não existir ou for o mesmo que o remetente
     */
    public void enviarRecado(String idSessao, String destinatario, String mensagem) {
        sistema.enviarRecado(idSessao, destinatario, mensagem);
    }

    /**
     * Lê o próximo recado da fila do usuário da sessão atual.
     * @param idSessao ID da sessão
     * @return Conteúdo do recado
     * @throws IllegalArgumentException Se a sessão for inválida
     * @throws IllegalStateException Se não houver recados
     */
    public String lerRecado(String idSessao) {
        return sistema.lerRecado(idSessao);
    }

    /**
     * Encerra o sistema, salvando os dados persistentes.
     */
    public void encerrarSistema() {
        sistema.salvarDados();
    }
}