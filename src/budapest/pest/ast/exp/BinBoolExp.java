package budapest.pest.ast.exp;

import budapest.pest.ast.visitor.ExpVisitor;
import budapest.util.printer.prec.Precedence;

public final class BinBoolExp extends BoolExp {

	public static enum Operator { AND, OR}
	
	public final BoolExp left;
	
	public final Operator op;
	
	public final BoolExp right;

	public BinBoolExp(int line, int column, final BoolExp left, 
			final Operator op, final BoolExp right) {
		super(line, column);
		this.left = left;
		this.op = op;
		this.right = right;
	}
	
	public Precedence getPrecedence() {
		switch(op) {
		case AND:
			return Precedence.AND_PRED;
		case OR:
			return Precedence.OR_PRED;
		}
		return null;
	}
	
	@Override
	public <R, A> R accept(ExpVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
}
