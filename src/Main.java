import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Enter input and output filenames one by one");
        Scanner reader = new Scanner(System.in);
        String input = reader.nextLine();
        String output = reader.nextLine();
        Lexer lex = new Lexer(Files.readString(Paths.get(input)));
        lex.Analyzer();
        lex.Print(output);
    }
}