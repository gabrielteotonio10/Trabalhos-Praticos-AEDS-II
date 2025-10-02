package tp4;

import java.util.ArrayList;

class Game {
    int id;
    String name;
    String dataLancamento;
    int estimativaJogadores;
    float preco;
    ArrayList<String> idiomasSuportadados;
    float notaCritica;
    float pontosUsuario;
    float quantidadeConquistas;
    ArrayList<String> empresasResponsaveis;
    ArrayList<String> categorias;
    ArrayList<String> generos;
    ArrayList<String> palavraChave;

    Game(){
        this.id = 0;
        this.name = "";
        this.dataLancamento = "";
        this.preco = 0;
        this.notaCritica = 0;
        this.pontosUsuario = 0;
        this.quantidadeConquistas = 0;
        this.idiomasSuportadados = new ArrayList<>();
        this.empresasResponsaveis = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.generos = new ArrayList<>();
        this.palavraChave = new ArrayList<>();
    }

    Game(int id, String name, String dataLancamento, int estimativaJogadores, float preco, 
        ArrayList<String> idiomasSuportadados, float notaCritica,
        float pontosUsuario, float quantidadeConquistas, ArrayList<String> empresasResponsaveis, 
        ArrayList<String> categorias,
        ArrayList<String> generos, ArrayList<String> palavraChave) {
        this.id = id;
        this.name = name;
        this.dataLancamento = dataLancamento;
        this.estimativaJogadores = estimativaJogadores;
        this.preco = preco;
        this.idiomasSuportadados = idiomasSuportadados;
        this.notaCritica = notaCritica;
        this.pontosUsuario = pontosUsuario;
        this.quantidadeConquistas = quantidadeConquistas;
        this.empresasResponsaveis = empresasResponsaveis; 
        this.categorias = categorias;
        this.generos = generos;
        this.palavraChave = palavraChave;
    }
}