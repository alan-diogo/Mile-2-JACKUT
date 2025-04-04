package br.ufal.ic.p2.jackut.model;

import br.ufal.ic.p2.jackut.model.exceptions.*;
import br.ufal.ic.p2.jackut.model.models.*;
import java.io.Serializable;

/**
 * Classe Facade que representa a interface principal do sistema Jackut.
 * Centraliza todas as opera��es do sistema e serve como ponto �nico de acesso
 * para a camada de apresenta��o e testes.
 */
public class Facade implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Inst�ncia do sistema que cont�m a l�gica de neg�cio
     */
    private Sistema sistema;

    /**
     * Constr�i uma nova fachada, carregando os dados persistentes do sistema.
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
     * Cria um novo usu�rio no sistema.
     * @param login Identificador �nico do usu�rio
     * @param senha Senha de acesso
     * @param nome Nome de exibi��o do usu�rio
     * @throws IllegalArgumentException Se os par�metros forem inv�lidos ou o login j� existir
     */
    public void criarUsuario(String login, String senha, String nome) {
        sistema.criarUsuario(login, senha, nome);
    }

    /**
     * Abre uma nova sess�o para um usu�rio.
     * @param login Login do usu�rio
     * @param senha Senha do usu�rio
     * @return ID da sess�o criada
     * @throws IllegalArgumentException Se o login ou senha forem inv�lidos
     */
    public String abrirSessao(String login, String senha) {
        return sistema.abrirSessao(login, senha);
    }

    /**
     * Obt�m um atributo do perfil de um usu�rio.
     * @param login Login do usu�rio
     * @param atributo Nome do atributo
     * @return Valor do atributo
     * @throws IllegalArgumentException Se o usu�rio n�o existir
     * @throws AtributoNaoPreenchidoException Se o atributo n�o estiver definido
     */
    public String getAtributoUsuario(String login, String atributo) {
        return sistema.getAtributoUsuario(login, atributo);
    }

    /**
     * Edita um atributo do perfil do usu�rio da sess�o atual.
     * @param idSessao ID da sess�o
     * @param atributo Nome do atributo
     * @param valor Novo valor do atributo
     * @throws IllegalArgumentException Se a sess�o for inv�lida
     */
    public void editarPerfil(String idSessao, String atributo, String valor) {
        sistema.editarPerfil(idSessao, atributo, valor);
    }

    /**
     * Adiciona um amigo para o usu�rio da sess�o atual.
     * @param idSessao ID da sess�o
     * @param amigo Login do amigo a ser adicionado
     * @throws UsuarioNaoCadastradoException Se o usu�rio ou amigo n�o existirem
     * @throws SessaoInvalidaException Se a sess�o for inv�lida
     * @throws AmizadeExistenteException Se j� existir amizade ou convite pendente
     */
    public void adicionarAmigo(String idSessao, String amigo) {
        sistema.adicionarAmigo(idSessao, amigo);
    }

    /**
     * Verifica se dois usu�rios s�o amigos.
     * @param login Login do primeiro usu�rio
     * @param amigo Login do segundo usu�rio
     * @return true se forem amigos, false caso contr�rio
     * @throws IllegalArgumentException Se algum dos usu�rios n�o existir
     */
    public boolean ehAmigo(String login, String amigo) {
        return sistema.ehAmigo(login, amigo);
    }

    /**
     * Obt�m a lista de amigos de um usu�rio.
     * @param login Login do usu�rio
     * @return String formatada com a lista de amigos no formato {amigo1,amigo2,...}
     * @throws IllegalArgumentException Se o usu�rio n�o existir
     */
    public String getAmigos(String login) {
        return sistema.getAmigos(login);
    }

    /**
     * Envia um recado para outro usu�rio.
     * @param idSessao ID da sess�o do remetente
     * @param destinatario Login do destinat�rio
     * @param mensagem Conte�do do recado
     * @throws IllegalArgumentException Se a sess�o for inv�lida, destinat�rio n�o existir ou for o mesmo que o remetente
     */
    public void enviarRecado(String idSessao, String destinatario, String mensagem) {
        sistema.enviarRecado(idSessao, destinatario, mensagem);
    }

    /**
     * L� o pr�ximo recado da fila do usu�rio da sess�o atual.
     * @param idSessao ID da sess�o
     * @return Conte�do do recado
     * @throws IllegalArgumentException Se a sess�o for inv�lida
     * @throws IllegalStateException Se n�o houver recados
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