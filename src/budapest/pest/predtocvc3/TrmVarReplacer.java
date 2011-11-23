package budapest.pest.predtocvc3;

import budapest.pest.ast.pred.trm.BinaryTrm;
import budapest.pest.ast.pred.trm.IntegerLiteralTrm;
import budapest.pest.ast.pred.trm.NegTrm;
import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.pred.trm.VarTrm;
import budapest.pest.ast.visitor.TrmVisitor;
import budapest.pest.pesttocvc3.PestVarContext;

public class TrmVarReplacer extends TrmVisitor<Trm, PestVarContext> {

	public Trm visit(BinaryTrm n, PestVarContext arg) {
		return new BinaryTrm(n.line, 
				n.column, 
				n.left.accept(this, arg), 
				n.op, 
				n.right.accept(this, arg)); 
	}
	
	public Trm visit(IntegerLiteralTrm n, PestVarContext arg) {
		return new IntegerLiteralTrm(n.line, n.column, n.value);
	}
	
	public Trm visit(NegTrm n, PestVarContext arg) {
		return new NegTrm(n.line, n.column, n.subTrm.accept(this, arg));
	}
	
	public Trm visit(VarTrm n, PestVarContext arg) {
		return new VarTrm(n.line, n.column, arg.getInstanceOf(n.name), n.type);
	}
}
