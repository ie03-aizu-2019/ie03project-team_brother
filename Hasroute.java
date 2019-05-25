import java.util.Arrays;

class Hasroute implements Comparable<Hasroute>{
    private double i;
    private int[]route;//1 origin!
    private double[] d;
    private int nextn = -2 ;
    private int psize = -1;
    Hasroute(double i,int[] route){
	this.i=i;
	this.route=route;
    }
    Hasroute(double i,int[] route,double[]d){
	this.i=i;
	this.route=route;
	this.d=new double[d.length];
	System.arraycopy(d,0,this.d,0,d.length);
    }
    Hasroute(double i,int[] route,double[]d,int psize){
	this.i=i;
	this.route=route;
	this.d=new double[d.length];
	System.arraycopy(d,0,this.d,0,d.length);
	this.psize=psize;
    }
    Hasroute(double i,int[] route,double[]d,int nextn,int flg){
	this.i=i;
	this.route=route;
	this.d=new double[d.length];
	System.arraycopy(d,0,this.d,0,d.length);
	this.nextn=nextn;
    }
    int getnext(){
	return nextn;
    }
    double getdis(){
	return i;
    }
    double[] getarraydis(){
	return d;
    }
    int[] getroute(){
	return route;
    }
    int getsize(){
	return route.length;
    }
    public int compareTo(Hasroute h){//using by sort methods
	if(this.i-h.getdis()<0.0){
	    return -1;
		}else if(this.i-h.getdis()>0.0){
	    return 1;
	}
	if(RouteSntimes(this).compareTo(RouteSntimes(h))<0.0){
	    return -1;
	}else if(RouteSntimes(this).compareTo(RouteSntimes(h))>0.0){
	    return 1;
	}
	return 0;
	
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
    public String RouteSntimes(Hasroute h){
	    String c="";
	    int[] myroute=h.getroute();
	    for(int i=1;i<myroute.length;i++){
	    if(i==1){
	        c=c+rep_NumtoS(myroute[i],false);
	    }else{
	        c=c+rep_NumtoS(myroute[i],true);
	     }
     }
	       return c;
    }
    public boolean equals(Object o){//using by contain methods
	if(o == this) return true;
	if(o.getClass() != this.getClass())return false;
	Hasroute has = (Hasroute)o;
        return 	Arrays.equals(this.route,has.route);
    }
    public void addn(int newnode){//add new node
	int[] newroute=new int[route.length+1];
	for(int i=0;i<newroute.length-1;i++){
	    newroute[i]=route[i];//copying before route
	}
	newroute[newroute.length-1]=newnode;//add new node
	route=newroute;
    }
}
