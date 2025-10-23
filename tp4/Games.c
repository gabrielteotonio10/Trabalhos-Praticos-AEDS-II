#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdbool.h>

#define TAM_LINHA 4096
#define TAM_CAMPO 512

typedef struct
{
    int id;
    char *name;
    char *releaseDate;
    int estimatedOwners;
    float price;
    char **supportedLanguages;
    int supportedLanguagesCount;
    int metacriticScore;
    float userScore;
    int achievements;
    char **publishers;
    int publishersCount;
    char **developers;
    int developersCount;
    char **categories;
    int categoriesCount;
    char **genres;
    int genresCount;
    char **tags;
    int tagsCount;
} Game;

// Declarações
char *lerCampo(char *linha, int *pos);
char **dividirCampos(char *texto, char delimitador, int *qtd);
char *removerEspacos(char *texto);
char *ajustarData(char *entrada);
void liberarJogo(Game *j);
void exibirJogo(Game *j);
void interpretarLinha(Game *j, char *linha);
void exibirArray(char **vetor, int qtd);

int main()
{
    const char *caminho = "/tmp/games.csv";
    FILE *arquivo = fopen(caminho, "r");
    if (!arquivo)
    {
        perror("Erro ao abrir o arquivo");
        return 1;
    }

    char buffer[TAM_LINHA];
    int totalJogos = 0;

    fgets(buffer, TAM_LINHA, arquivo); // pula cabeçalho
    while (fgets(buffer, TAM_LINHA, arquivo))
        totalJogos++;
    fclose(arquivo);

    Game *jogos = (Game *)malloc(sizeof(Game) * totalJogos);
    if (!jogos)
    {
        printf("Falha na alocação de memória.\n");
        return 1;
    }

    arquivo = fopen(caminho, "r");
    fgets(buffer, TAM_LINHA, arquivo); // pula cabeçalho
    int indice = 0;
    while (fgets(buffer, TAM_LINHA, arquivo))
    {
        interpretarLinha(&jogos[indice++], buffer);
    }
    fclose(arquivo);

    char entrada[TAM_CAMPO];
    while (fgets(entrada, TAM_CAMPO, stdin))
    {
        entrada[strcspn(entrada, "\n")] = 0;
        if (strcmp(entrada, "FIM") == 0)
            break;

        int idBuscado = atoi(entrada);
        for (int k = 0; k < totalJogos; k++)
        {
            if (jogos[k].id == idBuscado)
            {
                exibirJogo(&jogos[k]);
                break;
            }
        }
    }

    for (int k = 0; k < totalJogos; k++)
        liberarJogo(&jogos[k]);
    free(jogos);
    return 0;
}

// -------------------------------------------

char *lerCampo(char *linha, int *pos)
{
    char *campo = malloc(TAM_CAMPO);
    int i = 0;
    bool entreAspas = false;

    if (linha[*pos] == '"')
    {
        entreAspas = true;
        (*pos)++;
    }

    while (linha[*pos] && (entreAspas || linha[*pos] != ','))
    {
        if (entreAspas && linha[*pos] == '"')
        {
            (*pos)++;
            break;
        }
        campo[i++] = linha[(*pos)++];
    }

    if (linha[*pos] == ',')
        (*pos)++;
    campo[i] = '\0';
    return campo;
}

char **dividirCampos(char *texto, char delimitador, int *qtd)
{
    int separadores = 0;
    for (int i = 0; texto[i]; i++)
        if (texto[i] == delimitador)
            separadores++;
    *qtd = separadores + 1;

    char **partes = malloc(sizeof(char *) * (*qtd));
    int inicio = 0, atual = 0;

    for (int i = 0;; i++)
    {
        if (texto[i] == delimitador || texto[i] == '\0')
        {
            int tamanho = i - inicio;
            partes[atual] = malloc(tamanho + 1);
            strncpy(partes[atual], texto + inicio, tamanho);
            partes[atual][tamanho] = '\0';

            // Corrigido: cria nova string sem espaços e libera a antiga
            char *semEspacos = removerEspacos(partes[atual]);
            free(partes[atual]);
            partes[atual] = semEspacos;

            atual++;
            inicio = i + 1;
        }
        if (texto[i] == '\0')
            break;
    }
    return partes;
}

// ✅ Corrigido: retorna nova string sem modificar ponteiro original
char *removerEspacos(char *texto)
{
    while (isspace((unsigned char)*texto))
        texto++;

    char *copia = malloc(strlen(texto) + 1);
    strcpy(copia, texto);

    char *fim = copia + strlen(copia) - 1;
    while (fim > copia && isspace((unsigned char)*fim))
        *fim-- = '\0';

    return copia;
}

char *ajustarData(char *entrada)
{
    char *novaData = malloc(12);
    char dia[3] = "01", mesAbrev[4] = {0}, ano[5] = "0000";

    sscanf(entrada, "%3s %[^,], %s", mesAbrev, dia, ano);

    char *mesNum = "01";
    if (!strcmp(mesAbrev, "Feb"))
        mesNum = "02";
    else if (!strcmp(mesAbrev, "Mar"))
        mesNum = "03";
    else if (!strcmp(mesAbrev, "Apr"))
        mesNum = "04";
    else if (!strcmp(mesAbrev, "May"))
        mesNum = "05";
    else if (!strcmp(mesAbrev, "Jun"))
        mesNum = "06";
    else if (!strcmp(mesAbrev, "Jul"))
        mesNum = "07";
    else if (!strcmp(mesAbrev, "Aug"))
        mesNum = "08";
    else if (!strcmp(mesAbrev, "Sep"))
        mesNum = "09";
    else if (!strcmp(mesAbrev, "Oct"))
        mesNum = "10";
    else if (!strcmp(mesAbrev, "Nov"))
        mesNum = "11";
    else if (!strcmp(mesAbrev, "Dec"))
        mesNum = "12";

    sprintf(novaData, "%s/%s/%s", dia, mesNum, ano);
    free(entrada);
    return novaData;
}

void interpretarLinha(Game *j, char *linha)
{
    int pos = 0;

    j->id = atoi(lerCampo(linha, &pos));
    j->name = lerCampo(linha, &pos);
    j->releaseDate = ajustarData(lerCampo(linha, &pos));
    j->estimatedOwners = atoi(lerCampo(linha, &pos));

    char *precoStr = lerCampo(linha, &pos);
    j->price = (!precoStr[0] || strcmp(precoStr, "Free to Play") == 0) ? 0.0f : atof(precoStr);
    free(precoStr);

    char *linguas = lerCampo(linha, &pos);
    if (linguas[0] == '[')
        memmove(linguas, linguas + 1, strlen(linguas));
    linguas[strcspn(linguas, "]")] = '\0';
    for (int i = 0; linguas[i]; i++)
        if (linguas[i] == '\'')
            linguas[i] = ' ';
    j->supportedLanguages = dividirCampos(linguas, ',', &j->supportedLanguagesCount);
    free(linguas);

    j->metacriticScore = atoi(lerCampo(linha, &pos));
    j->userScore = atof(lerCampo(linha, &pos));
    j->achievements = atoi(lerCampo(linha, &pos));

    j->publishers = dividirCampos(lerCampo(linha, &pos), ',', &j->publishersCount);
    j->developers = dividirCampos(lerCampo(linha, &pos), ',', &j->developersCount);
    j->categories = dividirCampos(lerCampo(linha, &pos), ',', &j->categoriesCount);
    j->genres = dividirCampos(lerCampo(linha, &pos), ',', &j->genresCount);
    j->tags = dividirCampos(lerCampo(linha, &pos), ',', &j->tagsCount);
}

void exibirArray(char **vetor, int qtd)
{
    printf("[");
    for (int i = 0; i < qtd; i++)
    {
        printf("%s", vetor[i]);
        if (i < qtd - 1)
            printf(", ");
    }
    printf("]");
}

void exibirJogo(Game *j)
{
    char dataFormatada[12];
    strcpy(dataFormatada, j->releaseDate);
    if (dataFormatada[1] == '/')
    {
        memmove(dataFormatada + 1, dataFormatada, strlen(dataFormatada) + 1);
        dataFormatada[0] = '0';
    }

    printf("=> %d ## %s ## %s ## %d ## %.2f ## ",
           j->id, j->name, dataFormatada, j->estimatedOwners, j->price);
    exibirArray(j->supportedLanguages, j->supportedLanguagesCount);
    printf(" ## %d ## %.1f ## %d ## ",
           j->metacriticScore ? j->metacriticScore : -1,
           j->userScore ? j->userScore : -1.0f,
           j->achievements);
    exibirArray(j->publishers, j->publishersCount);
    printf(" ## ");
    exibirArray(j->developers, j->developersCount);
    printf(" ## ");
    exibirArray(j->categories, j->categoriesCount);
    printf(" ## ");
    exibirArray(j->genres, j->genresCount);
    printf(" ## ");
    exibirArray(j->tags, j->tagsCount);
    printf(" ##\n");
}

void liberarJogo(Game *j)
{
    free(j->name);
    free(j->releaseDate);
    for (int i = 0; i < j->supportedLanguagesCount; i++)
        free(j->supportedLanguages[i]);
    free(j->supportedLanguages);
    for (int i = 0; i < j->publishersCount; i++)
        free(j->publishers[i]);
    free(j->publishers);
    for (int i = 0; i < j->developersCount; i++)
        free(j->developers[i]);
    free(j->developers);
    for (int i = 0; i < j->categoriesCount; i++)
        free(j->categories[i]);
    free(j->categories);
    for (int i = 0; i < j->genresCount; i++)
        free(j->genres[i]);
    free(j->genres);
    for (int i = 0; i < j->tagsCount; i++)
        free(j->tags[i]);
    free(j->tags);
}
