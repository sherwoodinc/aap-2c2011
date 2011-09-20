package budapest.pest.ast.pred.trm;

import budapest.pest.ast.visitor.TrmVisitor;
import budapest.util.printer.prec.Precedence;

public final class VarTrm extends Trm {

	public final String name;
	
	public final Type type;
	
	public VarTrm(int line, int column, 
			final String name, final Type type) {
		super(line, column);
		this.name = name;
		this.type = type;
	}
	
	@Override
	public <R, A> R accept(TrmVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	public Precedence getPrecedence() {
		return Precedence.ATOM_TERM;
	}
	
}
