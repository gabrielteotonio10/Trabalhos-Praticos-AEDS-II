package tp4;

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

        // Pular cabeçalho
        if (sc.hasNextLine()) {
            sc.nextLine();
        }

        // Contador de linhas
        ArrayList<Game> gamesList = new ArrayList<>();

        while (sc.hasNextLine()) {
            // Lendo uma linha do arquivo
            String linha = sc.nextLine();
            // Lendo elementos do jogo
            if (!linha.trim().isEmpty() && Character.isDigit(linha.charAt(0))) {
                contador = 0;

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
                Game game = new Game(id, name, dataLancamento, estimativaJogadores, preco,
                        idiomasSuportadados, notaCritica,
                        pontosUsuario, quantidadeConquistas, empresasResponsaveis, categorias,
                        generos, palavraChave);
                gamesList.add(game);
            }
        }

        // Converter para array
        Game[] games = gamesList.toArray(new Game[0]);

        // Imprimindo o primeiro objeto Game
        if (games.length > 0) {
            System.out.println("ID: " + games[1].id);
            System.out.println("Nome: " + games[1].name);
            System.out.println("Data de Lançamento: " + games[1].dataLancamento);
            System.out.println("Estimativa de Jogadores: " + games[1].estimativaJogadores);
            System.out.println("Preço: R$" + games[1].preco);
            System.out.println("Idiomas Suportados: " + games[1].idiomasSuportadados);
            System.out.println("Nota da Crítica: " + games[1].notaCritica);
            System.out.println("Pontos do Usuário: " + games[1].pontosUsuario);
            System.out.println("Quantidade de Conquistas: " + games[1].quantidadeConquistas);
            System.out.println("Empresas Responsáveis: " + games[1].empresasResponsaveis);
            System.out.println("Categorias: " + games[1].categorias);
            System.out.println("Gêneros: " + games[1].generos);
            System.out.println("Palavras-chave: " + games[1].palavraChave);
        }
        sc.close();
    }

    // Ler ID
    public static int lerId(String linha) {
        int id = 0;
        while (contador < linha.length() && linha.charAt(contador) != ',') {
            id = (id * 10) + (linha.charAt(contador) - '0');
            contador++;
        }
        contador++; // pular vírgula
        return id;
    }

    // Ler nomes
    public static String lerName(String linha) {
        String name = "";
        // Verificar se tem aspas
        if (contador < linha.length() && linha.charAt(contador) == '"') {
            contador++; // pular aspas inicial
            while (contador < linha.length() && linha.charAt(contador) != '"') {
                name += linha.charAt(contador);
                contador++;
            }
            contador++; // pular aspas final
        } else {
            while (contador < linha.length() && linha.charAt(contador) != ',') {
                name += linha.charAt(contador);
                contador++;
            }
        }
        contador++; // pular vírgula
        return name;
    }

    // Ler data de lançamento
    public static String lerDataLancamento(String linha) {
        String dataLancamento = "";
        if (contador < linha.length() && linha.charAt(contador) == '"') {
            contador++; // pular aspas inicial

            String mes = "";
            for (int j = 0; j < 3 && contador < linha.length(); j++) {
                mes += linha.charAt(contador);
                contador++;
            }

            contador++; // pular espaço
            String dia = "";
            while (contador < linha.length() && Character.isDigit(linha.charAt(contador))) {
                dia += linha.charAt(contador);
                contador++;
            }

            contador += 2; // pular ", "
            String ano = "";
            while (contador < linha.length() && linha.charAt(contador) != '"') {
                ano += linha.charAt(contador);
                contador++;
            }
            contador++; // pular aspas final

            dataLancamento = dia + "/" + mes + "/" + ano;
        }
        contador++; // pular vírgula
        return dataLancamento;
    }

    // Ler estimativa de jogadores
    public static int lerEstimativaJogadores(String linha) {
        String estimativaJogadores = "";
        while (contador < linha.length() && linha.charAt(contador) != ',') {
            if (Character.isDigit(linha.charAt(contador))) {
                estimativaJogadores += linha.charAt(contador);
            }
            contador++;
        }
        contador++; // pular vírgula
        return estimativaJogadores.isEmpty() ? 0 : Integer.parseInt(estimativaJogadores);
    }

    // ler preço
    public static float lerPreco(String linha) {
        String preco = "";
        while (contador < linha.length() && linha.charAt(contador) != ',') {
            preco += linha.charAt(contador);
            contador++;
        }
        contador++; // pular vírgula

        if (preco.equals("Free to Play")) {
            return 0.0f;
        }
        try {
            return Float.parseFloat(preco);
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    // Ler idiomas suportados - CORRIGIDO
    public static ArrayList<String> lerIdiomasSuportadados(String linha) {
        ArrayList<String> idiomasSuportadados = new ArrayList<String>();

        if (contador < linha.length() && linha.charAt(contador) == '"') {
            contador++; // pular aspas inicial

            String campoCompleto = "";
            while (contador < linha.length() && linha.charAt(contador) != '"') {
                campoCompleto += linha.charAt(contador);
                contador++;
            }
            contador++; // pular aspas final

            // Processar conteúdo entre colchetes
            if (campoCompleto.startsWith("[") && campoCompleto.endsWith("]")) {
                String conteudo = campoCompleto.substring(1, campoCompleto.length() - 1);
                String[] idiomas = conteudo.split("', '");
                for (String idioma : idiomas) {
                    String idiomaLimpo = idioma.replace("'", "").trim();
                    if (!idiomaLimpo.isEmpty()) {
                        idiomasSuportadados.add(idiomaLimpo);
                    }
                }
            }
        } else if (contador < linha.length() && linha.charAt(contador) == '[') {
            // Formato sem aspas: ['English']
            String campoCompleto = "";
            while (contador < linha.length() && linha.charAt(contador) != ']') {
                campoCompleto += linha.charAt(contador);
                contador++;
            }
            campoCompleto += ']'; // adicionar o ] final
            contador++; // pular ]

            // Processar conteúdo
            if (campoCompleto.startsWith("[") && campoCompleto.endsWith("]")) {
                String conteudo = campoCompleto.substring(1, campoCompleto.length() - 1);
                String[] idiomas = conteudo.split("', '");
                for (String idioma : idiomas) {
                    String idiomaLimpo = idioma.replace("'", "").trim();
                    if (!idiomaLimpo.isEmpty()) {
                        idiomasSuportadados.add(idiomaLimpo);
                    }
                }
            }
        }

        contador++; // pular vírgula
        return idiomasSuportadados;
    }

    // Ler nota crítica - CORRIGIDO
    public static float lerNotaCritica(String linha) {
        String notaCritica = "";

        // Ler até a próxima vírgula
        while (contador < linha.length() && linha.charAt(contador) != ',') {
            notaCritica += linha.charAt(contador);
            contador++;
        }
        contador++; // pular vírgula

        notaCritica = notaCritica.trim();
        if (notaCritica.isEmpty()) {
            return -1.0f;
        }
        try {
            return Float.parseFloat(notaCritica);
        } catch (NumberFormatException e) {
            return -1.0f;
        }
    }

    // Ler pontos do usuário - CORRIGIDO
    public static float lerPontosUsuario(String linha) {
        String pontosUsuario = "";

        // Ler até a próxima vírgula
        while (contador < linha.length() && linha.charAt(contador) != ',') {
            pontosUsuario += linha.charAt(contador);
            contador++;
        }
        contador++; // pular vírgula

        pontosUsuario = pontosUsuario.trim();
        if (pontosUsuario.isEmpty() || pontosUsuario.equals("tbd")) {
            return -1.0f;
        }
        try {
            return Float.parseFloat(pontosUsuario);
        } catch (NumberFormatException e) {
            return -1.0f;
        }
    }

    // Ler quantidade de conquistas - CORRIGIDO
    public static float lerQuantidadeConquistas(String linha) {
        String quantidadeConquistas = "";

        // Ler até a próxima vírgula
        while (contador < linha.length() && linha.charAt(contador) != ',') {
            quantidadeConquistas += linha.charAt(contador);
            contador++;
        }
        contador++; // pular vírgula

        quantidadeConquistas = quantidadeConquistas.trim();
        if (quantidadeConquistas.isEmpty()) {
            return 0.0f;
        }
        try {
            return Float.parseFloat(quantidadeConquistas);
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    // Ler empresas responsáveis - CORRIGIDO
    public static ArrayList<String> lerEmpresasResponsaveis(String linha) {
        ArrayList<String> empresasResponsaveis = new ArrayList<>();

        // Verificar se está entre aspas
        if (contador < linha.length() && linha.charAt(contador) == '"') {
            contador++; // pular aspas inicial

            String campoCompleto = "";
            while (contador < linha.length() && linha.charAt(contador) != '"') {
                campoCompleto += linha.charAt(contador);
                contador++;
            }
            contador++; // pular aspas final

            // Separar por vírgulas
            if (!campoCompleto.trim().isEmpty()) {
                String[] partes = campoCompleto.split(",");
                for (String parte : partes) {
                    String nomeLimpo = parte.trim();
                    if (!nomeLimpo.isEmpty()) {
                        empresasResponsaveis.add(nomeLimpo);
                    }
                }
            }
        } else {
            // Sem aspas - ler até a vírgula
            String empresa = "";
            while (contador < linha.length() && linha.charAt(contador) != ',') {
                empresa += linha.charAt(contador);
                contador++;
            }
            String nomeLimpo = empresa.trim();
            if (!nomeLimpo.isEmpty()) {
                empresasResponsaveis.add(nomeLimpo);
            }
        }

        // Pular vírgula apenas se não chegou ao final
        if (contador < linha.length() && linha.charAt(contador) == ',') {
            contador++;
        }
        return empresasResponsaveis;
    }

    // Ler categorias - CORRIGIDO
    public static ArrayList<String> lerCategorias(String linha) {
        ArrayList<String> categorias = new ArrayList<String>();

        if (contador < linha.length() && linha.charAt(contador) == '"') {
            contador++; // pular aspas inicial

            String campoCompleto = "";
            while (contador < linha.length() && linha.charAt(contador) != '"') {
                campoCompleto += linha.charAt(contador);
                contador++;
            }
            contador++; // pular aspas final

            // Separar por vírgulas
            if (!campoCompleto.trim().isEmpty()) {
                String[] partes = campoCompleto.split(",");
                for (String parte : partes) {
                    String categoriaLimpa = parte.trim();
                    if (!categoriaLimpa.isEmpty()) {
                        categorias.add(categoriaLimpa);
                    }
                }
            }
        } else {
            // Sem aspas - ler até a vírgula
            String categoria = "";
            while (contador < linha.length() && linha.charAt(contador) != ',') {
                categoria += linha.charAt(contador);
                contador++;
            }
            String categoriaLimpa = categoria.trim();
            if (!categoriaLimpa.isEmpty()) {
                categorias.add(categoriaLimpa);
            }
        }

        // Pular vírgula apenas se não chegou ao final
        if (contador < linha.length() && linha.charAt(contador) == ',') {
            contador++;
        }
        return categorias;
    }

    // Ler gêneros - CORRIGIDO
    public static ArrayList<String> lerGeneros(String linha) {
        ArrayList<String> generos = new ArrayList<String>();

        if (contador < linha.length() && linha.charAt(contador) == '"') {
            contador++; // pular aspas inicial

            String campoCompleto = "";
            while (contador < linha.length() && linha.charAt(contador) != '"') {
                campoCompleto += linha.charAt(contador);
                contador++;
            }
            contador++; // pular aspas final

            // Separar por vírgulas
            if (!campoCompleto.trim().isEmpty()) {
                String[] partes = campoCompleto.split(",");
                for (String parte : partes) {
                    String generoLimpo = parte.trim();
                    if (!generoLimpo.isEmpty()) {
                        generos.add(generoLimpo);
                    }
                }
            }
        } else {
            // Sem aspas - ler até a vírgula
            String genero = "";
            while (contador < linha.length() && linha.charAt(contador) != ',') {
                genero += linha.charAt(contador);
                contador++;
            }
            String generoLimpo = genero.trim();
            if (!generoLimpo.isEmpty()) {
                generos.add(generoLimpo);
            }
        }

        // Pular vírgula apenas se não chegou ao final
        if (contador < linha.length() && linha.charAt(contador) == ',') {
            contador++;
        }
        return generos;
    }

    // Ler palavras-chave - CORRIGIDO
    public static ArrayList<String> lerPalavraChave(String linha) {
        ArrayList<String> palavraChave = new ArrayList<String>();

        if (contador < linha.length() && linha.charAt(contador) == '"') {
            contador++; // pular aspas inicial

            String campoCompleto = "";
            while (contador < linha.length() && linha.charAt(contador) != '"') {
                campoCompleto += linha.charAt(contador);
                contador++;
            }
            contador++; // pular aspas final

            // Separar por vírgulas
            if (!campoCompleto.trim().isEmpty()) {
                String[] partes = campoCompleto.split(",");
                for (String parte : partes) {
                    String palavraLimpa = parte.trim();
                    if (!palavraLimpa.isEmpty()) {
                        palavraChave.add(palavraLimpa);
                    }
                }
            }
        } else {
            // Sem aspas - ler até o final da linha
            String palavra = "";
            while (contador < linha.length()) {
                palavra += linha.charAt(contador);
                contador++;
            }
            String palavraLimpa = palavra.trim();
            if (!palavraLimpa.isEmpty()) {
                palavraChave.add(palavraLimpa);
            }
        }

        return palavraChave;
    }
}