import grammar.TesterLexer;
import grammar.TesterParser;
import logger.ConsoleLogger;
import logger.FileLogger;
import logger.Logger;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try {
            Logger logger;
            CliArgs.Config cfg = CliArgs.parse(args);

            if (cfg.help()) {
                System.out.println(CliArgs.helpText());
                return;
            }

            CharStream input;

            if (cfg.toFile()) {
                logger = new FileLogger(cfg.file());
            } else {
                logger = new ConsoleLogger();
            }

            logger.log("==> Running single file " + cfg.file());

            try {
                input = CharStreams.fromPath(cfg.file());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            TesterLexer lexer = new TesterLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            TesterParser parser = new TesterParser(tokens);

            ParseTree tree = parser.program();

            TesterVisitor visitor = new TesterVisitor(logger);
            visitor.visit(tree);
            logger.flush();
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