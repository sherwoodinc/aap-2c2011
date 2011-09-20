package budapest.pest.ast.pred.trm;

import budapest.pest.ast.visitor.TrmVisitor;
import budapest.util.printer.prec.Precedence;

public final class BinaryTrm extends Trm {

	public enum Operator { ADD, SUBS, MUL, DIV	}
	
	public final Trm left;
	
	public final Operator op;
	
	public final Trm right;

	public BinaryTrm(int line, int column, final Trm left, 
			final Operator op, final Trm right) {
		super(line, column);
		this.left = left;
		this.op = op;
		this.right = right;
	}

	@Override
	public <R, A> R accept(TrmVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
	public Precedence getPrecedence() {
		switch(op) {
		case ADD:
		case SUBS:
			return Precedence.SUM_TERM;
		case DIV:
		case MUL:
			return Precedence.MUL_TERM;
		}
		return null;
	}

}
