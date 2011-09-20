package budapest.pest.ast.pred.trm;

import budapest.pest.ast.visitor.TrmVisitor;
import budapest.util.printer.prec.Precedence;

public final class ArraySizeTrm extends Trm {

	public final String array;

	public ArraySizeTrm(int line, int column, final String array) {
		super(line, column);
		this.array = array;
	}
	
	@Override
	public <R, A> R accept(TrmVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
	public Precedence getPrecedence() {
		return Precedence.ATOM_TERM;
	}
	
}
