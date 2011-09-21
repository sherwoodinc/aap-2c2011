package budapest.pest.predtocvc3;

import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.BooleanLiteralPred;
import budapest.pest.ast.pred.NotPred;
import budapest.pest.ast.pred.QuantifiedPred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.dump.pest.PredPrinter;
import budapest.pest.pesttocvc3.PestVarContext;
import budapest.util.printer.prec.Brackets;

public class PredToCVC3Translator extends PredPrinter {
	
	PestVarContext initialContext;
	
	public PredToCVC3Translator(PestVarContext initialCtx) {
		initialContext = initialCtx;
	}

	public String visit(BinaryPred n, Void arg) {
		String result = super.visit(n, arg);
		result = result.replace("&&", "AND");
		result = result.replace("||", "OR");
		return "("+result+")";
	}

	public String visit(BooleanLiteralPred n, Void arg) {
		String result = super.visit(n, arg);
		result = result.replace("false", "FALSE");
		result = result.replace("true", "TRUE");
		return result;
	}

	public String visit(RelationPred n, Void arg) {
		String ret = n.left.accept(new TrmToCVC3Translator(initialContext), arg) + " ";
		
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
			ret += "/=";
			break;
		}
		
		ret += " " + n.right.accept(new TrmToCVC3Translator(initialContext), arg);
		return ret;
	}

	public String visit(NotPred n, Void arg) {
		String result = super.visit(n, arg);
		result = result.replace("! ", "NOT ");
		return result;
	}
	
	public String visit(QuantifiedPred n, Void arg) {
		String ret = "";
		switch(n.type) {
		case EXISTS:
			ret = "(EXISTS";
			break;
		case FORALL:
			ret = "(FORALL";
			break;
		}
		ret += " (";
		ret += n.var.toLowerCase();
		ret += ":INT): ";
		if (n.lowerBound != null && n.upperBound != null)
		{
			ret += " from ";
			ret += n.lowerBound.accept(new TrmToCVC3Translator(initialContext), arg);
			ret += " to ";
			ret += n.upperBound.accept(new TrmToCVC3Translator(initialContext), arg);
		}
		ret += " : " + Brackets.bracketsIfNeeded(n, n.subPred, this, arg);
		return ret;
	}
	
}
