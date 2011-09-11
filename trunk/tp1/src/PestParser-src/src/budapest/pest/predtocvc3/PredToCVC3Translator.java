package budapest.pest.predtocvc3;

import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.BooleanLiteralPred;
import budapest.pest.ast.pred.NotPred;
import budapest.pest.ast.pred.QuantifiedPred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.dump.pest.PredPrinter;
import budapest.util.printer.prec.Brackets;

public class PredToCVC3Translator extends PredPrinter {

	//TODO:Probar en cvc3 la validez e invalidez de las formulas.
	//07/09/2011 daba valid algo invalid.

	public String visit(BinaryPred n, Void arg) {
		String result = super.visit(n, arg);
		result = result.replace("&&", "AND");
		result = result.replace("||", "OR");
		return "( "+result+" )";
	}

	public String visit(BooleanLiteralPred n, Void arg) {
		String result = super.visit(n, arg);
		result = result.replace("false", "FALSE");
		result = result.replace("true", "TRUE");
		return result;
	}

	public String visit(RelationPred n, Void arg) {
		String result = super.visit(n, arg);
		result = result.replace("!=", "/=");
		return result;
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
			ret = "( EXISTS";
			break;
		case FORALL:
			ret = "( FORALL";
			break;
		}
		ret += " (";
		ret += n.var.toLowerCase();
		ret += ":INT): ";
		ret += Brackets.bracketsIfNeeded(n, n.subPred, this, arg);
		ret += " )"; 
		return ret;
	}
}
