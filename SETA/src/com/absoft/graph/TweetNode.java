package com.absoft.graph;

public class TweetNode extends GraphNode {
	
	         
	         private int interactions;
	         private String user_id;
	         private String text;
	         
	         public TweetNode(int tid,String id,String uid,String text) {
	        	 
	        	 this.node_id   = tid;
	        	 this.node_type = "tweet";
	        	 this.id        = id;
	        	 this.user_id   = uid;
	        	 this.text      = text;
	        	 this.degree    = 0;
	        	 
	        	 
	        	 
	         }
	         
	         public void setInteractions(int interactions) {
	        	 
	        	 this.interactions = interactions;
	         }
	         
	         public long get_id() {
	        	 
	        	 return this.node_id;
	         }
	         
	         public String get_user() {
	        	 
	        	 return this.user_id;
	         }
	         
	         public String get_text() {
	        	 
	        	 return this.text;
	         }
	         
	         
	         
	
	

}
