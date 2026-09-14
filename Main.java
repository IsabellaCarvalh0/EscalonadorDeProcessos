import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        for (int i = 1; i <= 10; i++) {

            String endereco;
            String enderecoArquivoCriado;

            if (i == 10) {
                endereco = "D:\\TESTE-" + i + ".txt";
                enderecoArquivoCriado = "D:\\TESTE-" + i + "-RESULTADO.txt";
            } else {
                endereco = "D:\\TESTE-0" + i + ".txt";
                enderecoArquivoCriado = "D:\\TESTE-0" + i + "-RESULTADO.txt";
            }

            Path caminho = Path.of(endereco);

            if (!Files.exists(caminho)) {
                System.out.println("Arquivo não encontrado: " + endereco);
                continue;
            }

            // Tudo que pode falhar (leitura, parsing, execução dos algoritmos) fica isolado
            // aqui dentro. Se ESTE arquivo der problema, o catch registra o erro e o loop
            // segue para o próximo arquivo (i++) normalmente.

            try {
                List<String> linhas = Files.readAllLines(caminho);

                // transforma em vetores
                List<String[]> linhasEmVetor = new ArrayList<>();

                for (String linha : linhas) {
                    if (linha.trim().isEmpty()) continue; // ignora linhas vazias no arquivo

                    StringTokenizer separador = new StringTokenizer(linha, " "); // Define o separador como um espaço
                    String temposDeCadaProcesso[] = new String[2];
                    int indice = 0;

                    while (separador.hasMoreTokens()) {
                        temposDeCadaProcesso[indice] = separador.nextToken();
                        indice++;
                    }

                    linhasEmVetor.add(temposDeCadaProcesso);
                }

                if (linhasEmVetor.isEmpty()) {
                    throw new IllegalStateException("Arquivo vazio ou sem linhas válidas.");
                }

                int quantum = 0;
                int somaExecucoes = 0;
                int maiorChegada = 0;
                List<Processo> processosBase = new ArrayList<>();

                for (int j = 0; j < linhasEmVetor.size(); j++) {
                    if (j == 0) {
                        quantum = Integer.parseInt(linhasEmVetor.get(j)[0]);
                    } else {
                        // formato da linha: <momento_chegada> <tempo_execucao>
                        int chegada = Integer.parseInt(linhasEmVetor.get(j)[0]);
                        int execucao = Integer.parseInt(linhasEmVetor.get(j)[1]);

                        // O construtor do Processo recebe (tempoDeExecucao, momentoDeChegada)
                        Processo p = new Processo(execucao, chegada);
                        processosBase.add(p);

                        somaExecucoes += execucao;
                        if (chegada > maiorChegada) {
                            maiorChegada = chegada;
                        }
                    }
                }

                if (processosBase.isEmpty()) {
                    throw new IllegalStateException("Nenhum processo encontrado após o quantum.");
                }

                int quantidadeDeProcessos = processosBase.size();
                // margem de segurança: cobre tempo de execução total + possível tempo ocioso até a última chegada
                int tempoDeExecucaoTotal = somaExecucoes + maiorChegada;

                // roda os 4 algoritmos, cada um com sua própria cópia dos processos

                List<Processo> pFifo = copiarProcessos(processosBase);
                Fifo fifo = new Fifo(quantidadeDeProcessos, tempoDeExecucaoTotal, pFifo);

                List<Processo> pSjf = copiarProcessos(processosBase);
                Sjf sjf = new Sjf(quantidadeDeProcessos, tempoDeExecucaoTotal, pSjf);

                List<Processo> pSrt = copiarProcessos(processosBase);
                Srt srt = new Srt(quantidadeDeProcessos, tempoDeExecucaoTotal, pSrt);

                List<Processo> pRr = copiarProcessos(processosBase);
                RoundRobin rr = new RoundRobin(quantidadeDeProcessos, tempoDeExecucaoTotal, pRr, quantum);

                // montando o arquivo de resultado

                StringBuilder saida = new StringBuilder();
                saida.append(formatarLinha(fifo.getTempoMedioDeResposta(), fifo.getTempoMedioDeEspera(), fifo.getTurnaroundMedio())).append(System.lineSeparator());
                saida.append(formatarLinha(sjf.getTempoMedioDeResposta(), sjf.getTempoMedioDeEspera(), sjf.getTurnaroundMedio())).append(System.lineSeparator());
                saida.append(formatarLinha(srt.getTempoMedioDeResposta(), srt.getTempoMedioDeEspera(), srt.getTurnaroundMedio())).append(System.lineSeparator());
                saida.append(formatarLinha(rr.getTempoMedioDeResposta(), rr.getTempoMedioDeEspera(), rr.getTurnaroundMedio())).append(System.lineSeparator());

                // A escrita do arquivo de saída também pode falhar (disco cheio, permissão, etc.)
                // e é tratada separadamente aqui
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(enderecoArquivoCriado))) {
                    bw.write(saida.toString());
                } catch (IOException e) {
                    System.out.println("Erro na escrita do arquivo: " + enderecoArquivoCriado + " -> " + e.getMessage());
                    continue; // não conta como "processado com sucesso"
                }

                System.out.println("Arquivo processado: " + endereco);

            } catch (NumberFormatException e) {
                String msg = "Erro de formato numérico no arquivo " + endereco + ": " + e.getMessage();
                System.out.println(msg);
                escreverArquivoDeErro(enderecoArquivoCriado, msg);
            } catch (ArrayIndexOutOfBoundsException e) {
                String msg = "Linha mal formatada (faltando valor) no arquivo " + endereco + ": " + e.getMessage();
                System.out.println(msg);
                escreverArquivoDeErro(enderecoArquivoCriado, msg);
            } catch (IOException e) {
                String msg = "Erro de leitura do arquivo " + endereco + ": " + e.getMessage();
                System.out.println(msg);
                escreverArquivoDeErro(enderecoArquivoCriado, msg);
            } catch (Exception e) {
                // rede de segurança: qualquer outro erro inesperado (nos algoritmos, por exemplo)
                // não derruba o processamento dos demais arquivos.
                String msg = "Erro inesperado ao processar " + endereco + ": " + e;
                System.out.println(msg);
                escreverArquivoDeErro(enderecoArquivoCriado, msg);
            }
        }
    }

    // cria uma lista de processos "zerados", independente da lista original
    static List<Processo> copiarProcessos(List<Processo> original) {
        List<Processo> copia = new ArrayList<>();
        for (Processo p : original) {
            copia.add(new Processo(p));
        }
        return copia;
    }

    // formata a saída com decimal de 3 casas
    static String formatarLinha(double resposta, double espera, double turnaround) {
        return String.format(Locale.forLanguageTag("pt-BR"), "%.3f %.3f %.3f", resposta, espera, turnaround);
    }

    // Gera o arquivo de resultado mesmo quando o processamento falhou, registrando
    // a mensagem de erro no lugar das métricas. Assim, para os 10 arquivos, sempre
    // existe um TESTE-XX-RESULTADO.txt correspondente, com sucesso ou com o motivo da falha.
    static void escreverArquivoDeErro(String enderecoArquivoCriado, String mensagemDeErro) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(enderecoArquivoCriado))) {
            bw.write("ERRO AO PROCESSAR ESTE ARQUIVO");
            bw.newLine();
            bw.write(mensagemDeErro);
            bw.newLine();
        } catch (IOException e) {
            // Se nem o arquivo de erro conseguir ser escrito (ex: pendrive removido),
            // só registra no console - não há mais nada a fazer por este arquivo.
            System.out.println("Não foi possível gravar o arquivo de erro para " + enderecoArquivoCriado + ": " + e.getMessage());
        }
    }
}
