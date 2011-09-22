package budapest.pest.predtocvc3;

import java.util.Map;

import budapest.pest.ast.pred.trm.ArrayAccessTrm;
import budapest.pest.ast.pred.trm.ArraySizeTrm;
import budapest.pest.ast.pred.trm.BinaryTrm;
import budapest.pest.ast.pred.trm.IntegerLiteralTrm;
import budapest.pest.ast.pred.trm.NegTrm;
import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.pred.trm.VarTrm;
import budapest.pest.ast.visitor.TrmVisitor;

/**
 * Este visitor de Terms reemplaza nombres de variables por sus Terms correspondientes.
 * El Map parámetro es el binding de los nombres de variables a Terms.
 */
public class TrmParamReplacer extends TrmVisitor<Trm, Map<String, Trm>> {

	// concrete trms
	public Trm visit(BinaryTrm n, Map<String, Trm> arg) {
		return new BinaryTrm(
				n.line,
				n.column,				
				n.left.accept(this, arg),
				n.op,
				n.right.accept(this, arg));	
	}
	
	public Trm visit(ArrayAccessTrm n, Map<String, Trm> arg) {
		n.index.accept(this, arg);
		return null;
	}
	
	public Trm visit(ArraySizeTrm n, Map<String, Trm> arg) {
		return null;
	}
	
	public Trm visit(IntegerLiteralTrm n, Map<String, Trm> arg) {
		return new IntegerLiteralTrm(n.line, n.column, n.value);
	}
	
	public Trm visit(NegTrm n, Map<String, Trm> arg) {
		return n.subTrm.accept(this, arg);
	}
	
	public Trm visit(VarTrm n, Map<String, Trm> arg) {
		return arg.get(n.name);
	}

}
