package budapest.pest.ast.pred;

import budapest.pest.ast.visitor.PredVisitor;
import budapest.util.printer.prec.Precedence;

public final class BooleanLiteralPred extends Pred {

	public static enum Type { TRUEE, FALSEE }

	public final Type type;

	public BooleanLiteralPred(int line, int column, final Type type) {
		super(line, column);
		this.type = type;
	}
	
	@Override
	public <R, A> R accept(PredVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	public Precedence getPrecedence() {
		return Precedence.ATOM_PRED;
	}
	
}
