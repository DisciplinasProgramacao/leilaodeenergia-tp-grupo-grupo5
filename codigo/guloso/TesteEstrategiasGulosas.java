package codigo.guloso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TesteEstrategiasGulosas {

    public static void main(String[] args) {
        int energiaDisponivel = 1000;
        int T = 10; // tamanho dos conjuntos
        StringBuilder resultados = new StringBuilder();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("codigo/guloso/resultados.txt"))) {
            for (int tamanho = T; tamanho <= 10 * T; tamanho += T) {
                long tempoTotalEstrategia1 = 0;
                long tempoTotalEstrategia2 = 0;
                int valorTotalEstrategia1 = 0;
                int valorTotalEstrategia2 = 0;

                for (int i = 0; i < 10; i++) {
                    List<Lance> lances = gerarLances(tamanho);

                    long inicio = System.nanoTime();
                    valorTotalEstrategia1 += algoritimosGulosos.executarEstrategia1(lances, energiaDisponivel);
                    long tempoEstrategia1 = System.nanoTime() - inicio;
                    tempoTotalEstrategia1 += tempoEstrategia1;

                    inicio = System.nanoTime();
                    valorTotalEstrategia2 += algoritimosGulosos.executarEstrategia2(lances, energiaDisponivel);
                    long tempoEstrategia2 = System.nanoTime() - inicio;
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
                System.out.println("Estratégia 2 - Tempo médio: " + (mediaTempoEstrategia2 / 1_000_000) + " ms, Valor médio: " + mediaValorEstrategia2);
            }
            
            writer.write(resultados.toString());
        } catch (IOException e) {
            e.printStackTrace();
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
}
