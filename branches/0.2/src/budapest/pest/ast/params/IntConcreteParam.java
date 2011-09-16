package budapest.pest.ast.params;

import budapest.pest.ast.exp.Exp;

public final class IntConcreteParam extends ConcreteParam {

	public final Exp exp;

	public IntConcreteParam(final Exp exp) {
		this.exp = exp;
	}

	@Override
	public String toString() {
		return exp.toString();
	}
	
}
