public class Processo {
    private int tempoDeExecucao;
    private int momentoDeChegada;
    private int tempoDeEspera;
    private int tempoDeResposta;

    // Quando receber do documento, transformar String para número
    public Processo(int tempoDeExecucao, int momentoDeChegada){
        this.tempoDeExecucao = tempoDeExecucao;
        this.momentoDeChegada = momentoDeChegada;
        tempoDeEspera = 0;
        tempoDeResposta = 0;
    }

    public int getTempoDeExecucao(){
        return tempoDeExecucao;
    }

    public int getMomentoDeChegada(){
        return momentoDeChegada;
    }

    public void diminuirTempoDeExecucao(){
        tempoDeExecucao -=1;
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
