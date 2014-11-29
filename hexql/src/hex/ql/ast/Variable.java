package hex.ql.ast;

/**
 * Created by jason on 14-10-26.
 */
public class Variable implements Node {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
