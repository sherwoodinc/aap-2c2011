package budapest.pest.ast.pred;

import budapest.pest.ast.visitor.PredVisitor;
import budapest.util.printer.prec.Precedence;

public final class NotPred extends Pred {
	
	public final Pred subPred;

	public NotPred(int line, int column, final Pred subPred) {
		super(line, column);
		this.subPred = subPred;
	}
	
	@Override
	public <R, A> R accept(PredVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	public Precedence getPrecedence() {
		return Precedence.UNARY_PRED;
	}
	
}
