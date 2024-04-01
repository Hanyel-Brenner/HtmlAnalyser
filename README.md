O arquivo HtmlAnalyser contém a função main.
A classe  HtmlHandler é responsável por abrir uma stream de leitura para uma url qualquer, ler o conteúdo do html, e escrevê-lo num arquivo local. A escrita e leitura é realizada a partir de buffers que trabalham apenas com uma linha de cada vez.
A classe LineParser é uma classe que fornece utilidades para analisar as linhas do arquivo html, categorizando-o nas categorias especificadas pelo enum StringCategory que contém as categorias EMPTY, OPENING_TAG,
CLOSING_TAG, MALFORMED, TEXT. Ou seja, classifica-as entre tags de abertura, tags de fechamento, linhas vazias, malformadas, ou texto, o que é feito a partir de expressões regulares.
HtmlAnalyser utiliza as classes HtmlHandler e LineParser para encontrar o trecho de texto mais profundo no arquivo, perpassando todo o arquivo, linha por linha.

Responsabilidade única) Se tratando dos princípios de programação SOLID, se tratando do princípio da responsabilidade única, acredito que na maior parte as funções estejam coesas e com apenas uma responsabilidade, mas a função
Analyse() da classe HtmlAnalyser parece ser a que mais viola esse princípio, pois a responsabilidade de colocar ou retirar elementos da pilha "stack" poderiam ter sido divididos em outras funções, por exemplo,
assim como comparar o trecho de texto encontrado com os anteriores. tornando a função menor e mais fácil de ler.

Inversão de dependências) A classe HtmlHandler, ao tratar da leitura e escrita de html através de buffers, não utiliza de nenhuma interface, sendo assim sua estrutra é pouquíssimo reutilizável, para resolver
isso, seria possível implementar uma interface FileHandler, que especifica melhor os principais métodos dessa classe, tornando o código mais genérico.

Prefira composição a herança) Herança não foi utilizada no código, composição foi o suficiente, diminuindo a necessidade de um tipo mais complexo de estrutura.

Principio de Demeter) Não se tratando apenas de chamada de funções, mas especificamente ao utilizar da enum "StringCategory" juntamente com outros métodos da classe LineParser, houveram chamadas muito grandes,
diminuindo parte do encapsulamento que era planejado ao criar a classe LineParser.

