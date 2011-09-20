package budapest.pest.checker.callgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CallGraph {

	final Set<String> nodes = new HashSet<String>();
	final Map<String, Lists> edges = new HashMap<String, Lists>(); 
	
	public void add(String caller, String callee) {
		addNode(caller);
		addNode(callee);
		
		edges.get(caller).outEdges.add(callee);
		edges.get(callee).inEdges.add(caller);
	}
	
	/**
	 * Assumes the graph has no cycles
	 * @return a topological ordering of the nodes
	 */
	public List<String> topo() {
		List<String> ret = new ArrayList<String>();
		Set<String> remainingNodes = new HashSet<String>(nodes);
		
		while(!remainingNodes.isEmpty()) {
			for(String n : remainingNodes) {
				List<String> n_edges = new ArrayList<String>(edges.get(n).outEdges);
				n_edges.retainAll(remainingNodes);
				
				if(n_edges.isEmpty()) {
					ret.add(n);
					remainingNodes.remove(n);
				}
			}
		}
		
		return ret;
	}

	public boolean hasCycle() {
		// empty graph is acyclic
		if(nodes.isEmpty())
			return false;
		
		Set<String> initNodes = initNodes();
		
		// if no node with no incoming edges, then graph is cyclic
		if(initNodes.isEmpty())
			return true;
		
		// try with each node with no incoming edges
		for(String node : initNodes) {

			// visit DFS from startNode
			if(visitDFS(node, new HashSet<String>()))
				return true;
		}
		
		// if every dfs does not visit repeated elements then no cycles
		return false;
	}
	
	private Set<String> initNodes() {
		Set<String> ret = new HashSet<String>();
		for(String node : nodes)
			if(edges.get(node).inEdges.size() == 0) 
				ret.add(node);
		return ret;
	}
	
	
	private boolean visitDFS(String node, Set<String> visited) {
		if(visited.contains(node))
			return true;
		
		for(String o : edges.get(node).outEdges)
			if(visitDFS(o, visited))
				return true;
		
		return false;
	}
	
	public void addNode(String node) {
		if(!nodes.contains(node)) {
			nodes.add(node);
			edges.put(node, new Lists());
		}
	}
	
	private static class Lists {
		
		public final List<String> inEdges = new ArrayList<String>();
		public final List<String> outEdges = new ArrayList<String>();
		
		@Override
		public String toString() {
			return "i: " + inEdges + ", o: " + outEdges;
		}
		
	}
	
}
