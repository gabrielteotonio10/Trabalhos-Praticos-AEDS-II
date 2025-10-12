package tp4;

import java.io.*;
import java.util.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;

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

    // Construtor padrão
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

    // Construtor completo
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

public class Chat {
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
        try {
            sc.close();
            is.close();
        } catch (IOException e) {
             // Ignorar ou logar
        }

        Scanner entrada = new Scanner(System.in);
        while (entrada.hasNextLine()) {
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
                    
                    // CORREÇÃO: Lógica de formatação condicional do preço
                    String priceFormatted;
                    
                    // Verifica se o preço tem valor 0.0 (deve ser formatado como 0.0)
                    if (encontrado.price == 0.0f) {
                        priceFormatted = "0.0";
                    } 
                    // Verifica se o preço tem a parte decimal .X0 (ex: 7.50 -> 7.5)
                    else if (Math.round(encontrado.price * 100.0f) % 10 == 0) {
                        priceFormatted = String.format(Locale.US, "%.1f", encontrado.price);
                    }
                    // Caso contrário, usa duas casas decimais (ex: 7.49, 19.99)
                    else {
                        priceFormatted = String.format(Locale.US, "%.2f", encontrado.price);
                    }
                    
                    // Tratamento N/A e formatação dos scores
                    String metacriticStr = encontrado.metacriticScore == -1 ? "N/A" : String.valueOf(encontrado.metacriticScore);
                    String userScoreStr = encontrado.userScore == -1.0f ? "N/A" : String.format(Locale.US, "%.1f", encontrado.userScore);
                    
                    // Imprimindo a saída no formato esperado
                    System.out.println("=> " + encontrado.id + " ## " + encontrado.name + " ## " +
                                       encontrado.releaseDate + " ## " + encontrado.estimatedOwners + " ## " +
                                       priceFormatted + " ## " + // USANDO O FORMATO CORRIGIDO
                                       formatarLista(encontrado.supportedLanguages) + " ## " +
                                       metacriticStr + " ## " +
                                       userScoreStr + " ## " +
                                       encontrado.achievements + " ## " +
                                       formatarLista(encontrado.publishers) + " ## " +
                                       formatarLista(encontrado.developers) + " ## " +
                                       formatarLista(encontrado.categories) + " ## " +
                                       formatarLista(encontrado.genres) + " ## " +
                                       formatarLista(encontrado.tags) + " ##");
                } else {
                    System.out.println("=> " + idBusca + " ## N/A");
                }
            } catch (NumberFormatException e) {
                // Entrada 'FIM' já é tratada
            }
        }
        entrada.close();
    }

    // === FUNÇÕES AUXILIARES ===

    public static String formatarLista(ArrayList<String> lista) {
        if (lista == null || lista.isEmpty()) return "[]";
        return "[" + String.join(", ", lista) + "]";
    }

    public static String lerCampo(String linha) {
        String campo = "";
        
        if (contador >= linha.length()) return "";

        if (linha.charAt(contador) == '"') {
            contador++; 
            while (contador < linha.length() && linha.charAt(contador) != '"') 
                campo += linha.charAt(contador++);
            contador++; 
            
            if (contador < linha.length() && linha.charAt(contador) == ',') 
                contador++; 
        } else {
            while (contador < linha.length() && linha.charAt(contador) != ',')
                campo += linha.charAt(contador++);
            
            if (contador < linha.length() && linha.charAt(contador) == ',') 
                contador++; 
        }
        return campo;
    }
    
    // --- Funções de Parsing Específicas com Normalização ---
    
    public static int lerId(String linha) {
        String num = lerCampo(linha);
        return Integer.parseInt(num.trim());
    }

    public static String lerName(String linha) {
        return lerCampo(linha).trim();
    }

    public static String lerDataLancamento(String linha) {
        String campo = lerCampo(linha);
        String data = campo.trim();
        
        if (data.startsWith("\"")) data = data.substring(1);
        if (data.endsWith("\"")) data = data.substring(0, data.length() - 1);
        
        // Separa por espaços e vírgulas
        String[] partes = data.split("[, ]+");
        
        String mesStr = "";
        String diaStr = "";
        String anoStr = "";

        // Lógica para extrair Mês, Dia e Ano de forma segura
        int idx = 0;
        if (partes.length > idx) mesStr = partes[idx++];
        
        // O dia pode estar no segundo ou terceiro elemento, dependendo se o Mês tem vírgula ou não
        if (partes.length > idx && partes[idx].matches("\\d+") && partes[idx].length() <= 2) {
             diaStr = partes[idx++];
        } else if (partes.length > idx + 1 && partes[idx+1].matches("\\d+") && partes[idx+1].length() <= 2) {
             // Caso o elemento atual seja algo como "18," (com vírgula colada)
             // Vamos ignorar este caso e focar na extração direta das partes válidas.
        }

        // O ano deve ser o último elemento de 4 dígitos.
        for (int i = partes.length - 1; i >= 0; i--) {
            if (partes[i].matches("\\d{4}")) {
                anoStr = partes[i];
                break;
            }
        }
        
        // Normalização
        if (diaStr.isEmpty()) diaStr = "1";
        if (anoStr.isEmpty()) anoStr = "0000";

        String dia = String.format("%02d", Integer.parseInt(diaStr));
        
        String m = switch (mesStr) {
            case "Jan" -> "01"; case "Feb" -> "02"; case "Mar" -> "03"; case "Apr" -> "04";
            case "May" -> "05"; case "Jun" -> "06"; case "Jul" -> "07"; case "Aug" -> "08";
            case "Sep" -> "09"; case "Oct" -> "10"; case "Nov" -> "11"; case "Dec" -> "12";
            default -> "01";
        };
        
        return dia + "/" + m + "/" + anoStr;
    }
    
    public static int lerEstimativaJogadores(String linha) {
        String num = lerCampo(linha);
        num = num.replaceAll("[^0-9]", "");
        return num.isEmpty() ? 0 : Integer.parseInt(num);
    }

    public static float lerPreco(String linha) {
        String preco = lerCampo(linha).trim();
        if (preco.equalsIgnoreCase("Free to Play") || preco.isEmpty()) return 0.0f;
        try { return Float.parseFloat(preco); } catch (Exception e) { return 0.0f; }
    }

    public static int lerNotaCritica(String linha) {
        String s = lerCampo(linha).trim();
        if (s.isEmpty()) return -1;
        try { return Integer.parseInt(s); } catch (Exception e) { return -1; }
    }

    public static float lerNotaUsuario(String linha) {
        String s = lerCampo(linha).trim();
        if (s.isEmpty() || s.equalsIgnoreCase("tbd")) return -1.0f;
        try { return Float.parseFloat(s); } catch (Exception e) { return -1.0f; }
    }

    public static int lerConquistas(String linha) {
        String s = lerCampo(linha).trim();
        if (s.isEmpty()) return 0;
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    public static ArrayList<String> lerCampoArray(String linha) {
        ArrayList<String> lista = new ArrayList<>();
        String campo = lerCampo(linha).trim();
        
        if (campo.isEmpty() || campo.equalsIgnoreCase("[]")) return lista;
        
        if (campo.startsWith("[")) campo = campo.substring(1);
        if (campo.endsWith("]")) campo = campo.substring(0, campo.length() - 1);
        
        String[] partes = campo.split(",");
        
        for (String s : partes) {
            String limpo = s.replace("'", "").replace("\"", "").trim();
            if (!limpo.isEmpty()) lista.add(limpo);
        }
        return lista;
    }

    public static ArrayList<String> lerIdiomas(String linha) { return lerCampoArray(linha); }
    public static ArrayList<String> lerPublishers(String linha) { return lerCampoArray(linha); }
    public static ArrayList<String> lerDevelopers(String linha) { return lerCampoArray(linha); }
    public static ArrayList<String> lerCategories(String linha) { return lerCampoArray(linha); }
    public static ArrayList<String> lerGenres(String linha) { return lerCampoArray(linha); }
    public static ArrayList<String> lerTags(String linha) { return lerCampoArray(linha); }
}