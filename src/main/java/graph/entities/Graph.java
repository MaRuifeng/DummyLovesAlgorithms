package graph.entities;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * An graph (directed by default) represented by vertex adjacency lists
 * 
 * @author ruifengm
 * @since 2018-Jun-3
 * 
 */
public class Graph {
	int numberOfVertices;
	LinkedList<Integer>[] adjListArray; 
	
	@SuppressWarnings("unchecked")
	public Graph(int numberOfVertices) {
		this.numberOfVertices = numberOfVertices; 
		this.adjListArray = new LinkedList[numberOfVertices]; 
		for (int i=0; i<numberOfVertices; i++) this.adjListArray[i] = new LinkedList<>();
	}
	
	public void addEdge(int start, int end) {
		if (start < 0 || end > this.numberOfVertices-1) return; 
		this.adjListArray[start].add(end);
	}
	
	public void print() {
		for (int i=0; i<this.numberOfVertices; i++) {
			System.out.print("Vertex " + i + ": ");
			for (Integer vertex: this.adjListArray[i]) System.out.print(" " + vertex); 
			System.out.println();
		}
	}
	
	/* Breadth First Search (Traverse) */
	/* Similar to the level order traversal of a binary tree, but need to take care of cycles. */
	public void BFS() {
		Queue<Integer> vertexQ = new LinkedList<>(); 
		boolean[] visited = new boolean[this.numberOfVertices];
		Arrays.fill(visited, false);
		for (int i=0; i<this.numberOfVertices; i++) {
			if(visited[i]) continue; 
			else {
				visited[i] = true;
				vertexQ.add(i); // add every vertex to leave no one behind
			}
			while(true) {
				int count = vertexQ.size(); 
				if (count == 0) break; 
				for (Integer vertex: vertexQ) System.out.print(vertex + " "); 
				while(count>0) {
					int head = vertexQ.poll(); 
					Iterator<Integer> iter = this.adjListArray[head].iterator();
					while(iter.hasNext()) {
						Integer vertex = iter.next();
						if(!visited[vertex.intValue()]) {
							vertexQ.add(vertex);
							visited[vertex.intValue()] = true;
						}
					}
					count--; 
				}
				System.out.println();
			}
		}
	}
	
	/* Depth First Search (Traverse) */
	/* Similar to the pre-/in-/post- order traversal of a binary tree, but need to take care of cycles. */
	public void DFS() {
		boolean[] visited = new boolean[this.numberOfVertices];
		Arrays.fill(visited, false);
		for (int i=0; i<this.numberOfVertices; i++) DFS(i, visited);
		System.out.println();
	}
	private void DFS(int vertex, boolean[] visited) {
		if (visited[vertex]) return; 
		else {
			System.out.print(vertex + " "); 
			visited[vertex] = true; 
			Iterator<Integer> iter = this.adjListArray[vertex].iterator();
			while(iter.hasNext()) DFS(iter.next(), visited);
		}
	}
	
	/* Find all non-cyclic paths from source vertex to destination vertex */
	/* Use the idea of DFS. */
	/* Cyclic paths are infinite hence we don't count them. */
	public void printAllLinearPaths(int src, int dest) {
		printAllLinearPaths("", src, dest);
		System.out.println();
	}
	private void printAllLinearPaths(String path, int src, int dest) {
		if (path.contains(String.valueOf(src))) return;
		if (src == dest) System.out.println(path + dest);
		else {
			path += (src + " -> "); 
			Iterator<Integer> iter = this.adjListArray[src].iterator();
			while(iter.hasNext()) printAllLinearPaths(path, iter.next(), dest);
		}
	}
}
