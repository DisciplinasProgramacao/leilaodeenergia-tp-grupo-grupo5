package codigo.lance;

public class Lance {
    public int quantidade;
    public int valor;

    public Lance(int quantidade, int valor) {
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public double valorPorMegawatt() {
        return (double) valor / quantidade;
    }
}