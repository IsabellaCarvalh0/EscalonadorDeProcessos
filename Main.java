import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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

            // verifica se arquivo existe
            if (!Files.exists(caminho)) {
                System.out.println("Arquivo não encontrado: " + endereco);
                continue;
            }

            // lê linhas
            List<String> linhas = Files.readAllLines(caminho);

            // conta quantidade de processos
            int quantidadeDeProcessos = -1;

            for (String linha : linhas) {
                quantidadeDeProcessos++;
            }

            // transforma em vetores
            List<String[]> linhasEmVetor = new ArrayList<>();

            for (String linha : linhas) {
                StringTokenizer separador = new StringTokenizer(linha, " ");

                int indice = 0;

                String temposDeCadaProcesso[] = new String[2];

                while (separador.hasMoreTokens()) {

                    String token = separador.nextToken();

                    temposDeCadaProcesso[indice] = token;

                    System.out.println(token);

                    indice++;
                }

                linhasEmVetor.add(temposDeCadaProcesso);
            }

            int quantum = 0;
            int tempoDeExecucaoTotal = 0;
            List<Processo> processos = new ArrayList<>();

            for (int j = 0; j < linhasEmVetor.size(); j++) {
                if (j == 0) {
                    quantum = Integer.parseInt(linhasEmVetor.get(j)[0]);
                } else {
                    int valor = Integer.parseInt(linhasEmVetor.get(j)[1]);
                    tempoDeExecucaoTotal += valor;
                    Processo p = new Processo(Integer.parseInt(linhasEmVetor.get(j)[0]), valor);
                    processos.add(p);

                }
            }


            // cria arquivo resultado
            try (
                    BufferedWriter bw = new BufferedWriter(new FileWriter(enderecoArquivoCriado))
            ) {
                bw.newLine();
            } catch (IOException e) {
                System.out.println("Erro na escrita do arquivo.");
            }
            System.out.println("Arquivo processado: " + endereco);
        }
    }
}

