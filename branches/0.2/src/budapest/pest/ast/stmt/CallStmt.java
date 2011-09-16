package budapest.pest.ast.stmt;

import java.util.List;

import budapest.pest.ast.params.ConcreteParam;
import budapest.pest.ast.visitor.PestVisitor;

public class CallStmt extends Stmt {

	public final String procName;
	public final List<ConcreteParam> params;
	
	public CallStmt(int line, int column, final String procName, 
			final List<ConcreteParam> params) {
		super(line, column);
		this.procName = procName;
		this.params = params;
	}

	@Override
	public <R, A> R accept(PestVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
}
