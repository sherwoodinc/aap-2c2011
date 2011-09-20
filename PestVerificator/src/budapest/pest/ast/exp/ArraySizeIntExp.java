package budapest.pest.ast.exp;

import budapest.pest.ast.visitor.ExpVisitor;
import budapest.util.printer.prec.Precedence;

public class ArraySizeIntExp extends IntExp {

	public final String array;

	public ArraySizeIntExp(int line, int column, final String array) {
		super(line, column);
		this.array = array;
	}
	
	@Override
	public <R, A> R accept(ExpVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
	public Precedence getPrecedence() {
		return Precedence.ATOM_TERM;
	}
	
}
