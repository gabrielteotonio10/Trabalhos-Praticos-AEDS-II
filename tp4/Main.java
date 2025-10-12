package tp4;
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

public class Main {
    // Variável que pula caracteres das linhas
    static int contador = 0;
    // Scanner
    public static Scanner sc;

    public static void main(String[] args) {
        // Abrindo do arquivo
        InputStream is = null;
        try {
            is = new FileInputStream("/tmp/games.csv");
        } catch (Exception e) {
            System.out.println("Arquivo não encontrado!");
            return;
        }
        sc = new Scanner(is);
        // Pula cabeçalho
        if (sc.hasNextLine())
            sc.nextLine();
        // Array
        ArrayList<Game> gamesList = new ArrayList<>();
        // Pesquisa por id
        while (sc.hasNextLine()) {
            String linha = sc.nextLine();
            // Capturando outras informações
            int id = capturaId(linha);
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
            contador = 0;
        }

        // Pegando entrada
        sc = new Scanner(System.in);
        String entrada;
        entrada = sc.nextLine();
        while (!entrada.equals("FIM")) {
            // Pegando posição da entrada no array
            Game jogo = new Game();
            for (int i = 0; i < gamesList.size(); i++) {
                if (gamesList.get(i).id == Integer.parseInt(entrada)) {
                    jogo = gamesList.get(i);
                    i = gamesList.size();
                }
            }
            // Printando entrada
            String releaseDate = (jogo.releaseDate.charAt(1) == '/' ? "0" + jogo.releaseDate : jogo.releaseDate);
            System.out.println("=> " + jogo.id + " ## " + jogo.name + " ## " + releaseDate
                    + " ## " + jogo.estimatedOwners + " ## " + jogo.price
                    + " ## " + printArray(jogo.supportedLanguages)
                    + " ## " + jogo.metacriticScore + " ## " + jogo.userScore
                    + " ## " + jogo.achievements
                    + " ## " + printArray(jogo.publishers)
                    + " ## " + printArray(jogo.developers)
                    + " ## " + printArray(jogo.categories)
                    + " ## " + printArray(jogo.genres)
                    + " ## " + printArray(jogo.tags)
                    + " ##");

            // Lendo novamente
            entrada = sc.nextLine();
        }
        sc.close();
    }

    // Printando o ArrayList
    static String printArray(ArrayList<String> teste) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < teste.size(); i++) {
            sb.append(teste.get(i));
            if (i < teste.size() - 1)
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
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

    // Capturando User Score
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
    static ArrayList<String> capturaUltimosArryays(String jogo) {
        ArrayList<String> categoria = new ArrayList<>();
        boolean teste = false;
        while (contador < jogo.length() && !Character.isAlphabetic(jogo.charAt(contador))) {
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