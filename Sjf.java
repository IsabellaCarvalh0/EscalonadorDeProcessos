public class Sjf {
    private int[][] matriz;
    private double tempoMedioDeEspera;
    private double tempoMedioDeResposta;
    private double turnaroundMedio;

    public Sjf(int quantidadeDeProcessos, int tempoDeExecucaoTotal, List<Processo> processos){

        // ------------------------CRIANDO A TABELA----------------------------------

        matriz = new int[quantidadeDeProcessos][tempoDeExecucaoTotal];

        // Para preencher a matriz:
        // 0 = fazendo nada
        // 1 = executando
        // 2 = esperando

        int processoAtual = -1; // processo em execução (não-preemptivo)

        for (int j = 0; j < matriz[0].length; j++) { // coluna - tempo

            // Se não há processo em execução (ou o atual já terminou), escolhe um novo
            if (processoAtual == -1 || processos.get(processoAtual).getRestingTime() <= 0) {
                processoAtual = -1;
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
                processoAtual = menorProcesso;
            }

            if (processoAtual != -1) {
                matriz[processoAtual][j] = 1; // executando
                processos.get(processoAtual).diminuirRestingTime();
            }

            for (int k = 0; k < matriz.length; k++) {
                Processo p = processos.get(k);
                if (p.getMomentoDeChegada() <= j && p.getRestingTime() > 0 && matriz[k][j] != 1) {
                    matriz[k][j] = 2; // esperando
                }
            }
        }

         // ------------------------ANALISANDO A TABELA----------------------------------
        for (int i=0; i<matriz.length; i++){
            Processo p = processos.get(i);
            int indiceInicio = -1;

            // Para verificar tempo total de espera:
            for (int j = 0; j< matriz[0].length; j++){
                if(matriz[i][j] == 2){ // esperando
                    p.aumentarTempoDeEspera();
                }
            }

            // Para verificar tempo de resposta: 
            for (int k = 0; k < matriz[0].length; k++){
                if (matriz[i][k] == 1){
                    indiceInicio = k;
                    break;
                }
            }

            p.setTempoDeResposta((indiceInicio - p.getMomentoDeChegada()));
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
