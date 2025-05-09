package br.ufal.ic.p2.jackut.model.models;

import br.ufal.ic.p2.jackut.model.exceptions.*;
import java.io.*;
import java.util.*;

/**
 * Classe que representa o sistema principal do Jackut. Gerencia usuários, sessões,
 * comunidades e operações relacionadas a amizades, recados, ídolos, paqueras e inimigos.
 * Também é responsável pela persistência dos dados do sistema.

 */
public class Sistema implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String ARQUIVO_DADOS = "dados.ser";

    /** Mapa de usuários cadastrados, onde a chave é o login do usuário. */
    private Map<String, Usuario> usuarios;
    /** Mapa de sessões ativas, onde a chave é o ID da sessão. */
    private Map<String, Sessao> sessoes;
    /** Mapa de comunidades, onde a chave é o nome da comunidade. */
    private Map<String, Comunidade> comunidades = new HashMap<>();

    /**
     * Construtor padrão que inicializa as estruturas de dados do sistema.
     */
    public Sistema() {
        this.usuarios = new HashMap<>();
        this.sessoes = new HashMap<>();
        this.comunidades = new HashMap<>(); // Ou aqui
    }

    /**
     * Carrega os dados do sistema a partir do arquivo de persistência.
     *
     * @return Instância do sistema carregada ou nova instância se o arquivo não existir.
     * @throws RuntimeException Se ocorrer erro de I/O ou desserialização.
     */
    public static Sistema carregarDados() {
        File arquivo = new File(ARQUIVO_DADOS);
        if (!arquivo.exists()) {
            return new Sistema(); // Retorna um novo sistema se o arquivo não existir
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            Sistema sistema = (Sistema) in.readObject();

            // Garante que o mapa de comunidades está inicializado
            if (sistema.comunidades == null) {
                sistema.comunidades = new HashMap<>();
            }

            return sistema;

        } catch (InvalidClassException e) {
            System.err.println("Aviso: Dados antigos incompatíveis. Criando novo sistema.");
            return new Sistema();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar dados: " + e.getMessage(), e);
        }
    }

    /**
     * Salva o estado atual do sistema em arquivo para persistência.
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
     * Cria um novo usuário no sistema.
     *
     * @param login Login único do usuário (não pode ser vazio ou nulo).
     * @param senha Senha do usuário (não pode ser vazia ou nula).
     * @param nome Nome de exibição do usuário.
     * @throws IllegalArgumentException Se login/senha forem inválidos ou o login já existir.
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
        usuarios.put(login, new Usuario(login, senha, nome)); // Adiciona o usuário ao mapa de usuários
    }

    /**
     * Abre uma nova sessão para um usuário autenticado.
     *
     * @param login Login do usuário.
     * @param senha Senha do usuário.
     * @return ID da sessão gerada.
     * @throws IllegalArgumentException Se o login ou senha forem inválidos.
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
     *
     * @param login Login do usuário.
     * @param atributo Nome do atributo (ex: "nome", "senha").
     * @return Valor do atributo.
     * @throws UsuarioNaoCadastradoException Se o usuário não existir.
     */
    public String getAtributoUsuario(String login, String atributo) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
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
            throw new UsuarioNaoCadastradoException();
        }
        Sessao sessao = sessoes.get(idSessao);
        if (sessao == null) {
            throw new IllegalArgumentException("Sessão inválida.");
        }
        sessao.getUsuario().editarAtributo(atributo, valor);
    }

    /**
     * Adiciona um amigo para o usuário da sessão atual.
     * Verifica relações de inimizade e envia convites.
     *
     * @param idSessao ID da sessão do usuário.
     * @param amigo Login do amigo a ser adicionado.
     * @throws UsuarioNaoCadastradoException Se o usuário ou amigo não existirem.
     * @throws IllegalArgumentException Se tentar adicionar a si mesmo ou o amigo for inimigo.
     */
    public void adicionarAmigo(String idSessao, String amigo) {
        Sessao sessao = getSessao(idSessao);
        Usuario usuario = sessao.getUsuario();
        Usuario usuarioAmigo = usuarios.get(amigo);

        if (usuarioAmigo == null) {
            throw new UsuarioNaoCadastradoException();
        }

        // Verifica se o amigo tem o usuário como inimigo
        if (usuarioAmigo.getInimigos().contains(usuario.getLogin())) {
            throw new IllegalArgumentException("Função inválida: " + usuarioAmigo.getNome() + " é seu inimigo.");
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
            throw new UsuarioNaoCadastradoException();
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
            throw new UsuarioNaoCadastradoException();
        }
        return usuario.getAmigosString();
    }

    /**
     * Envia um recado para outro usuário.
     * @param idSessao ID da sessão do remetente.
     * @param destinatario Login do destinatário.
     * @param recado Conteúdo do recado.
     * @throws IllegalArgumentException Se a sessão for inválida, destinatário não existir ou for o mesmo que o remetente.
     */
    public void enviarRecado(String idSessao, String destinatario, String recado) {
        Sessao sessao = getSessao(idSessao);
        Usuario remetente = sessao.getUsuario();
        Usuario usuarioDestinatario = usuarios.get(destinatario);

        if (usuarioDestinatario == null) {
            throw new UsuarioNaoCadastradoException();
        }

        // Verifica se o destinatário tem o remetente como inimigo
        if (usuarioDestinatario.getInimigos().contains(remetente.getLogin())) {
            throw new IllegalArgumentException("Função inválida: " + usuarioDestinatario.getNome() + " é seu inimigo.");
        }

        // Verifica se o destinatário existe
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
     * Lê o próximo recado da fila do usuário da sessão atual.
     * @param idSessao ID da sessão.
     * @return Conteúdo do recado.
     * @throws IllegalArgumentException Se a sessão for inválida.
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
    /**
     * Obtém as comunidades de um usuário com base no login fornecido.
     *
     * @param login O login do usuário cujas comunidades serão retornadas.
     * @return Uma representação em string das comunidades do usuário.
     * @throws UsuarioNaoCadastradoException Se o usuário com o login fornecido não estiver cadastrado.
     */
    public String getComunidades(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }
        return usuario.getComunidades().toString();
    }

    /**
     * Retorna a descrição de uma comunidade com base no nome fornecido.
     *
     * @param nome O nome da comunidade cuja descrição será retornada.
     * @return A descrição da comunidade.
     * @throws ComunidadeNaoExisteException Se a comunidade com o nome fornecido não existir.
     */
    public String getDescricaoComunidade(String nome) {
        if (!comunidades.containsKey(nome)) {
            throw new ComunidadeNaoExisteException();
        }
        return comunidades.get(nome).getDescricao();
    }

    /**
     * Obtém a comunidade completa com base no nome fornecido.
     *
     * @param nome O nome da comunidade a ser retornada.
     * @return A comunidade associada ao nome fornecido.
     * @throws ComunidadeNaoExisteException Se a comunidade com o nome fornecido não existir.
     */
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
     * @param nome Nome único da comunidade.
     * @param descricao Descrição da comunidade.
     * @param dono Login do usuário criador.
     * @throws ComunidadeExistenteException Se o nome já estiver em uso.
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
    /**
     * Obtém a sessão de um usuário com base no identificador da sessão fornecido.
     *
     * @param idSessao O identificador da sessão que será retornado.
     * @return A sessão associada ao identificador fornecido.
     * @throws UsuarioNaoCadastradoException Se a sessão com o identificador fornecido não existir.
     */

    public Sessao getSessao(String idSessao) {
        Sessao sessao = sessoes.get(idSessao);
        if (sessao == null) {
            throw new UsuarioNaoCadastradoException();
        }
        return sessao;
    }
    /**
     * Obtém o usuário com base no login fornecido.
     *
     * @param login O login do usuário que será retornado.
     * @return O usuário associado ao login fornecido.
     * @throws UsuarioNaoCadastradoException Se o usuário com o login fornecido não existir.
     */
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
     * @param loginUsuario Login do usuário a ser adicionado.
     * @throws ComunidadeNaoExisteException Se a comunidade não existir.
     * @throws UsuarioNaoCadastradoException Se o usuário não existir.
     */

    public void adicionarMembroComunidade(String nomeComunidade, String loginUsuario) {

        Comunidade comunidade = getComunidade(nomeComunidade);
        Usuario usuario = getUsuario(loginUsuario);

        comunidade.adicionarMembro(loginUsuario);
        usuario.adicionarComunidade(nomeComunidade);
    }
    /**
     * Retorna as comunidades de um usuário com base no login fornecido.
     *
     * @param login O login do usuário cujas comunidades serão retornadas.
     * @return Um {@code Set} contendo as comunidades do usuário.
     */

    public Set<String> getComunidadesDoUsuario(String login) {
        return getUsuario(login).getComunidades();
    }
    /**
     * Retorna as comunidades de um usuário com base no login fornecido, formatadas como uma string.
     *
     * @param login O login do usuário cujas comunidades serão retornadas.
     * @return Uma string representando as comunidades do usuário no formato {comunidade1,comunidade2,...}.
     */
    public String getComunidadesDoUsuarioFormatado(String login) {
        Usuario usuario = getUsuario(login);
        List<String> comunidades = new ArrayList<>(usuario.getComunidades());

        return "{" + String.join(",", comunidades) + "}";
    }
    /**
     * Retorna os membros de uma comunidade com base no nome fornecido, formatados como uma string.
     *
     * @param nome O nome da comunidade cujos membros serão retornados.
     * @return Uma string representando os membros da comunidade no formato {membro1,membro2,...}.
     * @throws ComunidadeNaoExisteException Se a comunidade com o nome fornecido não existir.
     */
    public String getMembrosComunidade(String nome) {
        if (!comunidades.containsKey(nome)) {
            throw new ComunidadeNaoExisteException();
        }
        List<String> membros = new ArrayList<>(comunidades.get(nome).getMembros());

        return "{" + String.join(",", membros) + "}";
    }
    /**
     * Adiciona o usuário de uma sessão a uma comunidade existente.
     * @param idSessao ID da sessão do usuário.
     * @param nomeComunidade Nome da comunidade à qual o usuário será adicionado.
     * @throws UsuarioNaoCadastradoException Se a sessão for inválida.
     * @throws ComunidadeNaoExisteException Se a comunidade não existir.
     */

    public void enviarMensagemComunidade(String idSessao, String nomeComunidade, String mensagem) {
        Sessao sessao = getSessao(idSessao); // Valida a sessão
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
     * Adiciona um usuário como ídolo. Gera relação mútua de fã/ídolo.
     *
     * @param idSessao ID da sessão do usuário.
     * @param idolo Login do ídolo a ser adicionado.
     * @throws UsuarioNaoCadastradoException Se o ídolo não existir.
     * @throws IllegalArgumentException Se o usuário for o próprio ídolo ou já for fã.
     */
    public void adicionarIdolo(String idSessao, String idolo) {
        Sessao sessao = getSessao(idSessao);
        Usuario usuario = sessao.getUsuario();
        Usuario usuarioIdolo = usuarios.get(idolo);

        if (usuarioIdolo == null) {
            throw new UsuarioNaoCadastradoException();
        }

        // Verifica se o ídolo tem o usuário como inimigo
        if (usuarioIdolo.getInimigos().contains(usuario.getLogin())) {
            throw new IllegalArgumentException("Função inválida: " + usuarioIdolo.getNome() + " é seu inimigo.");
        }

        // Agora você pode acessar o atributo 'fas' do Usuario corretamente
        if (usuario.getFas().contains(idolo)) {
            throw new JaEhFaException();
        }

        // Impedir que o usuário adicione a si mesmo como ídolo
        if (usuario.getLogin().equals(idolo)) {
            throw new BloqueioAutoIdolException();
        }

        // Impede adicionar um ídolo repetido
        if (usuario.ehFa(idolo)) {
            throw new UsuarioAddIdolException();
        }
        // Verificar se o ídolo existe
        if (!usuarios.containsKey(idolo)) {
            throw new UsuarioNaoCadastradoException();
        }

        // Adiciona o ídolo
        usuario.adicionarIdolo(idolo);
        Usuario idoloUsuario = usuarios.get(idolo);
        idoloUsuario.adicionarFa(usuario.getLogin());
    }

    /**
     * Adiciona um usuário como paquera. Notifica ambos se houver reciprocidade.
     *
     * @param idSessao ID da sessão do usuário.
     * @param paquera Login da paquera a ser adicionada.
     * @throws UsuarioNaoCadastradoException Se a paquera não existir.
     * @throws IllegalArgumentException Se for auto-adicionamento ou paquera repetida.
     */
    public void adicionarPaquera(String idSessao, String paquera) {
        Sessao sessao = getSessao(idSessao);
        Usuario usuario = sessao.getUsuario();
        Usuario usuarioPaquera = usuarios.get(paquera);

        if (usuarioPaquera == null) {
            throw new UsuarioNaoCadastradoException();
        }

        // Verifica se a paquera tem o usuário como inimigo
        if (usuarioPaquera.getInimigos().contains(usuario.getLogin())) {
            throw new IllegalArgumentException("Função inválida: " + usuarioPaquera.getNome() + " é seu inimigo.");
        }
        if (!usuarios.containsKey(paquera)) {
            throw new UsuarioNaoCadastradoException();
        }

        // Impede adicionar a si mesmo como paquera
        if (usuario.getLogin().equals(paquera)) {
            throw new BloqueioAutoPaqueraException();
        }

        // Verifica se o usuário já é paquera
        if (usuario.ehPaquera(paquera)) {
            throw new UsuarioAddPaqueraException();
        }

        usuario.adicionarPaquera(paquera);

        // Se ambos se adicionarem mutuamente, envia recado
        if (usuarioPaquera.ehPaquera(usuario.getLogin())) {
            usuario.receberRecado(usuarioPaquera.getNome() + " é seu paquera - Recado do Jackut.");
            usuarioPaquera.receberRecado(usuario.getNome() + " é seu paquera - Recado do Jackut.");
        }
    }
    /**
     * Verifica se o usuário com o login fornecido é fã do ídolo especificado.
     *
     * @param login O login do usuário que está verificando.
     * @param idolo O identificador do ídolo.
     * @return {@code true} se o usuário for fã do ídolo, {@code false} caso contrário.
     * @throws UsuarioNaoCadastradoException Se o usuário ou o ídolo não existirem no sistema.
     */
    public boolean ehFa(String login, String idolo) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null || !usuarios.containsKey(idolo)) {
            throw new UsuarioNaoCadastradoException();
        }
        return usuario.ehFa(idolo);
    }
    /**
     * Formata um conjunto de strings em uma string no formato {elemento1,elemento2,...}.
     *
     * @param conjunto O conjunto a ser formatado.
     * @return Uma string representando o conjunto formatado.
     */
    private String formatarSet(Set<String> conjunto) {
        return conjunto.isEmpty() ? "{}" : "{" + String.join(",", conjunto) + "}";
    }
    /**
     * Retorna os fãs de um usuário com base no login fornecido, formatados como uma string.
     *
     * @param login O login do usuário cujos fãs serão retornados.
     * @return Uma string representando os fãs do usuário no formato {fa1,fa2,...}.
     * @throws UsuarioNaoCadastradoException Se o usuário com o login fornecido não existir.
     */
    public String getFas(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }
        return formatarSet(usuario.getFas());
    }
    /**
     * Verifica se o usuário com o login fornecido está paquerando a pessoa especificada.
     *
     * @param login O login do usuário que está verificando.
     * @param paquera O identificador da pessoa que pode ser paquerada.
     * @return {@code true} se o usuário estiver paquerando a pessoa, {@code false} caso contrário.
     * @throws UsuarioNaoCadastradoException Se o usuário ou a pessoa não existirem no sistema.
     */
    public boolean ehPaquera(String login, String paquera) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null || !usuarios.containsKey(paquera)) {
            throw new UsuarioNaoCadastradoException();
        }
        return usuario.ehPaquera(paquera);
    }
    /**
     * Retorna as pessoas que o usuário está paquerando, formatadas como uma string.
     *
     * @param login O login do usuário cujas paqueras serão retornadas.
     * @return Uma string representando as paqueras do usuário no formato {paquera1,paquera2,...}.
     * @throws UsuarioNaoCadastradoException Se o usuário com o login fornecido não existir.
     */
    public String getPaqueras(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }
        return formatarSet(usuario.getPaqueras());
    }
    /**
     * Retorna os inimigos de um usuário com base no login fornecido, formatados como uma string.
     *
     * @param login O login do usuário cujos inimigos serão retornados.
     * @return Uma string representando os inimigos do usuário no formato {inimigo1,inimigo2,...}.
     * @throws UsuarioNaoCadastradoException Se o usuário com o login fornecido não existir.
     */
    public String getInimigos(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException();
        }
        return formatarSet(usuario.getInimigos());
    }
    /**
     * Adiciona um inimigo à lista de inimigos do usuário, com base no ID da sessão.
     *
     * @param idSessao O identificador da sessão do usuário.
     * @param inimigo O identificador do inimigo a ser adicionado.
     * @throws UsuarioNaoCadastradoException Se o inimigo não estiver cadastrado.
     * @throws JaInimigoException Se o usuário já for inimigo do usuário.
     * @throws BloqueioInimigoDeSiException Se o usuário tentar adicionar a si mesmo como inimigo.
     */
    public void adicionarInimigo(String idSessao, String inimigo) {
        Sessao sessao = getSessao(idSessao);
        Usuario usuario = sessao.getUsuario();
        String inimigoNormalizado = inimigo.toLowerCase(); // Normaliza para minúsculas

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
     * Remove completamente um usuário do sistema, incluindo relações e comunidades.
     *
     * @param idSessao ID da sessão do usuário a ser removido.
     * @throws UsuarioNaoCadastradoException Se a sessão for inválida.
     */
    public void removerUsuario(String idSessao) {
        Sessao sessao = sessoes.get(idSessao);
        if (sessao == null) {
            throw new UsuarioNaoCadastradoException();
        }
        Usuario usuario = sessao.getUsuario();
        String login = usuario.getLogin();

        // Remover o usuário do mapa de usuários
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
                // Se o usuário for apenas membro, removê-lo da comunidade
                comunidade.removerMembro(login);
            }
        }

        // Remover o usuário dos relacionamentos de outros usuários e limpar recados
        for (Usuario outroUsuario : usuarios.values()) {
            outroUsuario.removerAmigo(login);
            outroUsuario.removerIdolo(login);
            outroUsuario.removerFa(login);
            outroUsuario.removerPaquera(login);
            outroUsuario.removerInimigo(login);
            outroUsuario.getRecados().clear(); // Limpa os recados
        }

        // Remover a sessão ativa
        sessoes.remove(idSessao);
    }

    /**
     * Retorna o dono de uma comunidade com base no nome fornecido.
     *
     * @param nome O nome da comunidade cuja propriedade será retornada.
     * @return O login do dono da comunidade.
     * @throws ComunidadeNaoExisteException Se a comunidade com o nome fornecido não existir.
     */
    public String getDonoComunidade(String nome) {
        if (!comunidades.containsKey(nome)) {
            throw new ComunidadeNaoExisteException();
        }
        return getComunidade(nome).getDono();
    }
}