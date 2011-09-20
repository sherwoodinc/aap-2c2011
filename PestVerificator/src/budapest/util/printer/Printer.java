package budapest.util.printer;

import java.util.Collection;
import java.util.Iterator;

public final class Printer {

	private int level = 0;

	private boolean indented = false;

	private final StringBuilder buf = new StringBuilder();

	public void indent() {
		level++;
	}

	public void unindent() {
		level--;
	}

	private void makeIndent() {
		for (int i = 0; i < level; i++) {
			buf.append("    ");
		}
	}

	public void print(String arg) {
		if (!indented) {
			makeIndent();
			indented = true;
		}
		buf.append(arg);
	}

	public void println(String arg) {
		print(arg);
		println();
	}

	public void printInfix(Collection<?> c, String separator) {
		for (Iterator<?> i = c.iterator(); i.hasNext();) {
            Object o = i.next();
            print(o.toString());
            if (i.hasNext()) {
                print(separator);
            }
        }
	}
	
	public void println() {
		buf.append("\n");
		indented = false;
	}

	public String getSource() {
		return buf.toString();
	}

	@Override
	public String toString() {
		return getSource();
	}
	
}