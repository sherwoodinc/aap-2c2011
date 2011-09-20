package budapest.pest.ast.exp;

import budapest.pest.ast.visitor.ExpVisitor;
import budapest.util.printer.prec.Precedence;

public final class RelBoolExp extends BoolExp {

	public static enum Operator { EQ, NEQ, LT, LE, GT, GE	}  
	
	public final IntExp left;
	
	public final Operator op;
	
	public final IntExp right;

	public RelBoolExp(int line, int column, final IntExp left, 
			final Operator op, final IntExp right) {
		super(line, column);
		this.left = left;
		this.op = op;
		this.right = right;
	}

	public Precedence getPrecedence() {
		return Precedence.ATOM_PRED;
	}
	
	@Override
	public <R, A> R accept(ExpVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
}
