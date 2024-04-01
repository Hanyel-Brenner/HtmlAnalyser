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

