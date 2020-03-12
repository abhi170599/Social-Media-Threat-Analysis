package com.absoft.graph;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import com.absoft.graph.GraphEdge;
import com.absoft.graph.GraphNode;




public class Main {
	
    public static void main(String [] args) {
		
		
    	String databaseUri  = "mongodb+srv://parth:sinproject@cluster0-odmoa.mongodb.net/test?retryWrites=true&w=majority\");"; 
    			
    	String databaseName = "project";
    	
    	
		GraphHandler graphHandler = new GraphHandler();
		graphHandler.createGraphFromDatabase(databaseUri, databaseName);
		graphHandler.Visualise();
		graphHandler.PowerLawAnalysis();
		
		
				
		
		
	}
	

}
