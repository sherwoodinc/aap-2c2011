package budapest.pest.ast.exp;

import budapest.pest.ast.visitor.ExpVisitor;
import budapest.util.printer.prec.Precedence;

public final class ArithTopExp extends TopExp {

	public static enum Operator { ADD, SUBS, MUL, DIV }  
		
	public final TopExp left;
	
	public final Operator op;
	
	public final TopExp right;

	public ArithTopExp(int line, int column, final TopExp left, 
			final Operator op, final TopExp right) {
		super(line, column);
		this.left = left;
		this.op = op;
		this.right = right;
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
	
	@Override
	public <R, A> R accept(ExpVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

}
