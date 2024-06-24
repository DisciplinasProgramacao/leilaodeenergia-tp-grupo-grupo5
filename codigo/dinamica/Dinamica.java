package codigo.dinamica;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//  import codigo.lance.Lance;

public class Dinamica {

    public static Resultado resolverProgramacaoDinamica(List<Lance> lances, int energiaDisponivel) {
        int numLances = lances.size();
        int parte = energiaDisponivel/10;

        //inicia com tamanho +1 pra ter as linhas e colunas zeradas
        int[][] dp = new int[numLances + 1][energiaDisponivel + 1]; 
        List<List<Boolean>> escolhas = new ArrayList<>();

        for (int i = 0; i <= numLances; i++) {
            escolhas.add(new ArrayList<>());
            for (int j = 0; j <= energiaDisponivel; j++) {
                escolhas.get(i).add(false);
            }
        }

        for (int i = 1; i <= numLances; i++) {
            Lance lance = lances.get(i - 1);
            for (int j = 0; j <= energiaDisponivel; j++) {
                // Não escolher o lance atual
                dp[i][j] = dp[i - 1][j]; 

                if (j >= lance.quantidade) {
                    int novoValor = dp[i - 1][j - lance.quantidade] + lance.valor;

                    if (novoValor > dp[i][j]) {
                        dp[i][j] = novoValor;
                        escolhas.get(i).set(j, true);
                    }
                }
            }
        }

        // Rastrear os lances escolhidos e calcular a energia total usada
        List<Integer> lancesEscolhidos = new ArrayList<>();
        int energiaRestante = energiaDisponivel;
        int energiaUsada = 0;
        // formatar resultado
        for (int i = numLances; i > 0; i--) {
            if (escolhas.get(i).get(energiaRestante)) {
                lancesEscolhidos.add(i - 1);
                energiaRestante -= lances.get(i - 1).quantidade;
                energiaUsada += lances.get(i - 1).quantidade;
            }
        }

        return new Resultado(dp[numLances][energiaDisponivel], lancesEscolhidos, energiaUsada);
    }


    public static void main(String[] args) {
        int energiaDisponivel = 8000;
        String caminhoArquivo = "codigo/arquivo.txt"; 

        List<Lance> lances = lerLancesDoArquivo(caminhoArquivo);

        if (lances != null) {
            long inicio = System.nanoTime();
            Resultado resultado = resolverProgramacaoDinamica(lances, energiaDisponivel);
            long tempo = System.nanoTime() - inicio;

            System.out.println("Tempo: " + (tempo / 1_000_000) + " ms");
            System.out.println("Valor: " + resultado.valorTotal);
            System.out.println("Energia usada: " + resultado.energiaUsada);
            System.out.print("Lances escolhidos: ");
            for (int indice : resultado.escolhas) {
                System.out.print(lances.get(indice).nome + " ");
            }
            System.out.println("\n");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("codigo/dinamica/resultados.txt", true))) {
                writer.write("Tempo: " + (tempo / 1_000_000) + " ms\n");
                writer.write("Valor: " + resultado.valorTotal + "\n");
                writer.write("Energia usada: " + resultado.energiaUsada + "\n");
                writer.write("Lances escolhidos: ");
                for (int indice : resultado.escolhas) {
                    writer.write(lances.get(indice).nome + " ");
                }
                writer.write("\n\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
              energiaDisponivel = 1000;
        int T = lerTamanhoMaximo();
        StringBuilder resultados = new StringBuilder();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("codigo/dinamica/resultados.txt",true))) {
            for (int tamanho = T; tamanho <= 10 * T; tamanho += T) {
                long tempoTotal = 0;

                for (int i = 0; i < 10; i++) {
                     lances = gerarLances(tamanho);

                    long inicio = System.nanoTime();
                    Resultado resultado = resolverProgramacaoDinamica(lances, energiaDisponivel);
                    long tempo = System.nanoTime() - inicio;
                    tempoTotal += tempo;

                    resultados.append("Tamanho: ").append(tamanho).append("\n");
                    resultados.append("Tempo: ").append(tempo / 1_000_000).append(" ms\n");
                    resultados.append("Valor: ").append(resultado.valorTotal).append("\n");
                    resultados.append("Lances escolhidos: ");
                    for (int indice : resultado.escolhas) {
                        resultados.append("Empresa ").append(indice).append(" ");
                    }
                    resultados.append("\n\n");

                    System.out.println("Tamanho: " + tamanho);
                    System.out.println("Tempo: " + (tempo / 1_000_000) + " ms");
                    System.out.println("Valor: " + resultado.valorTotal);
                    System.out.print("Lances escolhidos: ");
                    for (int indice : resultado.escolhas) {
                        System.out.print("Empresa " + indice + " ");
                    }
                    System.out.println("\n");
                }

                long mediaTempo = tempoTotal / 10;
                resultados.append("Tamanho: ").append(tamanho).append(" - Média de tempo: ").append(mediaTempo / 1_000_000).append(" ms\n\n");
            }

            writer.write(resultados.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }
     private static List<Lance> gerarLances(int tamanho) {
        List<Lance> lances = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < tamanho; i++) {
            int quantidade = random.nextInt(1000) + 1;
            int valor = random.nextInt(1000) + 1;
            lances.add(new Lance(quantidade, valor));
        }
        return lances;
    }

    private static int lerTamanhoMaximo() {
        int T = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("codigo/backtracking/T.txt"))) {
            T = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return T;
    }

    private static List<Lance> lerLancesDoArquivo(String caminhoArquivo) {
        List<Lance> lances = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(caminhoArquivo))) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(";");
                if (partes.length == 3) {
                    String nome = partes[0];
                    int quantidade = Integer.parseInt(partes[1]);
                    int valor = Integer.parseInt(partes[2]);
                    lances.add(new Lance(nome, quantidade, valor));
                }
            }
        } catch (IOException e) {
            // e.printStackTrace();
            return null;
        }
        return lances;
    }
}

class Resultado {
    int valorTotal;
    List<Integer> escolhas;
    int energiaUsada;

    public Resultado(int valorTotal, List<Integer> escolhas, int energiaUsada) {
        this.valorTotal = valorTotal;
        this.escolhas = escolhas;
        this.energiaUsada = energiaUsada;
    }
}

class Lance {
    String nome;
    int quantidade;
    int valor;

    public Lance(String nome, int quantidade, int valor) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public Lance(int quantidade, int valor) {
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getValor() {
        return valor;
    }
}
