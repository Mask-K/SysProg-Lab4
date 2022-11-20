import java.util.ArrayList;
import java.util.Arrays;

public class Tokens {
    public static boolean isOperator(String symbol){
        ArrayList<String> operators = new ArrayList<>(Arrays.asList("-","=","+",
                "*","/","%","~","|","&","<<",">>","^","==","!=",
                "<",">","<=",">=","<=>","++","--","!","&&","||","."));
        return operators.contains(symbol);
    }
    public static boolean isKeyWord(String word){
        ArrayList<String> keyWords = new ArrayList<>(Arrays.asList("and", "auto", "bool", "break", "case", "catch",
                "char", "class", "const", "const_cast", "continue", "decltype", "default", "delete", "do", "double",
                "dynamic_cast", "else", "enum", "explicit", "extern", "false", "float", "for", "friend", "goto",
                "if", "inline", "int", "long", "main", "mutable", "namespace", "new", "nullptr", "operator", "or",
                "private", "protected", "public", "register", "reinterpret_cast", "return", "short", "signed",
                "sizeof", "static", "static_cast", "struct", "switch", "template", "this","throw", "true", "try",
                "typedef", "typeid", "typename", "union", "unsigned", "using", "virtual", "void", "volatile", "while"));
        return keyWords.contains(word);
    }
    public static boolean isDirective(String word){
        ArrayList<String> directives = new ArrayList<>(Arrays.asList("#define", "#include", "#ifdef", "ifndef",
                "#endif", "#error", "#if", "#elif", "#else", "#import", "#line", "#using", "#undef"));
        return directives.contains(word);
    }
    public static boolean isEmpty(char symbol){
        ArrayList<Character> empties = new ArrayList<>(Arrays.asList(' ','\n','\r','\t','\b'));
        return empties.contains(symbol);
    }
    public static boolean isPunctuation(char symbol){
        ArrayList<Character> punctuations = new ArrayList<>(Arrays.asList(',',';',':','?','(',')','{','}','[',']'));
        return punctuations.contains(symbol);
    }
}
