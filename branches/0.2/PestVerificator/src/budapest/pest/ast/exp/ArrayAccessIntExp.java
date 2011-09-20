package budapest.pest.ast.exp;

import budapest.pest.ast.visitor.ExpVisitor;
import budapest.util.printer.prec.Precedence;

public final class ArrayAccessIntExp extends IntExp {

	public final String array;
	public final IntExp index;
	
	public ArrayAccessIntExp(int line, int column, 
			final String array, final IntExp index) {
		super(line, column);
		this.array = array;
		this.index = index;
	}
	
	@Override
	public <R, A> R accept(ExpVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
	public Precedence getPrecedence() {
		return Precedence.ATOM_TERM;
	}
	
}
