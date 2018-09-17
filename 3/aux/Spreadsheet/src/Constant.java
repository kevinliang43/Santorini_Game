//represents and IFormula constant number
public class Constant implements IFormula {
    int constant;

    Constant(int constant) {
        this.constant = constant;
    }

    //returns the constant value when evaluated
    public int evaluate() throws Exception {
        return constant;
    }

    //checks if the formula is a cell reference, returns false
    public boolean cellReference() {
        return false;
    }
}
