import java.util.*;
public class Fifo {

    private Processo[][] matriz;

    public Fifo(int quantidadeDeProcessos, int tempoDeExecucaoTotal) {
        matriz = new Processo[quantidadeDeProcessos][tempoDeExecucaoTotal];
        // 0 = fazendo nada
        // 1 = executando
        // 2 = esperando
        for (int j = 0; j< matriz[0].length; j++){
            for(int i = 0; i < matriz.length; i++){

            }
        }
    }



}
