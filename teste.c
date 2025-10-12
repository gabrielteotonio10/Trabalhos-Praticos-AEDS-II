#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <strings.h> 
#include <math.h> // Necessário para fabs e fmodf

#define MAX_LINE_SIZE 10000
#define MAX_LIST_SIZE 100

// Definições de Estruturas (MANTIDAS)
typedef struct {
    char** items;
    int count;
} StringList;

typedef struct {
    int id;
    char name[500];
    char releaseDate[50];
    int estimatedOwners;
    float price;
    StringList supportedLanguages;
    int metacriticScore;
    float userScore;
    int achievements;
    StringList publishers;
    StringList developers;
    StringList categories;
    StringList genres;
    StringList tags;
} Game;

int contador = 0; 

// --- Funções Auxiliares para StringList (MANTIDAS) ---

void initStringList(StringList* list) {
    list->items = (char**)malloc(MAX_LIST_SIZE * sizeof(char*));
    list->count = 0;
}

void addToStringList(StringList* list, const char* item) {
    if (list->count < MAX_LIST_SIZE) {
        list->items[list->count] = (char*)malloc(strlen(item) + 1);
        strcpy(list->items[list->count], item);
        list->count++;
    }
}

void freeStringList(StringList* list) {
    for (int i = 0; i < list->count; i++) {
        free(list->items[i]);
    }
    free(list->items);
    list->count = 0;
}

char* formatarLista(StringList* lista) {
    if (lista->count == 0) {
        char* result = (char*)malloc(3);
        strcpy(result, "[]");
        return result;
    }
    
    int totalSize = 3; 
    for (int i = 0; i < lista->count; i++) {
        totalSize += strlen(lista->items[i]) + 2;
    }
    
    char* result = (char*)malloc(totalSize);
    strcpy(result, "[");
    
    for (int i = 0; i < lista->count; i++) {
        strcat(result, lista->items[i]);
        if (i < lista->count - 1) {
            strcat(result, ", ");
        }
    }
    
    strcat(result, "]");
    return result;
}

// --- Funções de Limpeza e Parsing de Campo (MANTIDAS) ---

void cleanToken(char* token) {
    if (token == NULL || *token == '\0') return;

    char* src = token;
    while (*src == ' ' || *src == '\'' || *src == '"' || *src == '[' || *src == ']') {
        src++;
    }
    
    if (src != token) {
        memmove(token, src, strlen(src) + 1);
    }

    int len = strlen(token);
    while (len > 0 && (token[len-1] == ' ' || token[len-1] == '\'' || token[len-1] == '"' || token[len-1] == ']' || token[len-1] == '[')) {
        token[--len] = '\0';
    }
}

char* lerCampo(const char* linha) {
    char* campo = (char*)malloc(MAX_LINE_SIZE);
    int campoIndex = 0;
    
    if (contador >= strlen(linha)) {
        strcpy(campo, "");
        return campo;
    }
    
    if (linha[contador] == '"') {
        contador++;
        while (contador < strlen(linha) && linha[contador] != '"') {
            campo[campoIndex++] = linha[contador++];
        }
        contador++;
        
        if (contador < strlen(linha) && linha[contador] == ',') {
            contador++; 
        }
    } else {
        while (contador < strlen(linha) && linha[contador] != ',') {
            campo[campoIndex++] = linha[contador++];
        }
        
        if (contador < strlen(linha) && linha[contador] == ',') {
            contador++; 
        }
    }
    
    campo[campoIndex] = '\0';
    return campo;
}

// --- Funções de Parsing Específicas com Normalização (MANTIDAS) ---

int lerId(const char* linha) {
    char* campo = lerCampo(linha);
    int id = atoi(campo);
    free(campo);
    return id;
}

char* lerName(const char* linha) {
    char* name = lerCampo(linha);
    return name;
}

char* lerDataLancamento(const char* linha) {
    char* campo = lerCampo(linha);
    char* dataFormatada = (char*)malloc(50);
    
    char mesStr[10] = "";
    int diaInt = 0;
    int anoInt = 0;
    
    if (sscanf(campo, "%3s %d, %d", mesStr, &diaInt, &anoInt) < 3) {
        diaInt = 1;
        sscanf(campo, "%3s %d", mesStr, &anoInt);
    }
    
    if (diaInt == 0) diaInt = 1;

    const char* m;
    if (strcasecmp(mesStr, "Jan") == 0) m = "01";
    else if (strcasecmp(mesStr, "Feb") == 0) m = "02";
    else if (strcasecmp(mesStr, "Mar") == 0) m = "03";
    else if (strcasecmp(mesStr, "Apr") == 0) m = "04";
    else if (strcasecmp(mesStr, "May") == 0) m = "05";
    else if (strcasecmp(mesStr, "Jun") == 0) m = "06";
    else if (strcasecmp(mesStr, "Jul") == 0) m = "07";
    else if (strcasecmp(mesStr, "Aug") == 0) m = "08";
    else if (strcasecmp(mesStr, "Sep") == 0) m = "09";
    else if (strcasecmp(mesStr, "Oct") == 0) m = "10";
    else if (strcasecmp(mesStr, "Nov") == 0) m = "11";
    else if (strcasecmp(mesStr, "Dec") == 0) m = "12";
    else m = "01"; 

    sprintf(dataFormatada, "%02d/%s/%d", diaInt, m, anoInt);
    
    free(campo);
    return dataFormatada;
}

int lerEstimativaJogadores(const char* linha) {
    char* campo = lerCampo(linha);
    char num[20] = "";
    int numIndex = 0;
    
    for (int i = 0; i < strlen(campo); i++) {
        if (isdigit(campo[i])) {
            num[numIndex++] = campo[i];
        }
    }
    num[numIndex] = '\0';
    free(campo);
    
    return numIndex == 0 ? 0 : atoi(num);
}

float lerPreco(const char* linha) {
    char* campo = lerCampo(linha);
    float price = 0.0f;
    
    if (strcasecmp(campo, "Free to Play") == 0) {
        price = 0.0f;
    } else {
        price = atof(campo);
    }
    free(campo);
    return price;
}

StringList lerArray(const char* linha) {
    StringList lista;
    initStringList(&lista);
    
    char* campo = lerCampo(linha);
    if (strlen(campo) == 0) {
        free(campo);
        return lista;
    }

    char temp[MAX_LINE_SIZE];
    strcpy(temp, campo);
    free(campo);

    char* token = strtok(temp, ",");
    while (token != NULL) {
        cleanToken(token);
        
        if (strlen(token) > 0) {
            addToStringList(&lista, token);
        }
        token = strtok(NULL, ",");
    }
    
    return lista;
}

StringList lerIdiomas(const char* linha) { return lerArray(linha); }
StringList lerPublishers(const char* linha) { return lerArray(linha); }
StringList lerDevelopers(const char* linha) { return lerArray(linha); }
StringList lerCategories(const char* linha) { return lerArray(linha); }
StringList lerGenres(const char* linha) { return lerArray(linha); }
StringList lerTags(const char* linha) { return lerArray(linha); }


int lerNotaCritica(const char* linha) {
    char* campo = lerCampo(linha);
    int score = -1;
    
    if (strlen(campo) > 0) {
        score = atoi(campo);
    }
    free(campo);
    return score;
}

float lerNotaUsuario(const char* linha) {
    char* campo = lerCampo(linha);
    float score = -1.0f;
    
    if (strlen(campo) > 0 && strcasecmp(campo, "tbd") != 0) {
        score = atof(campo);
    }
    free(campo);
    return score;
}

int lerConquistas(const char* linha) {
    char* campo = lerCampo(linha);
    int achievements = 0;
    
    if (strlen(campo) > 0) {
        achievements = atoi(campo);
    }
    free(campo);
    return achievements;
}

void initGame(Game* game) {
    game->id = 0;
    strcpy(game->name, "");
    strcpy(game->releaseDate, "");
    game->estimatedOwners = 0;
    game->price = 0.0f;
    game->metacriticScore = -1;
    game->userScore = -1.0f;
    game->achievements = 0;
    initStringList(&game->supportedLanguages);
    initStringList(&game->publishers);
    initStringList(&game->developers);
    initStringList(&game->categories);
    initStringList(&game->genres);
    initStringList(&game->tags);
}

void freeGame(Game* game) {
    freeStringList(&game->supportedLanguages);
    freeStringList(&game->publishers);
    freeStringList(&game->developers);
    freeStringList(&game->categories);
    freeStringList(&game->genres);
    freeStringList(&game->tags);
}

// --- Função Principal ---

int main() {
    FILE* file = fopen("/tmp/games.csv", "r");
    if (file == NULL) {
        printf("Arquivo não encontrado em /tmp/games.csv!\n");
        return 1;
    }
    
    char line[MAX_LINE_SIZE];
    Game* gamesList = (Game*)malloc(20000 * sizeof(Game));
    int gameCount = 0;
    
    // Pula cabeçalho
    fgets(line, sizeof(line), file);
    
    while (fgets(line, sizeof(line), file)) {
        if (strlen(line) > 0 && isdigit(line[0])) {
            contador = 0;
            
            int id = lerId(line);
            char* name = lerName(line);
            char* releaseDate = lerDataLancamento(line);
            int estimatedOwners = lerEstimativaJogadores(line);
            float price = lerPreco(line);
            StringList supportedLanguages = lerIdiomas(line);
            int metacriticScore = lerNotaCritica(line);
            float userScore = lerNotaUsuario(line);
            int achievements = lerConquistas(line);
            StringList publishers = lerPublishers(line);
            StringList developers = lerDevelopers(line);
            StringList categories = lerCategories(line);
            StringList genres = lerGenres(line);
            StringList tags = lerTags(line);
            
            initGame(&gamesList[gameCount]);
            gamesList[gameCount].id = id;
            strcpy(gamesList[gameCount].name, name);
            strcpy(gamesList[gameCount].releaseDate, releaseDate);
            gamesList[gameCount].estimatedOwners = estimatedOwners;
            gamesList[gameCount].price = price;
            gamesList[gameCount].supportedLanguages = supportedLanguages;
            gamesList[gameCount].metacriticScore = metacriticScore;
            gamesList[gameCount].userScore = userScore;
            gamesList[gameCount].achievements = achievements;
            gamesList[gameCount].publishers = publishers;
            gamesList[gameCount].developers = developers;
            gamesList[gameCount].categories = categories;
            gamesList[gameCount].genres = genres;
            gamesList[gameCount].tags = tags;
            
            gameCount++;
            
            free(name);
            free(releaseDate);
        }
    }
    fclose(file);
    
    char input[100];
    while (1) {
        if (fgets(input, sizeof(input), stdin) == NULL) break;
        input[strcspn(input, "\n")] = 0;
        
        if (strcasecmp(input, "FIM") == 0) break;
        
        int idBusca = atoi(input);
        Game* encontrado = NULL;
        
        for (int i = 0; i < gameCount; i++) {
            if (gamesList[i].id == idBusca) {
                encontrado = &gamesList[i];
                break;
            }
        }
        
        if (encontrado != NULL) {
            char* langs = formatarLista(&encontrado->supportedLanguages);
            char* pubs = formatarLista(&encontrado->publishers);
            char* devs = formatarLista(&encontrado->developers);
            char* cats = formatarLista(&encontrado->categories);
            char* gens = formatarLista(&encontrado->genres);
            char* tgs = formatarLista(&encontrado->tags);
            
            char metacriticStr[5];
            if (encontrado->metacriticScore == -1) {
                strcpy(metacriticStr, "N/A");
            } else {
                sprintf(metacriticStr, "%d", encontrado->metacriticScore);
            }
            
            char userScoreStr[10];
            if (encontrado->userScore == -1.0f) {
                strcpy(userScoreStr, "N/A");
            } else {
                sprintf(userScoreStr, "%.1f", encontrado->userScore);
            }

            // CORREÇÃO FINAL APLICADA AQUI: Formato do preço
            char priceStr[10];
            // Multiplicamos por 100 e checamos o resto. Se for zero (e.g., 7.50, 8.00), formatamos com 1 casa decimal.
            // Se o resto for diferente de zero (e.g., 29.99), formatamos com 2 casas.
            if (fmodf(roundf(encontrado->price * 100.0f), 10.0f) == 0.0f) {
                 sprintf(priceStr, "%.1f", encontrado->price);
            } else {
                 sprintf(priceStr, "%.2f", encontrado->price);
            }
            
            printf("=> %d ## %s ## %s ## %d ## %s ## %s ## %s ## %s ## %d ## %s ## %s ## %s ## %s ## %s ##\n",
                   encontrado->id, encontrado->name, encontrado->releaseDate,
                   encontrado->estimatedOwners, priceStr, langs, 
                   metacriticStr, 
                   userScoreStr,  
                   encontrado->achievements, pubs, devs, cats, gens, tgs);
            
            free(langs);
            free(pubs);
            free(devs);
            free(cats);
            free(gens);
            free(tgs);
        } else {
            printf("=> %d ## N/A\n", idBusca);
        }
    }
    
    // Libera memória
    for (int i = 0; i < gameCount; i++) {
        freeGame(&gamesList[i]);
    }
    free(gamesList);
    
    return 0;
}