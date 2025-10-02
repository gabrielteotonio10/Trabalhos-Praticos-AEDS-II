package tp3;

import java.util.Scanner;

public class StringInvertidaRec {
    private static final Scanner scanner = new Scanner(System.in);

    // Função recursiva que imprime a string ao contrário
    static void escrevendoContrario(String palavra, int tam) {
        if (tam > 0) {
            tam--; // move para o último índice válido
            System.out.print(palavra.charAt(tam)); // imprime o caractere
            escrevendoContrario(palavra, tam); // chama para o resto da string
        }
    }
    
    // Função recursiva principal que lê, processa e para quando encontra "FIM"
    static void recursaoPrincipal(String palavra) {
        // Condição de parada → quando a palavra for exatamente "FIM"
        if (!(palavra.length() == 3 && palavra.charAt(0) == 'F' 
                && palavra.charAt(1) == 'I' && palavra.charAt(2) == 'M')) {

            int tam = palavra.length();
            escrevendoContrario(palavra, tam); // inverte a string
            System.out.println();

            // Lê a próxima palavra e chama recursivamente
            palavra = scanner.nextLine();
            recursaoPrincipal(palavra);
        }
    }

    public static void main(String[] args) {
        String palavra = scanner.nextLine(); // primeira leitura
        recursaoPrincipal(palavra);
        scanner.close(); // fecha scanner no final
    }
}
