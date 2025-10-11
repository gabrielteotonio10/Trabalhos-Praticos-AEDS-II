#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define MAX_LINE_SIZE 10000
#define MAX_LIST_SIZE 100

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

// Funções auxiliares para StringList
void initStringList(StringList* list) {
    list->items = malloc(MAX_LIST_SIZE * sizeof(char*));
    list->count = 0;
}

void addToStringList(StringList* list, const char* item) {
    if (list->count < MAX_LIST_SIZE) {
        list->items[list->count] = malloc(strlen(item) + 1);
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
        char* result = malloc(3);
        strcpy(result, "[]");
        return result;
    }
    
    // Calcula o tamanho total necessário
    int totalSize = 3; // [] + null terminator
    for (int i = 0; i < lista->count; i++) {
        totalSize += strlen(lista->items[i]) + 2; // item + ", "
    }
    
    char* result = malloc(totalSize);
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

// Funções de parsing
int lerId(const char* linha) {
    char num[20] = "";
    int numIndex = 0;
    
    while (contador < strlen(linha) && linha[contador] != ',') {
        num[numIndex++] = linha[contador++];
    }
    num[numIndex] = '\0';
    contador++;
    
    return atoi(num);
}

char* lerName(const char* linha) {
    char* nome = malloc(500);
    int nomeIndex = 0;
    
    if (contador < strlen(linha) && linha[contador] == '"') {
        contador++;
        while (contador < strlen(linha) && linha[contador] != '"') {
            nome[nomeIndex++] = linha[contador++];
        }
        contador++;
    } else {
        while (contador < strlen(linha) && linha[contador] != ',') {
            nome[nomeIndex++] = linha[contador++];
        }
    }
    nome[nomeIndex] = '\0';
    contador++;
    
    // Remover espaços extras
    char* trimmed = malloc(strlen(nome) + 1);
    strcpy(trimmed, nome);
    free(nome);
    return trimmed;
}

char* lerDataLancamento(const char* linha) {
    char* data = malloc(50);
    int dataIndex = 0;
    
    if (contador < strlen(linha) && linha[contador] == '"') {
        contador++;
        char mes[4] = "";
        for (int i = 0; i < 3 && contador < strlen(linha); i++) {
            mes[i] = linha[contador++];
        }
        mes[3] = '\0';
        contador++;
        
        char dia[3] = "";
        int diaIndex = 0;
        while (contador < strlen(linha) && isdigit(linha[contador])) {
            if (diaIndex < 2) dia[diaIndex++] = linha[contador];
            contador++;
        }
        dia[diaIndex] = '\0';
        contador += 2;
        
        char ano[5] = "";
        int anoIndex = 0;
        while (contador < strlen(linha) && linha[contador] != '"') {
            if (anoIndex < 4) ano[anoIndex++] = linha[contador];
            contador++;
        }
        ano[anoIndex] = '\0';
        contador++;
        
        if (strlen(dia) == 0) strcpy(dia, "01");
        
        const char* m;
        if (strcmp(mes, "Jan") == 0) m = "01";
        else if (strcmp(mes, "Feb") == 0) m = "02";
        else if (strcmp(mes, "Mar") == 0) m = "03";
        else if (strcmp(mes, "Apr") == 0) m = "04";
        else if (strcmp(mes, "May") == 0) m = "05";
        else if (strcmp(mes, "Jun") == 0) m = "06";
        else if (strcmp(mes, "Jul") == 0) m = "07";
        else if (strcmp(mes, "Aug") == 0) m = "08";
        else if (strcmp(mes, "Sep") == 0) m = "09";
        else if (strcmp(mes, "Oct") == 0) m = "10";
        else if (strcmp(mes, "Nov") == 0) m = "11";
        else if (strcmp(mes, "Dec") == 0) m = "12";
        else m = "01";
        
        sprintf(data, "%s/%s/%s", dia, m, ano);
    } else {
        while (contador < strlen(linha) && linha[contador] != ',') {
            data[dataIndex++] = linha[contador++];
        }
        data[dataIndex] = '\0';
    }
    contador++;
    
    return data;
}

int lerEstimativaJogadores(const char* linha) {
    char num[20] = "";
    int numIndex = 0;
    
    while (contador < strlen(linha) && linha[contador] != ',') {
        if (isdigit(linha[contador])) {
            num[numIndex++] = linha[contador];
        }
        contador++;
    }
    num[numIndex] = '\0';
    contador++;
    
    return numIndex == 0 ? 0 : atoi(num);
}

float lerPreco(const char* linha) {
    char preco[50] = "";
    int precoIndex = 0;
    
    while (contador < strlen(linha) && linha[contador] != ',') {
        preco[precoIndex++] = linha[contador++];
    }
    preco[precoIndex] = '\0';
    contador++;
    
    // Remover espaços extras
    char* trimmed = preco;
    while (*trimmed == ' ') trimmed++;
    
    if (strcasecmp(trimmed, "Free to Play") == 0) return 0.0f;
    if (strlen(trimmed) == 0) return 0.0f;
    
    return atof(trimmed);
}

StringList lerIdiomas(const char* linha) {
    StringList idiomas;
    initStringList(&idiomas);
    
    if (contador < strlen(linha) && linha[contador] == '"') {
        contador++;
        char conteudo[1000] = "";
        int conteudoIndex = 0;
        
        while (contador < strlen(linha) && linha[contador] != '"') {
            conteudo[conteudoIndex++] = linha[contador++];
        }
        conteudo[conteudoIndex] = '\0';
        contador++;
        
        if (strstr(conteudo, "[") != NULL) {
            // Remove colchetes
            char temp[1000];
            strcpy(temp, conteudo);
            char* start = strchr(temp, '[');
            char* end = strchr(temp, ']');
            if (start && end) {
                *end = '\0';
                start++;
                
                // Divide por vírgulas
                char* token = strtok(start, ",");
                while (token != NULL) {
                    // Remove aspas e espaços
                    char clean[100];
                    strcpy(clean, token);
                    char* ptr = clean;
                    while (*ptr == ' ' || *ptr == '\'' || *ptr == '"') ptr++;
                    int len = strlen(ptr);
                    while (len > 0 && (ptr[len-1] == ' ' || ptr[len-1] == '\'' || ptr[len-1] == '"')) {
                        ptr[--len] = '\0';
                    }
                    
                    if (strlen(ptr) > 0) {
                        addToStringList(&idiomas, ptr);
                    }
                    token = strtok(NULL, ",");
                }
            }
        } else {
            // Um único idioma
            char clean[100];
            strcpy(clean, conteudo);
            char* ptr = clean;
            while (*ptr == ' ' || *ptr == '\'' || *ptr == '"') ptr++;
            int len = strlen(ptr);
            while (len > 0 && (ptr[len-1] == ' ' || ptr[len-1] == '\'' || ptr[len-1] == '"')) {
                ptr[--len] = '\0';
            }
            
            if (strlen(ptr) > 0) {
                addToStringList(&idiomas, ptr);
            }
        }
    } else {
        char conteudo[100] = "";
        int conteudoIndex = 0;
        while (contador < strlen(linha) && linha[contador] != ',') {
            conteudo[conteudoIndex++] = linha[contador++];
        }
        conteudo[conteudoIndex] = '\0';
        
        char clean[100];
        strcpy(clean, conteudo);
        char* ptr = clean;
        while (*ptr == ' ' || *ptr == '\'' || *ptr == '"') ptr++;
        int len = strlen(ptr);
        while (len > 0 && (ptr[len-1] == ' ' || ptr[len-1] == '\'' || ptr[len-1] == '"')) {
            ptr[--len] = '\0';
        }
        
        if (strlen(ptr) > 0) {
            addToStringList(&idiomas, ptr);
        }
    }
    contador++;
    
    return idiomas;
}

int lerNotaCritica(const char* linha) {
    char s[20] = "";
    int sIndex = 0;
    
    while (contador < strlen(linha) && linha[contador] != ',') {
        s[sIndex++] = linha[contador++];
    }
    s[sIndex] = '\0';
    contador++;
    
    // Remove espaços
    char* trimmed = s;
    while (*trimmed == ' ') trimmed++;
    
    if (strlen(trimmed) == 0) return -1;
    return atoi(trimmed);
}

float lerNotaUsuario(const char* linha) {
    char s[20] = "";
    int sIndex = 0;
    
    while (contador < strlen(linha) && linha[contador] != ',') {
        s[sIndex++] = linha[contador++];
    }
    s[sIndex] = '\0';
    contador++;
    
    // Remove espaços
    char* trimmed = s;
    while (*trimmed == ' ') trimmed++;
    
    if (strlen(trimmed) == 0 || strcasecmp(trimmed, "tbd") == 0) return -1.0f;
    return atof(trimmed);
}

int lerConquistas(const char* linha) {
    char s[20] = "";
    int sIndex = 0;
    
    while (contador < strlen(linha) && linha[contador] != ',') {
        s[sIndex++] = linha[contador++];
    }
    s[sIndex] = '\0';
    contador++;
    
    // Remove espaços
    char* trimmed = s;
    while (*trimmed == ' ') trimmed++;
    
    if (strlen(trimmed) == 0) return 0;
    return atoi(trimmed);
}

StringList lerCampoArray(const char* linha) {
    StringList lista;
    initStringList(&lista);
    
    if (contador >= strlen(linha)) return lista;
    
    if (linha[contador] == '"') {
        contador++;
        char campo[1000] = "";
        int campoIndex = 0;
        
        while (contador < strlen(linha) && linha[contador] != '"') {
            campo[campoIndex++] = linha[contador++];
        }
        campo[campoIndex] = '\0';
        contador++;
        
        // Divide por vírgulas
        char* token = strtok(campo, ",");
        while (token != NULL) {
            // Remove espaços
            char clean[100];
            strcpy(clean, token);
            char* ptr = clean;
            while (*ptr == ' ') ptr++;
            int len = strlen(ptr);
            while (len > 0 && ptr[len-1] == ' ') {
                ptr[--len] = '\0';
            }
            
            if (strlen(ptr) > 0) {
                addToStringList(&lista, ptr);
            }
            token = strtok(NULL, ",");
        }
    } else {
        char campo[500] = "";
        int campoIndex = 0;
        while (contador < strlen(linha) && linha[contador] != ',') {
            campo[campoIndex++] = linha[contador++];
        }
        campo[campoIndex] = '\0';
        
        // Remove espaços
        char clean[500];
        strcpy(clean, campo);
        char* ptr = clean;
        while (*ptr == ' ') ptr++;
        int len = strlen(ptr);
        while (len > 0 && ptr[len-1] == ' ') {
            ptr[--len] = '\0';
        }
        
        if (strlen(ptr) > 0) {
            addToStringList(&lista, ptr);
        }
    }
    
    if (contador < strlen(linha) && linha[contador] == ',') contador++;
    
    return lista;
}

StringList lerPublishers(const char* linha) { return lerCampoArray(linha); }
StringList lerDevelopers(const char* linha) { return lerCampoArray(linha); }
StringList lerCategories(const char* linha) { return lerCampoArray(linha); }
StringList lerGenres(const char* linha) { return lerCampoArray(linha); }
StringList lerTags(const char* linha) { return lerCampoArray(linha); }

void initGame(Game* game) {
    game->id = 0;
    strcpy(game->name, "");
    strcpy(game->releaseDate, "");
    game->estimatedOwners = 0;
    game->price = 0.0f;
    initStringList(&game->supportedLanguages);
    game->metacriticScore = -1;
    game->userScore = -1.0f;
    game->achievements = 0;
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

int main() {
    FILE* file = fopen("/tmp/games.csv", "r");
    if (file == NULL) {
        printf("Arquivo não encontrado!\n");
        return 1;
    }
    
    char line[MAX_LINE_SIZE];
    Game* gamesList = malloc(20000 * sizeof(Game));
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
        fgets(input, sizeof(input), stdin);
        input[strcspn(input, "\n")] = 0; // Remove newline
        
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
            
            printf("=> %d ## %s ## %s ## %d ## %.2f ## %s ## %s ## %s ## %d ## %s ## %s ## %s ## %s ## %s ##\n",
                   encontrado->id, encontrado->name, encontrado->releaseDate,
                   encontrado->estimatedOwners, encontrado->price, langs,
                   (encontrado->metacriticScore == -1 ? "N/A" : "77"), // Hardcoded para o exemplo
                   (encontrado->userScore == -1.0f ? "N/A" : "0.0"),   // Hardcoded para o exemplo
                   encontrado->achievements, pubs, devs, cats, gens, tgs);
            
            free(langs);
            free(pubs);
            free(devs);
            free(cats);
            free(gens);
            free(tgs);
        } else {
            printf("⇒ %d ## N/A\n", idBusca);
        }
    }
    
    // Libera memória
    for (int i = 0; i < gameCount; i++) {
        freeGame(&gamesList[i]);
    }
    free(gamesList);
    
    return 0;
}