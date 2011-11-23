package budapest.pest.checker;

import budapest.pest.checker.symtab.SymbolTable;
import budapest.pest.checker.symtab.SymbolTable.SymbolInfo.Type;
import budapest.util.ASTNode;
import budapest.util.report.ReportSet;

public class CheckerHelper {

	public static void checkIsArray(String name, SymbolTable st, ReportSet reports, ASTNode n) {
		if(!st.knows(name)) 
			reports.addErrorReport("Variable " + name + " doesn't exist", n);
		else if(st.type(name) != Type.ARRAY)
			reports.addErrorReport("Variable " + name + " isn't an array", n);
	}
	
	public static void checkIsInt(String name, SymbolTable st, ReportSet reports, ASTNode n) {
		if(!st.knows(name)) 
			reports.addErrorReport("Variable " + name + " doesn't exist", n);
		else if(st.type(name) != Type.INT)
			reports.addErrorReport("Variable " + name + " isn't int", n);
	}
	
	public static void checkIsBound(String name, SymbolTable st, ReportSet reports, ASTNode n) {
		if(!st.knows(name)) 
			reports.addErrorReport("Variable " + name + " doesn't exist", n);
		else if(st.type(name) != Type.PRED)
			reports.addErrorReport("Variable " + name + " isn't bound", n);
	}
	
}
