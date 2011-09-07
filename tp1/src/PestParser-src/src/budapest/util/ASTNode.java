package budapest.util;

/**
 * This abstract syntax tree is based on a Java parser implementation 
 * by Sreenivasa Viswanadha and Júlio Vilmar Gesser.
 */
public abstract class ASTNode {

	public final int line;
	public final int column;

	public ASTNode(int line, int column) {
		this.line = line;
		this.column = column;
	}

}