
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

class Game {
    int id;
    String name;
    String releaseDate;
    int estimatedOwners;
    float price;
    ArrayList<String> supportedLanguages;
    int metacriticScore;
    float userScore;
    int achievements;
    ArrayList<String> publishers;
    ArrayList<String> developers;
    ArrayList<String> categories;
    ArrayList<String> genres;
    ArrayList<String> tags;

    Game() {
        this.id = 0;
        this.name = "";
        this.releaseDate = "";
        this.estimatedOwners = 0;
        this.price = 0.0f;
        this.supportedLanguages = new ArrayList<>();
        this.metacriticScore = -1;
        this.userScore = -1.0f;
        this.achievements = 0;
        this.publishers = new ArrayList<>();
        this.developers = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.genres = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    Game(int id, String name, String releaseDate, int estimatedOwners, float price,
            ArrayList<String> supportedLanguages, int metacriticScore, float userScore, int achievements,
            ArrayList<String> publishers, ArrayList<String> developers,
            ArrayList<String> categories, ArrayList<String> genres, ArrayList<String> tags) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.estimatedOwners = estimatedOwners;
        this.price = price;
        this.supportedLanguages = supportedLanguages;
        this.metacriticScore = metacriticScore;
        this.userScore = userScore;
        this.achievements = achievements;
        this.publishers = publishers;
        this.developers = developers;
        this.categories = categories;
        this.genres = genres;
        this.tags = tags;
    }
}

public class t {
    // Scanner
    public static Scanner sc;

    public static void main(String[] args) {
        // Pegando entrada
        sc = new Scanner(System.in);
        String entrada;
        entrada = sc.nextLine();
        // Criando um ArrayList dos ids de entrada
        ArrayList<String> ids = new ArrayList<>();
        // Capturando os ids de entrada
        while (!entrada.equals("FIM")) {
            ids.add(entrada);
            entrada = sc.nextLine();
        }
        // Criando o ArrayList apenas com os jogos digitados
        ArrayList<Game> gamesList = JogosDigitados.inicializacao(ids);
        // Ordenando
        ordenacaoHeapSort(gamesList);
        // Printando o Array
        printando(gamesList);
        // Fechando o scanner
        sc.close();
    }

public static boolean isMaior(Game a, Game b) {
    if (a.estimatedOwners != b.estimatedOwners) {
        return a.estimatedOwners < b.estimatedOwners; // Mudança aqui: < em vez de >
    }
    return a.id < b.id;
}

    public static void ordenacaoHeapSort(ArrayList<Game> gameList) {
        int n = gameList.size();
        
        // Criar array temporário com índice 1-based
        ArrayList<Game> tmp = new ArrayList<>(n + 1);
        tmp.add(null); // Posição 0 (nula)
        for (int i = 0; i < n; i++) {
            tmp.add(gameList.get(i));
        }

        // Construção do heap
        for (int i = n / 2; i >= 1; i--) {
            heapify(tmp, n, i);
        }

        // Ordenação propriamente dita
        for (int i = n; i > 1; i--) {
            swap(1, i, tmp);
            heapify(tmp, i - 1, 1);
        }
        
        // Copiar de volta para o array original
        gameList.clear();
        for (int i = 1; i <= n; i++) {
            gameList.add(tmp.get(i));
        }
    }

    private static void heapify(ArrayList<Game> arr, int n, int i) {
        int largest = i;
        int left = 2 * i;
        int right = 2 * i + 1;

        // Verificar filho esquerdo
        if (left <= n && isMaior(arr.get(left), arr.get(largest))) {
            largest = left;
        }

        // Verificar filho direito
        if (right <= n && isMaior(arr.get(right), arr.get(largest))) {
            largest = right;
        }

        // Se o maior não é a raiz, trocar e continuar heapificando
        if (largest != i) {
            swap(i, largest, arr);
            heapify(arr, n, largest);
        }
    }

    public static void swap(int i, int j, ArrayList<Game> gameList) {
        Game temp = gameList.get(i);
        gameList.set(i, gameList.get(j));
        gameList.set(j, temp);
    }

    // Printando de forma ordenada
    static void printando(ArrayList<Game> jogosOrdenados) {
        for (int i = 0; i < jogosOrdenados.size(); i++) {
            String releaseDate = (jogosOrdenados.get(i).releaseDate.charAt(1) == '/'
                    ? "0" + jogosOrdenados.get(i).releaseDate
                    : jogosOrdenados.get(i).releaseDate);
            System.out.println(
                    "=> " + jogosOrdenados.get(i).id + " ## " + jogosOrdenados.get(i).name + " ## " + releaseDate
                            + " ## " + jogosOrdenados.get(i).estimatedOwners + " ## " + jogosOrdenados.get(i).price
                            + " ## " + printArray(jogosOrdenados.get(i).supportedLanguages)
                            + " ## " + jogosOrdenados.get(i).metacriticScore + " ## " + jogosOrdenados.get(i).userScore
                            + " ## " + jogosOrdenados.get(i).achievements
                            + " ## " + printArray(jogosOrdenados.get(i).publishers)
                            + " ## " + printArray(jogosOrdenados.get(i).developers)
                            + " ## " + printArray(jogosOrdenados.get(i).categories)
                            + " ## " + printArray(jogosOrdenados.get(i).genres)
                            + " ## " + printArray(jogosOrdenados.get(i).tags)
                            + " ##");
        }
    }

    // Printando o ArrayList
    static String printArray(ArrayList<String> teste) {
        StringBuilder sb = new StringBuilder();
        if (teste.size() > 0)
            sb.append("[");
        for (int i = 0; i < teste.size(); i++) {
            sb.append(teste.get(i));
            if (i < teste.size() - 1)
                sb.append(", ");
        }
        if (teste.size() > 0)
            sb.append("]");
        return sb.toString();
    }

}

// O restante do código da classe JogosDigitados permanece EXATAMENTE igual
class JogosDigitados {
    // Scanner
    public static Scanner sc;
    // Variável que pula caracteres das linhas
    static int contador = 0;
    // Ids de pesquisa
    static ArrayList<String> ids = new ArrayList<>();

    // Capturando os jogos
    static ArrayList<Game> inicializacao(ArrayList<String> idArray) {
        // Array
        ArrayList<Game> gamesList = new ArrayList<>();
        // Copiando IDs para a variável de classe 'ids'
        ids.clear();
        ids.addAll(idArray);
        // Abrindo do arquivo
        InputStream is = null;
        try {
            is = new FileInputStream("/tmp/games.csv");
        } catch (Exception e) {
            System.out.println("Arquivo não encontrado!");
            return gamesList;
        }
        sc = new Scanner(is);
        // Pula cabeçalho
        if (sc.hasNextLine())
            sc.nextLine();
        // Pesquisa por id
        while (sc.hasNextLine() && ids.size() > 0) {
            String linha = sc.nextLine();
            // Resetando o contador para a nova linha
            contador = 0; 
            // Capturando outras informações
            int id = capturaId(linha);
            if (igualId(id)) {
                String name = capturaName(linha);
                String releaseDate = capturaReleaseDate(linha);
                int estimatedOwners = capturaEstimatedOwners(linha);
                float price = capturaPrice(linha);
                ArrayList<String> supportedLanguages = capturaSupportedLanguages(linha);
                int metacriticScore = capturaMetacriticScore(linha);
                float userScore = capturaUserScore(linha);
                int achievements = capturaAchievements(linha);
                ArrayList<String> publishers = capturaUltimosArryays(linha);
                ArrayList<String> developers = capturaUltimosArryays(linha);
                ArrayList<String> categories = capturaUltimosArryays(linha);
                ArrayList<String> genres = capturaUltimosArryays(linha);
                ArrayList<String> tags = capturaUltimosArryays(linha);
                // Adicionando a classe
                Game jogo = new Game(id, name, releaseDate, estimatedOwners, price,
                        supportedLanguages, metacriticScore, userScore, achievements,
                        publishers, developers, categories, genres, tags);
                gamesList.add(jogo);
            }
        }
        return gamesList;
    }

    // Vendo se id é igual
    static boolean igualId(int id) {
        for (int i = 0; i < ids.size(); i++) {
            if (Integer.parseInt(ids.get(i)) == id) {
                ids.remove(i);
                return true;
            }
        }
        return false;
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
    static ArrayList<String> capturaSupportedLanguages(String jogo) {
        ArrayList<String> supportedLanguages = new ArrayList<>();
        while (contador < jogo.length() && jogo.charAt(contador) != ']') {
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
            supportedLanguages.add(lingua);
        }
        return supportedLanguages;
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

    // Capturando Últimos Arrays [505 Games]
    static ArrayList<String> capturaUltimosArryays(String jogo) {
        ArrayList<String> categoria = new ArrayList<>();
        boolean teste = false;
        while (contador < jogo.length() && !Character.isAlphabetic(jogo.charAt(contador))
                && !Character.isDigit(jogo.charAt(contador))) {
            if (jogo.charAt(contador) == '"')
                teste = true;
            contador++;
        }
        if (teste) {
            while (contador < jogo.length() && jogo.charAt(contador) != '"') {
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
                categoria.add(parte);
            }
        } else {
            String parte = "";
            while (contador < jogo.length() && jogo.charAt(contador) != ',') {
                parte += jogo.charAt(contador);
                contador++;
            }
            categoria.add(parte);
        }
        return categoria;
    }
}