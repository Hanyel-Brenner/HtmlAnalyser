import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HtmlHandler {

    private URL url;
    public BufferedReader inputHtmlBuffer;
    public BufferedWriter outputHtmlBuffer;

    public HtmlHandler(String path, String outputPath) throws IOException{
        this.url = new URL(path);
        this.inputHtmlBuffer = new BufferedReader(new InputStreamReader(this.url.openStream()));
        this.outputHtmlBuffer = new BufferedWriter(new FileWriter(new File(outputPath)));
    }

    public HtmlHandler(String path) throws IOException{
        this.url = new URL(path);
        this.inputHtmlBuffer = new BufferedReader(new InputStreamReader(this.url.openStream()));
    }

    public String readFromHTML() throws IOException{
        return this.inputHtmlBuffer.readLine();
    }

    String writeToLocalHTML(String content) throws IOException{

        if(content != null && content.length()>0) {
            this.outputHtmlBuffer.write(content);
            this.outputHtmlBuffer.newLine();
            return content;
        }
            return content;
    }

    public void loadFullPage() throws IOException{
        String content = this.readFromHTML();

        while( content != null){
            this.writeToLocalHTML(content);
            content = this.readFromHTML();
        }
    }
}
