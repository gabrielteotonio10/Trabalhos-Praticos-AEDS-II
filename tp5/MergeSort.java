import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;

class NoArrayListM {
    public static final int MAX_GAMES = 500;
    public static final int MAX_INNER_ARRAY = 50;
    public static final int MAX_IDS = 100;
}

class GameM {
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

    GameM() {
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

    GameM(int id, String name, String releaseDate, int estimatedOwners, float price,
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

public class MergeSort {
    static long comparacoes = 0;
    static long movimentacoes = 0;
    // Scanner
    public static Scanner sc;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        String entrada = sc.nextLine();
        String[] ids = new String[NoArrayListM.MAX_IDS];
        int idsTamanho = 0;

        while (!entrada.equals("FIM") && idsTamanho < ids.length) {
            ids[idsTamanho++] = entrada;
            entrada = sc.nextLine();
        }

        GameM[] gamesList = JogosDigitadosM.inicializacao(ids, idsTamanho);
        int gamesListTamanho = JogosDigitadosM.obterTamanhoGamesList();

        long startTime = System.nanoTime();
        ordenacaoMergeSort(gamesList, 0, gamesListTamanho - 1);
        long endTime = System.nanoTime();
        long tempoExecucao = (endTime - startTime) / 1000000;

        printandoCaros(gamesList, gamesListTamanho);
        System.out.println("");
        printandoBaratos(gamesList, gamesListTamanho);

        try {
            FileWriter fw = new FileWriter("885732_mergesort.txt");
            PrintWriter pw = new PrintWriter(fw);
            pw.println("885732" + "\t" + comparacoes + "\t" + movimentacoes + "\t" + tempoExecucao);
            pw.close();
        } catch (Exception e) {
            System.out.println("Erro ao escrever o arquivo de log: " + e.getMessage());
        }

        sc.close();
    }

    static void ordenacaoMergeSort(GameM gameList[], int esq, int dir) {
        if (esq < dir) {
            int meio = (esq + dir) / 2;
            ordenacaoMergeSort(gameList, esq, meio);
            ordenacaoMergeSort(gameList, meio + 1, dir);
            intercalar(gameList, esq, meio, dir);
        }
    }

    static void intercalar(GameM gameList[], int esq, int meio, int dir) {
        int n1 = meio - esq + 1;
        int n2 = dir - meio;
        GameM a1[] = new GameM[n1 + 1];
        GameM a2[] = new GameM[n2 + 1];

        for (int i = 0; i < n1; i++) {
            a1[i] = gameList[esq + i];
        }
        for (int j = 0; j < n2; j++) {
            a2[j] = gameList[meio + j + 1];
        }

        a1[n1] = new GameM();
        a2[n2] = new GameM();
        a1[n1].price = a2[n2].price = Float.MAX_VALUE;
        a1[n1].id = a2[n2].id = Integer.MAX_VALUE;

        for (int i = 0, j = 0, k = esq; k <= dir; k++) {
            comparacoes++;
            if (menor(a1[i], a2[j])) {
                gameList[k] = a1[i++];
                movimentacoes++;
            } else {
                gameList[k] = a2[j++];
                movimentacoes++;
            }
        }
    }

    static boolean menor(GameM a1, GameM a2) {
        comparacoes++;
        if (a1.price < a2.price)
            return true;
        comparacoes++;
        if (a1.price == a2.price && a1.id < a2.id)
            return true;
        return false;
    }

    // Printando mais caros
    static void printandoCaros(GameM[] jogosOrdenados, int tamanho) {
        System.out.println("| 5 preços mais caros |");
        for (int i = tamanho - 1; i > (tamanho - 6); i--) {
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

    // Printando de forma ordenada
    static void printandoBaratos(GameM[] jogosOrdenados, int tamanho) {
        System.out.println("| 5 preços mais baratos |");
        for (int i = 0; i < 5; i++) {
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

    // Printando o Array (adaptado)
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
    static GameM[] gamesList;
    static int gamesListTamanho;

    // Função para obter o tamanho final da lista de jogos
    public static int obterTamanhoGamesList() {
        return gamesListTamanho;
    }

    // Capturando os jogos
    static GameM[] inicializacao(String[] idArray, int tamanho) {
        // Array de jogos
        // CORRIGIDO: Era NoArrayList, agora é NoArrayListM
        gamesList = new GameM[NoArrayListM.MAX_GAMES];
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

                // CORRIGIDO: Era NoArrayList, agora é NoArrayListM
                String[] supportedLanguages = new String[NoArrayListM.MAX_INNER_ARRAY];
                int supportedLanguagesCount = capturaSupportedLanguages(linha, supportedLanguages);
                int metacriticScore = capturaMetacriticScore(linha);
                float userScore = capturaUserScore(linha);
                int achievements = capturaAchievements(linha);

                // CORRIGIDO: Era NoArrayList, agora é NoArrayListM
                String[] publishers = new String[NoArrayListM.MAX_INNER_ARRAY];
                int publishersCount = capturaUltimosArryays(linha, publishers);
                // CORRIGIDO: Era NoArrayList, agora é NoArrayListM
                String[] developers = new String[NoArrayListM.MAX_INNER_ARRAY];
                int developersCount = capturaUltimosArryays(linha, developers);
                // CORRIGIDO: Era NoArrayList, agora é NoArrayListM
                String[] categories = new String[NoArrayListM.MAX_INNER_ARRAY];
                int categoriesCount = capturaUltimosArryays(linha, categories);
                // CORRIGIDO: Era NoArrayList, agora é NoArrayListM
                String[] genres = new String[NoArrayListM.MAX_INNER_ARRAY];
                int genresCount = capturaUltimosArryays(linha, genres);
                // CORRIGIDO: Era NoArrayList, agora é NoArrayListM
                String[] tags = new String[NoArrayListM.MAX_INNER_ARRAY];
                int tagsCount = capturaUltimosArryays(linha, tags);

                // Adicionando a classe
                GameM jogo = new GameM(id, name, releaseDate, estimatedOwners, price,
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
        GameM[] resultadoFinal = new GameM[gamesListTamanho];
        for (int i = 0; i < gamesListTamanho; i++) {
            resultadoFinal[i] = gamesList[i];
        }
        gamesList = resultadoFinal;

        return resultadoFinal;
    }

    // ... restante da classe JogosDigitadosM (sem mudanças)
    // ... Vendo se id é igual
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