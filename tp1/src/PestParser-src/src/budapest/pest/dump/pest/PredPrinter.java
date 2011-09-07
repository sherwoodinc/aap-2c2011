package budapest.pest.dump.pest;

import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.BooleanLiteralPred;
import budapest.pest.ast.pred.QuantifiedPred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.ast.pred.NotPred;
import budapest.pest.ast.visitor.PredVisitor;
import budapest.util.printer.prec.Brackets;

public class PredPrinter extends PredVisitor<String, Void> {

	@Override
	public String visit(BinaryPred n, Void arg) {
		String ret = "";
		ret += Brackets.bracketsIfNeeded(n, n.left, this, arg);
		ret += " ";
		
		switch(n.op) {
		case AND:
			ret += "&&";
			break;
		case IFF:
			ret += "<=>";
			break;
		case IMPLIES:
			ret += "=>";
			break;
		case OR:
			ret += "||";
			break;
		}
		
		ret += " ";
		ret += Brackets.bracketsIfNeeded(n, n.right, this, arg);
		return ret;
	}

	@Override
	public String visit(BooleanLiteralPred n, Void arg) {
		String ret = "";
		switch (n.type) {
		case FALSEE:
			ret += "false";
			break;
		case TRUEE:
			ret += "true";
			break;
		}
		return ret;
	}

	@Override
	public String visit(RelationPred n, Void arg) {
		String ret = n.left.accept(new TrmPrinter(), arg) + " ";
		
		switch(n.op) {
		case EQ:
			ret += "=";
			break;
		case GE:
			ret += ">=";
			break;
		case GT:
			ret += ">";
			break;
		case LE:
			ret += "<=";
			break;
		case LT:
			ret += "<";
			break;
		case NEQ:
			ret += "!=";
			break;
		}
		
		ret += " " + n.right.accept(new TrmPrinter(), arg);
		return ret;
	}

	@Override
	public String visit(NotPred n, Void arg) {
		return "! " + Brackets.bracketsIfNeeded(n, n.subPred, this, arg);
	}

	@Override
	public String visit(QuantifiedPred n, Void arg) {
		String ret = "";
		switch(n.type) {
		case EXISTS:
			ret = "exists";
			break;
		case FORALL:
			ret = "forall";
			break;
		}
		ret += " ";
		ret += n.var;
		ret += " from ";
		ret += n.lowerBound.accept(new TrmPrinter(), arg);
		ret += " to ";
		ret += n.upperBound.accept(new TrmPrinter(), arg);
		ret += " : " + Brackets.bracketsIfNeeded(n, n.subPred, this, arg);
		return ret;
	}
	
}
