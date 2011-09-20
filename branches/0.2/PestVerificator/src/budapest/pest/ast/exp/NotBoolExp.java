package budapest.pest.ast.exp;

import budapest.pest.ast.visitor.ExpVisitor;
import budapest.util.printer.prec.Precedence;

public final class NotBoolExp extends BoolExp {
	
	public final BoolExp subExp;

	public NotBoolExp(int line, int column, final BoolExp subExp) {
		super(line, column);
		this.subExp = subExp;
	}

	public Precedence getPrecedence() {
		return Precedence.UNARY_TERM;
	}
	
	@Override
	public <R, A> R accept(ExpVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

}
