import java.util.*;

public class Fifo {

    private int[][] matriz;
    private double tempoMedioDeEspera;
    private double tempoMedioDeResposta;
    private double turnaroundMedio;

    public Fifo(int quantidadeDeProcessos, int tempoDeExecucaoTotal, List<Processo> processos) {
        
        // ------------------------CRIANDO A TABELA----------------------------------

        matriz = new int[quantidadeDeProcessos][tempoDeExecucaoTotal];
        // Para preencher a matriz:
        // 0 = fazendo nada
        // 1 = executando
        // 2 = esperando

        /*
            para cada i, uma linha da matriz:

            linha 1 = processo 1
            linha 2 = processo 2
            .
            .
            .
            linha n = processo n
        */

        for (int i=0; i<matriz.length; i++){
            for (int j = 0; j< matriz[0].length; j++){
                Processo p = processos.get(i);
                int chegada = p.getMomentoDeChegada();
                
                if (chegada > j || p.getRestingTime() == 0){ // O processo ainda nao chegou ou terminou de executar

                    matriz[i][j] = 0; // nao faz nada

                } else if (chegada <= j && p.getRestingTime()>0){ // o processo chegou e precisa executar
                    /*
                     verifica se naquele instante ha algum
                     processo que esta esperando ou executando
                     antes do processo atual
                    */
                    boolean livre = true;
                    for (int linha=0; linha < i; linha++){  
                        if (matriz[linha][j] != 0){
                            livre = false;
                        }
                    }

                    if (livre){
                        matriz[i][j] = 1;
                        p.diminuirRestingTime(); // executando

                    } else {
                        matriz[i][j] = 2; // esperando
                    }
                }
            }
        }

        // ------------------------ANALISANDO A TABELA----------------------------------
        for (int i=0; i<matriz.length; i++){
            Processo p = processos.get(i);
            for (int j = 0; j< matriz[0].length; j++){
                if(matriz[i][j] == 2){ // esperando
                    p.aumentarTempoDeEspera();
                    p.aumentarTempoDeResposta();
                }
            }
            p.setTurnaround();
        }
        
        double somaTempoDeEspera = 0;
        double somaTempoDeResposta = 0;
        double somaTurnaround = 0;

        for (Processo processo : processos){
            somaTempoDeEspera += processo.getTempoDeEspera();
            somaTempoDeResposta += processo.getTempoDeResposta();
            somaTurnaround += processo.getTurnaround();
        }

        tempoMedioDeEspera = somaTempoDeEspera / quantidadeDeProcessos;
        tempoMedioDeResposta = somaTempoDeResposta / quantidadeDeProcessos;
        turnaroundMedio = somaTurnaround / quantidadeDeProcessos;
    }

    public double getTempoMedioDeEspera(){
        return tempoMedioDeEspera;
    }

    public double getTempoMedioDeResposta(){
        return tempoMedioDeResposta;
    }

    public double getTurnaroundMedio(){
        return turnaroundMedio;
    }   
}
