package budapest.pest.dump.java;

import budapest.pest.ast.pred.trm.VarTrm;
import budapest.pest.dump.pest.TrmPrinter;

public final class TrmToJavaTranslator extends TrmPrinter {

	@Override
	public String visit(VarTrm n, Void arg) {
		String ret = "";
		switch(n.type) {
		case CURR_VALUE:
			break;
		case PRE_VALUE:
			ret += PestToJavaTranslator.PRE_PREFIX;
			break;
		}
		ret += n.name + "[0]";
		return ret;
	}
	
}
