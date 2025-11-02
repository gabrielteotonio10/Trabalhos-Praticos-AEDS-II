#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>

#define MAX_LINE_SIZE 4096
#define MAX_FIELD_SIZE 512
#define MAX_ARRAY_ELEMENTS 50
#define MAX_IDS 100

// Estrutura para armazenar os dados de um jogo
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

// Estrutura da célula dupla
typedef struct CelulaDupla
{
    struct CelulaDupla *prox;
    struct CelulaDupla *ant;
    Game *game;
} CelulaDupla;

// Estrutura da lista dupla
typedef struct
{
    CelulaDupla *primeiro;
    CelulaDupla *ultimo;
} ListaDupla;

// Protótipos das funções
void parseAndLoadGame(Game *game, char *line);
void printGame(Game *game);
void freeGame(Game *game);
char *getNextField(char *line, int *pos);
char **splitString(const char *str, char delimiter, int *count);
char *trim(char *str);
char *formatDate(char *dateStr);
void printStringArray(char **arr, int count);

// Funções da lista dupla
ListaDupla *criarLista();
void inserirInicio(ListaDupla *lista, Game *game);
void inserirFim(ListaDupla *lista, Game *game);
void inserirPosicao(ListaDupla *lista, Game *game, int pos);
Game *removerInicio(ListaDupla *lista);
Game *removerFim(ListaDupla *lista);
Game *removerPosicao(ListaDupla *lista, int pos);
int tamanhoLista(ListaDupla *lista);
void imprimirLista(ListaDupla *lista);
void imprimirRemovido(Game *game);
void liberarLista(ListaDupla *lista);

// Variáveis globais
char **ids;
int idsTamanho = 0;

// Lógica Principal
int main()
{
    char lineBuffer[MAX_LINE_SIZE];
    const char *filePath = "/tmp/games.csv";

    // Alocar memória para ids
    ids = (char **)malloc(sizeof(char *) * MAX_IDS);
    for (int i = 0; i < MAX_IDS; i++)
    {
        ids[i] = (char *)malloc(sizeof(char) * MAX_FIELD_SIZE);
    }

    // Ler IDs da entrada até "FIM"
    char input[MAX_FIELD_SIZE];
    while (fgets(input, MAX_FIELD_SIZE, stdin) != NULL)
    {
        input[strcspn(input, "\n")] = 0;
        if (strcmp(input, "FIM") == 0)
            break;
        strcpy(ids[idsTamanho++], input);
    }

    // Carregar todos os jogos do arquivo
    FILE *file = fopen(filePath, "r");
    if (file == NULL)
    {
        perror("Erro ao abrir o arquivo");
        return 1;
    }

    int gameCount = 0;
    fgets(lineBuffer, MAX_LINE_SIZE, file);
    while (fgets(lineBuffer, MAX_LINE_SIZE, file) != NULL)
    {
        gameCount++;
    }
    fclose(file);

    Game *allGames = (Game *)malloc(sizeof(Game) * gameCount);
    if (allGames == NULL)
    {
        printf("Erro de alocação de memória\n");
        return 1;
    }

    file = fopen(filePath, "r");
    if (file == NULL)
    {
        perror("Erro ao reabrir o arquivo");
        free(allGames);
        return 1;
    }

    fgets(lineBuffer, MAX_LINE_SIZE, file);
    int i = 0;
    while (fgets(lineBuffer, MAX_LINE_SIZE, file) != NULL)
    {
        parseAndLoadGame(&allGames[i], lineBuffer);
        i++;
    }
    fclose(file);

    // Criar lista e inserir jogos baseados nos IDs
    ListaDupla *lista = criarLista();

    for (i = 0; i < idsTamanho; i++)
    {
        int targetId = atoi(ids[i]);
        for (int j = 0; j < gameCount; j++)
        {
            if (allGames[j].id == targetId)
            {
                // Criar cópia do jogo para a lista
                Game *novoGame = (Game *)malloc(sizeof(Game));
                *novoGame = allGames[j]; // Cópia superficial
                // Deep copy para strings
                novoGame->name = strdup(allGames[j].name);
                novoGame->releaseDate = strdup(allGames[j].releaseDate);
                // Para arrays, seria necessário deep copy também
                inserirFim(lista, novoGame);
                break;
            }
        }
    }

    // Processar operações
    int n;
    scanf("%d", &n);
    getchar(); // Consumir newline

    for (i = 0; i < n; i++)
    {
        fgets(input, MAX_FIELD_SIZE, stdin);
        input[strcspn(input, "\n")] = 0;

        char operacao[3] = {0};
        strncpy(operacao, input, 2);

        if (strcmp(operacao, "II") == 0)
        {
            // Inserir no início
            int targetId = atoi(input + 3);
            for (int j = 0; j < gameCount; j++)
            {
                if (allGames[j].id == targetId)
                {
                    Game *novoGame = (Game *)malloc(sizeof(Game));
                    *novoGame = allGames[j];
                    novoGame->name = strdup(allGames[j].name);
                    novoGame->releaseDate = strdup(allGames[j].releaseDate);
                    inserirInicio(lista, novoGame);
                    break;
                }
            }
        }
        else if (strcmp(operacao, "IF") == 0)
        {
            // Inserir no fim
            int targetId = atoi(input + 3);
            for (int j = 0; j < gameCount; j++)
            {
                if (allGames[j].id == targetId)
                {
                    Game *novoGame = (Game *)malloc(sizeof(Game));
                    *novoGame = allGames[j];
                    novoGame->name = strdup(allGames[j].name);
                    novoGame->releaseDate = strdup(allGames[j].releaseDate);
                    inserirFim(lista, novoGame);
                    break;
                }
            }
        }
        else if (strcmp(operacao, "I*") == 0)
        {
            // Inserir em posição específica
            char *token = strtok(input + 3, " ");
            int pos = atoi(token);
            token = strtok(NULL, " ");
            int targetId = atoi(token);

            for (int j = 0; j < gameCount; j++)
            {
                if (allGames[j].id == targetId)
                {
                    Game *novoGame = (Game *)malloc(sizeof(Game));
                    *novoGame = allGames[j];
                    novoGame->name = strdup(allGames[j].name);
                    novoGame->releaseDate = strdup(allGames[j].releaseDate);
                    inserirPosicao(lista, novoGame, pos);
                    break;
                }
            }
        }
        else if (strcmp(operacao, "RI") == 0)
        {
            // Remover do início
            Game *removido = removerInicio(lista);
            if (removido != NULL)
            {
                printf("(R) %s\n", removido->name);
                freeGame(removido);
                free(removido);
            }
        }
        else if (strcmp(operacao, "RF") == 0)
        {
            // Remover do fim
            Game *removido = removerFim(lista);
            if (removido != NULL)
            {
                printf("(R) %s\n", removido->name);
                freeGame(removido);
                free(removido);
            }
        }
        else if (strcmp(operacao, "R*") == 0)
        {
            // Remover de posição específica
            int pos = atoi(input + 3);
            Game *removido = removerPosicao(lista, pos);
            if (removido != NULL)
            {
                printf("(R) %s\n", removido->name);
                freeGame(removido);
                free(removido);
            }
        }
    }
    // Imprimir lista final - DEVE VIR ANTES de liberarLista()
    imprimirLista(lista);

    // DEPOIS liberar memória
    liberarLista(lista);

    for (i = 0; i < gameCount; i++)
    {
        freeGame(&allGames[i]);
    }
    free(allGames);

    for (i = 0; i < MAX_IDS; i++)
    {
        free(ids[i]);
    }
    free(ids);

    return 0;
}

// Implementação das funções da lista dupla
ListaDupla *criarLista()
{
    ListaDupla *lista = (ListaDupla *)malloc(sizeof(ListaDupla));
    lista->primeiro = NULL;
    lista->ultimo = NULL;
    return lista;
}

void inserirInicio(ListaDupla *lista, Game *game)
{
    CelulaDupla *nova = (CelulaDupla *)malloc(sizeof(CelulaDupla));
    nova->game = game;
    nova->ant = NULL;
    nova->prox = lista->primeiro;

    if (lista->primeiro != NULL)
    {
        lista->primeiro->ant = nova;
    }
    else
    {
        lista->ultimo = nova;
    }
    lista->primeiro = nova;
}

void inserirFim(ListaDupla *lista, Game *game)
{
    CelulaDupla *nova = (CelulaDupla *)malloc(sizeof(CelulaDupla));
    nova->game = game;
    nova->prox = NULL;
    nova->ant = lista->ultimo;

    if (lista->ultimo != NULL)
    {
        lista->ultimo->prox = nova;
    }
    else
    {
        lista->primeiro = nova;
    }
    lista->ultimo = nova;
}

void inserirPosicao(ListaDupla *lista, Game *game, int pos)
{
    if (pos < 0 || pos > tamanhoLista(lista))
        return;

    if (pos == 0)
    {
        inserirInicio(lista, game);
        return;
    }
    if (pos == tamanhoLista(lista))
    {
        inserirFim(lista, game);
        return;
    }

    CelulaDupla *atual = lista->primeiro;
    for (int i = 0; i < pos; i++)
    {
        atual = atual->prox;
    }

    CelulaDupla *nova = (CelulaDupla *)malloc(sizeof(CelulaDupla));
    nova->game = game;
    nova->ant = atual->ant;
    nova->prox = atual;
    atual->ant->prox = nova;
    atual->ant = nova;
}

Game *removerInicio(ListaDupla *lista)
{
    if (lista->primeiro == NULL)
        return NULL;

    CelulaDupla *removida = lista->primeiro;
    Game *game = removida->game;

    lista->primeiro = removida->prox;
    if (lista->primeiro != NULL)
    {
        lista->primeiro->ant = NULL;
    }
    else
    {
        lista->ultimo = NULL;
    }

    free(removida);
    return game;
}

Game *removerFim(ListaDupla *lista)
{
    if (lista->ultimo == NULL)
        return NULL;

    CelulaDupla *removida = lista->ultimo;
    Game *game = removida->game;

    lista->ultimo = removida->ant;
    if (lista->ultimo != NULL)
    {
        lista->ultimo->prox = NULL;
    }
    else
    {
        lista->primeiro = NULL;
    }

    free(removida);
    return game;
}

Game *removerPosicao(ListaDupla *lista, int pos)
{
    if (pos < 0 || pos >= tamanhoLista(lista))
        return NULL;

    if (pos == 0)
        return removerInicio(lista);
    if (pos == tamanhoLista(lista) - 1)
        return removerFim(lista);

    CelulaDupla *atual = lista->primeiro;
    for (int i = 0; i < pos; i++)
    {
        atual = atual->prox;
    }

    Game *game = atual->game;
    atual->ant->prox = atual->prox;
    atual->prox->ant = atual->ant;

    free(atual);
    return game;
}

int tamanhoLista(ListaDupla *lista)
{
    int tam = 0;
    CelulaDupla *atual = lista->primeiro;
    while (atual != NULL)
    {
        tam++;
        atual = atual->prox;
    }
    return tam;
}

void imprimirLista(ListaDupla *lista)
{
    CelulaDupla *atual = lista->primeiro;
    int index = 0;
    while (atual != NULL)
    {
        printf("[%d] ", index++);
        printGame(atual->game);
        atual = atual->prox;
    }
}

void liberarLista(ListaDupla *lista)
{
    CelulaDupla *atual = lista->primeiro;
    while (atual != NULL)
    {
        CelulaDupla *prox = atual->prox;
        freeGame(atual->game);
        free(atual->game);
        free(atual);
        atual = prox;
    }
    free(lista);
}

// As funções parseAndLoadGame, printGame, freeGame, getNextField,
// splitString, trim, formatDate, printStringArray permanecem as mesmas
// da implementação anterior da pilha

// Função que preenche uma struct Game a partir de uma linha do CSV
void parseAndLoadGame(Game *game, char *line)
{
    int pos = 0;

    game->id = atoi(getNextField(line, &pos));
    game->name = getNextField(line, &pos);
    game->releaseDate = formatDate(getNextField(line, &pos));
    game->estimatedOwners = atoi(getNextField(line, &pos));

    char *priceStr = getNextField(line, &pos);
    game->price = (strcmp(priceStr, "Free to Play") == 0 || strlen(priceStr) == 0) ? 0.0f : atof(priceStr);
    free(priceStr);

    char *langStr = getNextField(line, &pos);
    langStr[strcspn(langStr, "]")] = 0;
    memmove(langStr, langStr + 1, strlen(langStr));
    for (int i = 0; langStr[i]; i++)
        if (langStr[i] == '\'')
            langStr[i] = ' ';
    game->supportedLanguages = splitString(langStr, ',', &game->supportedLanguagesCount);
    free(langStr);

    game->metacriticScore = atoi(getNextField(line, &pos));
    game->userScore = atof(getNextField(line, &pos));
    game->achievements = atoi(getNextField(line, &pos));

    game->publishers = splitString(getNextField(line, &pos), ',', &game->publishersCount);
    game->developers = splitString(getNextField(line, &pos), ',', &game->developersCount);
    game->categories = splitString(getNextField(line, &pos), ',', &game->categoriesCount);
    game->genres = splitString(getNextField(line, &pos), ',', &game->genresCount);
    game->tags = splitString(getNextField(line, &pos), ',', &game->tagsCount);
}

// Imprime uma struct Game no formato exigido
void printGame(Game *game)
{
    char formattedDate[12];
    strcpy(formattedDate, game->releaseDate);
    if (formattedDate[1] == '/')
    {
        memmove(formattedDate + 1, formattedDate, strlen(formattedDate) + 1);
        formattedDate[0] = '0';
    }

    printf("=> %d ## %s ## %s ## %d ## %.2f ## ",
           game->id, game->name, formattedDate, game->estimatedOwners, game->price);
    printStringArray(game->supportedLanguages, game->supportedLanguagesCount);
    printf(" ## %d ## %.1f ## %d ## ",
           game->metacriticScore,
           game->userScore,
           game->achievements);
    printStringArray(game->publishers, game->publishersCount);
    printf(" ## ");
    printStringArray(game->developers, game->developersCount);
    printf(" ## ");
    printStringArray(game->categories, game->categoriesCount);
    printf(" ## ");
    printStringArray(game->genres, game->genresCount);
    printf(" ## ");
    printStringArray(game->tags, game->tagsCount);
    printf(" ##\n");
}

// Libera a memória de uma única struct Game
void freeGame(Game *game)
{
    free(game->name);
    free(game->releaseDate);
    for (int i = 0; i < game->supportedLanguagesCount; i++)
        free(game->supportedLanguages[i]);
    free(game->supportedLanguages);
    for (int i = 0; i < game->publishersCount; i++)
        free(game->publishers[i]);
    free(game->publishers);
    for (int i = 0; i < game->developersCount; i++)
        free(game->developers[i]);
    free(game->developers);
    for (int i = 0; i < game->categoriesCount; i++)
        free(game->categories[i]);
    free(game->categories);
    for (int i = 0; i < game->genresCount; i++)
        free(game->genres[i]);
    free(game->genres);
    for (int i = 0; i < game->tagsCount; i++)
        free(game->tags[i]);
    free(game->tags);
}

// Pega o próximo campo do CSV, tratando aspas
char *getNextField(char *line, int *pos)
{
    char *field = (char *)malloc(sizeof(char) * MAX_FIELD_SIZE);
    int i = 0;
    bool inQuotes = false;

    if (line[*pos] == '"')
    {
        inQuotes = true;
        (*pos)++;
    }

    while (line[*pos] != '\0')
    {
        if (inQuotes)
        {
            if (line[*pos] == '"')
            {
                (*pos)++;
                break;
            }
        }
        else
        {
            if (line[*pos] == ',')
            {
                break;
            }
        }
        field[i++] = line[(*pos)++];
    }

    if (line[*pos] == ',')
    {
        (*pos)++;
    }

    field[i] = '\0';
    return field;
}

// Divide uma string em um array de strings
char **splitString(const char *str, char delimiter, int *count)
{
    int initialCount = 0;
    for (int i = 0; str[i]; i++)
        if (str[i] == delimiter)
            initialCount++;
    *count = initialCount + 1;

    char **result = (char **)malloc(sizeof(char *) * (*count));
    char buffer[MAX_FIELD_SIZE];
    int str_idx = 0;
    int result_idx = 0;

    for (int i = 0; i <= strlen(str); i++)
    {
        if (str[i] == delimiter || str[i] == '\0')
        {
            buffer[str_idx] = '\0';
            result[result_idx] = (char *)malloc(sizeof(char) * (strlen(buffer) + 1));
            strcpy(result[result_idx], trim(buffer));
            result_idx++;
            str_idx = 0;
        }
        else
        {
            buffer[str_idx++] = str[i];
        }
    }
    return result;
}

// Remove espaços das bordas
char *trim(char *str)
{
    char *end;
    while (isspace((unsigned char)*str))
        str++;
    if (*str == 0)
        return str;
    end = str + strlen(str) - 1;
    while (end > str && isspace((unsigned char)*end))
        end--;
    end[1] = '\0';
    return str;
}

// Formata a data para dd/MM/yyyy
char *formatDate(char *dateStr)
{
    char *formattedDate = (char *)malloc(sizeof(char) * 12);
    char monthStr[4] = {0};
    char day[3] = "01";
    char year[5] = "0000";

    sscanf(dateStr, "%s", monthStr);

    char *monthNum = "01";
    if (strcmp(monthStr, "Jan") == 0)
        monthNum = "01";
    else if (strcmp(monthStr, "Feb") == 0)
        monthNum = "02";
    else if (strcmp(monthStr, "Mar") == 0)
        monthNum = "03";
    else if (strcmp(monthStr, "Apr") == 0)
        monthNum = "04";
    else if (strcmp(monthStr, "May") == 0)
        monthNum = "05";
    else if (strcmp(monthStr, "Jun") == 0)
        monthNum = "06";
    else if (strcmp(monthStr, "Jul") == 0)
        monthNum = "07";
    else if (strcmp(monthStr, "Aug") == 0)
        monthNum = "08";
    else if (strcmp(monthStr, "Sep") == 0)
        monthNum = "09";
    else if (strcmp(monthStr, "Oct") == 0)
        monthNum = "10";
    else if (strcmp(monthStr, "Nov") == 0)
        monthNum = "11";
    else if (strcmp(monthStr, "Dec") == 0)
        monthNum = "12";

    char *ptr = dateStr;
    while (*ptr && !isdigit(*ptr))
        ptr++;
    if (isdigit(*ptr))
        sscanf(ptr, "%[^,], %s", day, year);

    sprintf(formattedDate, "%s/%s/%s", day, monthNum, year);
    free(dateStr);
    return formattedDate;
}

// Imprime um array de strings
void printStringArray(char **arr, int count)
{
    printf("[");
    for (int i = 0; i < count; i++)
    {
        printf("%s", arr[i]);
        if (i < count - 1)
        {
            printf(", ");
        }
    }
    printf("]");
}
