package budapest.pest.ast.pred.trm;

import budapest.pest.ast.visitor.TrmVisitor;
import budapest.util.printer.prec.Precedence;

public final class StringSizeTrm extends Trm {

	public final String string;

	public StringSizeTrm(int line, int column, final String string) {
		super(line, column);
		this.string = string;
	}
	
	@Override
	public <R, A> R accept(TrmVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
	public Precedence getPrecedence() {
		return Precedence.ATOM_TERM;
	}
	
}
