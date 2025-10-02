

import java.io.InputStream;
import java.util.Scanner;

import tp4.Game;

import java.util.ArrayList;

public class Main {
    public static Scanner sc;
    public static int contador = 0;

    public static void main(String[] args) {
        InputStream is = Main.class.getResourceAsStream("games.csv");
        if (is == null) {
            System.out.println("Arquivo não encontrado!");
            return;
        }
        sc = new Scanner(is);
        // Contador de linhas
        int n = 0;
        while (sc.hasNextLine()) {
            n++;
        }
        // Capturando cada objeto Game
        Game games[] = new Game[--n];
        int i = 0;
        while (sc.hasNextLine()) {
            // Lendo uma linha do arquivo
            String linha = sc.nextLine();
            // Lendo elementos do jogo
            char c = linha.charAt(0);
            if (Character.isDigit(c)) {
                int id = lerId(linha);
                String name = lerName(linha);
                String dataLancamento = lerDataLancamento(linha);
                int estimativaJogadores = lerEstimativaJogadores(linha);
                float preco = lerPreco(linha);
                ArrayList<String> idiomasSuportadados = lerIdiomasSuportadados(linha);
                float notaCritica = lerNotaCritica(linha);
                float pontosUsuario = lerPontosUsuario(linha);
                float quantidadeConquistas = lerQuantidadeConquistas(linha);
                ArrayList<String> empresasResponsaveis = lerEmpresasResponsaveis(linha);
                ArrayList<String> categorias = lerCategorias(linha);
                ArrayList<String> generos = lerGeneros(linha);
                ArrayList<String> palavraChave = lerPalavraChave(linha);
                // Criando o objeto Game
                games[i] = new Game(id, name, dataLancamento, estimativaJogadores, preco,
                        idiomasSuportadados, notaCritica,
                        pontosUsuario, quantidadeConquistas, empresasResponsaveis, categorias,
                        generos, palavraChave);
                i++;
                contador = 0;
            }
        }
        // Imprimindo o primeiro objeto Game
        System.out.println("ID: " + games[0].id);
        System.out.println("Nome: " +  games[0].name);
        System.out.println("Data de Lançamento: " +  games[0].dataLancamento);
        System.out.println("Estimativa de Jogadores: " +  games[0].estimativaJogadores);
        System.out.println("Preço: R$" +  games[0].preco);
        System.out.println("Idiomas Suportados: " +  games[0].idiomasSuportadados);
        System.out.println("Nota da Crítica: " +  games[0].notaCritica);
        System.out.println("Pontos do Usuário: " +  games[0].pontosUsuario);
        System.out.println("Quantidade de Conquistas: " +  games[0].quantidadeConquistas);
        System.out.println("Empresas Responsáveis: " +  games[0].empresasResponsaveis);
        System.out.println("Categorias: " +  games[0].categorias);
        System.out.println("Gêneros: " +  games[0].generos);
        System.out.println("Palavras-chave: " +  games[0].palavraChave);
        sc.close();
    }

    // Ler ID
    public static int lerId(String linha) {
        int id = 0;
        while (linha.charAt(contador) != ',') {
            id = (id * 10) + (linha.charAt(contador) - '0');
            contador++;
        }
        return id;
    }

    // Ler nomes
    public static String lerName(String linha) {
        String name = "";
        while (linha.charAt(contador) != ',') {
            name += linha.charAt(contador);
            contador++;
        }
        return name;
    }

    // Ler data de nscimento
    public static String lerDataLancamento(String linha) {
        String dataLancamento = "";
        contador++;
        String mes = "";
        for (int j = 0; j < 3; j++) {
            mes += linha.charAt(contador);
            contador++;
        }
        contador++;
        String dia = "";
        while (linha.charAt(contador) != ',') {
            dia += linha.charAt(contador);
            contador++;
        }
        contador++;
        String ano = "";
        while (linha.charAt(contador) != '"') {
            ano += linha.charAt(contador);
            contador++;
        }
        if (dia == "")
            dia = "01";
        if (mes == "")
            mes = "01";
        dataLancamento += dia + "/" + mes + "/" + ano;
        return dataLancamento;
    }

    // Ler estimativa de jogadores
    public static int lerEstimativaJogadores(String linha) {
        String estimativaJogadores = "";
        contador++;
        while (linha.charAt(contador) != ',') {
            if (Character.isDigit(linha.charAt(contador))) {
                estimativaJogadores += linha.charAt(contador);
            }
            contador++;
        }
        return Integer.parseInt(estimativaJogadores);
    }

    // ler preço
    public static float lerPreco(String linha) {
        String preco = "";
        while (linha.charAt(contador) != ',') {
            preco += linha.charAt(contador);
            contador++;
        }
        if (preco == "Free to Play")
            preco = "0.0";
        return Float.parseFloat(preco);
    }

    // Ler idiomas suportados
    public static ArrayList<String> lerIdiomasSuportadados(String linha) {
        ArrayList<String> idiomasSuportadados = new ArrayList<String>();
        while (linha.charAt(contador) != '[') {
            contador++;
        }
        contador -= 3;
        while (linha.charAt(contador) != ']') {
            contador += 5;
            String idioma = "";
            while (linha.charAt(contador + 1) != ',' && linha.charAt(contador + 1) != ']') {
                idioma += linha.charAt(contador);
                contador++;
            }
            idiomasSuportadados.add(idioma);
        }
        return idiomasSuportadados;
    }

    // Ler nota crítica
    public static float lerNotaCritica(String linha) {
        String notaCritica = "";
        while (linha.charAt(contador) != ',') {
            contador++;
        }
        while (linha.charAt(contador) != ',') {
            notaCritica += linha.charAt(contador);
            contador++;
        }
        if (notaCritica == "")
            notaCritica = "-1.0";
        return Float.parseFloat(notaCritica);
    }

    // Ler pontos do usuário
    public static float lerPontosUsuario(String linha) {
        String pontosUsuario = "";
        contador++;
        while (linha.charAt(contador) != ',') {
            pontosUsuario += linha.charAt(contador);
            contador++;
        }
        if (pontosUsuario == "" || pontosUsuario == "tbd")
            pontosUsuario = "-1.0";
        return Float.parseFloat(pontosUsuario);
    }

    // Ler quantidade de conquistas
    public static float lerQuantidadeConquistas(String linha) {
        String quantidadeConquistas = "";
        contador++;
        while (linha.charAt(contador) != ',') {
            quantidadeConquistas += linha.charAt(contador);
            contador++;
        }
        if (quantidadeConquistas == "")
            quantidadeConquistas = "0.0";
        return Float.parseFloat(quantidadeConquistas);
    }

    // Ler empresas responsáveis
    public static ArrayList<String> lerEmpresasResponsaveis(String linha) {
        while (contador < linha.length() && linha.charAt(contador) != ',') {
            contador++;
        }
        if (contador < linha.length()) {
            contador++;
        }
        ArrayList<String> empresasResponsaveis = new ArrayList<>();
        if (contador >= linha.length()) {
            return empresasResponsaveis;
        }
        boolean comAspas = linha.charAt(contador) == '"';
        if (comAspas) {
            contador++;
        }
        String campoBruto = "";
        if (comAspas) {
            while (contador < linha.length() && linha.charAt(contador) != '"') {
                campoBruto += linha.charAt(contador);
                contador++;
            }
            if (contador < linha.length() && linha.charAt(contador) == '"') {
                contador++;
            }
        } else {
            while (contador < linha.length() && linha.charAt(contador) != ',') {
                campoBruto += linha.charAt(contador);
                contador++;
            }
        }
        if (!campoBruto.trim().isEmpty()) {
            String[] partes = campoBruto.split(",");
            for (String parte : partes) {
                String nomeLimpo = parte.trim();
                if (!nomeLimpo.isEmpty()) {
                    nomeLimpo = nomeLimpo.replace("\"", "");
                    empresasResponsaveis.add(nomeLimpo);
                }
            }
        }
        return empresasResponsaveis;
    }

    // Ler categorias
    public static ArrayList<String> lerCategorias(String linha) {
        ArrayList<String> categorias = new ArrayList<String>();
        while (linha.charAt(++contador) != '"') {
            String categoria = "";
            while (linha.charAt(contador) != ',') {
                categoria += linha.charAt(contador);
                contador++;
            }
            categorias.add(categoria);
        }
        return categorias;
    }

    // Ler gêneros
    public static ArrayList<String> lerGeneros(String linha) {
        ArrayList<String> generos = new ArrayList<String>();
        while (linha.charAt(++contador) != '"') {
            String genero = "";
            while (linha.charAt(contador) != ',') {
                genero += linha.charAt(contador);
                contador++;
            }
            generos.add(genero);
        }
        return generos;
    }

    // Ler palavras-chave
    public static ArrayList<String> lerPalavraChave(String linha) {
        ArrayList<String> palavraChave = new ArrayList<String>();
        while (linha.charAt(++contador) != '"') {
            String palavra = "";
            while (linha.charAt(contador) != ',') {
                palavra += linha.charAt(contador);
                contador++;
            }
            palavraChave.add(palavra);
        }
        return palavraChave;
    }
}