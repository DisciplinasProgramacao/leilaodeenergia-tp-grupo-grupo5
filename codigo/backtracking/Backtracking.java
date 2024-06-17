package codigo.backtracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import codigo.lance.Lance;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Backtracking {
    private static int melhorValor;

    public static int resolverBacktracking(List<Lance> lances, int energiaDisponivel) {
        melhorValor = 0;
        backtrack(lances, 0, energiaDisponivel, 0);
        return melhorValor;
    }

    private static void backtrack(List<Lance> lances, int i, int energiaRestante, int valorAtual) {
        if (energiaRestante < 0) {
            return; 
        }
        if (i == lances.size()) {

            if (valorAtual > melhorValor) {
                 melhorValor = valorAtual;
            }
            return;
        }

        backtrack(lances, i + 1, energiaRestante, valorAtual);
       

   
        backtrack(lances, i + 1, energiaRestante - lances.get(i).quantidade, valorAtual + lances.get(i).valor);
    }

    public static void main(String[] args) {
        int energiaDisponivel = 1000;
        int tempoLimite = 30 * 1000; // 30 segundos
        int T = 0;

        for (int tamanho = 1; ; tamanho++) {
            boolean dentroDoTempo = true;
            long tempomedio = 0;
            for (int i = 0; i < 10; i++) {
                List<Lance> lances = gerarLances(tamanho);
                long inicio = System.currentTimeMillis();
                
                resolverBacktracking(lances, energiaDisponivel);
                long tempo = System.currentTimeMillis() - inicio;
                System.out.println("tempo: " + tempo +" - tamanho: "+ tamanho);
                tempomedio+=tempo;
            }
            tempomedio=tempomedio/10;
            if (tempomedio > tempoLimite) {
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
