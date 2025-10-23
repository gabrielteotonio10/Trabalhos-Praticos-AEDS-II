#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdbool.h>

#define MAX_STRING 1000
#define MAX_ARRAY_SIZE 100
#define MAX_OBJ 1851

typedef struct
{
    char **items;
    int size;
} ArrayList;

typedef struct
{
    int id;
    char name[MAX_STRING];
    char releaseDate[MAX_STRING];
    int estimatedOwners;
    float price;
    ArrayList supportedLanguages;
    int metacriticScore;
    float userScore;
    int achievements;
    ArrayList publishers;
    ArrayList developers;
    ArrayList categories;
    ArrayList genres;
    ArrayList tags;
} Game;

typedef struct
{
    int id;
} IDS;
static int contadorIdsReserva = 0;

// Variável global que pula caracteres das linhas
int contador = 0;

// Funções utilitárias para ArrayList
ArrayList createArrayList()
{
    ArrayList list;
    list.items = (char **)malloc(MAX_ARRAY_SIZE * sizeof(char *));
    list.size = 0;
    return list;
}

void addToArrayList(ArrayList *list, const char *item)
{
    if (list->size < MAX_ARRAY_SIZE)
    {
        list->items[list->size] = (char *)malloc(strlen(item) + 1);
        strcpy(list->items[list->size], item);
        list->size++;
    }
}

void freeArrayList(ArrayList *list)
{
    for (int i = 0; i < list->size; i++)
    {
        free(list->items[i]);
    }
    free(list->items);
    list->size = 0;
}

// Inicializa um jogo
void initGame(Game *game)
{
    game->id = 0;
    strcpy(game->name, "");
    strcpy(game->releaseDate, "");
    game->estimatedOwners = 0;
    game->price = 0.0f;
    game->supportedLanguages = createArrayList();
    game->metacriticScore = -1;
    game->userScore = -1.0f;
    game->achievements = 0;
    game->publishers = createArrayList();
    game->developers = createArrayList();
    game->categories = createArrayList();
    game->genres = createArrayList();
    game->tags = createArrayList();
}

// Printando o ArrayList
char *printArray(ArrayList teste)
{
    char *result = (char *)malloc(MAX_STRING * sizeof(char));
    strcpy(result, "");

    if (teste.size > 0)
    {
        strcat(result, "[");
        for (int i = 0; i < teste.size; i++)
        {
            strcat(result, teste.items[i]);
            if (i < teste.size - 1)
                strcat(result, ", ");
        }
        strcat(result, "]");
    }
    return result;
}

// Capturando Id
int capturaId(const char *jogo)
{
    int id = 0;
    while (contador < strlen(jogo) && isdigit(jogo[contador]))
    {
        id = id * 10 + (jogo[contador] - '0');
        contador++;
    }
    return id;
}

// Capturando nome
char *capturaName(const char *jogo)
{
    char *name = (char *)malloc(MAX_STRING * sizeof(char));
    strcpy(name, "");

    while (contador < strlen(jogo) && jogo[contador] != ',')
    {
        contador++;
    }
    contador++;

    while (contador < strlen(jogo) && jogo[contador] != ',')
    {
        char temp[2] = {jogo[contador], '\0'};
        strcat(name, temp);
        contador++;
    }
    return name;
}

// Capturando Release Date
char *capturaReleaseDate(const char *jogo)
{
    while (contador < strlen(jogo) && jogo[contador] != '"')
    {
        contador++;
    }
    contador++;

    char dia[MAX_STRING] = "";
    char mes[MAX_STRING] = "";
    char ano[MAX_STRING] = "";

    // Pegando mês
    for (int i = 0; contador < strlen(jogo) && i < 3; i++)
    {
        char temp[2] = {jogo[contador], '\0'};
        strcat(mes, temp);
        contador++;
    }

    // Convertendo mês
    if (strcmp(mes, "Jan") == 0)
        strcpy(mes, "01");
    else if (strcmp(mes, "Feb") == 0)
        strcpy(mes, "02");
    else if (strcmp(mes, "Mar") == 0)
        strcpy(mes, "03");
    else if (strcmp(mes, "Apr") == 0)
        strcpy(mes, "04");
    else if (strcmp(mes, "May") == 0)
        strcpy(mes, "05");
    else if (strcmp(mes, "Jun") == 0)
        strcpy(mes, "06");
    else if (strcmp(mes, "Jul") == 0)
        strcpy(mes, "07");
    else if (strcmp(mes, "Aug") == 0)
        strcpy(mes, "08");
    else if (strcmp(mes, "Sep") == 0)
        strcpy(mes, "09");
    else if (strcmp(mes, "Oct") == 0)
        strcpy(mes, "10");
    else if (strcmp(mes, "Nov") == 0)
        strcpy(mes, "11");
    else if (strcmp(mes, "Dec") == 0)
        strcpy(mes, "12");

    // Pulando espaço
    while (contador < strlen(jogo) && !isdigit(jogo[contador]) && jogo[contador] != ',')
    {
        contador++;
    }

    // Pegando dia
    while (contador < strlen(jogo) && isdigit(jogo[contador]))
    {
        char temp[2] = {jogo[contador], '\0'};
        strcat(dia, temp);
        contador++;
    }

    // Pulando espaço
    while (contador < strlen(jogo) && !isdigit(jogo[contador]))
    {
        contador++;
    }

    // Pegando ano
    while (contador < strlen(jogo) && jogo[contador] != '"')
    {
        char temp[2] = {jogo[contador], '\0'};
        strcat(ano, temp);
        contador++;
    }

    if (strlen(dia) == 0)
        strcpy(dia, "01");
    if (strlen(mes) == 0)
        strcpy(mes, "01");
    if (strlen(ano) == 0)
        strcpy(ano, "0000");

    char *result = (char *)malloc(MAX_STRING * sizeof(char));
    sprintf(result, "%s/%s/%s", dia, mes, ano);
    return result;
}

// Capturando Estimated Owners
int capturaEstimatedOwners(const char *jogo)
{
    int estimatedOwners = 0;

    while (contador < strlen(jogo) && jogo[contador] != ',')
    {
        contador++;
    }
    contador++;

    while (contador < strlen(jogo) && jogo[contador] != ',')
    {
        estimatedOwners = estimatedOwners * 10 + (jogo[contador] - '0');
        contador++;
    }

    return estimatedOwners;
}

// Capturando Price
float capturaPrice(const char *jogo)
{
    char price[MAX_STRING] = "";

    while (contador < strlen(jogo) && !isdigit(jogo[contador]) && jogo[contador] != 'F')
    {
        contador++;
    }

    while (contador < strlen(jogo) && (isdigit(jogo[contador]) || jogo[contador] == '.'))
    {
        char temp[2] = {jogo[contador], '\0'};
        strcat(price, temp);
        contador++;
    }

    // Remover caracteres não numéricos exceto ponto
    char cleanedPrice[MAX_STRING] = "";
    for (int i = 0; i < strlen(price); i++)
    {
        if (isdigit(price[i]) || price[i] == '.')
        {
            char temp[2] = {price[i], '\0'};
            strcat(cleanedPrice, temp);
        }
    }

    if (strlen(cleanedPrice) == 0 || strstr(jogo, "Free to play") != NULL)
    {
        return 0.0f;
    }

    return atof(cleanedPrice);
}

// Capturando idiomas
ArrayList capturaSupportedLanguages(const char *jogo)
{
    ArrayList supportedLanguages = createArrayList();

    while (contador < strlen(jogo) && jogo[contador] != ']')
    {
        char lingua[MAX_STRING] = "";

        while (contador < strlen(jogo) && !isalpha(jogo[contador]))
        {
            contador++;
        }

        while (contador < strlen(jogo) && jogo[contador] != ',' && jogo[contador] != ']')
        {
            if (isalpha(jogo[contador]) || jogo[contador] == ' ' || jogo[contador] == '-')
            {
                char temp[2] = {jogo[contador], '\0'};
                strcat(lingua, temp);
            }
            contador++;
        }

        if (strlen(lingua) > 0)
        {
            addToArrayList(&supportedLanguages, lingua);
        }
    }

    return supportedLanguages;
}

// Capturando Metacritic Score
int capturaMetacriticScore(const char *jogo)
{
    char metacriticScore[MAX_STRING] = "";

    while (contador < strlen(jogo) && jogo[contador] != ',')
    {
        contador++;
    }
    contador++;

    while (contador < strlen(jogo) && isdigit(jogo[contador]))
    {
        char temp[2] = {jogo[contador], '\0'};
        strcat(metacriticScore, temp);
        contador++;
    }

    if (strlen(metacriticScore) == 0)
        return -1;
    else
        return atoi(metacriticScore);
}

// Capturando User Score
float capturaUserScore(const char *jogo)
{
    char userScore[MAX_STRING] = "";

    while (contador < strlen(jogo) && jogo[contador] != ',')
    {
        contador++;
    }
    contador++;

    while (contador < strlen(jogo) && (isdigit(jogo[contador]) || jogo[contador] == '.'))
    {
        char temp[2] = {jogo[contador], '\0'};
        strcat(userScore, temp);
        contador++;
    }

    if (strlen(userScore) == 0)
        return -1.0f;
    else
        return atof(userScore);
}

// Capturando Achievements
int capturaAchievements(const char *jogo)
{
    char achievements[MAX_STRING] = "";

    while (contador < strlen(jogo) && jogo[contador] != ',')
    {
        contador++;
    }
    contador++;

    while (contador < strlen(jogo) && (isdigit(jogo[contador]) || jogo[contador] == '.'))
    {
        char temp[2] = {jogo[contador], '\0'};
        strcat(achievements, temp);
        contador++;
    }

    if (strlen(achievements) == 0)
        return -1;
    else
        return atoi(achievements);
}

// Capturando Últimos Arrays
ArrayList capturaUltimosArrays(const char *jogo)
{
    ArrayList categoria = createArrayList();
    int teste = 0;

    while (contador < strlen(jogo) && !isalpha(jogo[contador]) && !isdigit(jogo[contador]))
    {
        if (jogo[contador] == '"')
            teste = 1;
        contador++;
    }

    if (teste)
    {
        while (contador < strlen(jogo) && jogo[contador] != '"')
        {
            char parte[MAX_STRING] = "";

            while (contador < strlen(jogo) && jogo[contador] != ',' && jogo[contador] != '"')
            {
                char temp[2] = {jogo[contador], '\0'};
                strcat(parte, temp);
                contador++;
            }

            while (contador < strlen(jogo) && !isalpha(jogo[contador]) && !isdigit(jogo[contador]) && jogo[contador] != '"')
            {
                contador++;
            }

            if (strlen(parte) > 0)
            {
                addToArrayList(&categoria, parte);
            }
        }
    }
    else
    {
        char parte[MAX_STRING] = "";

        while (contador < strlen(jogo) && jogo[contador] != ',')
        {
            char temp[2] = {jogo[contador], '\0'};
            strcat(parte, temp);
            contador++;
        }

        if (strlen(parte) > 0)
        {
            addToArrayList(&categoria, parte);
        }
    }

    return categoria;
}

// Função para liberar memória de um jogo
void freeGame(Game *game)
{
    freeArrayList(&game->supportedLanguages);
    freeArrayList(&game->publishers);
    freeArrayList(&game->developers);
    freeArrayList(&game->categories);
    freeArrayList(&game->genres);
    freeArrayList(&game->tags);
}

// Testando se Ids são iguais
bool testeIds(int id, IDS ids[MAX_OBJ])
{
    for (int i = 0; i < contadorIdsReserva; i++)
    {
        if (id - ids[i].id == 0)
            return true;
    }
    return false;
}

// Ordenação por seleção
void ordenandoSelecao(int quantidadeGames, Game gamelist[]){
    for(int i = 0; i < quantidadeGames; i++){
        int menor = i;
        for(int j = (i + 1); j < quantidadeGames; j++){
            if(strcmp(gamelist[menor].name, gamelist[j].name) > 0){
                menor = j;
            }
        }
        Game aux = gamelist[i];
        gamelist[i] = gamelist[menor];
        gamelist[menor] = aux;
    }
} 

// Printando o objeto
void printDoObjeto(int tam, Game gamelist[]){
    for(int i = 0; i < tam; i++){
        // CORREÇÃO: Declarar e inicializar a variável 'jogo' com o elemento atual do array.
        Game jogo = gamelist[i]; 
        
        // --- Lógica para formatação da data ---
        char releaseDateTemp[MAX_STRING];
        char *releaseDate;
        
        // Se o dia tem apenas 1 dígito no formato 'D/MM/AAAA', adicionamos um '0'.
        // Ex: '2/10/2023' (posição [1] é '/') -> '02/10/2023'
        // Se sua captura de data já garante o '0', essa parte é ignorada.
        if (jogo.releaseDate[1] == '/') {
            // Cria uma nova string '0D/MM/AAAA'
            sprintf(releaseDateTemp, "0%s", jogo.releaseDate);
            releaseDate = releaseDateTemp;
        } else {
            // Usa a string original 'DD/MM/AAAA'
            releaseDate = jogo.releaseDate;
        }
        
        // --- Chamadas às funções printArray ---
        // Estas funções retornam ponteiros alocados com malloc.
        char *supportedLanguagesStr = printArray(jogo.supportedLanguages);
        char *publishersStr = printArray(jogo.publishers);
        char *developersStr = printArray(jogo.developers);
        char *categoriesStr = printArray(jogo.categories);
        char *genresStr = printArray(jogo.genres);
        char *tagsStr = printArray(jogo.tags);

        // --- Printando a linha completa ---
        // Formato: => ID ## NOME ## DATA ## OWNERS ## PREÇO ## LINGUAS ## META ## USER ## ACHIEVEMENTS ## PUBLISHERS ## DEVELOPERS ## CATEGORIES ## GENRES ## TAGS ##
        printf("=> %i ## %s ## %s ## %i ## %f ## %s ## %i ## %f ## %i ## %s ## %s ## %s ## %s ## %s ##\n", 
            jogo.id, 
            jogo.name, 
            releaseDate, 
            jogo.estimatedOwners, 
            jogo.price, 
            supportedLanguagesStr, 
            jogo.metacriticScore, 
            jogo.userScore, 
            jogo.achievements, 
            publishersStr, 
            developersStr, 
            categoriesStr, 
            genresStr, 
            tagsStr
        );
        
        // Liberar a memória alocada dinamicamente por printArray
        free(supportedLanguagesStr);
        free(publishersStr);
        free(developersStr);
        free(developersStr);
        free(categoriesStr);
        free(genresStr);
        free(tagsStr);
    }
}

int main()
{
    // Pegando entrada
    char entrada[MAX_STRING];
    scanf("%s", entrada);

    int i = 0;
    IDS ids[MAX_OBJ];
    while (strcmp(entrada, "FIM") != 0)
    {
        scanf("%i", &ids[i].id);
        i++;
        contadorIdsReserva++;
    }

    FILE *file = fopen("/tp5/games.csv", "r");
    if (file == NULL)
    {
        printf("Arquivo não encontrado!\n");
        return 1;
    }

    // Pula cabeçalho
    char linha[MAX_STRING];
    fgets(linha, MAX_STRING, file);

    // Array
    Game *gamesList = (Game *)malloc(10000 * sizeof(Game));
    int gamesCount = 0;

    // Processa cada linha
    while (fgets(linha, MAX_STRING, file) != NULL)
    {
        // Remove quebra de linha
        linha[strcspn(linha, "\n")] = 0;

        // Capturando informações
        contador = 0;
        int id = capturaId(linha);
        if (testeIds(id, ids))
        {
            char *name = capturaName(linha);
            char *releaseDate = capturaReleaseDate(linha);
            int estimatedOwners = capturaEstimatedOwners(linha);
            float price = capturaPrice(linha);
            ArrayList supportedLanguages = capturaSupportedLanguages(linha);
            int metacriticScore = capturaMetacriticScore(linha);
            float userScore = capturaUserScore(linha);
            int achievements = capturaAchievements(linha);

            // Reinicia contador para capturar arrays restantes
            contador = 0;
            ArrayList publishers = capturaUltimosArrays(linha);
            ArrayList developers = capturaUltimosArrays(linha);
            ArrayList categories = capturaUltimosArrays(linha);
            ArrayList genres = capturaUltimosArrays(linha);
            ArrayList tags = capturaUltimosArrays(linha);

            // Adicionando à estrutura
            initGame(&gamesList[gamesCount]);
            gamesList[gamesCount].id = id;
            strcpy(gamesList[gamesCount].name, name);
            strcpy(gamesList[gamesCount].releaseDate, releaseDate);
            gamesList[gamesCount].estimatedOwners = estimatedOwners;
            gamesList[gamesCount].price = price;
            gamesList[gamesCount].supportedLanguages = supportedLanguages;
            gamesList[gamesCount].metacriticScore = metacriticScore;
            gamesList[gamesCount].userScore = userScore;
            gamesList[gamesCount].achievements = achievements;
            gamesList[gamesCount].publishers = publishers;
            gamesList[gamesCount].developers = developers;
            gamesList[gamesCount].categories = categories;
            gamesList[gamesCount].genres = genres;
            gamesList[gamesCount].tags = tags;

            gamesCount++;

            // Libera memória alocada temporariamente
            free(name);
            free(releaseDate);

            contador = 0;
        }
    }

    fclose(file);

    ordenandoSelecao(gamesCount, gamesList);

    printDoObjeto(gamesCount, gamesList);

    // Libera memória
    for (int i = 0; i < gamesCount; i++)
    {
        freeGame(&gamesList[i]);
    }
    free(gamesList);

    return 0;
}