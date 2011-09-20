package budapest.pest.ast.exp;

import budapest.pest.ast.visitor.ExpVisitor;
import budapest.util.printer.prec.Precedence;

public final class ArithIntExp extends IntExp {

	public static enum Operator { ADD, SUBS, MUL, DIV }  
		
	public final IntExp left;
	
	public final Operator op;
	
	public final IntExp right;

	public ArithIntExp(int line, int column, final IntExp left, 
			final Operator op, final IntExp right) {
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
