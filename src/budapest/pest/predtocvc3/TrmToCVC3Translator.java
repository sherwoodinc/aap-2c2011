package budapest.pest.predtocvc3;

import budapest.pest.ast.pred.trm.VarTrm;
import budapest.pest.dump.pest.TrmPrinter;
import budapest.pest.pesttocvc3.PestVarContext;

public class TrmToCVC3Translator extends TrmPrinter {
	
	private PestVarContext zero_context;
	public TrmToCVC3Translator(PestVarContext zeroContext) {
		zero_context = zeroContext;
	}
	

	public String visit(VarTrm n, Void arg) {
		String result = super.visit(n, arg);
		return result.indexOf("@pre") > -1 ? varSubZero(result) : result;
	}
	
	private String varSubZero(String var){
		String result = var;
		int underscoreIndex = result.indexOf("_");
		if(underscoreIndex > -1)
			result = result.substring(0, underscoreIndex);
		return zero_context.getInstanceOf(result);
	}
	
}
