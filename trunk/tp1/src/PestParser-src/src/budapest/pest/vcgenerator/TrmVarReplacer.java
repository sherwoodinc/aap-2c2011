package budapest.pest.vcgenerator;

import budapest.pest.ast.pred.trm.ArrayAccessTrm;
import budapest.pest.ast.pred.trm.ArraySizeTrm;
import budapest.pest.ast.pred.trm.BinaryTrm;
import budapest.pest.ast.pred.trm.IntegerLiteralTrm;
import budapest.pest.ast.pred.trm.NegTrm;
import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.pred.trm.VarTrm;
import budapest.pest.ast.visitor.TrmVisitor;

public class TrmVarReplacer extends TrmVisitor<Trm, VarReplacement> {

	public Trm visit(BinaryTrm n, VarReplacement arg) {
		return new BinaryTrm(n.line, 
				n.column, 
				n.left.accept(this, arg), 
				n.op, 
				n.right.accept(this, arg)); 
	}
	
	public Trm visit(ArrayAccessTrm n, VarReplacement arg) {
		return new ArrayAccessTrm(n.line, 
				n.column, 
				arg.execute(n.array), 
				n.index.accept(this, arg), 
				n.type);		
	}
	
	public Trm visit(ArraySizeTrm n, VarReplacement arg) {
		return new ArraySizeTrm(n.line,
				n.column,
				arg.execute(n.array));
	}
	
	public Trm visit(IntegerLiteralTrm n, VarReplacement arg) {
		return new IntegerLiteralTrm(n.line, n.column, n.value);
	}
	
	public Trm visit(NegTrm n, VarReplacement arg) {
		return new NegTrm(n.line, n.column, n.subTrm.accept(this, arg));
	}
	
	public Trm visit(VarTrm n, VarReplacement arg) {
		return new VarTrm(n.line, n.column, arg.execute(n.name), n.type);
	}
}
