package budapest.pest.ast.exp;

import budapest.pest.ast.visitor.ExpVisitor;
import budapest.util.printer.prec.Precedence;

public final class VarIntExp extends IntExp {

	public final String name;

	public VarIntExp(int line, int column, final String name) {
		super(line, column);
		this.name = name;
	}
	
	@Override
	public <R, A> R accept(ExpVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	public Precedence getPrecedence() {
		return Precedence.ATOM_TERM;
	}
	
}
