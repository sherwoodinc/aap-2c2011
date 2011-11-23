package budapest.pest.ast.pred.trm;

import budapest.pest.ast.visitor.TrmVisitor;
import budapest.util.printer.prec.Precedence;

public final class StringLiteralTrm extends Trm {

	public final String value;

	public StringLiteralTrm(int line, int column, final String value) {
		super(line, column);
		this.value = value;
	}
	
	@Override
	public <R, A> R accept(TrmVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
	public Precedence getPrecedence() {
		return Precedence.ATOM_TERM;
	}

}
