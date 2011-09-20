package budapest.pest.ast.proc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import budapest.pest.ast.params.FormalParam;
import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.stmt.Stmt;
import budapest.pest.ast.visitor.PestVisitor;
import budapest.util.ASTNode;

public final class Procedure extends ASTNode {

	public final String name;
	public final List<FormalParam> params;

	public final Pred pre;
	public final Pred post;
	public final List<FormalParam> touches;
	
	public final Stmt stmt;
	
	public Procedure(int line, int column, final String name, 
			final List<FormalParam> params, final Pred pre, final Pred post, 
			final List<FormalParam> touches, final Stmt stmt) {
		super(line, column);
		this.name = name;
		this.params = params;
		this.pre = pre;
		this.post = post;
		this.touches = touches;
		this.stmt = stmt;
	}

	public <R, A> R accept(PestVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
	public Set<String> paramNames() {
		Set<String> ret = new HashSet<String>();
		for(FormalParam p : params)
			ret.add(p.name);
		return ret;
	}

}
