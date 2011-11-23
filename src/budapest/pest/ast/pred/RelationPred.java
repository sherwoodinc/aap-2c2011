package budapest.pest.ast.pred;

import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.visitor.PredVisitor;
import budapest.util.printer.prec.Precedence;

public final class RelationPred extends Pred {

	public static enum Operator { EQ, NEQ, LT, LE, GT, GE }
	
	public final Trm left;
	
	public final Operator op;
	
	public final Trm right;

	public RelationPred(int line, int column, final Trm left, 
			final Operator op, final Trm right) {
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
		return Precedence.ATOM_PRED;
	}
	
}
