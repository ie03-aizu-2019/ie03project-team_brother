import java.util.Comparator;

class Node{
    //This class using for Proority Queue :Dijkstra
    private int node;//node No.
    private double d;//distance
    Node(int node,double d){
	this.node=node;
	this.d=d;
    }
    public int getNode(){
	return node;
    }
    public double getDis(){
	return d;
    }
}
class Mycomparator_Node  implements Comparator{
    
    //Override compare of Comparator class to sort in Priority Queue.
    public int compare(Object obj1,Object obj2){
	//Using for Priority Queue(P312)
	Node n1=(Node)obj1;
	Node n2=(Node)obj2;
	
    //Sort by Distance(small order)
	double n1_d =n1.getDis();
	double n2_d =n2.getDis();
	if(n1_d < n2_d){
	    return 1;
	}else if(n1_d > n2_d){
	    return -1;
	}else{
	    return 0;
	}
    }    
}

