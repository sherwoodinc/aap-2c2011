package budapest.pest.ast.stmt;

import java.util.Set;

import budapest.pest.ast.visitor.PestVisitor;
import budapest.pest.checker.symtab.ProcsInfo;
import budapest.pest.dump.pest.PestPrinter;
import budapest.util.ASTNode;

public abstract class Stmt extends ASTNode {

	public Stmt(int line, int column) {
		super(line, column);
	}
	
	public abstract <R,A> R accept(PestVisitor<R, A> v, A arg);
	
	@Override
	public String toString() {
		PestPrinter pd = new PestPrinter();
		accept(pd, null);
		return pd.getSource();
	}

	public Set<String> modifiedVars(ProcsInfo pi) {
		// TODO Auto-generated method stub
		return null;
	}

	public String readVars() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> modifiedNonLocalVars(ProcsInfo pi) {
		// TODO Auto-generated method stub
		return null;
	}
	
}