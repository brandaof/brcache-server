# BRCache Server
O BRCache server é um sistema de cache de propósito geral.A distribuição dos binárias estão disponíveis 
no repositório SourceForge. (http://sourceforge.net/projects/brcache). A pronta disponibilidade do código fonte 
permite-lhe depurar o servidor, aprender seu funcionamento interno e criar versões personalizadas para seu uso pessoal 
ou empresarial.

## Obtendo os arquivos binários

O mais recente lançamento do BRCache está disponível na página de arquivos do BRCache no 
SourceForge, http://sourceforge.net/projects/brcache. Você também vai encontrar versões anteriores, 
bem como beta e candidatos a futuras versões.

## Pré-requisitos

Antes de instalar e executar o servidor, verifique o seu sistema para certificar-se de que você tem uma instalação 
funcional do JDK 1.5+. A maneira mais simples de fazer isso é executar o comando java -version para assegurar que 
o executável java está acessível e que está utilizando a versão 1.5 ou superior.
Não importa o local de instalação do BRCache em seu sistema. Evite instalar o BRCache em um diretório com nome que 
contenha espaço, pois pode causar problemas. Não existe nenhuma exigência para o acesso root na execução do BRCache em 
sistemas UNIX/Linux.

## Instalando o pacote binário

Depois de obter o arquivo binário que deseja instalar, use uma ferramenta para descompactar arquivos no formato ZIP 
para extrair o conteúdo do arquivo brcache-server-yy-xx.zip em um local de sua escolha. O processo de extração criará 
o diretório brcache-yy-xx.

## Estrutura de diretórios

A extração do arquivo ZIP cria o diretório brcache-yy-xx que contém scripts de inicialização do servidor, JARs e arquivos 
de configuração.

![alt text](https://1.bp.blogspot.com/-k-jF9hOf3fo/V_w1iAlrDhI/AAAAAAAAApY/OLh5MJqzlDQIVgPmDMmbZv0ud9XRv9y8wCLcB/s1600/img0.png)

| Diretório | Descrição                                                                                         |
| --------- | ------------------------------------------------------------------------------------------------- |
| data      | Local onde os dados serão persistidos após atingir o limite de uso da memória. Pode ser alterado. |
| lib       | O diretório lib é o local onde ficam todas as bibliotecas usadas pelo servidor.                   |

## Arquivo de configuração

No diretório raiz da instalação, brcache-yy-xx, contém o arquivo brcache.conf. Nele contém toda a configuração do servidor.

## Teste básico de instalação

Depois de ter instalado o BRCache, é aconselhável realizar um teste de inicialização simples para verificar que não há grandes 
problemas com a sua combinação de Java VM e sistema operacional. Para testar a instalação, vá para o diretório raiz e execute 
java -jar brcache-server-yy-xx.jar.

Se nenhuma mensagem for exibida, significa que o servidor iniciou normalmente.
