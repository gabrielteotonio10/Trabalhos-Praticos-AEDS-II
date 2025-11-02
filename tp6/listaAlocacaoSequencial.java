
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

class NoArrayListM {
    public static final int MAX_GAMES = 500;
    public static final int MAX_INNER_ARRAY = 50;
    public static final int MAX_IDS = 100;
}

class GameAS {
    int id;
    String name;
    String releaseDate;
    int estimatedOwners;
    float price;
    String[] supportedLanguages;
    int supportedLanguagesCount;
    int metacriticScore;
    float userScore;
    int achievements;
    String[] publishers;
    int publishersCount;
    String[] developers;
    int developersCount;
    String[] categories;
    int categoriesCount;
    String[] genres;
    int genresCount;
    String[] tags;
    int tagsCount;

    GameAS() {
        this.id = 0;
        this.name = "";
        this.releaseDate = "";
        this.estimatedOwners = 0;
        this.price = 0.0f;
        this.supportedLanguages = new String[NoArrayListM.MAX_INNER_ARRAY];
        this.supportedLanguagesCount = 0;
        this.metacriticScore = -1;
        this.userScore = -1.0f;
        this.achievements = 0;
        this.publishers = new String[NoArrayListM.MAX_INNER_ARRAY];
        this.publishersCount = 0;
        this.developers = new String[NoArrayListM.MAX_INNER_ARRAY];
        this.developersCount = 0;
        this.categories = new String[NoArrayListM.MAX_INNER_ARRAY];
        this.categoriesCount = 0;
        this.genres = new String[NoArrayListM.MAX_INNER_ARRAY];
        this.genresCount = 0;
        this.tags = new String[NoArrayListM.MAX_INNER_ARRAY];
        this.tagsCount = 0;
    }

    GameAS(int id, String name, String releaseDate, int estimatedOwners, float price,
            String[] supportedLanguages, int supportedLanguagesCount, int metacriticScore, float userScore,
            int achievements,
            String[] publishers, int publishersCount, String[] developers, int developersCount,
            String[] categories, int categoriesCount, String[] genres, int genresCount, String[] tags, int tagsCount) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.estimatedOwners = estimatedOwners;
        this.price = price;

        // Copiando arrays
        this.supportedLanguages = supportedLanguages;
        this.supportedLanguagesCount = supportedLanguagesCount;
        this.publishers = publishers;
        this.publishersCount = publishersCount;
        this.developers = developers;
        this.developersCount = developersCount;
        this.categories = categories;
        this.categoriesCount = categoriesCount;
        this.genres = genres;
        this.genresCount = genresCount;
        this.tags = tags;
        this.tagsCount = tagsCount;

        this.metacriticScore = metacriticScore;
        this.userScore = userScore;
        this.achievements = achievements;
    }
}

// Célula dupla
class CelulaDupla {
    public CelulaDupla prox;
    public CelulaDupla ant;
    public GameAS game;

    public CelulaDupla() {
        this(null);
    }

    public CelulaDupla(GameAS x) {
        prox = null;
        ant = null;
        game = x;
    }
}

// Lista dupla
class ListaDupla {
    private CelulaDupla primeiro, ultimo;

    public ListaDupla() {
        primeiro = new CelulaDupla();
        ultimo = primeiro;
    }

    // ------Inserções------
    // Inserir no início
    public void inserirInicio(GameAS game) {
        if (primeiro == ultimo) {
            primeiro.prox = new CelulaDupla(game);
            ultimo = primeiro.prox;
            ultimo.ant = primeiro;
        } else {
            CelulaDupla tmp = new CelulaDupla(game);
            tmp.prox = primeiro.prox;
            primeiro.prox = tmp;
            tmp.prox.ant = tmp;
            tmp.ant = primeiro;
            tmp = null;
        }
    }

    // Inserir no fim
    public void inserirFim(GameAS game) {
        if (primeiro == ultimo) {
            primeiro.prox = new CelulaDupla(game);
            ultimo = primeiro.prox;
            ultimo.ant = primeiro;
        } else {
            CelulaDupla tmp = new CelulaDupla(game);
            ultimo.prox = tmp;
            tmp.ant = ultimo;
            ultimo = tmp;
        }
    }

    // Inserir em posição
    public void inserir(GameAS game, int pos) {
        if (pos < 0 || pos > tamanho() + 1)
            return;
        if (pos == 0)
            inserirInicio(game);
        else if (pos == tamanho())
            inserirFim(game);
        else {
            CelulaDupla i = primeiro;
            for (int j = 0; j < pos; j++, i = i.prox) {
            }
            CelulaDupla tmp = new CelulaDupla(game);
            tmp.prox = i.prox;
            i.prox = tmp;
            tmp.ant = i;
            tmp.prox.ant = tmp;
            i = tmp = null;
        }
    }

    // Capturando tamanho
    public int tamanho() {
        int tam = 0;
        for (CelulaDupla i = primeiro.prox; i != null; i = i.prox) {
            tam++;
        }
        return tam;
    }

    // ------Remoções------
    // Remover do início
    public GameAS removerInicio() throws Exception {
        if (primeiro == ultimo)
            throw new Exception("Erro ao remover (vazia)!");
        printRemovido(0);
        GameAS elemento = primeiro.prox.game;
        if (primeiro.prox == ultimo) {
            CelulaDupla tmp = primeiro.prox;
            primeiro.prox = null;
            ultimo.ant = null;      
            ultimo = primeiro;
            tmp = null;
        } else {
            CelulaDupla tmp = primeiro.prox;
            primeiro.prox = primeiro.prox.prox;
            primeiro.prox.ant = primeiro;
            tmp.prox = tmp.ant = tmp = null;
        }
        return elemento;
    }

    // Remover do fim
    public GameAS removerFim() throws Exception {
        if (primeiro == ultimo)
            throw new Exception("Erro ao remover (vazia)!");
        int tam = tamanho();
        printRemovido(tam);
        GameAS elemento = ultimo.game;
        ultimo.ant.prox = null;
        ultimo = ultimo.ant;
        ultimo.prox = null;
        return elemento;
    }

    // Remover de posição
    public GameAS remover(int pos) throws Exception {
        pos ++;
        if (ultimo == primeiro)
            throw new Exception("Erro ao remover (vazia)!");
        GameAS elemento = null;
        int tam = tamanho();
        if (pos >= tam || pos < 0)
            throw new Exception("Erro ao remover (posição inválida)!");
        printRemovido(pos);
        if (pos == 0)
            elemento = removerInicio();
        else if (pos == tam - 1)
            elemento = removerFim();
        else {
            CelulaDupla i = primeiro;
            for (int j = 0; j < pos; j++, i = i.prox) {
            }
            i.ant.prox = i.prox;
            i.prox.ant = i.ant;
            elemento = i.game;
            i = i.prox = i.ant = i = null;
        }
        return elemento;
    }

    // ------Mostrar------
    // Mostrar removido
    public void printRemovido(int pos) {
        CelulaDupla i = primeiro.prox;
        int j = 1;
        for (; i != null && j < pos; i = i.prox, j++){}
        System.out.println("(R) " + i.game.name);
    }
    // Mostrar geral
    public void printando() {
        int j = 0;
        for (CelulaDupla i = primeiro.prox; i != null; i = i.prox) {
            String releaseDate = (i.game.releaseDate.charAt(1) == '/'
                    ? "0" + i.game.releaseDate
                    : i.game.releaseDate);
            System.out.println("[" + j + "]" +
                    " => " + i.game.id + " ## " + i.game.name + " ## " + releaseDate
                    + " ## " + i.game.estimatedOwners + " ## " + i.game.price
                    + " ## "
                    + printArray(i.game.supportedLanguages,
                            i.game.supportedLanguagesCount)
                    + " ## " + i.game.metacriticScore + " ## " + i.game.userScore
                    + " ## " + i.game.achievements
                    + " ## " + printArray(i.game.publishers, i.game.publishersCount)
                    + " ## " + printArray(i.game.developers, i.game.developersCount)
                    + " ## " + printArray(i.game.categories, i.game.categoriesCount)
                    + " ## " + printArray(i.game.genres, i.game.genresCount)
                    + " ## "
                    + (i.game.tagsCount == 0 ? ""
                            : printArray(i.game.tags, i.game.tagsCount))
                    + (i.game.tagsCount == 0 ? "" : " ##"));
            j++;
        }
    }

    // Printando o Array
    static String printArray(String[] array, int tamanho) {
        String resultado = "";
        // Se a lista estiver vazia, retorna "[]"
        if (tamanho == 0) {
            return "[]";
        }
        // Se não estiver vazia, formata a lista
        resultado += "[";
        for (int i = 0; i < tamanho; i++) {
            resultado += array[i];
            if (i < tamanho - 1) {
                resultado += ", ";
            }
        }
        resultado += "]";
        return resultado;
    }
}

public class listaAlocacaoSequencial {
    // Scanner
    public static Scanner sc;

    static int contador = 0;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        String entrada = sc.nextLine();
        String[] ids = new String[NoArrayListM.MAX_IDS];
        int idsTamanho = 0;

        while (!entrada.equals("FIM") && idsTamanho < ids.length) {
            ids[idsTamanho++] = entrada;
            entrada = sc.nextLine();
        }

        // Capturando os jogos digitados em uma lista dupla flexível
        ListaDupla games = JogosDigitadosM.inicializacao(ids, idsTamanho);

        int n = sc.nextInt();
        sc.nextLine();
        // Capturando as operações
        for (int i = 0; i < n; i++) {
            contador = 0;
            // Lendo a entrada
            String entrada2 = sc.nextLine();
            // Capturando operação, posição e id
            String operacao = capturandoOperacao(entrada2);

            String posicao = "";
            if (operacao.charAt(1) == '*')
                posicao = capturandoPosicao(entrada2);

            String id = "";
            GameAS game = null;
            if (operacao.charAt(0) == 'I') {
                id = capturandoIdFinal(entrada2);
                if (!id.isEmpty())
                    game = JogosDigitadosM.pegandoUmGame(id);
            }
            // Realizando a operação
            switch (operacao) {
                case "II":
                    games.inserirInicio(game);
                    break;
                case "I*":
                    games.inserir(game, Integer.parseInt(posicao));
                    break;
                case "IF":
                    games.inserirFim(game);
                    break;
                case "RI":
                    try {
                        games.removerInicio();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "R*":
                    try {
                        games.remover(Integer.parseInt(posicao));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "RF":
                    try {
                        games.removerFim();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    break;
            }
        }
        // Printando a lista final
        games.printando();
    }

    // Capturando operação
    static String capturandoOperacao(String entrada) {
        String operacao = "";
        while (contador < 2 && contador < entrada.length()) {
            operacao += entrada.charAt(contador);
            contador++;
        }
        contador++; // Pulando espaço
        return operacao;
    }

    // Capturando posição
    static String capturandoPosicao(String entrada) {
        String posicao = "";
        while (contador < entrada.length() && entrada.charAt(contador) != ' ') {
            posicao += entrada.charAt(contador);
            contador++;
        }
        contador++; // Pulando espaço
        return posicao;
    }

    // Capturando id final
    static String capturandoIdFinal(String entrada) {
        String id = "";
        while (contador < entrada.length()) {
            if (entrada.charAt(contador) != ' ') {
                id += entrada.charAt(contador);
                contador++;
            }
        }
        return id;
    }

}

// Capturando os jogos digitados através do id pelo usuário
class JogosDigitadosM {
    // Scanner
    public static Scanner sc;
    // Variável que pula caracteres das linhas
    static int contador = 0;
    // Ids de pesquisa e seu tamanho
    static String[] ids;
    static int idsTamanho;
    // Lista de jogos encontrados e seu tamanho
    static int gamesListTamanho;

    // Função para obter o tamanho final da lista de jogos
    public static int obterTamanhoGamesList() {
        return gamesListTamanho;
    }

    // Capturando os jogos
    static ListaDupla inicializacao(String[] idArray, int tamanho) {
        ListaDupla lista = new ListaDupla();
        gamesListTamanho = 0;

        // Copiando IDs para a variável de classe 'ids'
        ids = idArray;
        idsTamanho = tamanho;

        // Pesquisa por id
        for (int j = 0; j < tamanho; j++) {
            int indiceEncontrado = -1;

            try {
                java.io.File arquivo = new java.io.File("/tmp/games.csv");
                if (!arquivo.exists()) {
                    System.out.println("Arquivo 'games.csv' não encontrado!");
                    return lista;
                }

                InputStream is = new FileInputStream(arquivo); // abre do zero
                Scanner sc = new Scanner(is);

                // Pula cabeçalho
                if (sc.hasNextLine())
                    sc.nextLine();

                while (sc.hasNextLine() && indiceEncontrado == -1) {
                    String linha = sc.nextLine();
                    contador = 0;

                    int id = capturaId(linha);
                    indiceEncontrado = igualId(id);

                    if (indiceEncontrado != -1) {
                        String name = capturaName(linha);
                        String releaseDate = capturaReleaseDate(linha);
                        int estimatedOwners = capturaEstimatedOwners(linha);
                        float price = capturaPrice(linha);

                        String[] supportedLanguages = new String[NoArrayListM.MAX_INNER_ARRAY];
                        int supportedLanguagesCount = capturaSupportedLanguages(linha, supportedLanguages);
                        int metacriticScore = capturaMetacriticScore(linha);
                        float userScore = capturaUserScore(linha);
                        int achievements = capturaAchievements(linha);

                        String[] publishers = new String[NoArrayListM.MAX_INNER_ARRAY];
                        int publishersCount = capturaUltimosArryays(linha, publishers);
                        String[] developers = new String[NoArrayListM.MAX_INNER_ARRAY];
                        int developersCount = capturaUltimosArryays(linha, developers);
                        String[] categories = new String[NoArrayListM.MAX_INNER_ARRAY];
                        int categoriesCount = capturaUltimosArryays(linha, categories);
                        String[] genres = new String[NoArrayListM.MAX_INNER_ARRAY];
                        int genresCount = capturaUltimosArryays(linha, genres);
                        String[] tags = new String[NoArrayListM.MAX_INNER_ARRAY];
                        int tagsCount = capturaUltimosArryays(linha, tags);

                        GameAS jogo = new GameAS(id, name, releaseDate, estimatedOwners, price,
                                supportedLanguages, supportedLanguagesCount, metacriticScore, userScore, achievements,
                                publishers, publishersCount, developers, developersCount, categories, categoriesCount,
                                genres, genresCount, tags, tagsCount);

                        lista.inserirFim(jogo);
                        removerId(indiceEncontrado);
                    }
                }

                sc.close();
                is.close();

            } catch (Exception e) {
                System.out.println("Erro ao abrir ou ler o arquivo: " + e.getMessage());
            }
        }

        return lista;
    }

    static GameAS pegandoUmGame(String idV) {
        // Abrindo do arquivo
        InputStream is = null;
        // Criando o game
        try {
            java.io.File arquivo = new java.io.File("/tmp/games.csv");
            if (!arquivo.exists()) {
                System.out.println("Arquivo 'games.csv' não encontrado!");
                return null;
            }
            is = new FileInputStream(arquivo);
        } catch (Exception e) {
            System.out.println("Erro ao abrir o arquivo: " + e.getMessage());
            return null;
        }
        sc = new Scanner(is);
        // Criando objeto
        GameAS jogo = new GameAS();
        // Pesquisa por id
        boolean teste = false;
        // Pula cabeçalho
        if (sc.hasNextLine())
            sc.nextLine();
        while (sc.hasNextLine() && teste == false) {
            String linha = sc.nextLine();
            // Resetando o contador para a nova linha
            contador = 0;
            // Capturando outras informações
            int id = capturaId(linha);

            if (id == Integer.parseInt(idV)) {
                teste = true;
                String name = capturaName(linha);
                String releaseDate = capturaReleaseDate(linha);
                int estimatedOwners = capturaEstimatedOwners(linha);
                float price = capturaPrice(linha);

                String[] supportedLanguages = new String[NoArrayListM.MAX_INNER_ARRAY];
                int supportedLanguagesCount = capturaSupportedLanguages(linha, supportedLanguages);
                int metacriticScore = capturaMetacriticScore(linha);
                float userScore = capturaUserScore(linha);
                int achievements = capturaAchievements(linha);

                String[] publishers = new String[NoArrayListM.MAX_INNER_ARRAY];
                int publishersCount = capturaUltimosArryays(linha, publishers);
                String[] developers = new String[NoArrayListM.MAX_INNER_ARRAY];
                int developersCount = capturaUltimosArryays(linha, developers);
                String[] categories = new String[NoArrayListM.MAX_INNER_ARRAY];
                int categoriesCount = capturaUltimosArryays(linha, categories);
                String[] genres = new String[NoArrayListM.MAX_INNER_ARRAY];
                int genresCount = capturaUltimosArryays(linha, genres);
                String[] tags = new String[NoArrayListM.MAX_INNER_ARRAY];
                int tagsCount = capturaUltimosArryays(linha, tags);

                // Adicionando a classe
                jogo = new GameAS(id, name, releaseDate, estimatedOwners, price,
                        supportedLanguages, supportedLanguagesCount, metacriticScore, userScore, achievements,
                        publishers, publishersCount, developers, developersCount, categories, categoriesCount, genres,
                        genresCount, tags, tagsCount);
            }
        }
        sc.close();
        return jogo;
    }

    static int igualId(int id) {
        if (Integer.parseInt(ids[0]) == id)
            return 0;
        else
            return -1;
    }

    // Função para remover o ID da lista de pesquisa
    static void removerId(int indice) {
        if (indice >= 0 && indice < idsTamanho) {
            for (int j = indice; j < idsTamanho - 1; j++) {
                ids[j] = ids[j + 1];
            }
            ids[idsTamanho - 1] = null; // Limpa a última posição
            idsTamanho--; // Decrementa o tamanho
        }
    }

    // Capturando Id
    static int capturaId(String jogo) {
        int id = 0;
        while (contador < jogo.length() && Character.isDigit(jogo.charAt(contador))) {
            id = id * 10 + (jogo.charAt(contador) - '0');
            contador++;
        }
        return id;
    }

    // Capturando nome
    static String capturaName(String jogo) {
        String name = "";
        while (jogo.charAt(contador) != ',' && contador < jogo.length()) {
            contador++;
        }
        contador++;
        while (jogo.charAt(contador) != ',' && contador < jogo.length()) {
            name += jogo.charAt(contador);
            contador++;
        }
        return name;
    }

    // Capturando Release Date
    static String capturaReleaseDate(String jogo) {
        while (contador < jogo.length() && jogo.charAt(contador) != '"') {
            contador++;
        }
        contador++;
        String dia = "", mes = "", ano = "";
        // Pegando mês
        for (int i = 0; contador < jogo.length() && i < 3; i++) {
            mes += jogo.charAt(contador);
            contador++;
        }
        mes = mes.trim();
        switch (mes) {
            case "Jan":
                mes = "01";
                break;
            case "Feb":
                mes = "02";
                break;
            case "Mar":
                mes = "03";
                break;
            case "Apr":
                mes = "04";
                break;
            case "May":
                mes = "05";
                break;
            case "Jun":
                mes = "06";
                break;
            case "Jul":
                mes = "07";
                break;
            case "Aug":
                mes = "08";
                break;
            case "Sep":
                mes = "09";
                break;
            case "Oct":
                mes = "10";
                break;
            case "Nov":
                mes = "11";
                break;
            case "Dec":
                mes = "12";
                break;
            default:
                break;
        }
        // Pulando espaço
        while (contador < jogo.length() && !Character.isDigit(jogo.charAt(contador)) && jogo.charAt(contador) != ',') {
            contador++;
        }
        // Pegando dia
        while (contador < jogo.length() && Character.isDigit(jogo.charAt(contador))) {
            dia += jogo.charAt(contador);
            contador++;
        }
        // Pulando espaço
        while (contador < jogo.length() && !Character.isDigit(jogo.charAt(contador))) {
            contador++;
        }
        // Pegando ano
        while (contador < jogo.length() && jogo.charAt(contador) != '"') {
            ano += jogo.charAt(contador);
            contador++;
        }
        if (dia.isEmpty())
            dia = "01";
        if (mes.isEmpty())
            mes = "01";
        if (ano.isEmpty())
            ano = "0000";
        return dia + "/" + mes + "/" + ano;
    }

    // Capturando Estimated Owners
    static int capturaEstimatedOwners(String jogo) {
        int estimatedOwners = 0;
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            estimatedOwners = estimatedOwners * 10 + (jogo.charAt(contador) - '0');
            contador++;
        }
        return estimatedOwners;
    }

    // Capturando Price
    static float capturaPrice(String jogo) {
        String price = "";
        while (contador < jogo.length() && !Character.isDigit(jogo.charAt(contador)) && jogo.charAt(contador) != 'F') {
            contador++;
        }
        while (contador < jogo.length() && (Character.isDigit(jogo.charAt(contador)) || jogo.charAt(contador) == '.')) {
            price += jogo.charAt(contador);
            contador++;
        }
        price = price.trim();
        if (price.isEmpty() || price.equalsIgnoreCase("Free to play")) {
            return 0.0f;
        }
        price = price.replaceAll("[^0-9.]", "");
        try {
            return Float.parseFloat(price);
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    // Capturando idiomas
    static int capturaSupportedLanguages(String jogo, String[] supportedLanguages) {
        int count = 0;
        while (contador < jogo.length() && jogo.charAt(contador) != ']' && count < supportedLanguages.length) {
            String lingua = "";
            while (contador < jogo.length() && !Character.isAlphabetic(jogo.charAt(contador))) {
                contador++;
            }
            while (contador < jogo.length() && jogo.charAt(contador) != ',' && jogo.charAt(contador) != ']') {
                if (Character.isAlphabetic(jogo.charAt(contador)) || jogo.charAt(contador) == ' '
                        || jogo.charAt(contador) == '-') {
                    lingua += jogo.charAt(contador);
                }
                contador++;
            }
            supportedLanguages[count++] = lingua;
        }
        return count; // Retorna o tamanho real
    }

    // Capturando Metacritic Score
    static int capturaMetacriticScore(String jogo) {
        String metacriticScore = "";
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        while (contador < jogo.length() && Character.isDigit(jogo.charAt(contador))) {
            metacriticScore += jogo.charAt(contador);
            contador++;
        }
        if (metacriticScore.isEmpty())
            return -1;
        else
            return Integer.parseInt(metacriticScore);
    }

    // Capturando User Score
    static float capturaUserScore(String jogo) {
        String userScore = "";
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        while (contador < jogo.length() && (Character.isDigit(jogo.charAt(contador)) || jogo.charAt(contador) == '.')) {
            userScore += jogo.charAt(contador);
            contador++;
        }
        if (userScore.isEmpty())
            return -1.0f;
        else
            return Float.parseFloat(userScore);
    }

    // Capturando Achievements
    static int capturaAchievements(String jogo) {
        String achievements = "";
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        while (contador < jogo.length() && (Character.isDigit(jogo.charAt(contador)) || jogo.charAt(contador) == '.')) {
            achievements += jogo.charAt(contador);
            contador++;
        }
        if (achievements.isEmpty())
            return -1;
        else
            return Integer.parseInt(achievements);
    }

    // Capturando Últimos Arrays
    static int capturaUltimosArryays(String jogo, String[] categoria) {
        int count = 0;
        boolean teste = false;
        while (contador < jogo.length() && !Character.isAlphabetic(jogo.charAt(contador))
                && !Character.isDigit(jogo.charAt(contador))) {
            if (jogo.charAt(contador) == '"')
                teste = true;
            contador++;
        }
        if (teste) {
            while (contador < jogo.length() && jogo.charAt(contador) != '"' && count < categoria.length) {
                String parte = "";
                while (contador < jogo.length() && jogo.charAt(contador) != ',' && jogo.charAt(contador) != '"') {
                    parte += jogo.charAt(contador);
                    contador++;
                }
                while (contador < jogo.length() && !Character.isAlphabetic(jogo.charAt(contador))
                        && !Character.isDigit(jogo.charAt(contador))
                        && jogo.charAt(contador) != '"') {
                    contador++;
                }
                categoria[count++] = parte; // Adiciona ao array e incrementa o contador
            }
            contador++;
        } else {
            if (count < categoria.length) {
                String parte = "";
                while (contador < jogo.length() && jogo.charAt(contador) != ',') {
                    parte += jogo.charAt(contador);
                    contador++;
                }
                categoria[count++] = parte; // Adiciona ao array e incrementa o contador
            }
        }
        // Pula a vírgula depois do array (se houver)
        if (contador < jogo.length() && jogo.charAt(contador) == ',') {
            contador++;
        }
        return count; // Retorna o tamanho real
    }
}