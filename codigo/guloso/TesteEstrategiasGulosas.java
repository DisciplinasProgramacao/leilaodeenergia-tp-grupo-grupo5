package codigo.guloso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import codigo.lance.Lance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TesteEstrategiasGulosas {

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
    public static void main(String[] args) {
        String caminhoArquivo = "codigo/arquiavo.txt"; 

        List<Lance> lances = lerLancesDoArquivo(caminhoArquivo);
        StringBuilder resultados = new StringBuilder();
        if (lances != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("codigo/guloso/resultados.txt",true))) {
                int energiaDisponivel = 8000;
                    long tempoTotalEstrategia1 = 0;
                    long tempoTotalEstrategia2 = 0;
                    int valorTotalEstrategia1 = 0;
                    int valorTotalEstrategia2 = 0;
    
                    for (int i = 0; i < 10; i++) {
                        // lances = gerarLances(tamanho);
    
                        long inicio1 = System.nanoTime();
                        valorTotalEstrategia1 += algoritimosGulosos.executarEstrategia1(lances, energiaDisponivel);
                        long tempoEstrategia1 = System.nanoTime() - inicio1;
                        tempoTotalEstrategia1 += tempoEstrategia1;
    
                        long inicio2 = System.nanoTime();
                        valorTotalEstrategia2 += algoritimosGulosos.executarEstrategia2(lances, energiaDisponivel);
                        long tempoEstrategia2 = System.nanoTime() - inicio2;
                        tempoTotalEstrategia2 += tempoEstrategia2;
                    }
    
                    long mediaTempoEstrategia1 = tempoTotalEstrategia1 / 10;
                    long mediaTempoEstrategia2 = tempoTotalEstrategia2 / 10;
                    int mediaValorEstrategia1 = valorTotalEstrategia1 / 10;
                    int mediaValorEstrategia2 = valorTotalEstrategia2 / 10;
    
                    
                    resultados.append("Estratégia 1 - Tempo médio: ").append(tempoTotalEstrategia1 / 1_000_000).append(" ms, Valor : ").append(valorTotalEstrategia1).append("\n");
                    resultados.append("Estratégia 2 - Tempo médio: ").append(tempoTotalEstrategia2 / 1_000_000).append(" ms, Valor : ").append(valorTotalEstrategia2).append("\n");
                    resultados.append("\n");
                    
                    
                    System.out.println("Estratégia 1 - Tempo médio: " + (tempoTotalEstrategia1 / 1_000_000) + " ms, Valor : " + valorTotalEstrategia1);
                    System.out.println("Estratégia 2 - Tempo médio: " + (tempoTotalEstrategia2 / 1_000_000) + " ms, Valor : " + valorTotalEstrategia2);
                
                
                writer.write(resultados.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {

        int energiaDisponivel = 1000;
        int T =lerTamanhoMaximo(); // tamanho dos conjuntos
        
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("codigo/guloso/resultados.txt",true))) {
            for (int tamanho = T; tamanho <= 10 * T; tamanho += T) {
                long tempoTotalEstrategia1 = 0;
                long tempoTotalEstrategia2 = 0;
                int valorTotalEstrategia1 = 0;
                int valorTotalEstrategia2 = 0;

                for (int i = 0; i < 10; i++) {
                    lances = gerarLances(tamanho);

                    long inicio1 = System.nanoTime();
                    valorTotalEstrategia1 += algoritimosGulosos.executarEstrategia1(lances, energiaDisponivel);
                    long tempoEstrategia1 = System.nanoTime() - inicio1;
                    tempoTotalEstrategia1 += tempoEstrategia1;

                    long inicio2 = System.nanoTime();
                    valorTotalEstrategia2 += algoritimosGulosos.executarEstrategia2(lances, energiaDisponivel);
                    long tempoEstrategia2 = System.nanoTime() - inicio2;
                    tempoTotalEstrategia2 += tempoEstrategia2;
                }

                long mediaTempoEstrategia1 = tempoTotalEstrategia1 / 10;
                long mediaTempoEstrategia2 = tempoTotalEstrategia2 / 10;
                int mediaValorEstrategia1 = valorTotalEstrategia1 / 10;
                int mediaValorEstrategia2 = valorTotalEstrategia2 / 10;

                resultados.append("Tamanho: ").append(tamanho).append("\n");
                resultados.append("Estratégia 1 - Tempo médio: ").append(mediaTempoEstrategia1 / 1_000_000).append(" ms, Valor médio: ").append(mediaValorEstrategia1).append("\n");
                resultados.append("Estratégia 2 - Tempo médio: ").append(mediaTempoEstrategia2 / 1_000_000).append(" ms, Valor médio: ").append(mediaValorEstrategia2).append("\n");
                resultados.append("\n");
                
                System.out.println("Tamanho: " + tamanho);
                System.out.println("Estratégia 1 - Tempo médio: " + (mediaTempoEstrategia1 / 1_000_000) + " ms, Valor médio: " + mediaValorEstrategia1);
                System.out.println("Estratégia 2 - Tempo médio: " + (tempoTotalEstrategia2 / 1_000_000) + " ms, Valor médio: " + mediaValorEstrategia2);
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

}
