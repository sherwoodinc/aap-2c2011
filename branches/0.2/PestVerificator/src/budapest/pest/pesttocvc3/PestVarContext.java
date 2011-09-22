package budapest.pest.pesttocvc3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

// TODO: Auto-generated Javadoc
/**
 * PestVarContext representa un contexto donde las variables definidas tienen una version.
 * Cada contexto puede tener un contexto padre del que hereda variables y sus versiones.
 */
public class PestVarContext {

	/** Mapping compartido de variables a sus mñaximas versiones
	 * Para unicidad de nombres de variable.
	 */
	private static TreeMap<String, Integer> varMaxVersions = new TreeMap<String, Integer>();
	
	/** Contexto padre. */
	private PestVarContext parentContext;
	
	/** Versiones de las vars en el contexto actual. */
	private TreeMap<String, Integer> varVersions;
		
	/**
	 * Crea un contexto con ciertas variables iniciales
	 *
	 * @param vars las variables iniciales
	 */
	public PestVarContext(List<String> vars) {
		varVersions = new TreeMap<String, Integer>();
		parentContext = null;
		
		for(String var : vars)
			addNewVersionOf(var);
	}
	
	/**
	 * Crea un contexto como hijo de otro.
	 *
	 * @param parentContext Contexto padre
	 */
	public PestVarContext(PestVarContext parentContext) {
		this.parentContext = parentContext;
		varVersions = new TreeMap<String, Integer>();
	}
		
	/**
	 * Agrega una nueva "version" de una variable al contexto
	 *
	 * @param var la variable a versionar
	 * @return El nuevo nombre de la variable en este contexto
	 */
	public String addNewInstanceOf(String var) {
		return var + "_" + addNewVersionOf(var);
	}
	
	/**
	 * Avanza el número de versión de una var en este contexto, y lo devuelve.
	 *
	 * @param var the var
	 * @return the int
	 */
	public int addNewVersionOf(String var) {
		int newVersion = getNewVersionOf(var);
		varVersions.put(var, newVersion);
		return newVersion;
	}
	
	/**
	 * Obtiene la última versión de una var dada
	 *
	 * @param var Variable a obtener
	 * @return Nombre de su última versión
	 */
	public String getInstanceOf(String var) {
		return var + "_" + getVersionOf(var);
	}
	
	/**
	 * Obtiene todos los nombres versionados por este contexto
	 *
	 * @return las variables versionadas
	 */
	public List<String> getVarInstances() {
		List<String> result = new ArrayList<String>();
		for(String var : varVersions.keySet())
			result.add(getInstanceOf(var));
		return result;
	}
	
	/**
	 * Obtiene los nombres de las variables modificads (=versionadas) por este contexto.
	 *
	 * @return un conjunto de las variables modificadas.
	 */
	public Set<String> getModifiedVars() {
		return varVersions.keySet();
	}
	
	/**
	 * Devuelve la versión numérica de una variable..
	 *
	 * @param var La variable
	 * @return Su versión
	 */
	public int getVersionOf(String var) {
		if(varVersions.containsKey(var))
			return varVersions.get(var).intValue();
		
		if(parentContext != null)
			return parentContext.getVersionOf(var);
		
		return -1;
	}
	
	/**
	 * Obtiene del pool global una nueva versión para una variable
	 *
	 * @param var La variable a versionar
	 * @return Su nueva versión numèrica
	 */
	private static int getNewVersionOf(String var) {
		if(varMaxVersions.containsKey(var)) {
			int newMaxVersion = varMaxVersions.get(var).intValue() + 1;
			varMaxVersions.put(var, new Integer(newMaxVersion));
			return newMaxVersion;
		}
		varMaxVersions.put(var, 0);
		return 0;
	}

	/**
	 * Vacía el pool global de versiones.
	 */
	public static void reset() {
		varMaxVersions.clear();		
	}
}
