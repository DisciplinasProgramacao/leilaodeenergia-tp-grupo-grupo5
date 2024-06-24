package codigo.backtracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

import codigo.lance.Lance;

public class Backtracking {
    private static int melhorValor;
    private static List<Integer> melhorSolucao;
    private static int energiaUsada;

    public static Resultado resolverBacktracking(List<Lance> lances, int energiaDisponivel) {
        melhorValor = 0;
        melhorSolucao = new ArrayList<>();
        energiaUsada = 0;
        backtrack(lances, 0, energiaDisponivel, 0, new ArrayList<>(), 0);
        return new Resultado(melhorValor, melhorSolucao, energiaUsada);
    }

    private static void backtrack(List<Lance> lances, int i, int energiaRestante, int valorAtual, List<Integer> solucaoAtual, int energiaAtualUsada) {
        if (energiaRestante < 0) {
            return; 
        }
        if (i == lances.size()) {
            if (valorAtual > melhorValor) {
                melhorValor = valorAtual;
                melhorSolucao = new ArrayList<>(solucaoAtual);
                energiaUsada = energiaAtualUsada;
            }
            return;
        }

        // Não escolher o lance atual
        backtrack(lances, i + 1, energiaRestante, valorAtual, solucaoAtual, energiaAtualUsada);
       
        // Escolher o lance atual
        solucaoAtual.add(i);
        backtrack(lances, i + 1, energiaRestante - lances.get(i).quantidade, valorAtual + lances.get(i).valor, solucaoAtual, energiaAtualUsada + lances.get(i).quantidade);
        solucaoAtual.remove(solucaoAtual.size() - 1);
    }

    public static void main(String[] args) {
        int energiaDisponivel = 8000;
        String caminhoArquivo = "codigo/arquivo.txt"; 

        List<Lance> lances = lerLancesDoArquivo(caminhoArquivo);

        if (lances != null) {
            long inicio = System.nanoTime();
            Resultado resultado = resolverBacktracking(lances, energiaDisponivel);
            long tempo = System.nanoTime() - inicio;

            System.out.println("Tempo: " + (tempo / 1_000_000) + " ms");
            System.out.println("Valor: " + resultado.valorTotal);
            System.out.println("Energia usada: " + resultado.energiaUsada);
            System.out.print("Lances escolhidos: ");
            for (int indice : resultado.escolhas) {
                System.out.print(lances.get(indice).nome + " ");
            }
            System.out.println("\n");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("codigo/backtracking/resultados.txt", true))) {
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
        } else {
            energiaDisponivel = 1000;
            int T = 0;

            for (int tamanho = 1; ; tamanho++) {
                boolean dentroDoTempo = true;
                long tempomedio = 0;
                for (int i = 0; i < 10; i++) {
                    lances = gerarLances(tamanho);
                    long inicio = System.currentTimeMillis();
                    
                    resolverBacktracking(lances, energiaDisponivel);
                    long tempo = System.currentTimeMillis() - inicio;
                    System.out.println("Tempo: " + tempo + " ms - Tamanho: " + tamanho);
                    tempomedio += tempo;
                }
                tempomedio = tempomedio / 10;
                if (tempomedio > 30000) {
                    dentroDoTempo = false;
                }
                if (!dentroDoTempo) {
                    T = tamanho - 1;
                    break;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("codigo/backtracking/T.txt"))) {
                writer.write(String.valueOf(T));
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Tamanho máximo resolvível em 30 segundos: " + T);
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
