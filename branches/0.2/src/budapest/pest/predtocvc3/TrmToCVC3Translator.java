package budapest.pest.predtocvc3;

import budapest.pest.ast.pred.trm.ArrayAccessTrm;
import budapest.pest.ast.pred.trm.VarTrm;
import budapest.pest.dump.pest.TrmPrinter;

public class TrmToCVC3Translator extends TrmPrinter {

	public String visit(VarTrm n, Void arg) {
		String result = super.visit(n, arg);
		return result.indexOf("@pre") > -1 ? varSubZero(result) : result;
	}

	public String visit(ArrayAccessTrm n, Void arg) {
		String result = super.visit(n, arg);
		return result.indexOf("@pre") > -1 ? varSubZero(result) : result;
	}
	
	private String varSubZero(String var){
		String result = var;
		int underscoreIndex = result.indexOf("_");
		if(underscoreIndex > -1)
			result = result.substring(0, underscoreIndex);
		return result + "_0";
	}
	
}
