import grammar.TesterLexer;
import grammar.TesterParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        CharStream input;

        try {
            input = CharStreams.fromFileName("src/main/java/test.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TesterLexer lexer = new TesterLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TesterParser parser = new TesterParser(tokens);

        ParseTree tree = parser.program();

        TesterVisitor visitor = new TesterVisitor();
        System.out.println(visitor.visit(tree));
    }
}