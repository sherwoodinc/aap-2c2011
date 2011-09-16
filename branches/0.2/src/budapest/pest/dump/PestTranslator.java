package budapest.pest.dump;

import budapest.pest.ast.visitor.PestVisitor;
import budapest.util.printer.Printer;

public abstract class PestTranslator extends PestVisitor<Void, Void> {

	protected final Printer printer = new Printer();

	public String getSource() {
		return printer.getSource();
	}
}
