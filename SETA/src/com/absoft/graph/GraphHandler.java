package com.absoft.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


import org.bson.Document;

import com.google.common.base.Function;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import org.apache.commons.collections15.Transformer;


public class GraphHandler {

	   
	  
	   private MongoClient client;
	   private MongoDatabase database;
	   private Graph<GraphNode, GraphEdge> graph;
	   private MongoCollection<Document> userCollection;
	   private MongoCollection<Document> tweetCollection;
	   
	   
	   // to construct the graph from the database
	   public void createGraphFromDatabase(String uri,String databaseName) {
		   
		   MongoClientURI client_uri = new MongoClientURI(uri);
		   this.client   = new MongoClient(client_uri);
		   this.database =  this.client.getDatabase(databaseName); 
		   this.userCollection  = this.database.getCollection("user");
		   this.tweetCollection = this.database.getCollection("tweet");
		   this.createGraph();
	   }
	   /*
	    * load the graph from a file
	    */
	   public void loadGraphFromFile(String filepath) {
		   
		   
	   }
	   
	   
	  
	public void createGraph(){
		   
		   this.graph = new UndirectedSparseGraph<GraphNode,GraphEdge>();
		   
		   
		   //create the graph from the database
		   
		   //1. get the list of users
		   Map<String,UserNode> usermap = new HashMap<String,UserNode>();
		   
		   MongoCursor<Document> cursor_user = this.userCollection.find().iterator();
		   int i=1;
		   while (cursor_user.hasNext()) {
		      Document obj = cursor_user.next();
		      
		      UserNode new_user = new UserNode(i,obj.getString("user_name"),obj.get("user_id").toString(),obj.getInteger("user_followers"),obj.getString("user_location"));
		      //this.graph.addVertex(new_user);
		      usermap.put(obj.get("user_id").toString(),new_user);
		      i++;
		      System.out.println(obj);
		   }
		   
		   	   
		   //2. get the tweets
		   Map<String,TweetNode> tweetmap = new HashMap<String,TweetNode>();
		   MongoCursor<Document> cursor_tweet = this.tweetCollection.find().iterator();
		   i=1;
		   while (cursor_tweet.hasNext()) {
		      Document obj = cursor_tweet.next();
		      //System.out.println(obj.getString("tweet_id"));
		      
		      
		      TweetNode new_tweet = new TweetNode(i,obj.getString("tweet_id"),obj.getString("user_id"),obj.getString("text"));
		      tweetmap.put(obj.getString("tweet_id"),new_tweet);
		      
		      //create connection between the tweet and the users
		      ArrayList connected_users = (ArrayList) obj.get("related_users");
		      
		      //author user
		      UserNode author = usermap.get(obj.getString("user_id"));
		      
		      for(Object uid:connected_users) {
		    	  
		    	  UserNode u_node = usermap.get(uid.toString());
		    	  System.out.println(u_node);
		    	  System.out.println(new_tweet);
		    	  
		    	  //create a new edge
		    	  
		    	  float edge_weight = 1/connected_users.size();
		    	  
		    	  new_tweet.set_degree(new_tweet.get_degree()+1);
		    	  u_node.set_degree(u_node.get_degree()+2);
		    	  this.graph.addEdge(new GraphEdge(edge_weight),new_tweet,u_node,EdgeType.UNDIRECTED);
		    	  
		    	  
		    	  if(u_node.get_uid()!=author.get_uid()) {
		            author.set_degree(author.get_degree()+1);
		            
		    	    this.graph.addEdge(new GraphEdge(edge_weight),author,u_node,EdgeType.UNDIRECTED);
		    	  }	    	  
		      }
		       
		   	  i++;    
		   }
		   /* create K random edges between tweet nodes */
		      Object [] tweet_ids = tweetmap.keySet().toArray();
		      int k=900;
		      for(int j=0;j<k;j++) {
		    	  
		    	  String key1 =  (String)tweet_ids[new Random().nextInt(tweet_ids.length)];
		    	  String key2 =  (String)tweet_ids[new Random().nextInt(tweet_ids.length)];
		    	  
		    	  TweetNode node1 = tweetmap.get(key1);
		    	  TweetNode node2 = tweetmap.get(key2);
		    	  
		    	  this.graph.addEdge(new GraphEdge(new Random().nextFloat()),node1,node2,EdgeType.UNDIRECTED);
		    	  
		      }
             		 
		   	   
		  	   
	   }
	
	   /*
	    * Visualize the graph
	    */
	 
	   public void Visualise() {
		   
		   SwingUtilities.invokeLater(new Runnable(){
				
				@Override
				public void run() {
					
					createDisplay();
				}
						
			});
	   		   
	   }
	   
	   /*
	    * Function to create the GUI
	    */
	   
	   public void createDisplay() {
		   
		   JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			int NUM_VERTICES = this.graph.getVertexCount();
			int NUM_EDGES    = this.graph.getEdgeCount();
			
		    
			
			
			Dimension size = new Dimension(2000,1700);
			VisualizationViewer<GraphNode,GraphEdge> vv = 
					new VisualizationViewer<GraphNode,GraphEdge>(
							
							new FRLayout<GraphNode,GraphEdge>(this.graph,size)
							
							);
			
			//change color of the nodes according to the type of node
			Function<GraphNode,Paint> vertexColor = new Function<GraphNode,Paint>() {
	            public Paint apply(GraphNode g) {
	                if(g.get_type()=="tweet") return Color.YELLOW;
	                return Color.BLUE;
	            }
	        };
	        
	        //change the size of the nodes according to the degree of the node
	        Function<GraphNode,Shape> vertexSize = new Function<GraphNode,Shape>(){
	            public Shape apply(GraphNode g){
	                Ellipse2D circle = new Ellipse2D.Double(-15, -15, 10, 10);
	                // in this case, the vertex is twice as large
	                float scale = 100*g.get_degree()/NUM_VERTICES;
	                if(g.get_type()== "tweet") return AffineTransform.getScaleInstance(2+scale*10, 2+scale*10).createTransformedShape(circle);
	                else return AffineTransform.getScaleInstance(1+scale, 1+scale).createTransformedShape(circle);
	            }
	        };
			
			DefaultModalGraphMouse<String,Double> graphMouse = new DefaultModalGraphMouse<String,Double>();
			
			
			vv.setGraphMouse(graphMouse);
			vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
			vv.getRenderContext().setVertexShapeTransformer(vertexSize);
					
			frame.getContentPane().add(vv);
			frame.setSize(size);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			
	   }
	   
	   public void PowerLawAnalysis() {
		   
		   Map<Integer,Integer> degree_map = new TreeMap<Integer,Integer>();
		   
		   for(int i=0;i<this.graph.getVertexCount();i++) {
			   degree_map.put(i,0);
		   }
		   
		   for(GraphNode g : this.graph.getVertices()) {
			   degree_map.put(g.get_degree(), degree_map.get(g.get_degree())+1);
		   }
		   
		   System.out.println(degree_map);
		   
		   try {
                  FileWriter writer = new FileWriter("/home/abhi17/SEM 6/SIN/powerlaw.txt");
                  
                  for(Map.Entry<Integer,Integer> ent : degree_map.entrySet()) {
                	  writer.write(ent.getKey().toString()+","+ent.getValue().toString()+"\r\n");
                	  
                  }
                  
                 writer.close(); 
		   }catch(Exception e) {}
		   
		   
	   }
	   
	
	
	
}
