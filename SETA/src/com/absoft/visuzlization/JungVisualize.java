package com.absoft.visuzlization;

import java.awt.*;
import java.util.Random;

import javax.swing.*;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;



public class JungVisualize {

	
	
	public static void main(String [] args) {
		
		SwingUtilities.invokeLater(new Runnable(){
			
			@Override
			public void run() {
				
				createAndShowGUI();
			}
			
			
			
		});
		
	}
	
	private static void createAndShowGUI() {
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Graph<String,String> g = createGraph();
		
		
		Dimension size = new Dimension(800,800);
		VisualizationViewer<String,String> vv = 
				new VisualizationViewer<String,String>(
						
						new FRLayout<String,String>(g,size)
						
						);
		
		
		DefaultModalGraphMouse<String,Double> graphMouse = new DefaultModalGraphMouse<String,Double>();
		
		vv.setGraphMouse(graphMouse);
				
		frame.getContentPane().add(vv);
		frame.setSize(size);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		
		
	}
	
	public static Graph<String,String> createGraph(){
		
		Random random = new Random(0);
		
		int numVertices = 200;
		int numEdges    = 300;
		
		Graph<String,String> g = new DirectedSparseGraph<String,String>();
		
		for(int i=0;i<numVertices;i++) {
			
			g.addVertex("v"+i);
		}
		
		for(int i=0;i<numEdges;i++) {
			
			int v0 = random.nextInt(numVertices);
			int v1 = random.nextInt(numVertices);
			
			g.addEdge("e"+i,"v"+v0,"v"+v1);
			
		}
		
		return g;
		
		
	}
	
	
	
	
}
