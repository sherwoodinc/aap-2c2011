package budapest.pest.vcgenerator;

import java.util.ArrayList;
import java.util.List;

import budapest.pest.ast.pred.trm.ArrayAccessTrm;
import budapest.pest.ast.pred.trm.ArraySizeTrm;
import budapest.pest.ast.pred.trm.BinaryTrm;
import budapest.pest.ast.pred.trm.IntegerLiteralTrm;
import budapest.pest.ast.pred.trm.NegTrm;
import budapest.pest.ast.visitor.TrmVisitor;

public class TrmFreshVarGetter extends TrmVisitor<List<String>, Void> {
	
	public List<String> visit(BinaryTrm n, Void arg) {
		List<String> results = new ArrayList<String>();
		results.addAll(n.left.accept(this, arg));
		results.addAll(n.right.accept(this, arg));
		return results;
	}
	
	public List<String> visit(ArrayAccessTrm n, Void arg) {
		List<String> results = new ArrayList<String>();
		results.add(n.array);
		results.addAll(n.index.accept(this, arg));
		return results;
	}
	
	public List<String> visit(ArraySizeTrm n, Void arg) {
		List<String> results = new ArrayList<String>();
		results.add(n.array);
		return results;
	}
	
	public List<String> visit(IntegerLiteralTrm n, Void arg) {
		return new ArrayList<String>();
	}
	
	public List<String> visit(NegTrm n, Void arg) {
		List<String> results = new ArrayList<String>();
		results.addAll(n.subTrm.accept(this, arg));
		return results;
	}
	
}
