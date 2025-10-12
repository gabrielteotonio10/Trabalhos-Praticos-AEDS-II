import java.lang.classfile.instruction.SwitchCase;
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
    static int contador = 0;
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (sc.hasNextLine()) {
            contador = 0;
            String entrada = sc.nextLine();
            if (!entrada.trim().isEmpty() && Character.isDigit(entrada.charAt(0))) {
                int id = capturaId(entrada);
                String name = capturaName(entrada);
                String releaseDate = capturaReleaseDate(entrada);
                int estimatedOwners = capturaEstimatedOwners(entrada);
                float price = capturaPrice(entrada);
                ArrayList<String> supportedLanguages = capturaSupportedLanguages(entrada);
                int metacriticScore = capturaMetacriticScore(entrada);
                float userScore = capturaUserScore(entrada);
                float achievements = capturaAchievements(entrada);

                // Testes
                System.out.println(id);
                System.out.println(name);
                System.out.println(releaseDate);
                System.out.println(estimatedOwners);
                System.out.println(price);
                System.out.print("[");
                for (int i = 0; i < supportedLanguages.size(); i++) {
                    System.out.print(supportedLanguages.get(i));
                    if (!(i == supportedLanguages.size() - 1)) {
                        System.out.print(", ");
                    }
                }
                System.out.println("]");
                System.out.println(metacriticScore);
                System.out.println(userScore);
                System.out.println(achievements);
            }

        }
        sc.close();
    }

    // Capturando Id
    static int capturaId(String jogo) {
        int id = 0;
        while (Character.isDigit(jogo.charAt(contador))) {
            id = id * 10 + (jogo.charAt(contador) - '0');
            contador++;
        }
        return id;
    }

    // Capturando nome
    static String capturaName(String jogo) {
        String name = "";
        while (jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        while (jogo.charAt(contador) != ',') {
            name += jogo.charAt(contador);
            contador++;
        }
        return name;
    }

    // Capturando Release Date
    static String capturaReleaseDate(String jogo) {
        while (jogo.charAt(contador) != '"') {
            contador++;
        }
        contador++;
        String dia = "", mes = "", ano = "";
        // Pegando mês
        for (int i = 0; i < 3; i++) {
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
        while (!Character.isDigit(jogo.charAt(contador)) && jogo.charAt(contador) != ',') {
            contador++;
        }
        // Pegando dia
        while (Character.isDigit(jogo.charAt(contador))) {
            dia += jogo.charAt(contador);
            contador++;
        }
        // Pulando espaço
        while (!Character.isDigit(jogo.charAt(contador))) {
            contador++;
        }
        // Pegando ano
        while (jogo.charAt(contador) != '"') {
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
        while (jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        while (jogo.charAt(contador) != ',') {
            estimatedOwners = estimatedOwners * 10 + (jogo.charAt(contador) - '0');
            contador++;
        }
        return estimatedOwners;
    }

    // Capturando Price
    static float capturaPrice(String jogo) {
        String price = "";
        while (jogo.charAt(contador) != 'F' && !Character.isDigit(jogo.charAt(contador))) {
            contador++;
        }
        while (jogo.charAt(contador) != ',') {
            price += jogo.charAt(contador);
            contador++;
        }
        if (price.equals("Free to play"))
            return 0.0f;
        else
            return Float.parseFloat(price);
    }

    // Capturano idiomas
    static ArrayList capturaSupportedLanguages(String jogo) {
        ArrayList<String> supportedLanguages = new ArrayList<>();
        while (jogo.charAt(contador) != ']') {
            String lingua = "";
            while (!Character.isAlphabetic(jogo.charAt(contador))) {
                contador++;
            }
            while (jogo.charAt(contador) != ',' && jogo.charAt(contador) != ']') {
                if (Character.isAlphabetic(jogo.charAt(contador)) || jogo.charAt(contador) == ' ') {
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
        while (jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        while (Character.isDigit(jogo.charAt(contador))) {
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
        while (jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        while (Character.isDigit(jogo.charAt(contador)) || jogo.charAt(contador) == '.') {
            userScore += jogo.charAt(contador);
            contador++;
        }
        if (userScore.isEmpty())
            return -1.0f;
        else
            return Float.parseFloat(userScore);
    }

    // Capturando User Score
    static float capturaAchievements(String jogo) {
        String achievements = "";
        while (jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        while (Character.isDigit(jogo.charAt(contador)) || jogo.charAt(contador) == '.') {
            achievements += jogo.charAt(contador);
            contador++;
        }
        if (achievements.isEmpty())
            return -1.0f;
        else
            return Float.parseFloat(achievements);
    }
}
