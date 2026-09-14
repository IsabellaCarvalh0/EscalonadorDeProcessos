# Simulador de Algoritmos de Escalonamento de Processos

Programa em Java que simula quatro algoritmos clássicos de escalonamento de CPU — **FIFO**, **SJF** (não-preemptivo), **SRT** (SJF preemptivo) e **Round Robin** — a partir de arquivos de entrada com processos, e gera arquivos de saída com as métricas médias de cada algoritmo.

## Como funciona

Para cada arquivo de teste, o programa:

1. Lê o quantum (usado apenas pelo Round Robin) e a lista de processos (momento de chegada e tempo de execução de cada um).
2. Simula a execução de cada processo minuto a minuto, representando o estado do sistema em uma matriz `processos x tempo`, onde cada célula indica se o processo está executando, esperando ou ocioso.
3. A partir da matriz, calcula três métricas por processo: **tempo de resposta**, **tempo de espera** e **turnaround**.
4. Roda os quatro algoritmos de forma independente (cada um com sua própria cópia dos processos, para não haver interferência entre eles) e grava a média das três métricas de cada algoritmo em um arquivo de resultado.

## Algoritmos implementados

| Algoritmo | Classe | Preemptivo? | Critério de escolha |
|---|---|---|---|
| FIFO (First In, First Out) | `Fifo` | Não | Ordem de chegada |
| SJF (Shortest Job First) | `Sjf` | Não | Menor tempo de execução restante, mas só troca de processo quando o atual termina |
| SRT (Shortest Remaining Time) | `Srt` | Sim | Menor tempo de execução restante, reavaliado a cada unidade de tempo |
| Round Robin | `RoundRobin` | Sim (por quantum) | Fila circular (FIFO), com fatia de tempo fixa (`quantum`) por processo |

## Formato do arquivo de entrada

Cada arquivo de entrada (`TESTE-01.txt` a `TESTE-10.txt`) segue o formato:

```
<quantum>
<momento_de_chegada> <tempo_de_execucao>
<momento_de_chegada> <tempo_de_execucao>
...
```

- A **primeira linha** contém apenas o valor do quantum (usado pelo Round Robin; ignorado pelos demais algoritmos).
- Cada linha seguinte representa um processo, com dois valores separados por espaço: o momento em que ele chega ao sistema e seu tempo total de execução (burst time).

Exemplo (`TESTE-01.txt`):

```
2
0 2
2 3
3 1
3 7
6 3
6 2
12 2
15 1
```

## Formato do arquivo de saída

Para cada arquivo de entrada processado com sucesso, é gerado um arquivo `TESTE-XX-RESULTADO.txt` com **uma linha por algoritmo**, na ordem **FIFO, SJF, SRT, Round Robin**. Cada linha contém três valores separados por espaço, com vírgula como separador decimal e três casas decimais:

```
<tempo_de_resposta_médio> <tempo_de_espera_médio> <turnaround_médio>
```

Exemplo de saída correspondente ao `TESTE-01.txt`:

```
4,125 4,125 6,750
2,750 2,750 5,375
1,250 1,750 4,375
2,000 4,000 6,625
```

## Estrutura do projeto

```
├── Main.java          # Ponto de entrada: lê os arquivos, orquestra os algoritmos e grava os resultados
├── Processo.java       # Representa um processo (tempos de chegada, execução, espera, resposta, turnaround)
├── Fifo.java            # Implementação do escalonamento FIFO
├── Sjf.java              # Implementação do escalonamento SJF (não-preemptivo)
├── Srt.java               # Implementação do escalonamento SRT (SJF preemptivo)
└── RoundRobin.java        # Implementação do escalonamento Round Robin
```

## Como executar

O programa espera encontrar os arquivos de entrada em um pendrive, no caminho `D:\TESTE-01.txt` até `D:\TESTE-10.txt` (Windows). Os arquivos de resultado são gravados no mesmo diretório, com o sufixo `-RESULTADO.txt`.

```bash
javac *.java
java Main
```

> Se o caminho dos arquivos de entrada for diferente no seu ambiente, ajuste as variáveis `endereco` e `enderecoArquivoCriado` no início do laço `for` em `Main.java`.

## Tratamento de erros

O programa é tolerante a falhas: se um dos 10 arquivos não existir, estiver mal formatado ou causar algum erro durante a simulação, o processamento **continua normalmente para os demais arquivos**. Nesses casos:

- O erro é impresso no console, indicando qual arquivo falhou e o motivo.
- Ainda assim, é gerado um `TESTE-XX-RESULTADO.txt` para aquele arquivo, contendo a mensagem de erro no lugar das métricas — assim é possível identificar rapidamente quais testes precisam de atenção, sem precisar rodar o programa de novo.

## Métricas calculadas

- **Tempo de resposta**: intervalo entre a chegada do processo e o instante em que ele executa pela primeira vez.
- **Tempo de espera**: soma de todas as unidades de tempo em que o processo já chegou, ainda não terminou, mas não está executando.
- **Turnaround**: tempo total desde a chegada até a conclusão do processo (`tempo de execução + tempo de espera`).

Cada arquivo de resultado reporta a **média** dessas três métricas entre todos os processos daquele teste, para cada um dos quatro algoritmos.
