package budapest.pest.pesttocvc3;

import budapest.pest.ast.exp.BinBoolExp;
import budapest.pest.ast.exp.NotBoolExp;
import budapest.pest.ast.exp.RelBoolExp;
import budapest.pest.dump.pest.ExpPrinter;

public class ExpToCVC3Translator extends ExpPrinter {

	public String visit(BinBoolExp n, Void arg) {
		String result = super.visit(n, arg);
		result = result.replace("&&", "AND");
		result = result.replace("||", "OR");
		return "("+result+")";
	}
	
	public String visit(RelBoolExp n, Void arg) {
		String result = super.visit(n, arg);
		result = result.replace("!=", "/=");
		return result;
	}
	
	public String visit(NotBoolExp n, Void arg) {
		String result = super.visit(n, arg);
		result = result.replace("! ", "NOT ");
		return result;
	}
}
