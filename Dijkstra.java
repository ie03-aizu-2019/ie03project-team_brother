import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map.Entry;

class Dijkstra{ //referenced to p305 algorithms
    private int INF = Integer.MAX_VALUE;
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
    Dijkstra(){
    }
    double[] nTimesnTon(ArrayList<AdjacencyList>n,int st,int ed,int size,int psize,int times){
	this.size=size;
	double[]ret=new double[times+1];
	Hasroute spurroute,test;
	Hasroute spurroute_sep;
	int spurnode,befonode;
	for(int i = 1;i<times+1;i++){
	    ret[i]=-1;
	}
	delind=new ArrayList<Hasroute>();
	//	double[][]n2=new double[size+1][size+1];
	AdjacencyList [] n2 = new AdjacencyList[size+1];
	myclone(n,n2);
	//	double[][]n3=new double[size+1][size+1];
	AdjacencyList [] n3 = new AdjacencyList[size+1];
	myclone(n,n3);
	int p,p2,bcnt=0;
        a=new ArrayList<Hasroute>();//確定 0origin
	ArrayList<Hasroute>b=new ArrayList<Hasroute>();//不確定 0origin
        double hz;
	for(int i=1;i<=times;i++){
	    if(i==1){
		/*test n3*/
		/* for(int k=1;k<=size;k++){
			for(int l=1;l<=size;l++){
			    if(n3[k][l]==INF){
				System.out.print("0 ");
			    }else{
			    System.out.printf("%.2f ",n3[k][l]);
			    }
			}
			System.out.println("");
			}*/
		hz=nTon(n2,st,ed,size,psize);
		if(hz==-1.0)return ret;

                a.add(new Hasroute(d[ed],Route(ed),d,psize));
		//   System.out.println(Arrays.toString(Route(ed)));
		delroute(a.get(a.size()-1));/////
	    }
	    if(i>1){
		spurroute=a.get(a.size()-1);
		
		spurroute_sep=new Hasroute(0.0,new int[]{-1},new double[]{0.0},psize);
		//System.out.println(delind.size());//test
		for(int j=1;j<spurroute.getroute().length;j++){
    		    spurnode=(spurroute.getroute())[j];
		    //add node step by step
		    spurroute_sep.addn(spurnode);
			    for(int z=0;z<delind.size();z++){
				if(spurroute_sep.equals(delind.get(z))){
				    if(delind.get(z).getnext()!=-2){
					/*n3[spurnode][(delind.get(z)).getnext()]=INF;
					  n3[(delind.get(z)).getnext()][spurnode]=INF;*/
				 n3[spurnode].remove((delind.get(z)).getnext());
				 n3[(delind.get(z)).getnext()].remove(spurnode);	 	
			            }
				}
			    }
		   preSpur(spurroute,j);
		   hz=spurnTon(n3,st,ed,size,psize);
       		   if(hz==-1.0){
		           myclone(n,n3);	      //初期化
		       continue;
		   }
	      	   test=new Hasroute(d[ed],Route(ed),d,psize);
		       if(a.contains(test)||b.contains(test)){//重複を除く
		              myclone(n,n3);	      //初期化
			   continue;
		       }
		   b.add(test);
		   myclone(n,n3);	      //初期化
		}
		if(b.size()==0){
		    Collections.sort(a);//辞書順に小さい文字を優先させるため
		    //		    System.out.println(a.size());
		    for(int x=1;x<=a.size();x++){
			ret[x]=a.get(x-1).getdis();
		    }
		    return ret;
		}
		Collections.sort(b);
		a.add(b.get(0));//最短を確定へ
		b.remove(0);
		delroute(a.get(a.size()-1));
	    }
	}
	Collections.sort(a);//辞書順に小さい文字を優先させるため
	for(int x=1;x<=a.size();x++){
	    ret[x]=a.get(x-1).getdis();
	    }
	return ret;
    }
    void myclone(/*double[][] n,double[][]opo*/ ArrayList<AdjacencyList> n,AdjacencyList[]opo){
	/*	for(int i=0;i<=size;i++){
	    for(int j = 0;j<=size;j++){
	        opo[i][j]=n[i][j];
	    }
	    }*/
	for(int i=1;i<=size;i++){
	    opo[i]=new AdjacencyList();
	    for(Entry<Integer,Double>entry: /*n[i].entrySet()*/n.get(i).entrySet()){
		opo[i].put((Integer)(entry.getKey()),(Double)(entry.getValue()));
	    }
	}	
    }
    void delroute(Hasroute a){
	int[]route= a.getroute();
	int[]sep_route;
	boolean flg = false;
	Hasroute delr;
	for(int i =1;i<a.getsize();i++){
	    flg=false;
	    sep_route=new int[i+1];
       	    System.arraycopy(route,0,sep_route,0,i+1);
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
	for(int i=1;i<=size;i++){
	    d[i]=INF;
	    color[i]=w;
	    /*Adjacency list does not need to INF
	    for(int j=1;j<=size;j++){
		if(!(n[i][j]>0.0)){
		    n[i][j]=INF;
		}
		}*/
	}
	d[st]=0;
	p[st]=-1;
     
	while(true){
	    mincost=INF;
	    u=-1;
	    for(int i=1;i<=size;i++){
		if(color[i]!=b && d[i]<mincost){
		    mincost=d[i];
		    u=i;
		}
	    }
	    if(u==-1) break;
	    color[u]=b;

	    for(v=1;v<=size;v++){
		/*
		if(color[v]!=b &&n[u][v]!=INF){
		    if(d[v]>d[u]+n[u][v]){
			d[v]= d[u]+n[u][v];
			p[v]=u;
			color[v]=g;
		    }
		    }*/
		//		disn=n[u].get(v);//n[u][v]
	
		if(color[v]!=b &&n[u].containsKey(v)){
	       	disn=n[u].get(v);//n[u][v]
		    if(d[v]>d[u]+disn){
			d[v]= d[u]+disn;
			p[v]=u;
			color[v]=g;
		    }
		}		
	    }
	}
	if(d[ed]==INF)return -1.0;
	return d[ed];
    }
    double spurnTon(AdjacencyList[]n,int st,int ed,int size,int psize){
	double hz,disn;
	int u,v;
	double mincost;
	/* Adjacency list does not need to INF
	for(int i=1;i<=size;i++){
	    for(int j=1;j<=size;j++){
		if(!(n[i][j]>0.0)){
		    n[i][j]=INF;
		}
	    }
	}
	*/
	while(true){
	    mincost=INF;
	    u=-1;
	    for(int i=1;i<=size;i++){
		if(color[i]!=b && d[i]<mincost){
		    mincost=d[i];
		    u=i;
		}
	    }
	    if(u==-1) {
		break;
	    }
	    color[u]=b;
	    for(v=1;v<=size;v++){
		/*
		if(color[v]!=b &&n[u][v]!=INF){
		    if(d[v]>d[u]+n[u][v]){
			d[v]= d[u]+n[u][v];
			p[v]=u;
			color[v]=g;
		    }
		}
		*/	
		if(color[v]!=b &&n[u].containsKey(v)){
		disn=n[u].get(v);//n[u][v]	
		    if(d[v]>d[u]+disn){
			d[v]= d[u]+disn;
			p[v]=u;
			color[v]=g;
		    }
		} 
	    }
	}
	if(d[ed]==INF)return -1.0;
	return d[ed];
    }
    public void preSpur(Hasroute h,int ind){
	int i;
	for(i=0;i<=size ; i++){
	    d[i]=INF;
	    color[i]=w;
	    p[i]=0;
	}
	int[] ro=h.getroute();
	double[] adis=h.getarraydis();
	for(i = 1 ;i<ind;i++){
	    color[ro[i]]=b;
	    p[ro[i]]=ro[i-1];
	    d[ro[i]]=adis[ro[i]];
	    if(ro[i]==ed)break;
	}

	 color[ro[i]]=g;
	 p[ro[i]]=ro[i-1];
	 d[ro[i]]=adis[ro[i]];
    }

    
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
   public int[] retRoute(){
	return Route(this.ed);
    }
    public String retRouteS(int p,int na,int nc){
	return RouteSntimes(p,na,nc);
    }
}
