import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

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
    // Variáveis para o log
    private static final String MATRICULA = "885732";
    private static long tempoExecucao = 0;
    private static int numComparacoes = 0;

    public static void main(String[] args) {
        long inicio = System.currentTimeMillis();

        // Criando vetor geral
        String linha;
        int tamanho = 0;
        String ids[] = new String[0];
        linha = sc.nextLine();
        while (!linha.equals("FIM")) {
            tamanho++;
            String aux[] = ids;
            ids = new String[tamanho];
            for (int i = 0; i < tamanho - 1; i++) {
                ids[i] = aux[i];
            }
            ids[tamanho - 1] = linha;
            aux = null;
            linha = sc.nextLine();
        }
        // Criando objeto com ids digitados
        GamePesquisado gamesList[] = CriandoObjetos.objetos(tamanho, ids);

        // Ordenando o vetor
        if (gamesList != null) {
            oordenando(gamesList, 0, gamesList.length - 1);
        }

        // Vendo se os nomes digitados são presentes
        linha = sc.nextLine();
        while (!linha.equals("FIM")) {
            boolean achou = false;
            int dir = gamesList.length - 1, esq = 0, meio = 0;
            while (esq <= dir) {
                meio = (esq + dir) / 2;
                numComparacoes++; 
                if (linha.equals(gamesList[meio].name)) {
                    achou = true;
                    esq = dir + 1;
                } else {
                    numComparacoes++; 
                    if (linha.compareTo(gamesList[meio].name) > 0) {
                        esq = meio + 1;
                    } else {
                        dir = meio - 1;
                    }
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

        // Cálculo e gravação do log
        long fim = System.currentTimeMillis();
        tempoExecucao = fim - inicio;
        escreverLog();
    }


    private static void escreverLog() {
        try (FileWriter logWriter = new FileWriter(MATRICULA + "_binaria.txt")) {
            String logLine = MATRICULA + "\t" + tempoExecucao + "\t" + numComparacoes + "\n";
            logWriter.write(logLine);
        } catch (IOException e) {
            // Em um ambiente real, você faria um tratamento de erro mais robusto
            System.err.println("Erro ao escrever o arquivo de log: " + e.getMessage());
        }
    }

    // Oordenando o array de GamePesquisado com QuickSort (pela chave name)
    static void oordenando(GamePesquisado gameList[], int esq, int dir) {
        int i = esq, j = dir;
        int meio = (dir + esq) / 2;
        String pivo = gameList[meio].name;
        while (i <= j) {

            while (gameList[i].name.compareTo(pivo) < 0) {
                // numComparacoes++; // Descomente para contar comparações da ordenação
                i++;
            }
            while (gameList[j].name.compareTo(pivo) > 0) {
                // numComparacoes++; // Descomente para contar comparações da ordenação
                j--;
            }
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
        gameList[i] = gameList[j];
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
                System.out.println("Arquivo 'games.csv' não encontrado em /tmp!");
                return new GamePesquisado[0]; // Retorna vetor vazio se o arquivo não existir
            }
            is = new FileInputStream(arquivo);
        } catch (Exception e) {
            System.out.println("Erro ao abrir o arquivo: " + e.getMessage());
            return new GamePesquisado[0]; // Retorna vetor vazio em caso de erro
        }

        Scanner scArquivo = new Scanner(is); // Usa um Scanner local para o arquivo
        // Pula cabeçalho
        if (scArquivo.hasNextLine())
            scArquivo.nextLine();
        // Pesquisa por id
        int cont = 0;
        int idProcurado;
        String linha;
        while (scArquivo.hasNextLine() && cont < tamanho) {
            linha = scArquivo.nextLine();
            // Capturando outras informações
            int id = capturaId(linha);
            String name = capturaName(linha);
            if ((idProcurado = igualId(ids, id)) != -1) {
                GamePesquisado jogo = new GamePesquisado(name);
                gameList[cont] = jogo;
                cont++;

                ids[idProcurado] = null; // Marca como usado
            }
            contador = 0;
        }
        scArquivo.close();
        return gameList;
    }

    // Vendo se id é igual - Retorna o índice do id no vetor ids ou -1 se não
    // encontrar.
    static int igualId(String ids[], int id) {
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] != null) { // Verifica se o ID não foi marcado como usado
                try {
                    if (Integer.parseInt(ids[i]) == id) {
                        return i;
                    }
                } catch (NumberFormatException e) {
                    // Ignora IDs inválidos que não são números inteiros
                }
            }
        }
        return -1;
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
            contador++; // Pula até a primeira vírgula (depois do ID)
        }
        contador++; // Pula a vírgula
        // Lida com nomes entre aspas duplas, se houver
        if (contador < jogo.length() && jogo.charAt(contador) == '"') {
            contador++; // Pula as aspas de abertura
            while (contador < jogo.length() && jogo.charAt(contador) != '"') {
                name += jogo.charAt(contador);
                contador++;
            }
            contador++; // Pula as aspas de fechamento
        } else {
            // Captura o nome até a próxima vírgula
            while (contador < jogo.length() && jogo.charAt(contador) != ',') {
                name += jogo.charAt(contador);
                contador++;
            }
        }
        return name;
    }
}