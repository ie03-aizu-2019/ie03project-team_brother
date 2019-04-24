class Dijkstra{ //referenced to p305 algorithms
    private int INF = Integer.MAX_VALUE;
    private int w=0;
    private int g=1;
    private int b=2;
    private int color[];
    private double d[];
    private int p[];
    private int size;
    private int psize;
    private int st;
    private int ed;
    Dijkstra(){
    }
    double nTon(double[][]n,int st,int ed,int size,int psize){
	color=new int[size+1];
	d=new double[size+1];
	p=new int[size+1];
	this.ed=ed;
	this.st=st;
	this.psize=psize;
	double hz;
	int u,v;
	double mincost;
	for(int i=1;i<=size;i++){
	    d[i]=INF;
	    color[i]=w;
	    for(int j=1;j<=size;j++){
		if(!(n[i][j]>0)){
		    n[i][j]=INF;
		}
	    }
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
		if(color[v]!=b &&n[u][v]!=INF){
		    if(d[v]>d[u]+n[u][v]){
			d[v]= d[u]+n[u][v];
			p[v]=u;
			color[v]=g;
		    }
		    //		    if(d[v]=d[u]+n[u][v]){}
		}
	    }
	}
	if(d[ed]==INF)return -1.0;
	return d[ed];
    }
    public String rep_NumtoS(int d,boolean bla){
	String s1,s2="";
	if(bla)s2=" ";
	if(psize<d){
	    s1=Integer.toString(d-psize);
	    s2=s2+"C";
	}else{
	    s1=Integer.toString(d);
	}
	return s2+s1;
    }
    public String Route(int ed){
	String c="";
	int i,po;
	po=p[ed];
	if(po!=-1){c=rep_NumtoS(ed,true)+c;
         }else {
	  c=rep_NumtoS(ed,false)+c;
	}
	while(po!=-1){
	 if(p[po]==-1){
	     c=rep_NumtoS(po,false)+c;
	 }else{
	     c=rep_NumtoS(po,true)+c;
	 }
	    po=p[po];
	}
	return c;
    }
    public String retRoute(){
	return Route(this.ed);
    }
}
