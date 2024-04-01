/*O arquivo HtmlAnalyser contém a função main.
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
diminuindo parte do encapsulamento que era planejado ao criar a classe LineParser. */


import java.io.IOException;
import java.util.*;

public class HtmlAnalyser{


    Stack<String> stack;
    HtmlHandler htmlHandler;
    LineParser lineParser;
    int max_depth;
    StringBuilder text; 
    boolean status = true; 
    
    public HtmlAnalyser(String url) throws IOException{

            this.stack = new Stack<String>();
            this.htmlHandler = new HtmlHandler(url);
            this.lineParser = new LineParser();
            this.max_depth = 0;
            this.text = new StringBuilder();
    
    }

    public boolean getStatus() throws IOException{
        this.analyseHtml();
        return this.status;
    }

    public String getText(){
        return this.text.toString();
    }

    public void analyseHtml() throws IOException{
        int depth = 0;
        String string = this.htmlHandler.readFromHTML();
        String trimmedString = this.lineParser.trimString(string); 
        LineParser.StringCategory type;
        

        while(trimmedString != null){

            type = this.lineParser.categorize(trimmedString);

            if(type == LineParser.StringCategory.OPENING_TAG){
                depth++;
                this.stack.push(trimmedString);   
            }
            if(type == LineParser.StringCategory.CLOSING_TAG){
                String openingTag = lineParser.getRespectiveOpeningTag(trimmedString);
            
                if(openingTag.equals(this.stack.peek())) {
                    this.stack.pop();
                    depth--;
                }
                else this.status = false;
            }
            if(type == LineParser.StringCategory.TEXT){
                if(depth > this.max_depth){
                    this.max_depth = depth;
                    this.text = this.text.replace(0,this.text.length(),trimmedString);

                    //the following if statement might never happen because more than one line pieces of text might no appear in tests. 
                    this.htmlHandler.placeMarker();
                    String nextLine = htmlHandler.readFromHTML();
                    String nextLineTrimmed = lineParser.trimString(nextLine);
                    if(lineParser.categorize(nextLineTrimmed) == LineParser.StringCategory.TEXT){
                        while(lineParser.categorize(nextLineTrimmed) == LineParser.StringCategory.TEXT){
                            this.text = this.text.replace(0,9999,this.text +"!\n"+nextLineTrimmed);
                            nextLine = htmlHandler.readFromHTML();
                            nextLineTrimmed = lineParser.trimString(nextLine);
                        } 
                        this.htmlHandler.backToMarker(); 
                    }
                    else{
                        this.htmlHandler.backToMarker();
                    }
                    //
                }
            }

            if(type == LineParser.StringCategory.MALFORMED){
                this.status = false;
            }

            if(this.status == false) return;

            string = this.htmlHandler.readFromHTML();
            if(string != null) trimmedString = this.lineParser.trimString(string); 
            else trimmedString = null;

        }
}
    public static void main(String[] args) throws IOException{
        HtmlAnalyser htmlAnalyser;

        try{
            htmlAnalyser = new HtmlAnalyser(args[0]);
            boolean status = htmlAnalyser.getStatus();

            if(status == false){
                System.out.println("malformed HTML");
            }

            else{
                System.out.println(htmlAnalyser.getText());
            }

        }catch(IOException e){
            System.out.println("URL connection error");
        }

    }
}

