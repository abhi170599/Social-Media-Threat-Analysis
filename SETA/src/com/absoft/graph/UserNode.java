package com.absoft.graph;

public class UserNode extends GraphNode {

	   
	  private String username;
	  private String location;
	  private int followers_count;
	  
	  
	  
	  public UserNode(int uid,String username,String id,int followers_count,String location) {
		  
		     this.node_id         =  uid;
		     this.node_type       =  "user";
		     this.username        = username;
		     this.id              = id;
		     this.followers_count = followers_count;
		     this.location        = location;
		     this.degree          = 0;
				  
	  }
	  
	 
	  
	  public String get_username() {
		  return this.username;
	  }
	  
	  public String get_location() {
		  return this.location;
	  }
	  
	  public String get_uid() {
		  return this.id;
	  }
	  
	  
	
}
