import java.util.*;

public class RoundRobin{

    private int[][] matriz;
    private double tempoMedioDeEspera;
    private double tempoMedioDeResposta;
    private double turnaroundMedio;

    public RoundRobin(int quantidadeDeProcessos, int tempoDeExecucaoTotal, List<Processo> processos, int quantum){

        // ------------------------ CRIANDO A TABELA ----------------------------------

        matriz = new int[quantidadeDeProcessos][tempoDeExecucaoTotal];

        // Para preencher a matriz:
        // 0 = fazendo nada
        // 1 = executando
        // 2 = esperando

        Queue<Processo> fila = new LinkedList<>();
        List<Processo> aindaNaoChegaram = new ArrayList<>(processos); // controle de quem ainda não entrou na fila
        Processo processoAtual = null;
        int tempoRestanteQuantum = 0;

        for (int j = 0; j < matriz[0].length; j++) { // coluna - tempo

            // 1) Adiciona à fila quem chegou exatamente neste instante j
            Iterator<Processo> it = aindaNaoChegaram.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                if (p.getMomentoDeChegada() <= j) {
                    fila.add(p);
                    it.remove();
                }
            }

            // 2) Se o processo atual estourou o quantum ou terminou, ele sai da CPU
            if (processoAtual != null && (tempoRestanteQuantum == 0 || processoAtual.getRestingTime() <= 0)) {
                if (processoAtual.getRestingTime() > 0) {
                    fila.add(processoAtual); // volta pro fim da fila se ainda não terminou
                }
                processoAtual = null;
            }

            // 3) Se não há processo rodando, pega o próximo da fila
            if (processoAtual == null && !fila.isEmpty()) {
                processoAtual = fila.poll();
                tempoRestanteQuantum = quantum;
            }

            // 4) Executa o processo atual (se houver)
            if (processoAtual != null) {
                int indice = processos.indexOf(processoAtual);
                matriz[indice][j] = 1; // executando
                processoAtual.diminuirRestingTime();
                tempoRestanteQuantum--;
            }

            // 5) Marca quem está esperando (já chegou, ainda não terminou, não é quem está executando)
            for (int k = 0; k < matriz.length; k++) {
                Processo p = processos.get(k);
                if (p.getMomentoDeChegada() <= j && p.getRestingTime() > 0 && matriz[k][j] != 1) {
                    matriz[k][j] = 2; // esperando
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
