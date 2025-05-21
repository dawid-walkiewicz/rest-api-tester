import java.nio.file.*;

/**
 * Command line arguments parser for the tester.
 * Options:
 * <ul>
 *   <li><code>-f, --file &lt;file&gt;</code>: single test file</li>
 *   <li><code>-h, --help</code>: show this help</li>
 * </ul>
 *
 * If nothing is given, single test file is assumed.
 */
public final class CliArgs {
    public record Config(Path file, boolean help, boolean toFile) {
    }

    /**
     * Exception thrown when parsing fails
     */
    public static final class ParseException extends RuntimeException {
        public ParseException(String msg) {
            super(msg);
        }
    }

    /**
     * Parses the {@code args} array. Throws {@link ParseException} for invalid
     * parameters or non-existent paths.
     */
    public static Config parse(String[] args) {

        Path file = null;
        boolean help = false;
        boolean toFile = false;

        for (int i = 0; i < args.length; i++) {
            String a = args[i];

            switch (a) {
                case "-f", "--file" -> {
                    ensureNoPath(file, a);
                    file = nextPath(args, ++i, a);
                }
                case "-t", "--toFile" -> toFile = true;
                case "-h", "--help" -> help = true;
                default -> {
                    if (a.startsWith("-"))
                        throw new ParseException("Unknown option: " + a);
                    ensureNoPath(file, a);
                    file = Path.of(a).toAbsolutePath();
                }
            }
        }

        if (!help && file == null) {
            throw new ParseException("Missing file.  See --help.");
        }

        if (file != null && !Files.isRegularFile(file))
            throw new ParseException("File does not exist: " + file);

        return new Config(file, help, toFile);
    }

    private static void ensureNoPath(Path file, String opt) {
        if (file != null)
            throw new ParseException("File already specified");
    }

    private static Path nextPath(String[] args, int idx, String opt) {
        if (idx >= args.length)
            throw new ParseException("No argument after " + opt);
        return Path.of(args[idx]).toAbsolutePath();
    }

    /**
     * Returns the help text.
     */
    public static String helpText() {
        return """
                Usage: tester [-f] <file> [-h]
                  -f, --file <file>      single test file
                  -t, --toFile           create test results file
                  -h, --help             show this help
                """;
    }

    private CliArgs() {
    }
}
