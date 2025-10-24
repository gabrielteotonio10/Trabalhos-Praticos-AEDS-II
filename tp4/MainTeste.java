package tp4;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

class NoArrayList {
    public static final int MAX_GAMES = 500;
    public static final int MAX_INNER_ARRAY = 50;
    public static final int MAX_IDS = 100;
}

class Game {
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

    Game() {
        this.id = 0;
        this.name = "";
        this.releaseDate = "";
        this.estimatedOwners = 0;
        this.price = 0.0f;
        this.supportedLanguages = new String[NoArrayList.MAX_INNER_ARRAY];
        this.supportedLanguagesCount = 0;
        this.metacriticScore = -1;
        this.userScore = -1.0f;
        this.achievements = 0;
        this.publishers = new String[NoArrayList.MAX_INNER_ARRAY];
        this.publishersCount = 0;
        this.developers = new String[NoArrayList.MAX_INNER_ARRAY];
        this.developersCount = 0;
        this.categories = new String[NoArrayList.MAX_INNER_ARRAY];
        this.categoriesCount = 0;
        this.genres = new String[NoArrayList.MAX_INNER_ARRAY];
        this.genresCount = 0;
        this.tags = new String[NoArrayList.MAX_INNER_ARRAY];
        this.tagsCount = 0;
    }

    Game(int id, String name, String releaseDate, int estimatedOwners, float price,
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

public class MainTeste {
    public static Scanner sc;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        String entrada = sc.nextLine();

        String[] ids = new String[NoArrayList.MAX_IDS];
        int idsTamanho = 0;

        while (!entrada.equals("FIM") && idsTamanho < ids.length) {
            if (idsTamanho < ids.length) {
                ids[idsTamanho++] = entrada;
            }
            entrada = sc.nextLine();
        }

        Game[] gamesList = JogosDigitados.inicializacao(ids, idsTamanho);
        int gamesListTamanho = JogosDigitados.obterTamanhoGamesList();

        printando(gamesList, gamesListTamanho);

        sc.close();
    }

    static void printando(Game[] jogosOrdenados, int tamanho) {
        for (int i = 0; i < tamanho; i++) {
            String releaseDate = (jogosOrdenados[i].releaseDate.charAt(1) == '/'
                    ? "0" + jogosOrdenados[i].releaseDate
                    : jogosOrdenados[i].releaseDate);
            System.out.println(
                    "=> " + jogosOrdenados[i].id + " ## " + jogosOrdenados[i].name + " ## " + releaseDate
                            + " ## " + jogosOrdenados[i].estimatedOwners + " ## " + jogosOrdenados[i].price
                            + " ## "
                            + printArray(jogosOrdenados[i].supportedLanguages,
                                    jogosOrdenados[i].supportedLanguagesCount)
                            + " ## " + jogosOrdenados[i].metacriticScore + " ## " + jogosOrdenados[i].userScore
                            + " ## " + jogosOrdenados[i].achievements
                            + " ## " + printArray(jogosOrdenados[i].publishers, jogosOrdenados[i].publishersCount)
                            + " ## " + printArray(jogosOrdenados[i].developers, jogosOrdenados[i].developersCount)
                            + " ## " + printArray(jogosOrdenados[i].categories, jogosOrdenados[i].categoriesCount)
                            + " ## " + printArray(jogosOrdenados[i].genres, jogosOrdenados[i].genresCount)
                            + " ## "
                            + (jogosOrdenados[i].tagsCount == 0 ? ""
                                    : printArray(jogosOrdenados[i].tags, jogosOrdenados[i].tagsCount))
                            + (jogosOrdenados[i].tagsCount == 0 ? "" : " ##"));
        }
    }

    static String printArray(String[] array, int tamanho) {
        String resultado = "";
        if (tamanho == 0)
            return "[]";
        resultado += "[";
        for (int i = 0; i < tamanho; i++) {
            resultado += array[i];
            if (i < tamanho - 1)
                resultado += ", ";
        }
        resultado += "]";
        return resultado;
    }
}

// Capturando os jogos digitados através do id pelo usuário
class JogosDigitados {
    // Scanner
    public static Scanner sc;
    // Variável que pula caracteres das linhas
    static int contador = 0;
    // Ids de pesquisa e seu tamanho
    static String[] ids;
    static int idsTamanho;
    // Lista de jogos encontrados e seu tamanho
    static Game[] gamesList;
    static int gamesListTamanho;

    // Função para obter o tamanho final da lista de jogos
    public static int obterTamanhoGamesList() {
        return gamesListTamanho;
    }

    // Capturando os jogos
    static Game[] inicializacao(String[] idArray, int tamanho) {
        // Array de jogos
        gamesList = new Game[NoArrayList.MAX_GAMES];
        gamesListTamanho = 0;

        // Copiando IDs para a variável de classe 'ids'
        ids = idArray;
        idsTamanho = tamanho;

        // Abrindo do arquivo
        InputStream is = null;
        try {
            java.io.File arquivo = new java.io.File("/tmp/games.csv");
            if (!arquivo.exists()) {
                System.out.println("Arquivo 'games.csv' não encontrado!");
                return gamesList;
            }
            is = new FileInputStream(arquivo);
        } catch (Exception e) {
            System.out.println("Erro ao abrir o arquivo: " + e.getMessage());
            return gamesList;
        }

        sc = new Scanner(is);
        // Pula cabeçalho
        if (sc.hasNextLine())
            sc.nextLine();

        // Pesquisa por id
        while (sc.hasNextLine() && idsTamanho > 0 && gamesListTamanho < gamesList.length) {
            String linha = sc.nextLine();
            // Resetando o contador para a nova linha
            contador = 0;
            // Capturando outras informações
            int id = capturaId(linha);

            int indiceEncontrado = igualId(id);
            if (indiceEncontrado != -1) {
                String name = capturaName(linha);
                String releaseDate = capturaReleaseDate(linha);
                int estimatedOwners = capturaEstimatedOwners(linha);
                float price = capturaPrice(linha);

                String[] supportedLanguages = new String[NoArrayList.MAX_INNER_ARRAY];
                int supportedLanguagesCount = capturaSupportedLanguages(linha, supportedLanguages);
                int metacriticScore = capturaMetacriticScore(linha);
                float userScore = capturaUserScore(linha);
                int achievements = capturaAchievements(linha);

                String[] publishers = new String[NoArrayList.MAX_INNER_ARRAY];
                int publishersCount = capturaUltimosArryays(linha, publishers);
                String[] developers = new String[NoArrayList.MAX_INNER_ARRAY];
                int developersCount = capturaUltimosArryays(linha, developers);
                String[] categories = new String[NoArrayList.MAX_INNER_ARRAY];
                int categoriesCount = capturaUltimosArryays(linha, categories);
                String[] genres = new String[NoArrayList.MAX_INNER_ARRAY];
                int genresCount = capturaUltimosArryays(linha, genres);
                String[] tags = new String[NoArrayList.MAX_INNER_ARRAY];
                int tagsCount = capturaUltimosArryays(linha, tags);

                // Adicionando a classe
                Game jogo = new Game(id, name, releaseDate, estimatedOwners, price,
                        supportedLanguages, supportedLanguagesCount, metacriticScore, userScore, achievements,
                        publishers, publishersCount, developers, developersCount, categories, categoriesCount, genres,
                        genresCount, tags, tagsCount);
                gamesList[gamesListTamanho++] = jogo;
                removerId(indiceEncontrado);
            }
        }
        sc.close();

        // Redimensiona o array final para o tamanho real, mantendo a lógica do seu
        // código original
        Game[] resultadoFinal = new Game[gamesListTamanho];
        for (int i = 0; i < gamesListTamanho; i++) {
            resultadoFinal[i] = gamesList[i];
        }
        gamesList = resultadoFinal;

        return resultadoFinal;
    }

    // Vendo se id é igual
    static int igualId(int id) {
        for (int i = 0; i < idsTamanho; i++) {
            if (Integer.parseInt(ids[i]) == id) {
                return i;
            }
        }
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