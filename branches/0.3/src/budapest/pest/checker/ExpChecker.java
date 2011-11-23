package budapest.pest.checker;

import budapest.pest.ast.exp.VarExp;
import budapest.pest.ast.visitor.ExpVisitor;
import budapest.pest.checker.symtab.SymbolTable;
import budapest.util.report.ReportSet;

public class ExpChecker extends ExpVisitor<Void, SymbolTable> {

	public final ReportSet reports;
	
	public ExpChecker(ReportSet reports) {
		this.reports = reports;
	}
	
	@Override
	public Void visit(VarExp n, SymbolTable st) {
		CheckerHelper.checkIsInt(n.name, st, reports, n);
		
		return null;
	}
	
}
