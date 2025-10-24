import java.util.*;
import java.io.*;
import java.util.Locale;

public class gamesH {
    // ---------- ATRIBUTOS ----------
    public int id;
    public String name;
    public String releaseDate;
    public int estimatedOwners;
    public float price;
    public String[] supportedLanguages;
    public int metacriticScore;
    public float userScore;
    public int achievements;
    public String[] publishers;
    public String[] developers;
    public String[] categories;
    public String[] genres;
    public String[] tags;

    // ---------- Construtor ----------
    public gamesH() {
    }

    // ---------- ParseInt ----------
    public int ParseInt(String s) {
        int resultado = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9')
                resultado = resultado * 10 + (c - '0');
        }
        return resultado;
    }

    // ---------- ParseFloat ----------
    public float ParseFloat(String s) {
        float resultado = 0.0f;
        boolean ponto = false;
        float divisor = 10.0f;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                int digito = c - '0';
                if (!ponto)
                    resultado = resultado * 10 + digito;
                else {
                    resultado += digito / divisor;
                    divisor *= 10;
                }
            } else if (c == '.')
                ponto = true;
        }
        return resultado;
    }

    // ---------- manualEquals ----------
    public boolean manualEquals(String s1, String s2) {
        if (s1 == null || s2 == null)
            return false;
        if (s1.length() != s2.length())
            return false;
        for (int i = 0; i < s1.length(); i++)
            if (s1.charAt(i) != s2.charAt(i))
                return false;
        return true;
    }

    // ---------- manualSubstring ----------
    public String manualSubstring(String s, int begin, int end) {
        String r = "";
        for (int i = begin; i < end; i++)
            r += s.charAt(i);
        return r;
    }

    // ---------- manualIsEmpty ----------
    public boolean manualIsEmpty(String s) {
        return s == null || s.length() == 0;
    }

    // ---------- manualTrim ----------
    public String manualTrim(String s) {
        int inicio = 0, fim = s.length() - 1;
        while (inicio <= fim && s.charAt(inicio) == ' ')
            inicio++;
        while (fim >= inicio && s.charAt(fim) == ' ')
            fim--;
        if (inicio > fim)
            return "";
        return manualSubstring(s, inicio, fim + 1);
    }

    // ---------- formatarData ----------
    public String formatarData(String data) {
        if (manualIsEmpty(data))
            return "00/00/0000";
        if (data.length() >= 2 && data.charAt(0) == '"' && data.charAt(data.length() - 1) == '"')
            data = manualSubstring(data, 1, data.length() - 1);

        String[] partes = new String[3];
        int j = 0;
        String temp = "";
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            if (c == ' ') {
                partes[j++] = temp;
                temp = "";
            } else
                temp += c;
        }
        partes[j] = temp;

        if (partes.length < 3)
            return "00/00/0000";

        String diaComVirgula = partes[1];
        String dia = "";
        for (int i = 0; i < diaComVirgula.length(); i++) {
            char c = diaComVirgula.charAt(i);
            if (c != ',')
                dia += c;
        }

        String mes = partes[0];
        String ano = partes[2];
        String numeroMes = switch (mes) {
            case "Jan" -> "01";
            case "Feb" -> "02";
            case "Mar" -> "03";
            case "Apr" -> "04";
            case "May" -> "05";
            case "Jun" -> "06";
            case "Jul" -> "07";
            case "Aug" -> "08";
            case "Sep" -> "09";
            case "Oct" -> "10";
            case "Nov" -> "11";
            case "Dec" -> "12";
            default -> "00";
        };

        if (dia.length() == 1)
            dia = "0" + dia;
        return dia + "/" + numeroMes + "/" + ano;
    }

    // ---------- formatarColchetes ----------
    public String[] formatarColchetes(String campo) {
        if (campo == null)
            return new String[0];
        if (campo.length() >= 2 && campo.charAt(0) == '"' && campo.charAt(campo.length() - 1) == '"')
            campo = manualSubstring(campo, 1, campo.length() - 1);
        if (campo.length() >= 2 && campo.charAt(0) == '[' && campo.charAt(campo.length() - 1) == ']')
            campo = manualSubstring(campo, 1, campo.length() - 1);

        if (manualIsEmpty(campo))
            return new String[0];

        // Contar vírgulas
        int count = 1;
        for (int i = 0; i < campo.length(); i++)
            if (campo.charAt(i) == ',')
                count++;

        String[] lista = new String[count];
        int idx = 0;
        String temp = "";
        for (int i = 0; i < campo.length(); i++) {
            char c = campo.charAt(i);
            if (c == ',' && temp.length() > 0) {
                lista[idx++] = manualTrim(temp);
                temp = "";
            } else if (c != '\'')
                temp += c;
        }
        if (temp.length() > 0)
            lista[idx++] = manualTrim(temp);

        // Ajustar tamanho exato
        if (idx < count) {
            String[] resultado = new String[idx];
            for (int i = 0; i < idx; i++)
                resultado[i] = lista[i];
            return resultado;
        }

        return lista;
    }

    // ---------- splitManual ----------
    public String[] splitManual(String linha) {
        int count = 0;
        boolean dentroAspas = false, dentroColchete = false;
        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c == '"')
                dentroAspas = !dentroAspas;
            else if (c == '[')
                dentroColchete = true;
            else if (c == ']')
                dentroColchete = false;
            else if (c == ',' && !dentroAspas && !dentroColchete)
                count++;
        }
        String[] campos = new String[count + 1];
        int j = 0;
        String temp = "";
        dentroAspas = false;
        dentroColchete = false;
        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c == '"')
                dentroAspas = !dentroAspas;
            else if (c == '[')
                dentroColchete = true;
            else if (c == ']')
                dentroColchete = false;
            if (c == ',' && !dentroAspas && !dentroColchete) {
                campos[j++] = temp;
                temp = "";
            } else
                temp += c;
        }
        campos[j] = temp;
        return campos;
    }

    // ---------- HeapSort crescente ----------
    public void heapSort(gamesH[] vetor) {
        int n = vetor.length;
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(vetor, n, i);
        for (int i = n - 1; i > 0; i--) {
            swap(vetor, 0, i);
            heapify(vetor, i, 0);
        }
    }

    public void heapify(gamesH[] vetor, int n, int i) {
        int maior = i;
        int esq = 2 * i + 1, dir = 2 * i + 2;
        if (esq < n && (vetor[esq].estimatedOwners > vetor[maior].estimatedOwners ||
                (vetor[esq].estimatedOwners == vetor[maior].estimatedOwners && vetor[esq].id > vetor[maior].id)))
            maior = esq;
        if (dir < n && (vetor[dir].estimatedOwners > vetor[maior].estimatedOwners ||
                (vetor[dir].estimatedOwners == vetor[maior].estimatedOwners && vetor[dir].id > vetor[maior].id)))
            maior = dir;
        if (maior != i) {
            swap(vetor, i, maior);
            heapify(vetor, n, maior);
        }
    }

    public void swap(gamesH[] vetor, int a, int b) {
        gamesH tmp = vetor[a];
        vetor[a] = vetor[b];
        vetor[b] = tmp;
    }

    // ---------- toString ----------
    @Override
    public String toString() {
        String dataFormatada = formatarData(this.releaseDate);

        // criar arrays concatenados manualmente
        String sl = "[";
        for (int i = 0; i < supportedLanguages.length; i++) {
            sl += supportedLanguages[i];
            if (i != supportedLanguages.length - 1)
                sl += ", ";
        }
        sl += "]";

        String pubs = "[";
        for (int i = 0; i < publishers.length; i++) {
            pubs += publishers[i];
            if (i != publishers.length - 1)
                pubs += ", ";
        }
        pubs += "]";

        String devs = "[";
        for (int i = 0; i < developers.length; i++) {
            devs += developers[i];
            if (i != developers.length - 1)
                devs += ", ";
        }
        devs += "]";

        String cats = "[";
        for (int i = 0; i < categories.length; i++) {
            cats += categories[i];
            if (i != categories.length - 1)
                cats += ", ";
        }
        cats += "]";

        String gens = "[";
        for (int i = 0; i < genres.length; i++) {
            gens += genres[i];
            if (i != genres.length - 1)
                gens += ", ";
        }
        gens += "]";

        String tgs = "[";
        for (int i = 0; i < tags.length; i++) {
            tgs += tags[i];
            if (i != tags.length - 1)
                tgs += ", ";
        }
        tgs += "]";

        String priceFormatado = String.format(Locale.US, "%.2f", this.price);
        String userScoreFormatado = String.format(Locale.US, "%.1f", this.userScore);

        return "=> " + id + " ## " + name + " ## " + dataFormatada + " ## " + estimatedOwners + " ## " + priceFormatado
                + " ## " + sl + " ## " + metacriticScore + " ## " + userScoreFormatado + " ## " + achievements
                + " ## " + pubs + " ## " + devs + " ## " + cats + " ## " + gens + " ## " + tgs + " ##";
    }

    // ---------- MAIN ----------
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        gamesH g = new gamesH();

        // usar array grande fixo em vez de ArrayList
        gamesH[] lista = new gamesH[1000]; // supondo no máximo 1000 jogos
        int count = 0;

        String idStr = sc.nextLine();
        while (!g.manualEquals(idStr, "FIM")) {
            BufferedReader br = new BufferedReader(new FileReader("/tmp/games.csv"));
            br.readLine();
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = g.splitManual(linha);
                if (g.ParseInt(campos[0]) == g.ParseInt(idStr)) {
                    gamesH novo = new gamesH();
                    novo.id = g.ParseInt(campos[0]);
                    novo.name = campos[1];
                    novo.releaseDate = campos[2];
                    novo.estimatedOwners = g.ParseInt(campos[3]);
                    novo.price = g.manualEquals(campos[4], "Free to Play") ? 0.0f : g.ParseFloat(campos[4]);
                    novo.supportedLanguages = g.formatarColchetes(campos[5]);
                    novo.metacriticScore = g.manualIsEmpty(campos[6]) ? -1 : g.ParseInt(campos[6]);
                    novo.userScore = g.manualIsEmpty(campos[7]) || g.manualEquals(campos[7], "tbd") ? -1.0f
                            : g.ParseFloat(campos[7]);
                    novo.achievements = g.manualIsEmpty(campos[8]) ? 0 : g.ParseInt(campos[8]);
                    novo.publishers = g.formatarColchetes(campos[9]);
                    novo.developers = g.formatarColchetes(campos[10]);
                    novo.categories = g.formatarColchetes(campos[11]);
                    novo.genres = g.formatarColchetes(campos[12]);
                    novo.tags = g.formatarColchetes(campos[13]);
                    lista[count++] = novo;
                    break;
                }
            }
            br.close();
            idStr = sc.nextLine();
        }
        sc.close();

        // criar array exato
        gamesH[] vetor = new gamesH[count];
        for (int i = 0; i < count; i++)
            vetor[i] = lista[i];

        g.heapSort(vetor);

        for (int i = 0; i < vetor.length; i++) {
            System.out.println(vetor[i].toString());
        }
    }
}