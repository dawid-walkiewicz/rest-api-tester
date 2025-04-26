import grammar.TesterParser;
import grammar.TesterParserBaseVisitor;
import symbols.GlobalSymbols;
import symbols.LocalSymbols;
import types.*;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TesterVisitor extends TesterParserBaseVisitor<Value> {
    private GlobalSymbols<Value> globalVars;
    private LocalSymbols<Value> localVars;
    private static final long DEFAULT_TIMEOUT_MS = 10_000;

    private String currentTestCaseName;
    private int currentTestRepeats;
    private int currentTestTimeout;

    private final HttpRequestWrapper executor = new HttpRequestWrapper();
    private Map<String, String> options = new HashMap<>();

    @Override
    public Value visitProgram(TesterParser.ProgramContext ctx) {
        globalVars = new GlobalSymbols<>();
        localVars = new LocalSymbols<>();
        return super.visitProgram(ctx);
    }

    @Override
    public Value visitEnvDeclaration(TesterParser.EnvDeclarationContext ctx) {
        globalVars.newSymbol(ctx.ID().getText(), visit(ctx.value()));
        return globalVars.getSymbol(ctx.ID().getText());
    }

    @Override
    public Value visitTestCase(TesterParser.TestCaseContext ctx) {
        pushScope();

        currentTestCaseName = processString(ctx.STRING().getText());
        if (ctx.optionsBlock() != null) {
            visit(ctx.optionsBlock());
        }

        Value result = super.visitTestCase(ctx);

        popScope();
        return result;
    }

    @Override
    public Value visitOptionsBlock(TesterParser.OptionsBlockContext ctx) {
        return visitOptionsList(ctx.optionsList());
    }

    @Override
    public Value visitOptionsList(TesterParser.OptionsListContext ctx) {
        return super.visitOptionsList(ctx);
    }

    @Override
    public Value visitOption(TesterParser.OptionContext ctx) {
        if ("repeat".equals(ctx.ID().getText())) {
            Value result = visit(ctx.optionValue());
            currentTestRepeats = (int) ((NumberValue) result).value();
            return result;
        } else if ("timeout".equals(ctx.ID().getText())) {
            Value result = visit(ctx.optionValue());
            currentTestTimeout = (int) ((NumberValue) result).value();
            return result;
        } else {
            return super.visitOption(ctx);
        }
    }

    @Override
    public Value visitOptionValue(TesterParser.OptionValueContext ctx) {
        if (ctx.NUMBER() != null) {
            double number = Double.parseDouble(ctx.NUMBER().getText());
            return new NumberValue(number);
        } else if (ctx.STRING() != null) {
            String text = processString(ctx.STRING().getText());
            return new StringValue(text);
        }
        return super.visitOptionValue(ctx);
    }

    @Override
    public Value visitStatement(TesterParser.StatementContext ctx) {
        return super.visitStatement(ctx);
    }

    @Override
    public Value visitVarDeclaration(TesterParser.VarDeclarationContext ctx) {
        setVariable(ctx.ID().getText(), visit(ctx.value()));
        return localVars.getSymbol(ctx.ID().getText());
    }

    @Override
    public Value visitVarReassignment(TesterParser.VarReassignmentContext ctx) {
        Value value = visit(ctx.value());
        localVars.setSymbol(ctx.ID().getText(), value);
        return value;
    }

    @Override
    public Value visitRequest(TesterParser.RequestContext ctx) {

        String method = ctx.method().getText();
        String endpoint = processString(ctx.endpoint().getText());
        String jsonBody = ctx.obj() != null ? parseJsonObj(ctx.obj()) : null;

        HttpResult response;
        System.out.println("-------------------------------------");
        System.out.println(currentTestCaseName + ":");
        Duration duration = Duration.ofMillis(currentTestTimeout <= 0 ? DEFAULT_TIMEOUT_MS : currentTestTimeout);
        if (currentTestRepeats > 1 ) {
            switch (method) {
                case "GET" -> response = executor.sendRequestBenchmark(endpoint, currentTestRepeats, duration,"GET");
                case "HEAD" -> response = executor.sendRequestBenchmark(endpoint, currentTestRepeats, duration,"HEAD");
                case "DELETE" -> response = executor.sendRequestBenchmark(endpoint, currentTestRepeats, duration,"DELETE");
                case "POST" -> response = executor.sendRequestBenchmark(endpoint, currentTestRepeats, duration, jsonBody,"POST");
                case "PUT" -> response = executor.sendRequestBenchmark(endpoint, currentTestRepeats, duration, jsonBody,"PUT");
                default -> throw new RuntimeException("Unsupported method: " + method);
            }
        } else {
            switch (method) {
                case "GET" -> response = executor.sendGet(endpoint, duration);
                case "HEAD" -> response = executor.sendHead(endpoint, duration);
                case "DELETE" -> response = executor.sendDelete(endpoint, duration);
                case "PUT" -> response = executor.sendPut(endpoint, jsonBody, duration);
                case "POST" -> response = executor.sendPost(endpoint, jsonBody, duration);
                default -> throw new RuntimeException("Unsupported method: " + method);
            }
        }


        System.out.println("Send " + method + " " + endpoint);
        System.out.println(response);
        System.out.println("-------------------------------------");


        currentTestRepeats = 0;
        currentTestTimeout = 0;

        return null;
    }

    private String parseJsonObj(TesterParser.ObjContext ctx) {
        if (ctx.pair().isEmpty()) return "{}";

        StringBuilder json = new StringBuilder("{");
        List<TesterParser.PairContext> pairs = ctx.pair();
        for (int i = 0; i < pairs.size(); i++) {
            var pair = pairs.get(i);
            String key = processString(pair.STRING().getText());
            String value = parseValue(pair.value());

            json.append("\"").append(key).append("\":").append(value);
            if (i < pairs.size() - 1) json.append(",");
        }
        json.append("}");
        return json.toString();
    }
    private String parseValue(TesterParser.ValueContext ctx) {
        if (ctx.STRING() != null) return interpolateString(ctx.STRING().getText());
        if (ctx.NUMBER() != null) return ctx.NUMBER().getText();
        if (ctx.TRUE() != null) return "true";
        if (ctx.FALSE() != null) return "false";
        if (ctx.obj() != null) return parseJsonObj(ctx.obj());
        if (ctx.arr() != null) return parseArray(ctx.arr());
        return "\"<unsupported>\"";
    }
    private String parseArray(TesterParser.ArrContext ctx) {
        if (ctx.value().isEmpty()) return "[]";
        return ctx.value().stream()
                .map(this::parseValue)
                .collect(Collectors.joining(",", "[", "]"));
    }



    @Override
    public Value visitMethod(TesterParser.MethodContext ctx) {
        return super.visitMethod(ctx);
    }

    @Override
    public Value visitEndpoint(TesterParser.EndpointContext ctx) {
        return super.visitEndpoint(ctx);
    }

    @Override
    public Value visitObj(TesterParser.ObjContext ctx) {
        Map<String, Value> map = new LinkedHashMap<>();
        List<TesterParser.PairContext> pairs = ctx.pair();
        for (TesterParser.PairContext p : pairs) {
            Pair pair = (Pair) visit(p);

             String key = pair.key();
             Value val  = pair.value();
             map.put(key, val);
        }
        return new ObjectValue(map);
    }

    @Override
    public Value visitPair(TesterParser.PairContext ctx) {
        String rawKey = ctx.STRING().getText();
        String key = processString(rawKey);
        Value val = visit(ctx.value());
        return new Pair(key, val);
    }

    @Override
    public Value visitArr(TesterParser.ArrContext ctx) {
        List<Value> items = new ArrayList<>();
        for (TesterParser.ValueContext v : ctx.value()) {
            items.add(visit(v));
        }
        return new ListValue(items);
    }

    @Override
    public Value visitValue(TesterParser.ValueContext ctx) {
        if (ctx.STRING() != null) {
            String text = processString(ctx.STRING().getText());
            return new StringValue(text);
        }
        else if (ctx.NUMBER() != null) {
            double num = Double.parseDouble(ctx.NUMBER().getText());
            return new NumberValue(num);
        }
        else if (ctx.TRUE() != null) {
            return new BooleanValue(true);
        }
        else if (ctx.FALSE() != null) {
            return new BooleanValue(false);
        }
        else if (ctx.obj() != null) {
            return visit(ctx.obj());
        }
        else if (ctx.arr() != null) {
            return visit(ctx.arr());
        }
        else if (ctx.path() != null) {
            return visit(ctx.path());
        }

        return super.visitValue(ctx);
    }

    @Override
    public Value visitAssertion(TesterParser.AssertionContext ctx) {
        Value result = visit(ctx.boolExpr());

        //TODO: Better output
        System.out.println(ctx.boolExpr().getText() + " = " + result);
        return result;
    }

    @Override
    public Value visitAssertionExpr(TesterParser.AssertionExprContext ctx) {
        var operator = switch (ctx.comparison().getText()) {
            case "==" -> Operator.EQ;
            case "!=" -> Operator.NEQ;
            case "<" -> Operator.LT;
            case "<=" -> Operator.LTE;
            case ">" -> Operator.GT;
            case ">=" -> Operator.GTE;
            default -> throw new RuntimeException("Unknown operator: " + ctx.comparison());
        };

        Value left = visit(ctx.lval);
        Value right = visit(ctx.rval);

        return new BooleanValue(left.applyOperator(operator, right));
    }

    @Override
    public Value visitProperty(TesterParser.PropertyContext ctx) {
        if (ctx.STRING() != null) {
            String text = processString(ctx.STRING().getText());
            return new StringValue(text);
        } else if (ctx.ID() != null) {
            String id = ctx.ID().getText();
            return new StringValue(id);
        } else if (ctx.NUMBER() != null) {
            double num = Double.parseDouble(ctx.NUMBER().getText());
            return new NumberValue(num);
        }
        return super.visitProperty(ctx);
    }

    @Override
    public Value visitBracketAccess(TesterParser.BracketAccessContext ctx) {
        return visit(ctx.property());
    }

    @Override
    public Value visitPath(TesterParser.PathContext ctx) {
        String root = ctx.ID().getText();

        List<Value> properties = new ArrayList<>();
        for (TesterParser.BracketAccessContext bracket : ctx.bracketAccess()) {
            properties.add(visit(bracket));
        }

        return dig(root, properties);
    }

    @Override
    public Value visitBoolExpr(TesterParser.BoolExprContext ctx) {
        if (ctx.assertionExpr() != null) {
            return visitAssertionExpr(ctx.assertionExpr());
        }

        if (ctx.NOT() != null && ctx.boolExpr().size() == 1) {
            boolean childBool = asBoolean(visit(ctx.boolExpr(0)));
            return new BooleanValue(!childBool);
        }

        if (ctx.LPAREN() != null && ctx.RPAREN() != null && ctx.boolExpr().size() == 1) {
            return visit(ctx.boolExpr(0));
        }

        if (ctx.boolExpr().size() == 2) {
            boolean leftVal = asBoolean(visit(ctx.lval));
            boolean rightVal = asBoolean(visit(ctx.rval));

            if (ctx.AND() != null) {
                return new BooleanValue(leftVal && rightVal);
            } else if (ctx.OR() != null) {
                return new BooleanValue(leftVal || rightVal);
            }
        }

        return super.visitBoolExpr(ctx);
    }

    public void pushScope() {
        localVars.enterScope();
    }

    public void popScope() {
        localVars.leaveScope();
    }

    public void setVariable(String paramName, Value value) {
        localVars.newSymbol(paramName);
        localVars.setSymbol(paramName, value);
    }

    private String stripQuotes(String text) {
        return text.substring(1, text.length() - 1);
    }

    private String interpolateString(String input) {

        Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z_][a-zA-Z0-9_]*)\\}");
        Matcher matcher = pattern.matcher(input);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String varName = matcher.group(1);
            Value varValue = localVars.getSymbol(varName);
            if (varValue == null) {
                throw new RuntimeException("Unknown var: " + varName);
            }
            String replacement = varValue.toString();
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    private String processString(String input) {
        return interpolateString(stripQuotes(input));
    }

    private Value dig(String rootName, List<Value> properties) {
        Value root = localVars.getSymbol(rootName);
        if (root == null) {
            throw new RuntimeException("Undefined variable: " + rootName);
        }

        for (Value property : properties) {
            if (property instanceof StringValue) {
                String propName = ((StringValue) property).value();
                if (root instanceof ObjectValue) {
                    root = ((ObjectValue) root).get(propName);
                } else if (root instanceof ListValue) {
                    throw new RuntimeException("Illegal argument: " + root.type() + " does not support: " + property.type());
                } else {
                    throw new RuntimeException("Undefined property: " + propName + " in " + root.type());
                }
            } else if (property instanceof NumberValue) {
                double propName = ((NumberValue) property).value();
                if (root instanceof ObjectValue) {
                    throw new RuntimeException("Illegal argument: " + root.type() + " does not support: " + property.type());
                } else if (root instanceof ListValue) {
                    root = ((ListValue) root).get((int) propName);
                } else {
                    throw new RuntimeException("Undefined property: " + propName + " in " + root.type());
                }
            }
        }

        return root;
    }

    private boolean asBoolean(Value v) {
        if (!(v instanceof BooleanValue)) {
            throw new RuntimeException("Value is not a boolean: " + v.type());
        }
        return ((BooleanValue) v).value();
    }
}