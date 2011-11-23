package budapest.pest.ast.exp;

import budapest.pest.ast.visitor.ExpVisitor;
import budapest.util.printer.prec.Precedence;

public final class LiteralStringExp extends TopExp {
	
	public final String value;

	public LiteralStringExp(int line, int column, final String value) {
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
