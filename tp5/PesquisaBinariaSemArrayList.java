import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

class GamePesquisado {
    String name;
    GamePesquisado() {
        this.name = "";
    }
    GamePesquisado(String name) {
        this.name = name;
    }
}

public class PesquisaBinariaSemArrayList {
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Criando vetor geral
        String linha;
        int tamanho = 0;
        String ids[] = new String[0];
        linha = sc.nextLine();
        while (!linha.equals("FIM")) {
            tamanho++;
            String aux[] = ids;
            ids = new String[tamanho];
            for(int i = 0; i < tamanho - 1; i++){
                ids[i] = aux[i];
            }
            ids[tamanho - 1] = linha;
            aux = null;
            linha = sc.nextLine();
        }
        // Criando objeto com ids digitados
        GamePesquisado gamesList[] = CriandoObjetos.objetos(tamanho, ids);
        // Oordenando o ArrayList
        if (!gamesList.equals(null))
            oordenando(gamesList, 0, gamesList.length - 1);
        // Vendo se os nomes digitados são presentes
        linha = sc.nextLine();
        while (!linha.equals("FIM")) {
            boolean achou = false;
            int dir = gamesList.length - 1, esq = 0, meio = 0;
            while (esq <= dir) {
                meio = (esq + dir) / 2;
                if (linha.equals(gamesList[meio].name)) {
                    achou = true;
                    esq = dir + 1;
                } else if (linha.compareTo(gamesList[meio].name) > 0) {
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
    static void oordenando(GamePesquisado gameList[], int esq, int dir) {
        int i = esq, j = dir;
        int meio = (dir + esq) / 2;
        String pivo = gameList[meio].name;
        while (i <= j) {
            while (gameList[i].name.compareTo(pivo) < 0)
                i++;
            while (gameList[j].name.compareTo(pivo) > 0)
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
    static void swap(GamePesquisado gameList[], int i, int j) {
        GamePesquisado aux = gameList[i];
        gameList[i] = gameList [j];
        gameList[j] = aux;
    }
}

class CriandoObjetos {
    // Variável que pula caracteres das linhas
    static int contador = 0;
    // Scanner
    public static Scanner sc = new Scanner(System.in);

    static GamePesquisado[] objetos(int tamanho, String ids[]) {
        GamePesquisado gameList[] = new GamePesquisado[tamanho];
        // Abrindo do arquivo
        InputStream is = null;
        try {
            java.io.File arquivo = new java.io.File("/tmp/games.csv");
            if (!arquivo.exists()) {
                System.out.println("Arquivo 'games.csv' não encontrado na pasta do projeto!");
                return gameList;
            }
            is = new FileInputStream(arquivo);
        } catch (Exception e) {
            System.out.println("Erro ao abrir o arquivo: " + e.getMessage());
            return gameList;
        }

        sc = new Scanner(is);
        // Pula cabeçalho
        if (sc.hasNextLine())
            sc.nextLine();
        // Pesquisa por id
        int cont = 0;
        while (sc.hasNextLine() && ids.length > 0) {
            String linha = sc.nextLine();
            // Capturando outras informações
            int id = capturaId(linha);
            String name = capturaName(linha);
            if (igualId(ids,id)) {
                GamePesquisado jogo = new GamePesquisado(name);
                gameList[cont] = jogo;
                cont++;
            }
            contador = 0;
        }
        sc.close();
        return gameList;
    }

    // Vendo se id é igual
    static boolean igualId(String ids[], int id) {
        for (int i = 0; i < ids.length; i++) {
            if (Integer.parseInt(ids[i]) == id) {
                String aux[] = new String[ids.length - 1];
                int cont = 0;
                for(int j = 0; j < ids.length; j++){
                    if(j != i){
                        aux[cont] = ids[j];
                        cont++;
                    }
                }
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
