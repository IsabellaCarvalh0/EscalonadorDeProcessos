import java.util.List;

public class Srt {
    private int[][] matriz;
    private double tempoMedioDeEspera;
    private double tempoMedioDeResposta;
    private double turnaroundMedio;

    public Srt(int quantidadeDeProcessos, int tempoDeExecucaoTotal, List<Processo> processos){

        // ------------------------CRIANDO A TABELA----------------------------------

        matriz = new int[quantidadeDeProcessos][tempoDeExecucaoTotal];

        // Para preencher a matriz:
        // 0 = fazendo nada
        // 1 = executando
        // 2 = esperando

        for (int j = 0; j < matriz[0].length; j++) { // coluna - tempo

            int menorProcesso = -1;

            for (int i = 0; i < matriz.length; i++) { // linha - processo
                Processo p = processos.get(i);
                if (p.getMomentoDeChegada() <= j && p.getRestingTime() > 0) {
                    if (menorProcesso == -1) {
                        menorProcesso = i;
                    } else if (p.getRestingTime() < processos.get(menorProcesso).getRestingTime()) {
                        menorProcesso = i;
                    }
                }
            }

            if (menorProcesso != -1) {
                matriz[menorProcesso][j] = 1; // executando
                processos.get(menorProcesso).diminuirRestingTime();
            }

            for (int k = 0; k < matriz.length; k++) {
                Processo p = processos.get(k);
                if (p.getMomentoDeChegada() <= j && p.getRestingTime() > 0 && matriz[k][j] != 1) {
                    matriz[k][j] = 2; // esperando
                }
            }
        }

        for (int i = 0; i < matriz.length; i++){
            Processo p = processos.get(i);
            int indiceInicio = -1;

            for (int j = 0; j < matriz[0].length; j++){
                if (matriz[i][j] == 2){
                    p.aumentarTempoDeEspera();
                }
                if (matriz[i][j] == 1 && indiceInicio == -1){
                    indiceInicio = j;
                }
            }

            p.setTempoDeResposta(indiceInicio - p.getMomentoDeChegada());
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

        tempoMedioDeEspera = (double) somaTempoDeEspera / quantidadeDeProcessos;
        tempoMedioDeResposta = (double) somaTempoDeResposta / quantidadeDeProcessos;
        turnaroundMedio = (double) somaTurnaround / quantidadeDeProcessos;
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
