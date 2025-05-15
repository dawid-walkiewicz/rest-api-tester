import java.nio.file.*;

/**
 * Command line arguments parser for the tester.
 * Options:
 * <ul>
 *   <li><code>-f, --file &lt;file&gt;</code>: single test file</li>
 *   <li><code>-d, --dir &lt;dir&gt;</code>: test directory</li>
 *   <li><code>-h, --help</code>: show this help</li>
 * </ul>
 *
 * If no <code>-f</code> or <code>-d</code> is given, help is shown.
 */
public final class CliArgs {
    public record Config(Path file, Path dir, boolean help) {
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
        Path dir = null;
        boolean help = false;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-f", "--file" -> {
                    ensureNoPath(file, dir, args[i]);
                    file = nextPath(args, ++i, args[i - 1]);
                }
                case "-d", "--dir" -> {
                    ensureNoPath(file, dir, args[i]);
                    dir = nextPath(args, ++i, args[i - 1]);
                }
                case "-h", "--help" -> help = true;
                default -> throw new ParseException("Unknown option: " + args[i]);
            }
        }

        if (!help && file == null && dir == null) {
            help = true;
        }

        validatePath(file, true, "File");
        validatePath(dir, false, "Directory");

        return new Config(file, dir, help);
    }

    private static void ensureNoPath(Path file, Path dir, String opt) {
        if (file != null || dir != null)
            throw new ParseException(opt + " cannot be combined with another path");
    }

    private static Path nextPath(String[] args, int idx, String opt) {
        if (idx >= args.length)
            throw new ParseException("No argument after " + opt);
        return Path.of(args[idx]).toAbsolutePath();
    }

    private static void validatePath(Path p, boolean mustBeFile, String what)
            throws ParseException {
        if (p == null) return;
        boolean ok = mustBeFile ? Files.isRegularFile(p) : Files.isDirectory(p);
        if (!ok) throw new ParseException(what + " does not exist: " + p);
    }

    /**
     * Returns the help text.
     */
    public static String helpText() {
        return """
                Usage: tester (-f <file> | -d <directory>) [-h]
                  -f, --file <file>      single test file
                  -d, --dir  <directory> test directory (recursively)
                  -h, --help             show this help
                """;
    }

    private CliArgs() {
    }
}
