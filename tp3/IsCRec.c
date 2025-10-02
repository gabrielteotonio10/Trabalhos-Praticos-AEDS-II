#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <stdbool.h>
#include <stdlib.h>

// Verifica recursivamente se a palavra representa um número (inteiro ou real)
// contador: usado para limitar a no máximo 1 ponto ou vírgula
bool ehNumero(char palavra[], int tam, int i, int contador) {
    if (i < tam) {
        // Se o caractere não for dígito/ponto/vírgula ou já tiver 2 separadores → não é número
        if ((!isdigit(palavra[i]) && palavra[i] != '.' && palavra[i] != ',') || contador == 2) {
            return false;
        }
        if (palavra[i] == '.' || palavra[i] == ',') {
            contador++;
        }
        return ehNumero(palavra, tam, i + 1, contador);
    }
    return true;
}

// Verifica se todos os caracteres são vogais
bool ehVogal(char palavra[], int tam, int i) {
    if (i < tam) {
        char letra = tolower(palavra[i]);
        if (letra != 'a' && letra != 'e' && letra != 'i' && letra != 'o' && letra != 'u') {
            return false;
        }
        return ehVogal(palavra, tam, i + 1);
    }
    return true;
}

// Verifica se todos os caracteres são consoantes
bool ehConsoante(char palavra[], int tam, int i) {
    if (i < tam) {
        char letra = tolower(palavra[i]);
        // Se for vogal, ponto ou vírgula não é consoante
        if (letra == 'a' || letra == 'e' || letra == 'i' || letra == 'o' || letra == 'u' || letra == '.' || letra == ',') {
            return false;
        }
        return ehConsoante(palavra, tam, i + 1);
    }
    return true;
}

// Verifica se o número é inteiro (não tem ponto nem vírgula)
bool ehInteiro(char palavra[], int tam, int i) {
    if (i < tam) {
        if (palavra[i] == '.' || palavra[i] == ',') {
            return false;
        }
        return ehInteiro(palavra, tam, i + 1);
    }
    return true;
}

// Verifica se o número é real (tem pelo menos um ponto ou vírgula)
bool ehReal(char palavra[], int tam, int i) {
    if (i < tam) {
        if (palavra[i] == '.' || palavra[i] == ',') {
            return true;
        }
        return ehReal(palavra, tam, i + 1);
    }
    return false;
}

// Função principal recursiva que classifica a entrada e lê a próxima linha
void recursaoPrincipal(char palavra[], int maxTam) {
    if (strcmp(palavra, "FIM") != 0) { // condição de parada
        int tam = strlen(palavra);
        bool numero = ehNumero(palavra, tam, 0, 0);

        if (numero) {
            // Primeiro "NAO NAO" porque não é vogal nem consoante
            printf("NAO NAO ");
            if (ehInteiro(palavra, tam, 0))
                printf("SIM SIM\n");
            else if (ehReal(palavra, tam, 0))
                printf("NAO SIM\n");
        } else {
            // Se não é número pode ser só vogais, só consoantes ou nenhum
            if (ehVogal(palavra, tam, 0))
                printf("SIM NAO NAO NAO\n");
            else if (ehConsoante(palavra, tam, 0))
                printf("NAO SIM NAO NAO\n");
            else
                printf("NAO NAO NAO NAO\n");
        }

        // Lê a próxima palavra e chama recursivamente
        fgets(palavra, maxTam, stdin);
        palavra[strcspn(palavra, "\n")] = '\0'; // remove o \n
        recursaoPrincipal(palavra, maxTam);
    }
}

int main() {
    char palavra[1000];
    fgets(palavra, sizeof(palavra), stdin);
    palavra[strcspn(palavra, "\n")] = '\0'; // remove o \n
    recursaoPrincipal(palavra, sizeof(palavra));
}
