package budapest.pest.dump.pest;

import budapest.pest.ast.pred.trm.ArrayAccessTrm;
import budapest.pest.ast.pred.trm.ArraySizeTrm;
import budapest.pest.ast.pred.trm.BinaryTrm;
import budapest.pest.ast.pred.trm.IntegerLiteralTrm;
import budapest.pest.ast.pred.trm.NegTrm;
import budapest.pest.ast.pred.trm.VarTrm;
import budapest.pest.ast.visitor.TrmVisitor;
import budapest.util.printer.prec.Brackets;

public class TrmPrinter extends TrmVisitor<String, Void> {

	@Override
	public String visit(BinaryTrm n, Void arg) {
		String ret = "";		
		ret += Brackets.bracketsIfNeeded(n, n.left, this, arg);
		ret += " ";
		
		switch(n.op) {
		case ADD:
			ret += "+";
			break;
		case DIV:
			ret += "/";
			break;
		case MUL:
			ret += "*";
			break;
		case SUBS:
			ret += "-";
			break;
		}
		
		ret += " ";
		ret += Brackets.bracketsIfNeeded(n, n.right, this, arg);
		return ret;
	}
	
	@Override
	public String visit(IntegerLiteralTrm n, Void arg) {
		return Integer.toString(n.value);
	}

	@Override
	public String visit(NegTrm n, Void arg) {
		return "-" + Brackets.bracketsIfNeeded(n, n.subTrm, this, arg);
	}

	@Override
	public String visit(VarTrm n, Void arg) {
		String ret = n.name;
		switch(n.type) {
		case PRE_VALUE:
			ret += "@pre";
			break;
		case CURR_VALUE:
			break;
		}
		return ret;
	}

	@Override
	public String visit(ArrayAccessTrm n, Void arg) {
		String ret = n.array;
		switch(n.type) {
		case PRE_VALUE:
			ret += "@pre";
			break;
		case CURR_VALUE:
			break;
		}
		ret += "[" + n.index.accept(this, arg) + "]";
		return ret;
	}
	
	@Override
	public String visit(ArraySizeTrm n, Void arg) {
		return "|" + n.array + "|";
	}
	
}
