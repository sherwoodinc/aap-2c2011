package budapest.pest.checker;

import budapest.pest.ast.exp.ArrayAccessIntExp;
import budapest.pest.ast.exp.ArraySizeIntExp;
import budapest.pest.ast.exp.VarIntExp;
import budapest.pest.ast.visitor.ExpVisitor;
import budapest.pest.checker.symtab.SymbolTable;
import budapest.util.report.ReportSet;

public class ExpChecker extends ExpVisitor<Void, SymbolTable> {

	public final ReportSet reports;
	
	public ExpChecker(ReportSet reports) {
		this.reports = reports;
	}

	@Override
	public Void visit(ArraySizeIntExp n, SymbolTable st) {
		CheckerHelper.checkIsArray(n.array, st, reports, n);
		
		return null;
	}
	
	@Override
	public Void  visit(ArrayAccessIntExp n, SymbolTable st) {
		CheckerHelper.checkIsArray(n.array, st, reports, n);
		
		return null;
	}
	
	@Override
	public Void  visit(VarIntExp n, SymbolTable st) {
		CheckerHelper.checkIsInt(n.name, st, reports, n);
		
		return null;
	}
	
}
