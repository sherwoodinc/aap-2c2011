package budapest.pest.ast.pred;

import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.visitor.PredVisitor;
import budapest.util.printer.prec.Precedence;

public final class QuantifiedPred extends Pred {

	public static enum Type { FORALL, EXISTS } 
	
	public final Type type;
	public final String var;
	public final Trm lowerBound;
	public final Trm upperBound;
	public final Pred subPred;
	
	public QuantifiedPred(int line, int column, final Type type, final String var, 
			final Trm lowerBound, final Trm upperBound, final Pred subPred) {
		super(line, column);
		this.type = type;
		this.var = var;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
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
