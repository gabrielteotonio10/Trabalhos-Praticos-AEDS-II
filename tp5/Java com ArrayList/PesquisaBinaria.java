import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

class GamePesquisado {
    String name;
    GamePesquisado() {
        this.name = "";
    }
    GamePesquisado(String name) {
        this.name = name;
    }
}

public class PesquisaBinaria {
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Criando vetor geral
        String linha;
        linha = sc.nextLine();
        ArrayList<String> ids = new ArrayList<>();
        while (!linha.equals("FIM")) {
            ids.add(linha);
            linha = sc.nextLine();
        }
        // Criando objeto com ids digitados
        ArrayList<GamePesquisado> gamesList = CriandoObjetos.objetos(ids);
        // Oordenando o ArrayList
        if (!gamesList.isEmpty())
            oordenando(gamesList, 0, gamesList.size() - 1);
        // Vendo se os nomes digitados são presentes
        linha = sc.nextLine();
        while (!linha.equals("FIM")) {
            boolean achou = false;
            int dir = gamesList.size() - 1, esq = 0, meio = 0;
            while (esq <= dir) {
                meio = (esq + dir) / 2;
                if (linha.equals(gamesList.get(meio).name)) {
                    achou = true;
                    esq = dir + 1;
                } else if (linha.compareTo(gamesList.get(meio).name) > 0) {
                    esq = meio + 1;
                } else {
                    dir = meio - 1;
                }
            }
            if (achou)
                System.out.println(" SIM");
            else
                System.out.println(" NAO");
            linha = sc.nextLine();
        }
        // Fechando o scanner
        sc.close();
    }

    // Oordenando o array de String com QuickSort
    static void oordenando(ArrayList<GamePesquisado> gameList, int esq, int dir) {
        int i = esq, j = dir;
        int meio = (dir + esq) / 2;
        String pivo = gameList.get(meio).name;
        while (i <= j) {
            while (gameList.get(i).name.compareTo(pivo) < 0)
                i++;
            while (gameList.get(j).name.compareTo(pivo) > 0)
                j--;
            if (i <= j) {
                swap(gameList, i, j);
                i++;
                j--;
            }
        }
        if (esq < j)
            oordenando(gameList, esq, j);
        if (i < dir)
            oordenando(gameList, i, dir);
    }

    // Trocando posições
    static void swap(ArrayList<GamePesquisado> gameList, int i, int j) {
        GamePesquisado aux = gameList.get(i);
        gameList.set(i, gameList.get(j));
        gameList.set(j, aux);
    }
}

class CriandoObjetos {
    // Variável que pula caracteres das linhas
    static int contador = 0;
    // Scanner
    public static Scanner sc = new Scanner(System.in);
    // Ids de pesquisa
    static ArrayList<String> ids = new ArrayList<>();

    static ArrayList<GamePesquisado> objetos(ArrayList<String> idArray) {
        ids = idArray;
        // Array
        ArrayList<GamePesquisado> gamesList = new ArrayList<>();
        // Abrindo do arquivo
        InputStream is = null;
        try {
            java.io.File arquivo = new java.io.File("/tmp/games.csv");
            if (!arquivo.exists()) {
                System.out.println("Arquivo 'games.csv' não encontrado na pasta do projeto!");
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
        while (sc.hasNextLine() && ids.size() > 0) {
            String linha = sc.nextLine();
            // Capturando outras informações
            int id = capturaId(linha);
            String name = capturaName(linha);
            if (igualId(id)) {
                GamePesquisado jogo = new GamePesquisado(name);
                gamesList.add(jogo);
            }
            // Adicionando a classe
            contador = 0;
        }
        sc.close();
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
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            name += jogo.charAt(contador);
            contador++;
        }
        return name;
    }
}
