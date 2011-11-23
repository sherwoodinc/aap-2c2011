package budapest.pest.ast.exp;

import budapest.pest.ast.visitor.ExpVisitor;
import budapest.util.printer.prec.Precedence;

public final class LiteralIntExp extends TopExp {
	
	public final int value;

	public LiteralIntExp(int line, int column, final int value) {
		super(line, column);
		this.value = value;
	}
	
	@Override
	public <R, A> R accept(ExpVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	public Precedence getPrecedence() {
		return Precedence.ATOM_TERM;
	}
	
}
