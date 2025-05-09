package br.ufal.ic.p2.jackut.model.models;

import br.ufal.ic.p2.jackut.model.exceptions.*;
import java.io.*;
import java.util.*;

/**
 * Classe que representa o sistema principal do Jackut. Gerencia usu�rios, sess�es,
 * comunidades e opera��es relacionadas a amizades, recados, �dolos, paqueras e inimigos.
 * Tamb�m � respons�vel pela persist�ncia dos dados do sistema.

 */
public class Sistema implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String ARQUIVO_DADOS = "dados.ser";

    /** Mapa de usu�rios cadastrados, onde a chave � o login do usu�rio. */
    private Map<String, Usuario> usuarios;
    /** Mapa de sess�es ativas, onde a chave � o ID da sess�o. */
    private Map<String, Sessao> sessoes;
    /** Mapa de comunidades, onde a chave � o nome da comunidade. */
    private Map<String, Comunidade> comunidades = new HashMap<>();

    /**
     * Construtor padr�o que inicializa as estruturas de dados do sistema.
     */
    public Sistema() {
        this.usuarios = new HashMap<>();
        this.sessoes = new HashMap<>();
        this.comunidades = new HashMap<>(); // Ou aqui
    }

    /**
     * Carrega os dados do sistema a partir do arquivo de persist�ncia.
     *
     * @return Inst�ncia do sistema carregada ou nova inst�ncia se o arquivo n�o existir.
     * @throws RuntimeException Se ocorrer erro de I/O ou desserializa��o.
     */
    public static Sistema carregarDados() {
        File arquivo = new File(ARQUIVO_DADOS);
        if (!arquivo.exists()) {
            return new Sistema(); // Retorna um novo sistema se o arquivo n�o existir
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            Sistema sistema = (Sistema) in.readObject();

            // Garante que o mapa de comunidades est� inicializado
            if (sistema.comunidades == null) {
                sistema.comunidades = new HashMap<>();
            }

            return sistema;

        } catch (InvalidClassException e) {
            System.err.println("Aviso: Dados antigos incompat�veis. Criando novo sistema.");
            return new Sistema();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar dados: " + e.getMessage(), e);
        }
    }

    /**
     * Salva o estado atual do sistema em arquivo para persist�ncia.
     *
     * @throws RuntimeException Se ocorrer erro de I/O durante o salvamento.
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
        this.usuarios = new HashMap<>();
        this.sessoes = new HashMap<>();
        this.comunidades = new HashMap<>();
    }

    /**
     * Cria um novo usu�rio no sistema.
     *
     * @param login Login �nico do usu�rio (n�o pode ser vazio ou nulo).
     * @param senha Senha do usu�rio (n�o pode ser vazia ou nula).
     * @param nome Nome de exibi��o do usu�rio.
     * @throws IllegalArgumentException Se login/senha forem inv�lidos ou o login j� existir.
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
        usuarios.put(login, new Usuario(login, senha, nome)); // Adiciona o usu�rio ao mapa de usu�rios
    }

    /**
     * Abre uma nova sess�o para um usu�rio autenticado.
     *
     * @param login Login do usu�rio.
     * @param senha Senha do usu�rio.
     * @return ID da sess�o gerada.
     * @throws IllegalArgumentException Se o login ou senha forem inv�lidos.
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
     *
     * @param login Login do usu�rio.
     * @param atributo Nome do atributo (ex: "nome", "senha").
     * @return Valor do atributo.
     * @throws UsuarioNaoCadastradoException Se o usu�rio n�o existir.
     */
    public String getAtributoUsuario(String login, String atributo) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
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
            throw new UsuarioNaoCadastradoException();
        }
        Sessao sessao = sessoes.get(idSessao);
        if (sessao == null) {
            throw new IllegalArgumentException("Sess�o inv�lida.");
        }
        sessao.getUsuario().editarAtributo(atributo, valor);
    }

    /**
     * Adiciona um amigo para o usu�rio da sess�o atual.
     * Verifica rela��es de inimizade e envia convites.
     *
     * @param idSessao ID da sess�o do usu�rio.
     * @param amigo Login do amigo a ser adicionado.
     * @throws UsuarioNaoCadastradoException Se o usu�rio ou amigo n�o existirem.
     * @throws IllegalArgumentException Se tentar adicionar a si mesmo ou o amigo for inimigo.
     */
    public void adicionarAmigo(String idSessao, String amigo) {
        Sessao sessao = getSessao(idSessao);
        Usuario usuario = sessao.getUsuario();
        Usuario usuarioAmigo = usuarios.get(amigo);

        if (usuarioAmigo == null) {
            throw new UsuarioNaoCadastradoException();
        }

        // Verifica se o amigo tem o usu�rio como inimigo
        if (usuarioAmigo.getInimigos().contains(usuario.getLogin())) {
            throw new IllegalArgumentException("Fun��o inv�lida: " + usuarioAmigo.getNome() + " � seu inimigo.");
        }

        if (idSessao == null || idSessao.isEmpty()) {
            throw new UsuarioNaoCadastradoException();
        }

        if (sessao == null) {
            throw new UsuarioNaoCadastradoException();
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
            throw new UsuarioNaoCadastradoException();
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
            throw new UsuarioNaoCadastradoException();
        }
        return usuario.getAmigosString();
    }

    /**
     * Envia um recado para outro usu�rio.
     * @param idSessao ID da sess�o do remetente.
     * @param destinatario Login do destinat�rio.
     * @param recado Conte�do do recado.
     * @throws IllegalArgumentException Se a sess�o for inv�lida, destinat�rio n�o existir ou for o mesmo que o remetente.
     */
    public void enviarRecado(String idSessao, String destinatario, String recado) {
        Sessao sessao = getSessao(idSessao);
        Usuario remetente = sessao.getUsuario();
        Usuario usuarioDestinatario = usuarios.get(destinatario);

        if (usuarioDestinatario == null) {
            throw new UsuarioNaoCadastradoException();
        }

        // Verifica se o destinat�rio tem o remetente como inimigo
        if (usuarioDestinatario.getInimigos().contains(remetente.getLogin())) {
            throw new IllegalArgumentException("Fun��o inv�lida: " + usuarioDestinatario.getNome() + " � seu inimigo.");
        }

        // Verifica se o destinat�rio existe
        if (!usuarios.containsKey(destinatario)) {
            throw new UsuarioNaoCadastradoException();
        }

        // Verifica envio para si mesmo
        if (remetente.getLogin().equals(destinatario)) {
            throw new BloqueioAutoRecadoException();
        }

        usuarios.get(destinatario).receberRecado(recado);
    }

    /**
     * L� o pr�ximo recado da fila do usu�rio da sess�o atual.
     * @param idSessao ID da sess�o.
     * @return Conte�do do recado.
     * @throws IllegalArgumentException Se a sess�o for inv�lida.
     */
    public String lerRecado(String idSessao) {
        Sessao sessao = getSessao(idSessao);
        Usuario usuario = sessao.getUsuario();
        if (!usuarios.containsKey(usuario.getLogin())) {
            throw new UsuarioNaoCadastradoException();
        }
        if (sessao == null) {
            throw new UsuarioNaoCadastradoException();
        }
        return sessao.getUsuario().lerRecado();
    }

    // M�todo para obter uma comunidade pelo nome
    public String getComunidades(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }
        return usuario.getComunidades().toString();
    }

    public String getDescricaoComunidade(String nome) {
        if (!comunidades.containsKey(nome)) {
            throw new ComunidadeNaoExisteException();
        }
        return comunidades.get(nome).getDescricao();
    }

    public Comunidade getComunidade(String nome) {
        Comunidade comunidade = comunidades.get(nome);
        if (comunidade == null) {
            throw new ComunidadeNaoExisteException();
        }
        return comunidade;
    }
    /**
     * Cria uma nova comunidade no sistema.
     *
     * @param nome Nome �nico da comunidade.
     * @param descricao Descri��o da comunidade.
     * @param dono Login do usu�rio criador.
     * @throws ComunidadeExistenteException Se o nome j� estiver em uso.
     */

    public void criarComunidade(String nome, String descricao, String dono) {
        if (comunidades.containsKey(nome)) {
            throw new ComunidadeExistenteException();
        }
        Comunidade comunidade = new Comunidade(nome, descricao, dono);
        comunidades.put(nome, comunidade);
        Usuario usuario = getUsuario(dono);
        usuario.adicionarComunidade(nome);
    }

    public Sessao getSessao(String idSessao) {
        Sessao sessao = sessoes.get(idSessao);
        if (sessao == null) {
            throw new UsuarioNaoCadastradoException();
        }
        return sessao;
    }
    public Usuario getUsuario(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }
        return usuario;
    }
    /**
     * Adiciona um membro a uma comunidade existente.
     *
     * @param nomeComunidade Nome da comunidade.
     * @param loginUsuario Login do usu�rio a ser adicionado.
     * @throws ComunidadeNaoExisteException Se a comunidade n�o existir.
     * @throws UsuarioNaoCadastradoException Se o usu�rio n�o existir.
     */

    public void adicionarMembroComunidade(String nomeComunidade, String loginUsuario) {

        Comunidade comunidade = getComunidade(nomeComunidade);
        Usuario usuario = getUsuario(loginUsuario);

        comunidade.adicionarMembro(loginUsuario);
        usuario.adicionarComunidade(nomeComunidade);
    }

    public Set<String> getComunidadesDoUsuario(String login) {
        return getUsuario(login).getComunidades();
    }

    public String getComunidadesDoUsuarioFormatado(String login) {
        Usuario usuario = getUsuario(login);
        List<String> comunidades = new ArrayList<>(usuario.getComunidades());

        return "{" + String.join(",", comunidades) + "}";
    }

    public String getMembrosComunidade(String nome) {
        if (!comunidades.containsKey(nome)) {
            throw new ComunidadeNaoExisteException();
        }
        List<String> membros = new ArrayList<>(comunidades.get(nome).getMembros());

        return "{" + String.join(",", membros) + "}";
    }
    /**
     * Adiciona o usu�rio de uma sess�o a uma comunidade existente.
     * @param idSessao ID da sess�o do usu�rio.
     * @param nomeComunidade Nome da comunidade � qual o usu�rio ser� adicionado.
     * @throws UsuarioNaoCadastradoException Se a sess�o for inv�lida.
     * @throws ComunidadeNaoExisteException Se a comunidade n�o existir.
     */

    public void enviarMensagemComunidade(String idSessao, String nomeComunidade, String mensagem) {
        Sessao sessao = getSessao(idSessao); // Valida a sess�o
        Usuario remetente = sessao.getUsuario();
        Comunidade comunidade = getComunidade(nomeComunidade); // Busca a comunidade

        // Formata a mensagem com o login do remetente
        String mensagemCompleta = remetente.getLogin() + ": " + mensagem;

        // Envia a mensagem para todos os membros da comunidade
        for (String membro : comunidade.getMembros()) {
            Usuario usuario = getUsuario(membro);
            usuario.receberMensagemComunidade(mensagem);
        }
    }

    /**
     * Adiciona um usu�rio como �dolo. Gera rela��o m�tua de f�/�dolo.
     *
     * @param idSessao ID da sess�o do usu�rio.
     * @param idolo Login do �dolo a ser adicionado.
     * @throws UsuarioNaoCadastradoException Se o �dolo n�o existir.
     * @throws IllegalArgumentException Se o usu�rio for o pr�prio �dolo ou j� for f�.
     */
    public void adicionarIdolo(String idSessao, String idolo) {
        Sessao sessao = getSessao(idSessao);
        Usuario usuario = sessao.getUsuario();
        Usuario usuarioIdolo = usuarios.get(idolo);

        if (usuarioIdolo == null) {
            throw new UsuarioNaoCadastradoException();
        }

        // Verifica se o �dolo tem o usu�rio como inimigo
        if (usuarioIdolo.getInimigos().contains(usuario.getLogin())) {
            throw new IllegalArgumentException("Fun��o inv�lida: " + usuarioIdolo.getNome() + " � seu inimigo.");
        }

        // Agora voc� pode acessar o atributo 'fas' do Usuario corretamente
        if (usuario.getFas().contains(idolo)) {
            throw new JaEhFaException();
        }

        // Impedir que o usu�rio adicione a si mesmo como �dolo
        if (usuario.getLogin().equals(idolo)) {
            throw new BloqueioAutoIdolException();
        }

        // Impede adicionar um �dolo repetido
        if (usuario.ehFa(idolo)) {
            throw new UsuarioAddIdolException();
        }
        // Verificar se o �dolo existe
        if (!usuarios.containsKey(idolo)) {
            throw new UsuarioNaoCadastradoException();
        }

        // Adiciona o �dolo
        usuario.adicionarIdolo(idolo);
        Usuario idoloUsuario = usuarios.get(idolo);
        idoloUsuario.adicionarFa(usuario.getLogin());
    }

    /**
     * Adiciona um usu�rio como paquera. Notifica ambos se houver reciprocidade.
     *
     * @param idSessao ID da sess�o do usu�rio.
     * @param paquera Login da paquera a ser adicionada.
     * @throws UsuarioNaoCadastradoException Se a paquera n�o existir.
     * @throws IllegalArgumentException Se for auto-adicionamento ou paquera repetida.
     */
    public void adicionarPaquera(String idSessao, String paquera) {
        Sessao sessao = getSessao(idSessao);
        Usuario usuario = sessao.getUsuario();
        Usuario usuarioPaquera = usuarios.get(paquera);

        if (usuarioPaquera == null) {
            throw new UsuarioNaoCadastradoException();
        }

        // Verifica se a paquera tem o usu�rio como inimigo
        if (usuarioPaquera.getInimigos().contains(usuario.getLogin())) {
            throw new IllegalArgumentException("Fun��o inv�lida: " + usuarioPaquera.getNome() + " � seu inimigo.");
        }
        if (!usuarios.containsKey(paquera)) {
            throw new UsuarioNaoCadastradoException();
        }

        // Impede adicionar a si mesmo como paquera
        if (usuario.getLogin().equals(paquera)) {
            throw new BloqueioAutoPaqueraException();
        }

        // Verifica se o usu�rio j� � paquera
        if (usuario.ehPaquera(paquera)) {
            throw new UsuarioAddPaqueraException();
        }

        usuario.adicionarPaquera(paquera);

        // Se ambos se adicionarem mutuamente, envia recado
        if (usuarioPaquera.ehPaquera(usuario.getLogin())) {
            usuario.receberRecado(usuarioPaquera.getNome() + " � seu paquera - Recado do Jackut.");
            usuarioPaquera.receberRecado(usuario.getNome() + " � seu paquera - Recado do Jackut.");
        }
    }
    public boolean ehFa(String login, String idolo) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null || !usuarios.containsKey(idolo)) {
            throw new UsuarioNaoCadastradoException();
        }
        return usuario.ehFa(idolo);
    }
    private String formatarSet(Set<String> conjunto) {
        return conjunto.isEmpty() ? "{}" : "{" + String.join(",", conjunto) + "}";
    }
    public String getFas(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }
        return formatarSet(usuario.getFas());
    }

    public boolean ehPaquera(String login, String paquera) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null || !usuarios.containsKey(paquera)) {
            throw new UsuarioNaoCadastradoException();
        }
        return usuario.ehPaquera(paquera);
    }
    public String getPaqueras(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }
        return formatarSet(usuario.getPaqueras());
    }

    public String getInimigos(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }
        return formatarSet(usuario.getInimigos());
    }
    public void adicionarInimigo(String idSessao, String inimigo) {
        Sessao sessao = getSessao(idSessao);
        Usuario usuario = sessao.getUsuario();
        String inimigoNormalizado = inimigo.toLowerCase(); // Normaliza para min�sculas

        if (!usuarios.containsKey(inimigoNormalizado)) {
            throw new UsuarioNaoCadastradoException();
        }
        if (usuario.getInimigos().contains(inimigoNormalizado)) {
            throw new JaInimigoException();
        }
        if (usuario.getLogin().equals(inimigoNormalizado)) {
            throw new BloqueioInimigoDeSiException();
        }
        usuario.adicionarInimigo(inimigoNormalizado);
    }
    /**
     * Remove completamente um usu�rio do sistema, incluindo rela��es e comunidades.
     *
     * @param idSessao ID da sess�o do usu�rio a ser removido.
     * @throws UsuarioNaoCadastradoException Se a sess�o for inv�lida.
     */
    public void removerUsuario(String idSessao) {
        Sessao sessao = sessoes.get(idSessao);
        if (sessao == null) {
            throw new UsuarioNaoCadastradoException();
        }
        Usuario usuario = sessao.getUsuario();
        String login = usuario.getLogin();

        // Remover o usu�rio do mapa de usu�rios
        usuarios.remove(login);

        // Ajustar as comunidades
        Iterator<Map.Entry<String, Comunidade>> iterator = comunidades.entrySet().iterator();
        while (iterator.hasNext()) {
            Comunidade comunidade = iterator.next().getValue();
            if (comunidade.getDono().equals(login)) {
                // Remover a comunidade do conjunto de comunidades de todos os membros
                for (String membro : comunidade.getMembros()) {
                    Usuario membroUsuario = usuarios.get(membro);
                    if (membroUsuario != null) {
                        membroUsuario.getComunidades().remove(comunidade.getNome());
                    }
                }
                iterator.remove(); // Remove a comunidade do mapa
            } else {
                // Se o usu�rio for apenas membro, remov�-lo da comunidade
                comunidade.removerMembro(login);
            }
        }

        // Remover o usu�rio dos relacionamentos de outros usu�rios e limpar recados
        for (Usuario outroUsuario : usuarios.values()) {
            outroUsuario.removerAmigo(login);
            outroUsuario.removerIdolo(login);
            outroUsuario.removerFa(login);
            outroUsuario.removerPaquera(login);
            outroUsuario.removerInimigo(login);
            outroUsuario.getRecados().clear(); // Limpa os recados
        }

        // Remover a sess�o ativa
        sessoes.remove(idSessao);
    }

    public String getDonoComunidade(String nome) {
        if (!comunidades.containsKey(nome)) {
            throw new ComunidadeNaoExisteException();
        }
        return getComunidade(nome).getDono();
    }
}