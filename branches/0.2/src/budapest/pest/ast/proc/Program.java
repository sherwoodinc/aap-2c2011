package budapest.pest.ast.proc;

import java.util.List;

import budapest.pest.ast.visitor.PestVisitor;
import budapest.pest.checker.PestChecker;
import budapest.pest.checker.symtab.ProcsInfo;
import budapest.pest.dump.pest.PestPrinter;
import budapest.util.ASTNode;
import budapest.util.report.ReportSet;

public final class Program extends ASTNode {

	public final List<Procedure> procs;

	public Program(int line, int column, final List<Procedure> procs) {
		super(line, column);
		this.procs = procs;
	}
	
	public Procedure getMain() {
		return procs.get(0);
	}
	
	@Override
	public String toString() {
		PestPrinter pd = new PestPrinter();
		accept(pd, null);
		return pd.getSource();
	}
	
	public <R,A> R accept(PestVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}
	
	public ReportSet typeCheck(ReportSet rs, ProcsInfo pi) {
		PestChecker pc = new PestChecker(rs, pi);
		accept(pc, null);
		return pc.reports;
	}

	public ProcsInfo getProcsInfo(ReportSet rs) {
		ProcsInfo pi = new ProcsInfo();
		
		// register all procedures info
		for(Procedure proc : procs) {
			if(pi.knows(proc.name))
				rs.addErrorReport("Redefinition of procedure " + proc.name, proc);
			else
				pi.put(proc);
		}
		
		return pi;
	}
	
}
