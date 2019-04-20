class Dijkstra{ //referenced to p305 algorithms
    private int INF = Integer.MAX_VALUE;
    private int w=0;
    private int g=1;
    private int b=2;
    Dijkstra(){
    }
    double nTon(double[][]n,int st,int ed,int size){
	int[]color=new int[size+1];
	double []d=new double[size+1];
	int[]p=new int[size+1];
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
		}
	    }
	}
	if(d[ed]==INF)return -1.0;
	return d[ed];
    }
}
