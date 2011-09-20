package budapest.pest.predtocvc3;

import java.util.HashSet;
import java.util.Set;

import budapest.pest.ast.pred.trm.ArrayAccessTrm;
import budapest.pest.ast.pred.trm.ArraySizeTrm;
import budapest.pest.ast.pred.trm.BinaryTrm;
import budapest.pest.ast.pred.trm.IntegerLiteralTrm;
import budapest.pest.ast.pred.trm.NegTrm;
import budapest.pest.ast.pred.trm.VarTrm;
import budapest.pest.ast.visitor.TrmVisitor;

public class TrmVarGetter extends TrmVisitor<Set<String>, Void> {
	
	public Set<String> visit(BinaryTrm n, Void arg) {
		Set<String> results = new HashSet<String>();
		results.addAll(n.left.accept(this, arg));
		results.addAll(n.right.accept(this, arg));
		return results;
	}
	
	public Set<String> visit(ArrayAccessTrm n, Void arg) {
		Set<String> results = new HashSet<String>();
		results.add(n.array);
		results.addAll(n.index.accept(this, arg));
		return results;
	}
	
	public Set<String> visit(ArraySizeTrm n, Void arg) {
		Set<String> results = new HashSet<String>();
		results.add(n.array);
		return results;
	}
	
	public Set<String> visit(IntegerLiteralTrm n, Void arg) {
		return new HashSet<String>();
	}
	
	public Set<String> visit(NegTrm n, Void arg) {
		Set<String> results = new HashSet<String>();
		results.addAll(n.subTrm.accept(this, arg));
		return results;
	}
	
	public Set<String> visit(VarTrm n, Void arg) {
		Set<String> results = new HashSet<String>();
		results.add(n.name);
		return results;
	}
	
}
