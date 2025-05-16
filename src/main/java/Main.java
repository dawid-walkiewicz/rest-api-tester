import grammar.TesterLexer;
import grammar.TesterParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            CliArgs.Config cfg = CliArgs.parse(args);

            if (cfg.help()) {
                System.out.println(CliArgs.helpText());
                return;
            }

            CharStream input;


            System.out.println("==> Running single file " + cfg.file());

            try {
                input = CharStreams.fromPath(cfg.file());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            TesterLexer lexer = new TesterLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            TesterParser parser = new TesterParser(tokens);

            ParseTree tree = parser.program();

            TesterVisitor visitor = new TesterVisitor();
            visitor.visit(tree);
        } catch (CliArgs.ParseException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println(CliArgs.helpText());
            System.exit(2);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}