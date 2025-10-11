
package tp4;
import java.io.*;
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
    public static Scanner sc;
    public static int contador = 0;

    public static void main(String[] args) {
        InputStream is = null;
        try {
            is = new FileInputStream("/tmp/games.csv");
        } catch (Exception e) {
            System.out.println("Arquivo não encontrado!");
            return;
        }

        sc = new Scanner(is);
        if (sc.hasNextLine()) sc.nextLine(); // pula cabeçalho

        ArrayList<Game> gamesList = new ArrayList<>();

        while (sc.hasNextLine()) {
            String linha = sc.nextLine();
            if (!linha.trim().isEmpty() && Character.isDigit(linha.charAt(0))) {
                contador = 0;

                int id = lerId(linha);
                String name = lerName(linha);
                String releaseDate = lerDataLancamento(linha);
                int estimatedOwners = lerEstimativaJogadores(linha);
                float price = lerPreco(linha);
                ArrayList<String> supportedLanguages = lerIdiomas(linha);
                int metacriticScore = lerNotaCritica(linha);
                float userScore = lerNotaUsuario(linha);
                int achievements = lerConquistas(linha);
                ArrayList<String> publishers = lerPublishers(linha);
                ArrayList<String> developers = lerDevelopers(linha);
                ArrayList<String> categories = lerCategories(linha);
                ArrayList<String> genres = lerGenres(linha);
                ArrayList<String> tags = lerTags(linha);

                Game game = new Game(id, name, releaseDate, estimatedOwners, price,
                        supportedLanguages, metacriticScore, userScore, achievements,
                        publishers, developers, categories, genres, tags);
                gamesList.add(game);
            }
        }
        sc.close();

        Scanner entrada = new Scanner(System.in);
        while (true) {
            String linha = entrada.nextLine().trim();
            if (linha.equalsIgnoreCase("FIM")) break;

            try {
                int idBusca = Integer.parseInt(linha);
                Game encontrado = null;

                for (Game g : gamesList) {
                    if (g.id == idBusca) {
                        encontrado = g;
                        break;
                    }
                }

                if (encontrado != null) {
                    System.out.print("=> " + encontrado.id + " ## " + encontrado.name + " ## " +
                            encontrado.releaseDate + " ## " + encontrado.estimatedOwners + " ## " +
                            String.format("%.2f", encontrado.price) + " ## " + 
                            formatarLista(encontrado.supportedLanguages) + " ## " +
                            (encontrado.metacriticScore == -1 ? "N/A" : encontrado.metacriticScore) + " ## " +
                            (encontrado.userScore == -1.0f ? "N/A" : String.format("%.1f", encontrado.userScore)) + " ## " +
                            encontrado.achievements + " ## " +
                            formatarLista(encontrado.publishers) + " ## " +
                            formatarLista(encontrado.developers) + " ## " +
                            formatarLista(encontrado.categories) + " ## " +
                            formatarLista(encontrado.genres) + " ## " +
                            formatarLista(encontrado.tags) + " ##");
                    System.out.println();
                } else {
                    System.out.println("⇒ " + idBusca + " ## N/A");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número de ID ou 'FIM' para encerrar.");
            }
        }
        entrada.close();
    }

    // === FUNÇÕES AUXILIARES ===

    public static String formatarLista(ArrayList<String> lista) {
        if (lista.isEmpty()) return "[]";
        return "[" + String.join(", ", lista) + "]";
    }

    public static int lerId(String linha) {
        String num = "";
        while (contador < linha.length() && linha.charAt(contador) != ',')
            num += linha.charAt(contador++);
        contador++;
        return Integer.parseInt(num.trim());
    }

    public static String lerName(String linha) {
        String nome = "";
        if (contador < linha.length() && linha.charAt(contador) == '"') {
            contador++;
            while (contador < linha.length() && linha.charAt(contador) != '"') 
                nome += linha.charAt(contador++);
            contador++;
        } else {
            while (contador < linha.length() && linha.charAt(contador) != ',')
                nome += linha.charAt(contador++);
        }
        contador++;
        return nome.trim();
    }

    public static String lerDataLancamento(String linha) {
        String data = "";
        if (contador < linha.length() && linha.charAt(contador) == '"') {
            contador++;
            String mes = "";
            for (int i = 0; i < 3 && contador < linha.length(); i++) 
                mes += linha.charAt(contador++);
            contador++;
            String dia = "";
            while (contador < linha.length() && Character.isDigit(linha.charAt(contador)))
                dia += linha.charAt(contador++);
            contador += 2;
            String ano = "";
            while (contador < linha.length() && linha.charAt(contador) != '"')
                ano += linha.charAt(contador++);
            contador++;
            if (dia.equals("")) dia = "01";
            String m = switch (mes) {
                case "Jan" -> "01"; case "Feb" -> "02"; case "Mar" -> "03"; case "Apr" -> "04";
                case "May" -> "05"; case "Jun" -> "06"; case "Jul" -> "07"; case "Aug" -> "08";
                case "Sep" -> "09"; case "Oct" -> "10"; case "Nov" -> "11"; case "Dec" -> "12";
                default -> "01";
            };
            data = dia + "/" + m + "/" + ano;
        } else {
            while (contador < linha.length() && linha.charAt(contador) != ',')
                data += linha.charAt(contador++);
        }
        contador++;
        return data.trim();
    }

    public static int lerEstimativaJogadores(String linha) {
        String num = "";
        while (contador < linha.length() && linha.charAt(contador) != ',') {
            if (Character.isDigit(linha.charAt(contador)))
                num += linha.charAt(contador);
            contador++;
        }
        contador++;
        return num.isEmpty() ? 0 : Integer.parseInt(num);
    }

    public static float lerPreco(String linha) {
        String preco = "";
        while (contador < linha.length() && linha.charAt(contador) != ',')
            preco += linha.charAt(contador++);
        contador++;
        preco = preco.trim();
        if (preco.equalsIgnoreCase("Free to Play")) return 0.0f;
        if (preco.isEmpty()) return 0.0f;
        try { return Float.parseFloat(preco); } catch (Exception e) { return 0.0f; }
    }

    public static ArrayList<String> lerIdiomas(String linha) {
        ArrayList<String> idiomas = new ArrayList<>();
        if (contador < linha.length() && linha.charAt(contador) == '"') {
            contador++;
            String conteudo = "";
            while (contador < linha.length() && linha.charAt(contador) != '"')
                conteudo += linha.charAt(contador++);
            contador++;
            if (conteudo.contains("[")) {
                conteudo = conteudo.replace("[", "").replace("]", "");
                String[] partes = conteudo.split(",");
                for (String s : partes) {
                    String idioma = s.replace("'", "").replace("\"", "").trim();
                    if (!idioma.isEmpty()) idiomas.add(idioma);
                }
            } else {
                String idioma = conteudo.replace("'", "").replace("\"", "").trim();
                if (!idioma.isEmpty()) idiomas.add(idioma);
            }
        } else {
            String conteudo = "";
            while (contador < linha.length() && linha.charAt(contador) != ',')
                conteudo += linha.charAt(contador++);
            String idioma = conteudo.replace("'", "").replace("\"", "").trim();
            if (!idioma.isEmpty()) idiomas.add(idioma);
        }
        contador++;
        return idiomas;
    }

    public static int lerNotaCritica(String linha) {
        String s = "";
        while (contador < linha.length() && linha.charAt(contador) != ',')
            s += linha.charAt(contador++);
        contador++;
        s = s.trim();
        if (s.isEmpty()) return -1;
        try { return Integer.parseInt(s); } catch (Exception e) { return -1; }
    }

    public static float lerNotaUsuario(String linha) {
        String s = "";
        while (contador < linha.length() && linha.charAt(contador) != ',')
            s += linha.charAt(contador++);
        contador++;
        s = s.trim();
        if (s.isEmpty() || s.equalsIgnoreCase("tbd")) return -1.0f;
        try { return Float.parseFloat(s); } catch (Exception e) { return -1.0f; }
    }

    public static int lerConquistas(String linha) {
        String s = "";
        while (contador < linha.length() && linha.charAt(contador) != ',')
            s += linha.charAt(contador++);
        contador++;
        s = s.trim();
        if (s.isEmpty()) return 0;
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    public static ArrayList<String> lerPublishers(String linha) {
        return lerCampoArray(linha);
    }

    public static ArrayList<String> lerDevelopers(String linha) {
        return lerCampoArray(linha);
    }

    public static ArrayList<String> lerCategories(String linha) {
        return lerCampoArray(linha);
    }

    public static ArrayList<String> lerGenres(String linha) {
        return lerCampoArray(linha);
    }

    public static ArrayList<String> lerTags(String linha) {
        return lerCampoArray(linha);
    }

    public static ArrayList<String> lerCampoArray(String linha) {
        ArrayList<String> lista = new ArrayList<>();
        if (contador >= linha.length()) return lista;
        
        if (linha.charAt(contador) == '"') {
            contador++;
            String campo = "";
            while (contador < linha.length() && linha.charAt(contador) != '"')
                campo += linha.charAt(contador++);
            contador++;
            for (String s : campo.split(",")) {
                String limpo = s.trim();
                if (!limpo.isEmpty()) lista.add(limpo);
            }
        } else {
            String campo = "";
            while (contador < linha.length() && linha.charAt(contador) != ',')
                campo += linha.charAt(contador++);
            String limpo = campo.trim();
            if (!limpo.isEmpty()) lista.add(limpo);
        }
        if (contador < linha.length() && linha.charAt(contador) == ',') contador++;
        return lista;
    }
}