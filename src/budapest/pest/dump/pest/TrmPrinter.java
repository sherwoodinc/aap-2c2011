package budapest.pest.dump.pest;

import budapest.pest.ast.pred.trm.BinaryTrm;
import budapest.pest.ast.pred.trm.IntegerLiteralTrm;
import budapest.pest.ast.pred.trm.NegTrm;
import budapest.pest.ast.pred.trm.StringLiteralTrm;
import budapest.pest.ast.pred.trm.StringSizeTrm;
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
	public String visit(StringLiteralTrm n, Void arg) {
		return "\"" + n.value + "\"";
	}
	
	@Override
	public String visit(StringSizeTrm n, Void arg) {
		return "|" + n.string + "|";
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
	
}
