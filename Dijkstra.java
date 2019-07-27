import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.PriorityQueue;

class Dijkstra{
    private double INF = Integer.MAX_VALUE;
    private int w=0;
    private int g=1;
    private int b=2;
    private int color[];
    private double d[];
    private int p[];
    private Hasroute has[];//when distance equals between two paths,use;
    private int size;
    private int psize;
    private int st;
    private int ed;
    private ArrayList<Hasroute>delind;
    private ArrayList<Hasroute>a;//確定 0origin
    PriorityQueue<Node> nodes = new PriorityQueue<Node>(new Mycomparator_Node());
    Dijkstra(){
    }
    double[] nTimesnTon(ArrayList<AdjacencyList>n,int st,int ed,int size,int psize,int times){
	this.size=size;
	double[]ret=new double[times+1];
	Hasroute spurroute,test;
	Hasroute spurroute_sep;
	int spurnode,befonode;
	
	//initialize d[]
	for(int i = 1;i<times+1;i++){
	    ret[i]=-1;
	}
	delind=new ArrayList<Hasroute>();
	
	//Make two AdjancyList(n2 = original ,n3 = can be delete (Using Yen's algorithms))
	AdjacencyList [] n2 = new AdjacencyList[size+1];
	myclone(n,n2);
	AdjacencyList [] n3 = new AdjacencyList[size+1];
	myclone(n,n3);
	
	int p,p2,bcnt=0;
        a=new ArrayList<Hasroute>();//desided 0-origin
	ArrayList<Hasroute>b=new ArrayList<Hasroute>();//not desided 0-origin
        double hz;

	// "i" is "i" th shorthst path 
	for(int i=1;i<=times;i++){
	    if(i==1){
		//When first shortest path detecting, go to nTon(simple dijkstra).
		hz=nTon(n2,st,ed,size,psize);
		if(hz==-1.0)return ret;
		
                //1st shortest path is enterd A(desided route) 
                a.add(new Hasroute(d[ed],Route(ed),d,psize));
		
		//delete route along first shortest path
		delroute(a.get(a.size()-1));
	    }
	    if(i>1){
		spurroute=a.get(a.size()-1);
		
		spurroute_sep=new Hasroute(0.0,new int[]{-1},new double[]{0.0},psize);
		for(int j=1;j<spurroute.getroute().length;j++){
    		    spurnode=(spurroute.getroute())[j];
		    spurroute_sep.addn(spurnode);

		    // delete route of n3 along delind[]
			    for(int z=0;z<delind.size();z++){
				if(spurroute_sep.equals(delind.get(z))){
				    if(delind.get(z).getnext()!=-2){
				 n3[spurnode].remove((delind.get(z)).getnext());
				 n3[(delind.get(z)).getnext()].remove(spurnode);	 	
			            }
				}
			    }
			    
		   //prepare the parameter to use dijkstra
		   preSpur(spurroute,j);

		   //detect candidate path
		   hz=spurnTon(n3,st,ed,size,psize);

		   //if path is not detected,ignore
       		   if(hz==-1.0){
		           myclone(n,n3);	      //initialize
		           continue;
		   }
	      	   test=new Hasroute(d[ed],Route(ed),d,psize);
		       if(a.contains(test)||b.contains(test)){//重複を除く except duplicate path
		           myclone(n,n3);	      //initialize
			   continue;
		       } 
		   b.add(test);// candicate path add to b
		   myclone(n,n3);	      //initialize
		}
		
		//if candicate path nothing, finish the detect.
		if(b.size()==0){
		    Collections.sort(a);//辞書順に小さい文字を優先させるため Priority is given to the small characters in the dictionary order

		    //convert ArrayList to array.
		    for(int x=1;x<=a.size();x++){
			ret[x]=a.get(x-1).getdis();
		    }
		    return ret;
		}
		
		//Sort candicate path
		Collections.sort(b);
		
		a.add(b.get(0));//最短を確定へ move shortest path in b to a
		b.remove(0);
		delroute(a.get(a.size()-1));
	    }
	}
	Collections.sort(a);//辞書順に小さい文字を優先させるため Priority is given to the small characters in the dictionary order
	for(int x=1;x<=a.size();x++){
	    ret[x]=a.get(x-1).getdis();
	    }
	return ret;
    }
    void myclone(/*double[][] n,double[][]opo*/ ArrayList<AdjacencyList> n,AdjacencyList[]opo){
	for(int i=1;i<=size;i++){
	    opo[i]=new AdjacencyList();
	    for(Entry<Integer,Double>entry: /*n[i].entrySet()*/n.get(i).entrySet()){
		opo[i].put((Integer)(entry.getKey()),(Double)(entry.getValue()));
	    }
	}	
    }

    //search delete index of route along route "a" 
    void delroute(Hasroute a){
	int[]route= a.getroute();
	int[]sep_route;
	boolean flg = false;
	Hasroute delr;
	for(int i =1;i<a.getsize();i++){
	    flg=false;
	    sep_route=new int[i+1];

	    //get the route 0 to i+1(ex (Original[Hasroute])A-D-C-B (1)A (2)A-D (3)A-D-C (4)A-D-C-B )
       	    System.arraycopy(route,0,sep_route,0,i+1);
	    
	    //if "i" is end,put the flag 
	    if(i==a.getsize()-1){
		delr=new Hasroute(0.0,sep_route,new double[]{0.0});
	    }else{
		delr=new Hasroute(0.0,sep_route,new double[]{0.0},route[i+1],1);		
	    }
            for(int z=0;z<delind.size();z++){
		if(delr.equals(delind.get(z))){
		    if(delr.getnext()==delind.get(z).getnext()){
			flg=true;
		    }
		}
		
	    }
	    if(flg ==false){
	      delind.add(delr);
	    }
	}
	return;
    }

    //Simple dijkstra: One query can use one time only.
    double nTon(AdjacencyList[]n,int st,int ed,int size,int psize){
	color=new int[size+1];
	d=new double[size+1];
	p=new int[size+1];
	has=new Hasroute[size+1];
	this.ed=ed;
	this.st=st;
	this.psize=psize;
	double hz,disn;
	int u,v;
	double mincost;
	Hasroute ihas;
	Node node;
	for(int i=1;i<=size;i++){
	    d[i]=INF;
	    color[i]=w;
	}
	d[st]=0;
	p[st]=-1;
	nodes.add(new Node(st,0));
	node = nodes.poll();
        while(node!=null){
	    color[node.getNode()]=b;
	    //if smaller than d[],program ignore
	    if(d[node.getNode()]<node.getDis()){
		node=nodes.poll();
		continue;
	    }
	    //obtain by small distance order
	    for(int entry:n[node.getNode()].keySet()){	    
	       if(color[entry]!=b &&n[node.getNode()].containsKey(entry)){
		   disn=n[node.getNode()].get(entry);//n[u][v]
		   if(d[entry]>d[node.getNode()]+disn){
		       d[entry]=d[node.getNode()]+disn;
		       color[entry]=g;
		       nodes.add(new Node(entry,d[entry]));
		       p[entry]=node.getNode();	       
		   }
	       }
	     }
	    node=nodes.poll();
	}
	if(d[ed]==INF)return -1.0;
	return d[ed];
    }

    //Dijkstra with delete route by delroute. already seted the parameter in halfway by preSpur.
    double spurnTon(AdjacencyList[]n,int st,int ed,int size,int psize){
	double hz,disn;
	int u,v;
	double mincost;
	Node node;
	node=null;
	node = nodes.poll();
        while(node!=null){
	    color[node.getNode()]=b;
 	    //if smaller than d[],program ignore
	    if(d[node.getNode()]<node.getDis()){
		node=null;
		node=nodes.poll();
		continue;
	    }
	    //obtain by small distance order
	    for(int entry:n[node.getNode()].keySet()){	
	       if(color[entry]!=b &&n[node.getNode()].containsKey(entry)){
		   disn=n[node.getNode()].get(entry);//n[u][v]
		   if(d[entry]>d[node.getNode()]+disn){
		       d[entry]=d[node.getNode()]+disn;
		       color[entry]=g;
		       nodes.add(new Node(entry,d[entry]));
		       p[entry]=node.getNode();
		   }
	       }
	    }
	    node=nodes.poll();
	}
	if(d[ed]==INF)return -1.0;
	return d[ed];
}
    //preSpur can initialize in delete version Dijkstra,making d and p and color halfway
    public void preSpur(Hasroute h,int ind){
	int i;
	//Initialize parameter
	for(i=0;i<=size ; i++){
	    d[i]=INF;
	    color[i]=w;
	    p[i]=0;
	}
	int[] ro=h.getroute();
	double[] adis=h.getarraydis();
	//Restore parameter from old route.
	for(i = 1 ;i<ind;i++){
	    color[ro[i]]=b;
	    p[ro[i]]=ro[i-1];
	    d[ro[i]]=adis[ro[i]];
	    if(ro[i]==ed)break;
	}
	//choose the start node
	 color[ro[i]]=g;
	 p[ro[i]]=ro[i-1];
	 d[ro[i]]=adis[ro[i]];
    	 nodes.add(new Node(ro[i],d[ro[i]]));
    }

    //rep_NumtoS converts numbers to including "C" numbers, and output the route. 
    public String rep_NumtoS(int d,boolean bla,int na,int nc){
	String s1,s2="";
	if(bla)s2=" ";
	if(psize<d&&d<=size-na-nc){
	    s1=Integer.toString(d-psize);
	    s2=s2+"C";
	}else{
	    s1=Integer.toString(d);
	}
	return s2+s1;
    }
    
    //RouteSntimes invoke rep_NumtoS and read i-th shortest path .
    public String RouteSntimes(int p,int na,int nc){
	       String c="";
	       int[] myroute=(a.get(p-1)).getroute();
	       for(int i=1;i<myroute.length;i++){
		   if(i==1){
		       c=c+rep_NumtoS(myroute[i],false,na,nc);
		   }else{
		       c=c+rep_NumtoS(myroute[i],true,na,nc);
		   }
	       }
	       return c;
	    }
   //Using by Hasroute,to make route r[] from p[]
   public int[] Route(int ed){
       ArrayList<Integer> list= new ArrayList<Integer>();
	int i,po;
	po=ed;
        if(p[ed]==-1){
	    return new int[]{-1,ed};
	}
	while(po!=-1){
	  list.add(0,po);
	  po=p[po];
	}
	list.add(0,po);
	int[] r= new int[list.size()];
	for(i = 0;i<list.size();i++){
	    r[i]=(int)(list.get(i));
	}   
	return r;  
   }
    //Return path by String
    public String retRouteS(int p,int na,int nc){
	return RouteSntimes(p,na,nc);
    }
}
