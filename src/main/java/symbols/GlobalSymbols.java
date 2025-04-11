package symbols;

import java.util.HashMap;

public class GlobalSymbols<T> {

    HashMap<String, T> memory = new HashMap<>();

    /**
     * default constructor
     */
    public GlobalSymbols() {
    }

    /**
     * checks the existence of the variable in symbol table
     * @param name
     * @return true - exists, false - not exists
     */
    public boolean hasSymbol(String name) {
        return memory.containsKey(name);
    }

    /**
     * makes new variable in the symbol table (the value is null)
     * @param name
     * @throws RuntimeException if the variable already exists
     */
    public void newSymbol(String name) throws RuntimeException{
        if (! hasSymbol(name))
            memory.put(name, null);
        else
            throw new RuntimeException("Variable " + name +" exists!");
    }

    /**
     * makes new variable in the symbol table
     * @param name
     * @param value
     * @throws RuntimeException if the variable already exists
     */
    public void newSymbol(String name, T value) throws RuntimeException{
        if (! hasSymbol(name))
            memory.put(name, value);
        else
            throw new RuntimeException("Variable " + name +" exists!");
    }

    /**
     * sets value to the variable <b>existing</b> in the symbol table
     * @param name
     * @param value
     * @throws RuntimeException if the variable does not exist
     */
    public void setSymbol(String name, T value) throws RuntimeException {
        if( hasSymbol(name))
            memory.put(name, value);
        else
            throw new RuntimeException("Variable " + name +" does not exist!");
    }

    /**
     * gets value of the variable <b>existing</b> in the symbol table
     * @param name
     * @return the value
     * @throws RuntimeException if the variable does not exist
     */
    public T getSymbol(String name) throws RuntimeException {
        if( hasSymbol(name))
            return memory.get(name);
        else
            throw new RuntimeException("Variable " + name +" does not exist!");
    }

}
