package graph;

import graph.entities.Graph;

/**
 * Adjacency list representation of a graph. 
 * 
 * Another possible representation is the adjacency matrix representation which involves a lot of unused space 
 * if the edges are sparse. 
 * 
 * V: number of vertices       E: number of edges
 * 
 * [Adjacency Matrix Representation]
 * - Add/Remove/Query an edge: O(1)
 * - Add a vertex: O(V^2)
 * - Space: O(V^2)
 * 
 *  * [Adjacency List Representation]
 * - Add/Remove an edge: O(1)
 * - Query an edge: O(V)
 * - Add a vertex: O(1)
 * - Space: generally much lower than O(V^2), C(V,2) in worst case where every node is connected to all others. 
 * 
 * @author ruifengm
 * @since 2018-Jun-3
 * 
 * https://www.geeksforgeeks.org/graph-and-its-representations/
 */
public class GraphAdjacencyList {
	
	public static void main(String[] args) {
		System.out.println("Welcome to the rabbit hole of directed graphs represented in adjacency lists!");
		
		Graph graph = new Graph(7); 
		graph.addEdge(0, 1);
//		graph.addEdge(0, 2);
//		graph.addEdge(0, 3);
		graph.addEdge(0, 4);
		graph.addEdge(0, 5);
		graph.addEdge(4, 0);
		graph.addEdge(5, 0);
		graph.addEdge(1, 2);
		graph.addEdge(1, 4);
		graph.addEdge(1, 5);
		graph.addEdge(2, 1);
		graph.addEdge(2, 3);
		graph.addEdge(3, 4);
		graph.addEdge(4, 5);
		graph.addEdge(6, 0); // vertex 6 is unreachable from all other vertices
		graph.addEdge(6, 1); // vertex 6 is unreachable from all other vertices
		graph.addEdge(6, 2); // vertex 6 is unreachable from all other vertices
		
		System.out.println("\n/* Print out the graph by listing out every vertex and its connected adjacents. */");
		graph.print();
		
		System.out.println("\n/* Breadth First Search (Traverse) */");
		graph.BFS();
		
		System.out.println("\n/* Depth First Search (Traverse) */");
		graph.DFS();
		
		System.out.println("\n/* Print all linear paths from given source to destination. */");
		System.out.println("From 0 to 5: ");
		graph.printAllLinearPaths(0, 5);
		System.out.println("From 1 to 5: ");
		graph.printAllLinearPaths(1, 5);
		System.out.println("From 1 to 6: ");
		graph.printAllLinearPaths(1, 6);
		System.out.println("From 0 to 0: ");
		graph.printAllLinearPaths(0, 0);
		System.out.println("\nAll rabbits gone.");
	}

}
