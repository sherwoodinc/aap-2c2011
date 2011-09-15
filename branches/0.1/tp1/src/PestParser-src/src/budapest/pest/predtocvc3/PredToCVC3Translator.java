package budapest.pest.predtocvc3;

import java.util.List;

import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.BooleanLiteralPred;
import budapest.pest.ast.pred.NotPred;
import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.pred.QuantifiedPred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.dump.pest.PredPrinter;
import budapest.pest.vcgenerator.PredVarManager;
import budapest.util.printer.prec.Brackets;

public class PredToCVC3Translator extends PredPrinter {

	public String execute(Pred n) {
		List<String> vars = new PredVarManager().getUsedVars(n);
		String ret = "";
		for(String var : vars){
			ret += var + ",";
		}
		if(ret.length() > 0){
			ret = ret.substring(0, ret.length() - 1);
			ret += ":INT;";
		}
		ret += "QUERY " + n.accept(this, null) + ";";
		return ret;
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
			ret = "(EXISTS";
			break;
		case FORALL:
			ret = "(FORALL";
			break;
		}
		ret += " (";
		ret += n.var.toLowerCase();
		ret += ":INT): ";
		ret += Brackets.bracketsIfNeeded(n, n.subPred, this, arg);
		ret += ")"; 
		return ret;
	}
}
