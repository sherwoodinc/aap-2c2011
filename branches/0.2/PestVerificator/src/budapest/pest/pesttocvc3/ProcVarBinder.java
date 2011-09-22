package budapest.pest.pesttocvc3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import budapest.pest.ast.params.IntConcreteParam;
import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.proc.Procedure;
import budapest.pest.ast.proc.Program;
import budapest.pest.ast.stmt.CallStmt;

/**
 * ProcVarBinder es una clase auxiliar que calcula el "binding" de parámetros formales a expresiones para una llamada a 
 * función.
 */
public class ProcVarBinder {
	// Procedimiento a llamar
	public Procedure callee;
	// Programa principal
	public Program program;
	// Instrucción concreta que realiza la llamada
	public CallStmt callStmt;
	// Variables tocadas por la llamada a procedimiento
	public Set<String> touchedVars;
	// Traducción de nombres de parámetros a expresiones usadas como parámetro en la llamada
	public Map<String, Trm> bindings;
	
	
	/**
	 * Crea un nuevo ProcVarBinder
	 *
	 * @param call Llamada a procedimiento
	 * @param prog Programa en el que ocurre la llamada
	 */
	public ProcVarBinder(CallStmt call, Program prog) {
		program = prog;		
		callStmt = call;
		// Busca el procedimiento llamado por la llamada a función
		for (Procedure p : prog.procs) {
			if (p.name.equals(call.procName)) {
				callee = p;
				break;
			}
		}
		
		// Crea map parámetro -> Expresión
		bindings = new HashMap<String, Trm>();
		touchedVars = new HashSet<String>();
		for (int i=0; i < call.params.size(); ++i)
		{
			// Hacemos cast de ConcreteParameter a IntConcreteParameter, asumiendo que es el único tipo.
			// Agregamos el bind de el nombre de parámetro 'i' con el iésimo parámetro pasado en la llamada
			bindings.put(callee.params.get(i).name, ((IntConcreteParam)call.params.get(i)).exp.accept(new ExpToTrmTranslator(), null));
			if (callee.touches.contains(callee.params.get(i)))
			{
				// Agrega la expresión pasada como iésimo parámetro a la lista de vars tocadas.
				// Esto es porque si el parámetro formal es uno de los modificados, se tuvo que pasar 
				// una variable como parámetro.
				touchedVars.add(callStmt.params.get(i).toString());
			}
		}			
	}
}
