public class Processo {
    private int tempoDeExecucao;
    private int restingTime;
    private int momentoDeChegada;
    private int tempoDeEspera;
    private int tempoDeResposta;
    private int turnaround;

    // Quando receber do documento, transformar String para número
    public Processo(int tempoDeExecucao, int momentoDeChegada){
        this.tempoDeExecucao = tempoDeExecucao;
        this.momentoDeChegada = momentoDeChegada;
        restingTime = tempoDeExecucao;
        tempoDeEspera = 0;
        tempoDeResposta = 0;
        turnaround = 0;
    }

    public void setTurnaround(){
        turnaround = tempoDeExecucao + tempoDeEspera;
    }

    public int getTurnaround(){
        return turnaround;
    }

    public int getTempoDeExecucao(){
        return tempoDeExecucao;
    }

    public int getMomentoDeChegada(){
        return momentoDeChegada;
    }

    public void diminuirRestingTime(){
        restingTime-=1;
    }

    public int getRestingTime(){
        return restingTime;
    }
    public int getTempoDeResposta() {
        return tempoDeResposta;
    }

    public void setTempoDeResposta(int tempoDeResposta) {
        this.tempoDeResposta = tempoDeResposta;
    }

    public int getTempoDeEspera() {
        return tempoDeEspera;
    }

    public void setTempoDeEspera(int tempoDeEspera) {
        this.tempoDeEspera = tempoDeEspera;
    }

    public void aumentarTempoDeEspera(){
        tempoDeEspera+=1;
    }

    public void aumentarTempoDeResposta(){
        tempoDeResposta+=1;
    }
}
