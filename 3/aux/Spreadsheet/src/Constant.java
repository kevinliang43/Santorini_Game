public class Constant implements IFormula {
    int constant;

    Constant(int constant) {
        this.constant = constant;
    }

    @Override
    public int evaluate() {
        return constant;
    }
}
