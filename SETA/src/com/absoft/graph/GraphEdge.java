package com.absoft.graph;

public class GraphEdge {

	
	
	private double weight;
	private int edge_id;
	
	static int edge_count = 0;
	
	public GraphEdge(double weight) {
		
		this.edge_id = edge_count+=1;
		
		this.weight   = weight;
		
	}
	
	public String toString() {
		
		return "E"+edge_id;
	}
	
	
	
	
}
