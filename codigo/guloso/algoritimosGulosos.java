package codigo.guloso;
import java.util.Comparator;
import java.util.List;

import codigo.lance.Lance;

public class algoritimosGulosos {

    public static int executarEstrategia1(List<Lance> lances, int energiaDisponivel) {
        lances.sort(Comparator.comparingDouble(Lance::valorPorMegawatt).reversed());

        int energiaVendida = 0;
        int valorTotal = 0;

        for (Lance lance : lances) {
            if (energiaVendida + lance.quantidade <= energiaDisponivel) {
                energiaVendida += lance.quantidade;
                valorTotal += lance.valor;
            }
        }

        return valorTotal;
    }

    public static int executarEstrategia2(List<Lance> lances, int energiaDisponivel) {
        lances.sort(Comparator.comparingInt(lance -> -lance.valor));

        int energiaVendida = 0;
        int valorTotal = 0;

        for (Lance lance : lances) {
            if (energiaVendida + lance.quantidade <= energiaDisponivel) {
                energiaVendida += lance.quantidade;
                valorTotal += lance.valor;
            }
        }

        return valorTotal;
    }
}
