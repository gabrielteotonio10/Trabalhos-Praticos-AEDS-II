#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>

#define MAX_LINE_SIZE 4096
#define MAX_FIELD_SIZE 512
#define MAX_ARRAY_ELEMENTS 50

//Estrutura para armazenar os dados de um jogo
typedef struct {
    int id;
    char* name;
    char* releaseDate;
    int estimatedOwners;
    float price;
    char** supportedLanguages;
    int supportedLanguagesCount;
    int metacriticScore;
    float userScore;
    int achievements;
    char** publishers;
    int publishersCount;
    char** developers;
    int developersCount;
    char** categories;
    int categoriesCount;
    char** genres;
    int genresCount;
    char** tags;
    int tagsCount;
} Game;

// Variáveis globais para contagem
int comparacoes = 0;
int movimentacoes = 0;

void parseAndLoadGame(Game* game, char* line);
void printGame(Game* game);
void freeGame(Game* game);
char* getNextField(char* line, int* pos);
char** splitString(const char* str, char delimiter, int* count);
char* trim(char* str);
char* formatDate(char* dateStr);
void printStringArray(char** arr, int count);
void quicksort(Game* games, int esq, int dir);
int partition(Game* games, int esq, int dir);
void swap(Game* a, Game* b);
int compareGames(Game* a, Game* b);
void criarLog(const char* matricula, int comp, int mov, double tempo);
void parseDate(const char* dateStr, int* day, int* month, int* year);

//Lógica Principal
int main() {
    char lineBuffer[MAX_LINE_SIZE];
    const char* filePath = "/tmp/games.csv";
    
    FILE* file = fopen(filePath, "r");
    if (file == NULL) {
        perror("Erro ao abrir o arquivo");
        return 1;
    }
    
    int gameCount = 0;
    fgets(lineBuffer, MAX_LINE_SIZE, file); 
    while (fgets(lineBuffer, MAX_LINE_SIZE, file) != NULL) {
        gameCount++;
    }
    fclose(file);

    Game* allGames = (Game*) malloc(sizeof(Game) * gameCount);
    if (allGames == NULL) {
        printf("Erro de alocação de memória\n");
        return 1;
    }
    
    file = fopen(filePath, "r");
    if (file == NULL) {
        perror("Erro ao reabrir o arquivo");
        free(allGames);
        return 1;
    }

    fgets(lineBuffer, MAX_LINE_SIZE, file); 
    int i = 0;
    while (fgets(lineBuffer, MAX_LINE_SIZE, file) != NULL) {
        parseAndLoadGame(&allGames[i], lineBuffer);
        i++;
    }
    fclose(file);

    char inputId[MAX_FIELD_SIZE];
    Game* gamesParaOrdenar = (Game*) malloc(sizeof(Game) * gameCount);
    int countParaOrdenar = 0;
    
    while (fgets(inputId, MAX_FIELD_SIZE, stdin) != NULL) {
        inputId[strcspn(inputId, "\n")] = 0;
        if (strcmp(inputId, "FIM") == 0) {
            break;
        }

        int targetId = atoi(inputId);
        for (i = 0; i < gameCount; i++) {
            if (allGames[i].id == targetId) {

                gamesParaOrdenar[countParaOrdenar] = allGames[i];
                countParaOrdenar++;
                break;
            }
        }
    }

    clock_t inicio = clock();
    quicksort(gamesParaOrdenar, 0, countParaOrdenar - 1);
    clock_t fim = clock();
    double tempo = ((double)(fim - inicio)) / CLOCKS_PER_SEC;

    for (i = 0; i < countParaOrdenar; i++) {
        printGame(&gamesParaOrdenar[i]);
    }

    criarLog("SUA_MATRICULA", comparacoes, movimentacoes, tempo);

    free(gamesParaOrdenar);
    
    for (i = 0; i < gameCount; i++) {
        freeGame(&allGames[i]);
    }
    free(allGames);

    return 0;
}

void parseDate(const char* dateStr, int* day, int* month, int* year) {
    sscanf(dateStr, "%d/%d/%d", day, month, year);
}

// Função de comparação entre dois jogos
int compareGames(Game* a, Game* b) {
    comparacoes++;
    
    int dayA, monthA, yearA;
    int dayB, monthB, yearB;
    
    parseDate(a->releaseDate, &dayA, &monthA, &yearA);
    parseDate(b->releaseDate, &dayB, &monthB, &yearB);

    if (yearA != yearB) {
        return yearA - yearB;
    }

    if (monthA != monthB) {
        return monthA - monthB;
    }
    
    if (dayA != dayB) {
        return dayA - dayB;
    }
    
    // Em caso de empate na data completa, compara pelo ID
    if (a->id < b->id) return -1;
    if (a->id > b->id) return 1;
    return 0;
}

// Função para trocar dois jogos
void swap(Game* a, Game* b) {
    Game temp = *a;
    *a = *b;
    *b = temp;
    movimentacoes += 3; // Cada swap conta como 3 movimentações
}

// Função de partição do Quicksort
int partition(Game* games, int esq, int dir) {
    Game pivo = games[dir];
    int i = esq - 1;
    
    for (int j = esq; j < dir; j++) {
        if (compareGames(&games[j], &pivo) <= 0) {
            i++;
            swap(&games[i], &games[j]);
        }
    }
    
    swap(&games[i + 1], &games[dir]);
    return i + 1;
}

// Implementação do Quicksort
void quicksort(Game* games, int esq, int dir) {
    if (esq < dir) {
        int indicePivo = partition(games, esq, dir);
        quicksort(games, esq, indicePivo - 1);
        quicksort(games, indicePivo + 1, dir);
    }
}

// Função para criar arquivo de log
void criarLog(const char* matricula, int comp, int mov, double tempo) {
    FILE* logFile = fopen("suamatricula_quicksort.txt", "w");
    if (logFile != NULL) {
        fprintf(logFile, "%s\t%d\t%d\t%f\n", matricula, comp, mov, tempo);
        fclose(logFile);
    }
}

// Função que preenche uma struct Game a partir de uma linha do CSV
void parseAndLoadGame(Game* game, char* line) {
    int pos = 0;

    game->id = atoi(getNextField(line, &pos));
    game->name = getNextField(line, &pos);
    game->releaseDate = formatDate(getNextField(line, &pos));
    game->estimatedOwners = atoi(getNextField(line, &pos));
    
    char* priceStr = getNextField(line, &pos);
    game->price = (strcmp(priceStr, "Free to Play") == 0 || strlen(priceStr) == 0) ? 0.0f : atof(priceStr);
    free(priceStr);

    char* langStr = getNextField(line, &pos);
    langStr[strcspn(langStr, "]")] = 0; 
    memmove(langStr, langStr + 1, strlen(langStr)); 
    for(int i = 0; langStr[i]; i++) if(langStr[i] == '\'') langStr[i] = ' ';
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
void printGame(Game* game) {
    char formattedDate[12];
    strcpy(formattedDate, game->releaseDate);
    if(formattedDate[1] == '/') {
        memmove(formattedDate + 1, formattedDate, strlen(formattedDate) + 1);
        formattedDate[0] = '0';
    }

    char priceStr[16];
    if (game->price == 0.0f) {
        strcpy(priceStr, "0.0");  
    } else {
        sprintf(priceStr, "%.2f", game->price);
        
        char *dot = strchr(priceStr, '.');
        if (dot != NULL) {
            char *end = priceStr + strlen(priceStr) - 1;
            
            while (end > dot && *end == '0') {
                *end = '\0';
                end--;
            }
            
            if (end == dot) {
                *dot = '\0';
            }
        }
    }

    // Formata userScore para mostrar 0.0 quando for 0
    char userScoreStr[16];
    if (game->userScore == 0.0f) {
        strcpy(userScoreStr, "0.0");
    } else {
        sprintf(userScoreStr, "%.1f", game->userScore);
    }

    printf("=> %d ## %s ## %s ## %d ## %s ## ", 
        game->id, game->name, formattedDate, game->estimatedOwners, priceStr);
    printStringArray(game->supportedLanguages, game->supportedLanguagesCount);
    printf(" ## %d ## %s ## %d ## ", 
        game->metacriticScore == 0 ? -1 : game->metacriticScore, 
        userScoreStr, 
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
void freeGame(Game* game) {
    free(game->name);
    free(game->releaseDate);
    for (int i = 0; i < game->supportedLanguagesCount; i++) free(game->supportedLanguages[i]);
    free(game->supportedLanguages);
    for (int i = 0; i < game->publishersCount; i++) free(game->publishers[i]);
    free(game->publishers);
    for (int i = 0; i < game->developersCount; i++) free(game->developers[i]);
    free(game->developers);
    for (int i = 0; i < game->categoriesCount; i++) free(game->categories[i]);
    free(game->categories);
    for (int i = 0; i < game->genresCount; i++) free(game->genres[i]);
    free(game->genres);
    for (int i = 0; i < game->tagsCount; i++) free(game->tags[i]);
    free(game->tags);
}

// Pega o próximo campo do CSV, tratando aspas
char* getNextField(char* line, int* pos) {
    char* field = (char*) malloc(sizeof(char) * MAX_FIELD_SIZE);
    int i = 0;
    bool inQuotes = false;
    
    if (line[*pos] == '"') {
        inQuotes = true;
        (*pos)++;
    }
    
    while (line[*pos] != '\0') {
        if (inQuotes) {
            if (line[*pos] == '"') {
                (*pos)++;
                break;
            }
        } else {
            if (line[*pos] == ',') {
                break;
            }
        }
        field[i++] = line[(*pos)++];
    }
    
    if (line[*pos] == ',') {
        (*pos)++;
    }
    
    field[i] = '\0';
    return field;
}

// Divide uma string em um array de strings
char** splitString(const char* str, char delimiter, int* count) {
    int initialCount = 0;
    for(int i = 0; str[i]; i++) if(str[i] == delimiter) initialCount++;
    *count = initialCount + 1;

    char** result = (char**) malloc(sizeof(char*) * (*count));
    char buffer[MAX_FIELD_SIZE];
    int str_idx = 0;
    int result_idx = 0;

    for (int i = 0; i <= strlen(str); i++) {
        if (str[i] == delimiter || str[i] == '\0') {
            buffer[str_idx] = '\0';
            result[result_idx] = (char*) malloc(sizeof(char) * (strlen(buffer) + 1));
            strcpy(result[result_idx], trim(buffer));
            result_idx++;
            str_idx = 0;
        } else {
            buffer[str_idx++] = str[i];
        }
    }
    return result;
}

// Remove espaços das bordas
char* trim(char* str) {
    char *end;
    while(isspace((unsigned char)*str)) str++;
    if(*str == 0) return str;
    end = str + strlen(str) - 1;
    while(end > str && isspace((unsigned char)*end)) end--;
    end[1] = '\0';
    return str;
}

// Formata a data para dd/MM/yyyy
char* formatDate(char* dateStr) {
    char* formattedDate = (char*) malloc(sizeof(char) * 12);
    char monthStr[4] = {0};
    char day[3] = "01";
    char year[5] = "0000";

    sscanf(dateStr, "%s", monthStr);

    char* monthNum = "01";
    if (strcmp(monthStr, "Feb") == 0) monthNum = "02";
    else if (strcmp(monthStr, "Mar") == 0) monthNum = "03";
    else if (strcmp(monthStr, "Apr") == 0) monthNum = "04";
    else if (strcmp(monthStr, "May") == 0) monthNum = "05";
    else if (strcmp(monthStr, "Jun") == 0) monthNum = "06";
    else if (strcmp(monthStr, "Jul") == 0) monthNum = "07";
    else if (strcmp(monthStr, "Aug") == 0) monthNum = "08";
    else if (strcmp(monthStr, "Sep") == 0) monthNum = "09";
    else if (strcmp(monthStr, "Oct") == 0) monthNum = "10";
    else if (strcmp(monthStr, "Nov") == 0) monthNum = "11";
    else if (strcmp(monthStr, "Dec") == 0) monthNum = "12";

    char* ptr = dateStr;
    while(*ptr && !isdigit(*ptr)) ptr++;
    if(isdigit(*ptr)) sscanf(ptr, "%[^,], %s", day, year);
    
    sprintf(formattedDate, "%s/%s/%s", day, monthNum, year);
    free(dateStr);
    return formattedDate;
}

// Imprime um array de strings
void printStringArray(char** arr, int count) {
    printf("[");
    for (int i = 0; i < count; i++) {
        printf("%s", arr[i]);
        if (i < count - 1) {
            printf(", ");
        }
    }
    printf("]");
}