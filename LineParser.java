import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineParser{

    private String openingTagPattern = "^<[a-zA-Z]+>$";
    private String closingTagPattern = "^</[a-zA-z]+>$";
    
    public enum StringCategory{
        EMPTY,
        OPENING_TAG,
        CLOSING_TAG,
        TEXT,
        MALFORMED
    }
    
    public String trimString(String line){
        String newLine = line.trim();
        return newLine;
    }

    public boolean matcher(String regex, String line){
        Pattern pattern = Pattern.compile(regex);
        Matcher validator = pattern.matcher(line);
        return validator.find(); 
    } 

    public String getRespectiveOpeningTag(String closingTag){
        String openingTag = closingTag.replace("</","<");
        return openingTag;
    }

    public StringCategory categorize(String line){

        boolean  isEmpty = (line.length() == 0);
        boolean isOpeningTag = this.matcher(this.openingTagPattern, line);
        boolean isClosingTag = this.matcher(this.closingTagPattern, line);

        if(isEmpty) return StringCategory.EMPTY;
        else if(isOpeningTag) return StringCategory.OPENING_TAG;
        else if(isClosingTag) return StringCategory.CLOSING_TAG;
        else{
            if(this.matcher("^.*<.*/.*/.*>.*$",line)){  //this identifies the tags that have two slashes and any other combination of letters
                return StringCategory.MALFORMED;
            }  
            if(this.matcher("^.*<.*(<*>*)*>.*$",line)){ //this identifies extra "<" or ">" inside the supposed tag. 
                return StringCategory.MALFORMED;
            }
        }
            return StringCategory.TEXT;
        
    }

}