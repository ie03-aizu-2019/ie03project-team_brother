import java.util.Comparator;

class Node{
    private int node;
    private double d;
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
    public int compare(Object obj1,Object obj2){
	//Using for Priority Queue(P312)
	Node n1=(Node)obj1;
	Node n2=(Node)obj2;

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

