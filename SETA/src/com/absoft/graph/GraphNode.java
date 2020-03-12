package com.absoft.graph;



public abstract class GraphNode {
	
	
	protected String node_type;
	protected long node_id;
	protected String id;
	protected double malliciousness_index;
	protected int degree;
	
	public void set_index(double index) {
		
		this.malliciousness_index = index;
	}
	
	public double get_index() {
		
		return this.malliciousness_index;
	}
	
	public String get_type() {
		return this.node_type;
	}
	
	public void set_degree(int deg) {
		  this.degree = deg;
	  }
	  
	public int get_degree() {return this.degree;}
	
		

}
