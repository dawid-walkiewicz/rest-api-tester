import grammar.TesterParser;
import grammar.TesterParserBaseVisitor;
import symbols.GlobalSymbols;
import symbols.LocalSymbols;
import types.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TesterVisitor extends TesterParserBaseVisitor<Value> {
    private GlobalSymbols<Value> globalVars;
    private LocalSymbols<Value> localVars;

    private String currentTestCaseName;
    private int currentTestRepeats;
    private int currentTestTimeout;

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
        return super.visitOptionsBlock(ctx);
    }

    @Override
    public Value visitOptionsList(TesterParser.OptionsListContext ctx) {
        return super.visitOptionsList(ctx);
    }

    @Override
    public Value visitOption(TesterParser.OptionContext ctx) {
        if ("repeat".equals(ctx.ID().getText())) {
            Value result = visit(ctx.optionValue());
            currentTestRepeats = (int) ((NumberValue) result).getValue();
            return result;
        } else if ("timeout".equals(ctx.ID().getText())) {
            Value result = visit(ctx.optionValue());
            currentTestTimeout = (int) ((NumberValue) result).getValue();
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
        return super.visitRequest(ctx);
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
        return super.visitAssertion(ctx);
    }

    @Override
    public Value visitAssertionExpr(TesterParser.AssertionExprContext ctx) {
        return super.visitAssertionExpr(ctx);
    }

    @Override
    public Value visitComparison(TesterParser.ComparisonContext ctx) {
        return super.visitComparison(ctx);
    }

    @Override
    public Value visitRootElement(TesterParser.RootElementContext ctx) {
        String root = "";
        if (ctx.RESPONSE() != null) {
            root = ctx.RESPONSE().getText();
        } else if (ctx.BODY() != null) {
            root = ctx.BODY().getText();
        } else if (ctx.HEADERS() != null) {
            root = ctx.HEADERS().getText();
        } else if (ctx.STATUS() != null) {
            root = ctx.STATUS().getText();
        } else if (ctx.TYPE() != null) {
            root = ctx.TYPE().getText();
        } else if (ctx.ID() != null) {
            root = ctx.ID().getText();
        }
        return new StringValue(root);
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
        Value root = visit(ctx.rootElement());
        String rootName = ((StringValue) root).getValue();

        List<Value> properties = new ArrayList<>();
        for (TesterParser.BracketAccessContext bracket : ctx.bracketAccess()) {
            properties.add(visit(bracket));
        }

        return dig(rootName, properties);
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
            String varName = matcher.group(1); // nazwa zmiennej wewnątrz ${...}
            // pobierz wartość zmiennej z aktualnej tabeli symboli (np. localVars)
            Value varValue = localVars.getSymbol(varName);
            if (varValue == null) {
                // obsługa błędu lub załóż, że niewłaściwa zmienna
                throw new RuntimeException("Nieznana zmienna: " + varName);
            }
            // zastąp w stringu:
            String replacement = varValue.toString(); // lub cokolwiek innego
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
                String propName = ((StringValue) property).getValue();
                if (root instanceof ObjectValue) {
                    root = ((ObjectValue) root).getValue(propName);
                } else if (root instanceof ListValue) {
                    throw new RuntimeException("Illegal argument: " + root.type() + " does not support: " + property.type());
                } else {
                    throw new RuntimeException("Undefined property: " + propName + " in " + root.type());
                }
            } else if (property instanceof NumberValue) {
                Double propName = ((NumberValue) property).getValue();
                if (root instanceof ObjectValue) {
                    throw new RuntimeException("Illegal argument: " + root.type() + " does not support: " + property.type());
                } else if (root instanceof ListValue) {
                    root = ((ListValue) root).get(propName.intValue());
                } else {
                    throw new RuntimeException("Undefined property: " + propName + " in " + root.type());
                }
            }
        }

        return root;
    }

}