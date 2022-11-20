import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Lexer {
    String input;
    int state=0;
    int symbolPos=0;
    char symbol;
    String word;

    ArrayList<String> wordsList = new ArrayList<>();
    ArrayList<String> tokensList = new ArrayList<>();

    void TokenAddNextSymb(String tokenName){
        TokenAdd(tokenName);
        symbolPos++;
    }
    void TokenAdd(String tokenName) {
        if (word != "" && tokenName != "COMMENT") {
            wordsList.add(word);
            tokensList.add(tokenName);
        }
        state = 0;
    }
    void NextSymb(){
        word = word +symbol;
        symbolPos++;
    }
    void SkipToken(){
        while(symbolPos < input.length() && !Tokens.isEmpty(symbol)&&!Tokens.isOperator(String.valueOf(symbol))&&!Tokens.isPunctuation(symbol)){
            symbol= input.charAt(symbolPos++);
            word = word + symbol;
        }
    }
    void TokenAddAndSkip(){
        SkipToken();
        TokenAdd("ERROR INPUT");
    }
    void  Automata(){
        switch (state){
            case 0: {
                word = "";
                if (symbol == '0') {
                    state = 1;
                    word += symbol;
                    ++symbolPos;
                    if(symbolPos == input.length()) TokenAddNextSymb("NUMBER");
                }
                else if (Character.isDigit(symbol))
                    state = 3;
                else if(Tokens.isEmpty(symbol))
                    symbolPos++;
                else if(Character.isLetter(symbol) || symbol == '_')
                    state = 4;
                else if(symbol == '#')
                    state = 6;
                else if(Tokens.isPunctuation(symbol))
                    state = 7;
                else if(symbol == '/'){
                    NextSymb();
                    symbol = input.charAt(symbolPos);
                    if(symbol == '/')
                        state = 9;
                    else if(symbol == '*'){
                        NextSymb();
                        state = 10;
                    }
                    else
                        state = 8;
                }
                else if(Tokens.isOperator(String.valueOf(symbol)))
                    state=8;
                else if(symbol == '\'')
                    state = 11;
                else if(symbol == '\"'){
                    word = word +symbol;
                    symbolPos++;
                    state = 12;
                }
                else{
                    TokenAddAndSkip();
                }
                break;
            }
            case 1:{
                if(symbol == 'x'){
                    word+=symbol;
                    ++symbolPos;
                    state = 2;
                }
                else if(symbol == '.' || symbol == 'b' || Character.isDigit(symbol)){
                    word+=symbol;
                    ++symbolPos;
                    state = 3;
                }
                else if(Tokens.isEmpty(symbol) || Tokens.isPunctuation(symbol)
                        || Tokens.isOperator(String.valueOf(symbol))){
                    TokenAdd("NUMBER");
                }
                else{
                    TokenAddAndSkip();
                }
                break;
            }
            case 2:{
                if(Character.isDigit(symbol)||(symbol>='A'&&symbol<='F') || (symbol >= 'a' && symbol <= 'f')){
                    NextSymb();
                    if(symbolPos == input.length() || Tokens.isOperator(String.valueOf(symbol))
                            || Tokens.isEmpty(symbol) || Tokens.isPunctuation(symbol))
                        TokenAdd("NUMBER");
                }
                else if(Tokens.isOperator(String.valueOf(symbol))
                        || Tokens.isEmpty(symbol) || Tokens.isPunctuation(symbol))
                    TokenAdd("NUMBER");
                else
                    TokenAddAndSkip();
                break;
            }
            case 3:{
                if(Character.isDigit(symbol)||symbol=='.'){
                    NextSymb();
                    if(symbolPos == input.length())
                        TokenAdd("NUMBER");
                }
                else if(Tokens.isOperator(String.valueOf(symbol))
                        || Tokens.isEmpty(symbol) || Tokens.isPunctuation(symbol))
                    TokenAdd("NUMBER");
                else
                    TokenAddAndSkip();
                break;
            }
            case 4:{
                if(Character.isLetter(symbol) || symbol == '_'){
                    NextSymb();
                    if(symbolPos == input.length())
                        if(Tokens.isKeyWord(word))
                            TokenAdd("KEYWORD");
                        else
                            TokenAdd("IDENTIFIER");
                }
                else if(Character.isDigit(symbol)){
                    NextSymb();
                    state = 5;
                }
                else if(Tokens.isKeyWord(word) )
                    TokenAdd("KEYWORD");
                else if(Tokens.isOperator(String.valueOf(symbol))
                        || Tokens.isEmpty(symbol) || Tokens.isPunctuation(symbol))
                    TokenAdd("IDENTIFIER");
                else
                    TokenAddAndSkip();
                break;
            }
            case 5:{
                if(Tokens.isOperator(String.valueOf(symbol))
                        || Tokens.isEmpty(symbol) || Tokens.isPunctuation(symbol))
                    TokenAdd("IDENTIFIER");
                else{
                    NextSymb();
                    if(symbolPos == input.length())
                        TokenAdd("IDENTIFIER");
                }
                break;
            }
            case 6:{
                if(!(Character.isLetter(symbol) || symbol == '#')){
                    if((Tokens.isOperator(String.valueOf(symbol)) || Tokens.isEmpty(symbol)
                            || Tokens.isPunctuation(symbol)) && Tokens.isDirective(word))
                        TokenAdd("PREPROCESSOR DIRECTIVE");
                    else
                        TokenAddAndSkip();
                }
                else{
                    NextSymb();
                    if(symbolPos == input.length())
                        TokenAddAndSkip();
                }
                break;
            }
            case 7:{
                word = word +symbol;
                TokenAddNextSymb("PUNCTUATION");
                break;
            }
            case 8:{
                if (Tokens.isOperator(String.valueOf(symbol))) {
                    NextSymb();
                    if(symbolPos == input.length())
                        TokenAdd("OPERATOR");
                }
                else TokenAdd("OPERATOR");
                break;
            }
            case 9:{
                if(Tokens.isEmpty(symbol) && symbol != ' ')
                    TokenAdd("COMMENT");
                else{
                    NextSymb();
                    if(symbolPos == input.length())
                        TokenAdd("COMMENT");
                }
                break;
            }
            case 10:{
                if(symbolPos == input.length())
                    TokenAdd("COMMENT");
                else{
                    NextSymb();
                    if(symbol == '*'){
                        symbol = input.charAt(symbolPos);
                        if(symbol == '/'){
                            word += symbol;
                            TokenAdd("COMMENT");
                            NextSymb();
                        }
                    }
                }
                break;
            }
            case 11:{
                word += symbol;
                ++symbolPos;
                if(symbolPos == input.length())
                    TokenAddAndSkip();
                symbol = input.charAt(symbolPos++);
                word+=symbol;
                if(symbolPos == input.length())
                    TokenAddAndSkip();
                symbol = input.charAt(symbolPos++);
                word+=symbol;
                if(symbol != '\'')
                    TokenAddAndSkip();
                else
                    TokenAdd("CHAR CONSTANT");
                ++symbolPos;
                break;
            }
            case 12:{
                if(symbol!='\"'){
                    word = word +symbol;
                    symbolPos++;
                }
                else{
                    word = word +symbol;
                    TokenAddNextSymb("LITERAL");
                }
                break;
            }
        }
    }

    public Lexer(String line){
        input = line;
    }
    public void Analyzer(){
        word ="";
        while(symbolPos < input.length()){
            symbol= input.charAt(symbolPos);
            Automata();
        }
    }
    public void Print(String filename) throws IOException {
        FileWriter fileWriter = new FileWriter(filename);
        for(int i=0;i<wordsList.size();i++){
            fileWriter.write(wordsList.get(i)+" : "+tokensList.get(i)+"\n");
        }fileWriter.close();
    }
}
