# BRCache Server
O BRCache server é um sistema de cache de propósito geral com suporte transacional. A distribuição dos binárias estão disponíveis 
no repositório SourceForge. (http://sourceforge.net/projects/brcache). A pronta disponibilidade do código fonte 
permite-lhe depurar o servidor, aprender seu funcionamento interno e criar versões personalizadas para seu uso pessoal 
ou empresarial.
O mais recente lançamento do BRCache está disponível na página de arquivos do BRCache no 
SourceForge, http://sourceforge.net/projects/brcache. Você também vai encontrar versões anteriores, 
bem como beta e candidatos a futuras versões.

## Teste de performance em comparação com o Memcached

![alt text](https://1.bp.blogspot.com/-KxKCK5FkB1Y/WAfH9TPhNfI/AAAAAAAAAqg/RgiOZ1h-RPwPDhk24_4uT_Vw4RhgPL7PQCLcB/s320/brcache_x_memcached.png)

Em experimento realizado, o BRCache demonstrou ser mais rápido que o Memcached.
O experimento está disponível no repositório de arquivos do SourceForge (https://sourceforge.net/projects/brcache/files/).

### O experimento

No experimento somente foi considerado o armazenamento em memória. A instância de cada servidor foi configurada para consumir no máximo 8GB. Os métodos testados foram o de escrita e leitura. Nenhum cliente foi utilizado. Foram criados métodos que geram as solicitações e processam as respostas.
O experimento foi dividido em duas etapas. A primeira etapa escreve os dados no cache. Cada registro tem um tamanho fixo de 1k. São utilizados grupos de 50 até 300 clientes, com 50 de frequência. Cada cliente faz 50 registros únicos. Cada agrupamento de clientes é executado três vezes e o resultado com menor tempo médio de resposta é considerado. A segunda etapa lê os dados do cache. Cada leitura tem um tamanho fixo de 1k, gerado na primeira etapa. São utilizados grupos de 50 até 300 clientes, com 50 de frequência. Cada cliente lê 50 registros únicos. Cada agrupamento de clientes é executado três vezes e o resultado com menor tempo médio de resposta é considerado.

### O hardware

O cliente e o servidor foram executados na mesma máquina com a seguinte configuração:

  * processador Intel(R) Core(TM) i3-2100  (3.10GHz 64bits)
  * 16GB de memória.

### O servidor de cache

O experimento foi feito com a versão 1.4.5 64bit do Memcached e a versão 1.0 beta 4 do BRCache sobre Java HotSpot(TM) 64-Bit Server VM (build 20.45-b01, mixed mode).

### A configuração de arranque

Para o experimento, o servidor Memcached foi iniciado usando os parâmetros de linha de comando m igual a 8000 e t igual a 8.

```
memcached -m 8000 -t 8
```

O servidor BRCache foi iniciado usando os parâmetros de linha de comando server e XX:ParallelGCThreads igual a 8.

```
java -server -XX:ParallelGCThreads=8 -jar brcache-server-1.0-b4.jar
```

# As configurações adicionais do cache

O Memcached não necessitou de configurações adicionais e o BRCache foi configurado como se segue abaixo:

```
port=9090
max_connections=1024  
swapper_thread=2  
memory_access_type=unsafe  
timeout_connection=1024  
reuse_address=false  
data_path=/mnt/brcache  
nodes_buffer_size=1024m  
nodes_page_size=1k  
nodes_swap_factor=0.1  
index_buffer_size=512m  
index_page_size=1k  
index_swap_factor=0.1  
data_buffer_size=4000m  
data_page_size=8k  
data_block_size=1k  
data_swap_factor=0.1  
write_buffer_size=16k  
read_buffer_size=16k  
max_size_entry=128m  
max_size_key=64  
transaction_support=false  `
```
### O cálculo

A quantidade de operações por segundo foi obtida com a fórmula: ops = (1000000000*i)/t, onde:

t: tempo total, em nano segundos, que o agrupamento de clientes demora para executar todas as operações.
i: quantidade total de operações executadas pelo agrupamento de clientes.
1000000000: constante que representa um segundo em nano segundos.

### Resultado

O método de escrita do BRCache demonstrou ser mais rápido que o Memcached ao passo que a quantidade de clientes aumentava. Demonstrada pela curva ascendente do gráfico abaixo.

![alt text](https://3.bp.blogspot.com/-7yBBzpOktdw/WAeQLVQCBTI/AAAAAAAAAqA/oede4bXWv5UgMB44jMMsLtPjlY2ydJ6sQCLcB/s400/graph-put.png)

O método de leitura do BRCache é tão rápido quanto o do Memcached. Demonstrado pelo entrelaçamento das linhas do gráfico abaixo.
Foram feitos testes usando milissegundo como unidade de tempo. Nesse caso, os resultados demonstraram que o BRCache foi mais rápido em ambos os métodos. Isso sugere que a diferença de performance, no caso do método de leitura, está na base de nano segundos.

![alt text](https://1.bp.blogspot.com/-m-oOjvJ_wj8/WAeVt9s4rpI/AAAAAAAAAqQ/X_OxYDtlqNYNeKOi3Wjt209SnU7EWP1OQCLcB/s400/graph-get.png)

### Variação nos resultados

Foram feitos inúmeros testes e a performance de ambos os servidores variaram para mais ou menos de 70.000 operações por segundo. Chegando a passar de 200.000 mil operações por segundo. Fazendo uma análise preliminar, essa variação se deve a sensibilidade do experimento e ao modo como os dados trafegam, mesmo o cliente e o servidor estando em uma mesma máquina, Por exemplo, o laço da linha 67, do método de leitura, em ambos clientes variavelmente foi executado mais de uma vez.

### Conclusão

Mesmo se tratando de uma versão beta, o BRCache demonstrou ser tão ou mais rápido que a última versão estável do Memcached. Outros experimentos ainda serão feitos.


