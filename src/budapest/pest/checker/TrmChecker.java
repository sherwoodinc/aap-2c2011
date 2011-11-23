package budapest.pest.checker;

import budapest.pest.ast.pred.trm.VarTrm;
import budapest.pest.ast.visitor.TrmVisitor;
import budapest.pest.checker.symtab.SymbolTable;
import budapest.pest.checker.symtab.SymbolTable.SymbolInfo.Type;
import budapest.util.report.ReportSet;

public class TrmChecker extends TrmVisitor<Void, SymbolTable> {

	public final ReportSet reports; 
	
	public TrmChecker(ReportSet reports) {
		this.reports = reports;
	}
	
	@Override
	public Void visit(VarTrm n, SymbolTable st) {
		// a var term can be a quant var or a regular var with @pre or not
		
		if(!st.knows(n.name))
			reports.addErrorReport("Undefined variable " + n.name, n);
		else if(st.type(n.name) == Type.PRED && 
				n.type == budapest.pest.ast.pred.trm.Trm.Type.PRE_VALUE)
			reports.addErrorReport("Variable " + n.name + " is bound, can not " +
					"speak of its pre value", n);
		else if(st.type(n.name) == Type.ARRAY)
			reports.addErrorReport("Variable " + n.name + " is an array and was " +
					"used as an integer", n);
		
		return null;
	}
	

}
