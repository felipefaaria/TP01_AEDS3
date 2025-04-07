# INTRODUÇÃO

O trabalho consiste na implementação de um sistema de gerenciamento de séries de streaming e seus episódios (PUCFlix), com base em um CRUD. O sistema permite realizar operações de criação, leitura, atualização e exclusão de séries e episódios, garantindo um relacionamento 1:N entre suas entidades (séries e episódios respectivamente).

Foram utilizadas as seguintes estruturas de dados para realizar as buscas dentro do programa:

- Tabela Hash Extensível
- Árvore B+

# PARTICIPANTES

- Arthur de Sá Camargo  
- Daniel Pedrosa Collina  
- Felipe Silva Faria  

# CLASSES

## Episodio.java
Representa um episódio de série com atributos como ID, ID da série, nome, temporada, data de lançamento (lançamento) e duração. Implementa a interface `Registro`, possibilitando serialização para armazenamento em arquivos binários. O método `fromByteArray` reconstrói o objeto a partir de bytes, respeitando a ordem dos atributos para garantir integridade.

## Serie.java
Modela uma série de TV com atributos como ID, nome, ano de lançamento (lançamento), sinopse e plataforma de streaming. Também implementa a interface `Registro`, permitindo sua manipulação como registro em arquivo. Possui dois construtores — um com ID e outro sem, para facilitar a criação de novas instâncias.

# AED3

## Arquivo.java
Classe genérica que gerencia leitura e escrita de entidades que implementam `Registro`. Usa `RandomAccessFile` para acesso direto ao arquivo `.db` e aplica `HashExtensivel` como índice primário, permitindo localização eficiente dos registros por ID.

## ArvoreBMais.java
Implementa uma árvore B+ parametrizada, adequada para buscas rápidas e ordenadas. Aceita pares genéricos que estendem `RegistroArvoreBMais`. Os dados são mantidos em arquivos binários e podem ter chaves repetidas com pares únicos (ex: ID de série e ID de episódio).

## HashExtensivel.java
Aplica a técnica de hashing extensível para armazenar pares chave/endereço em disco, com divisão dinâmica de cestos. Permite criação, leitura, exclusão e crescimento automático à medida que mais registros são inseridos. Os objetos armazenados devem implementar `RegistroHashExtensivel`.

## ParIDEndereco.java
Modela o par (ID, endereço em arquivo), essencial para a tabela hash extensível. Tamanho fixo e operações de serialização padronizadas, seguindo o contrato de `RegistroHashExtensivel`.

## ParIdId.java
Define um par de inteiros (id1, id2) para uso como chave composta em árvore B+. Suporta comparação condicional e serialização. Ideal para representar relações como (idSerie, idEpisodio).

## RegistroArvoreBMais.java
Interface para objetos utilizados em árvore B+, exigindo métodos como `toByteArray`, `fromByteArray`, `compareTo` e `clone`, todos voltados para registros de tamanho fixo.

## RegistroHashExtensivel.java
Define o comportamento necessário para objetos que serão armazenados em estruturas de hashing extensível, incluindo métodos de hash, serialização e tamanho fixo.

# MODELO

## ArquivoEpisodios.java
Classe que herda de `Arquivo` e gerencia os dados dos episódios. Utiliza duas árvores B+ para indexação: uma para buscar por nome (`indiceNome`) e outra para associar séries a episódios (`indiceRelacaoSerieEp`). O construtor cuida da criação de diretórios e da configuração dos índices.

## ArquivoSeries.java
Gerencia os dados das séries, também herdando de `Arquivo`. Cria a estrutura de diretórios e utiliza uma árvore B+ (`indiceNome`) para mapear nomes de séries a seus respectivos IDs, otimizando a busca.

## ParNomeId.java
Classe que representa um par (nome, ID), usada como índice em árvore B+. Implementa `RegistroArvoreBMais`, com métodos de serialização e comparação. Inclui normalização de strings para manter consistência na indexação textual.

# MENU

## MenuEpisodios.java
Responsável pela interface com o usuário para gerenciamento de episódios. Exibe menu textual com opções de inserção, busca, edição e exclusão. Utiliza `ArquivoEpisodios` e `ArquivoSeries` para operar sobre os dados. Inclui tratamento de exceções para garantir estabilidade.

## MenuSeries.java
Interface de gerenciamento das séries. Permite executar operações básicas e validar vínculos com episódios antes de excluir uma série. Utiliza instâncias de `ArquivoSeries` e `ArquivoEpisodios` para manter a integridade dos dados.

## Principal.java
Classe principal do sistema. Apresenta o menu inicial com as opções principais e direciona o usuário para `MenuSeries` ou `MenuEpisodios`. Implementa um loop de controle simples com tratamento de entrada inválida.

# EXPERIÊNCIA

No início do desenvolvimento deste trabalho prático, enfrentamos algumas dificuldades para entender exatamente o que deveria ser feito, principalmente no que diz respeito à implementação do relacionamento 1:N entre séries e episódios utilizando a Árvore B+. Foi um desafio compreender como estruturar corretamente o índice com o par `(idSerie; idEpisodio)` e como integrá-lo com o CRUD que já havíamos desenvolvido em sala. Além de trabalhar com a Tabela Hash Extensível, que também foi uma primeira vez, ou seja, o estranhamento inicial com estruturas que não havíamos trabalhado antes ocorreu assim como com a Árvore B+.

A operação mais difícil de implementar foi, sem dúvida, a **alteração (update)** de registros. Isso aconteceu porque, diferentemente da inclusão ou da leitura, a alteração envolve múltiplas verificações e pode afetar os índices no arquivo.

A princípio, todos os requisitos do trabalho foram implementados com sucesso. As funcionalidades de CRUD para séries e episódios, o relacionamento 1:N via Árvore B+, e os índices com tabela hash extensível foram devidamente desenvolvidos e integrados ao sistema. Realizamos diversos testes com inserções, buscas, alterações e exclusões, tanto de séries quanto de episódios, e o comportamento do sistema se manteve estável e consistente. Todos os testes indicam que as funcionalidades estão funcionando corretamente.

# CHECKLIST

- [x] As operações de inclusão, busca, alteração e exclusão de séries estão implementadas e funcionando corretamente?  
- [x] As operações de inclusão, busca, alteração e exclusão de episódios, por série, estão implementadas e funcionando corretamente?  
- [x] Essas operações usam a classe CRUD genérica para a construção do arquivo e as classes Tabela Hash Extensível e Árvore B+ como índices diretos e indiretos?  
- [x] O atributo de ID de série, como chave estrangeira, foi criado na classe de episódios?  
- [x] Há uma árvore B+ que registre o relacionamento 1:N entre episódios e séries?  
- [x] Há uma visualização das séries que mostre os episódios por temporada?  
- [x] A remoção de séries checa se há algum episódio vinculado a ela?  
- [x] A inclusão da série em um episódio se limita às séries existentes?  
- [x] O trabalho está funcionando corretamente?  
- [x] O trabalho está completo?  
- [x] O trabalho é original e não a cópia de um trabalho de outro grupo?  
