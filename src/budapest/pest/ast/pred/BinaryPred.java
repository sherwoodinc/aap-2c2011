package budapest.pest.ast.pred;

import budapest.pest.ast.visitor.PredVisitor;
import budapest.util.printer.prec.Precedence;

public final class BinaryPred extends Pred {

	public static enum Operator { AND, OR, IMPLIES, IFF	}
	
	public final Pred left;
	
	public final Operator op;
	
	public final Pred right;

	public BinaryPred(int line, int column, final Pred left, 
			final Operator op, final Pred right) {
		super(line, column);
		this.left = left;
		this.op = op;
		this.right = right;
	}
	
	@Override
	public <R, A> R accept(PredVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	public Precedence getPrecedence() {
		switch(op) {
		case AND:
			return Precedence.AND_PRED;
		case IFF:
		case IMPLIES:
			return Precedence.IMPLIES_PRED;
		case OR:
			return Precedence.OR_PRED;
		}
		return null;
	}
	
}
