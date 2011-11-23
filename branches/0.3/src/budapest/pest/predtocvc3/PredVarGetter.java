package budapest.pest.predtocvc3;

import java.util.HashSet;
import java.util.Set;

import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.BooleanLiteralPred;
import budapest.pest.ast.pred.NotPred;
import budapest.pest.ast.pred.QuantifiedPred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.ast.visitor.PredVisitor;

public class PredVarGetter extends PredVisitor<Set<String>, Void> {
	
	public Set<String> visit(BinaryPred n, Void arg) {
		Set<String> result = new HashSet<String>();
		result.addAll(n.left.accept(this, arg));
		result.addAll(n.right.accept(this, arg));
		return result;
	}
	
	public Set<String> visit(BooleanLiteralPred n, Void arg) {
		return new HashSet<String>();
	}
	
	public Set<String> visit(NotPred n, Void arg) {
		Set<String> result = new HashSet<String>();
		result.addAll(n.subPred.accept(this, arg));
		return result;
	}
	
	public Set<String> visit(QuantifiedPred n, Void arg) {
		Set<String> result = new HashSet<String>();
		result.add(n.var);
		result.addAll(n.subPred.accept(this, arg));
		if (n.lowerBound != null)
			result.addAll(n.lowerBound.accept(new TrmVarGetter(), arg));
		if (n.upperBound != null)
			result.addAll(n.upperBound.accept(new TrmVarGetter(), arg));
		return result;
	}
	
	public Set<String> visit(RelationPred n, Void arg) {
		Set<String> result = new HashSet<String>();
		result.addAll(n.left.accept(new TrmVarGetter(), arg));
		result.addAll(n.right.accept(new TrmVarGetter(), arg));
		return result;
	}
}
