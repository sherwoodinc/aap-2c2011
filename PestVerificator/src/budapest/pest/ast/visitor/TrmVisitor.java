package budapest.pest.ast.visitor;

import budapest.pest.ast.pred.trm.ArrayAccessTrm;
import budapest.pest.ast.pred.trm.ArraySizeTrm;
import budapest.pest.ast.pred.trm.BinaryTrm;
import budapest.pest.ast.pred.trm.IntegerLiteralTrm;
import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.pred.trm.NegTrm;
import budapest.pest.ast.pred.trm.VarTrm;

public abstract class TrmVisitor<R,A> {

	// basic trm
	public R visit(Trm n, A arg) {
		throw new IllegalStateException(n.getClass().getName());
	}
	
	// concrete trms
	public R visit(BinaryTrm n, A arg) {
		n.left.accept(this, arg);
		n.right.accept(this, arg);
		return null;
	}
	
	public R visit(ArrayAccessTrm n, A arg) {
		n.index.accept(this, arg);
		return null;
	}
	
	public R visit(ArraySizeTrm n, A arg) {
		return null;
	}
	
	public R visit(IntegerLiteralTrm n, A arg) {
		return null;
	}
	
	public R visit(NegTrm n, A arg) {
		n.subTrm.accept(this, arg);
		return null;
	}
	
	public R visit(VarTrm n, A arg) {
		return null;
	}
	
}
