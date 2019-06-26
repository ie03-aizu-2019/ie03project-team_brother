import java.util.Scanner;
import java.util.ArrayList;
import java.util.Stack;

class Phase3_9{
    static int N,M,P,Q;
    static Intersection it = new Intersection();
    static Sort sort = new Sort();
    static Distance dist = new Distance();
    static Dijkstra dijk = new Dijkstra();
    static double EPS = 0.00000001;

    // input data
    static Point p[];
    static Line  l[];
    static Point ad[];
    static String s[], d[];
    static int k[];

    // Intersection Point(Array and ArrayList)
    static Point[] IpArray;
    static ArrayList<Point> Ip = new ArrayList<Point>();

    static double[][] graph; // graph
    
    public static void main(String argv[]){
	
	// input data
	InputData();

	// Ip init
	Ip.add(new Point(-1,-1));

	// calclation Intersection Point
	calcIntersection();
	
	IpArray = new Point[Ip.size()];
	Ip.toArray(IpArray);

	// sort
	IpArray = sort.shellSort(IpArray,IpArray.length-1);
	
	// set id
	for(int i=1; i<IpArray.length; i++){
	    IpArray[i].setID(p.length + i-1);
	    
	    for(int j=1; j<l.length; j++)
		l[j].setID(IpArray[i]);
	}

	// result
	//for(int i=1; i<IpArray.length; i++)
	//  System.out.printf("%.5f %.5f\n"
	//	      ,IpArray[i].getX(),IpArray[i].getY());

	for(int i=1; i<=P; i++)
	    Projection(ad[i]);

	// make a weighted graph
	makeGraph();

	//for(int i=1; i<graph.length; i++){
	//for(int j=1; j<graph[i].length; j++){
	//System.out.printf("%.2f ",graph[i][j]);
	//}
	//System.out.println();
	//}
	
	// input data
	for(int i=1; i<=Q; i++){
	    int sint = 0;
	    int dint = 0;
	    double[] dijk_ans;
	    
	    if(s[i].startsWith("C")){
		s[i] = s[i].substring(1);
		sint += N;
	    }

	    if(d[i].startsWith("C")){
		d[i] = d[i].substring(1);
		dint += N;
	    }
	    	    
	    sint += Integer.parseInt(s[i]);
	    dint += Integer.parseInt(d[i]);	    	
	    
	    if(sint>N+IpArray.length || dint>N+IpArray.length)
		System.out.println("NA");
	    
	    else{
		dijk_ans = dijk.nTimesnTon(graph, sint, dint, graph.length-1, N, k[i]);

		for(int j=1; j<dijk_ans.length; j++){
		    if(dijk_ans[j]>0){
			System.out.printf("%.5f\n",dijk_ans[j]);
			System.out.println(dijk.retRouteS(j));
		    }

		    else if(j==1){
			System.out.println("NA");
		    }
		    
		    else
			break;  
		}
	    }
	}
	
	// and more...
	//getHighways();
    }

    public static void InputData(){
	Scanner sc = new Scanner(System.in);
	
	N=sc.nextInt();
       	M=sc.nextInt();
	P=sc.nextInt();
	Q=sc.nextInt();
	
	p = new Point[N+1];  //(x,y)
	l = new Line[M+1];   //(start,end)
	ad = new Point[P+1];
	s = new String[Q+1];
	d = new String[Q+1];
	k = new int[Q+1];

	// N info
	for(int i=1; i<=N; i++){
	    p[i]=new Point(sc.nextInt(),sc.nextInt());
	    p[i].setID(i);
	}

	// M info
	for(int i=1; i<=M; i++){
	    l[i]=new Line(p[sc.nextInt()],p[sc.nextInt()]);
	}

	// P info
	for(int i=1; i<=P; i++){
	    ad[i]=new Point(sc.nextInt(),sc.nextInt());
	    ad[i].setID(i);
	}

	// Q info
	for(int i=1; i<=Q; i++){
	    s[i] = sc.next();
	    d[i] = sc.next();
	    k[i] = sc.nextInt();
	}
    }

    public static void calcIntersection(){
	Point ans;
	double isin;
	int inflag;
	int hasflag;
	
	for(int i=1; i<=M; i++){
	    // intersection point
	    for(int j=i+1; j<=M; j++){
		
		// calc the intersection
		ans = it.calc(l[i],l[j]);
		
		// output
		if(ans.getX()==-1&&ans.getY()==-1){
		    //System.out.println("NA");
		}
		
		else{
		    // check Ip has same point
		    hasflag = 0;
		    for(int k=1; k<Ip.size(); k++){
			if(-EPS<=Ip.get(k).getX()-ans.getX() && Ip.get(k).getX()-ans.getX()<=EPS
			   && -EPS<=Ip.get(k).getY()-ans.getY() && Ip.get(k).getY()-ans.getY()<=EPS){
			    hasflag = 1;
			}
		    }
		    
		    l[i].insert(ans);
		    l[j].insert(ans);
		    
		    // If Ip doesn't have same point, insert this point
		    if(hasflag!=1){
			Ip.add(ans);
		    }
		}
	    }

	    // connecting point
	    for(int j=1; j<N; j++){

		// if point already registered on line, then ignore.
		inflag = 0;
		for(int k=0; k<l[i].getSize(); k++){
		    isin = dist.pTop(l[i].getList(k),p[j]);
		    if(-EPS<=isin && isin<=EPS){
			inflag = 1;
			break;
		    }
		}
		
		if(inflag==1)
		    continue;

		// judgement whether point exist on line segment
		isin = dist.pTop(l[i].getStart(),p[j])
		    +  dist.pTop(p[j],l[i].getEnd())
		    -  dist.pTop(l[i].getStart(),l[i].getEnd());
		
		if(-EPS<=isin && isin<=EPS){
		    l[i].insert(p[j]);
		}
	    }
	}
    }

    public static void makeGraph(){
	
	graph = new double[N + IpArray.length][N + IpArray.length];
	
	// graph initialize(all weights set 0)
	for(int i=0; i<graph.length; i++){
	    for(int j=0; j<graph[i].length; j++){
		graph[i][j] = 0;
	    }
	}

	// graph input
	for(int i=1; i<l.length; i++){
	    for(int j=0; j<l[i].getSize()-1; j++){
		graph[l[i].getList(j).getID()][l[i].getList(j+1).getID()]
		    = dist.pTop(l[i].getList(j), l[i].getList(j+1));

		
		graph[l[i].getList(j+1).getID()][l[i].getList(j).getID()]
		    = dist.pTop(l[i].getList(j+1), l[i].getList(j));
	    }
	}
    }

    public static void Projection(Point ad_0){
	Point orth;  // orthogonal projection vector
	Point st;
	Point st_backup;
	Point ed;
	Point ad;
	double d;
	double isin;
	
	Point s_orth = new Point(0, 0);
	double shortest = 999;

	for(int i=1; i<=N; i++){
	    d = dist.pTop(ad_0, p[i]);

	    // System.out.println(d+"["+p[i].getX()+" "+p[i].getY()+"]");
	    
	    if(shortest > d){
		shortest = d;
		s_orth = p[i];
	    }
	}
	
	for(int i=1; i<=M; i++){
	    st = new Point(l[i].getStart().getX(), l[i].getStart().getY());
	    st_backup = new Point(l[i].getStart().getX(), l[i].getStart().getY());
	    ed =  new Point(l[i].getEnd().getX(), l[i].getEnd().getY());
	    ad = new Point(ad_0.getX(), ad_0.getY());

	    st.sub(st_backup);  // O = (0, 0)
	    ed.sub(st_backup);  // A = ed-st
	    ad.sub(st_backup);  // B = ad-st
	    
	    orth = new Point((ed.getX()*ad.getX() + ed.getY()*ad.getY())
			     / (dist.pTop(st,ed)*dist.pTop(st,ed)) * ed.getX()
			     ,((ed.getX()*ad.getX() + ed.getY()*ad.getY())
			       / (dist.pTop(st,ed)*dist.pTop(st,ed)) * ed.getY()));

	    isin = dist.pTop(st,orth)+dist.pTop(orth,ed) - dist.pTop(st,ed);

	    if(!(-EPS<=isin && isin<=EPS))  // except point out of line
		continue;
	    
	    orth.add(st_backup);
	    d = dist.pTop(ad_0, orth);

	    if(-EPS<=d && d<=EPS)  // except d = 0
		continue;
	    
	    // System.out.println(d+"["+orth.getX()+" "+orth.getY()+"]");
	    
	    if(shortest > d){
		shortest = d;
		s_orth = orth;
	    }
	}

	//System.out.println((float)s_orth.getX()+" "+(float)s_orth.getY());
    }

    public static void getHighways(){
	double g_back;
	ArrayList<Integer> S_highways = new ArrayList<Integer>();
	ArrayList<Integer> E_highways = new ArrayList<Integer>();

	for(int i=1; i<graph.length; i++){
	    for(int j=i; j<graph[i].length; j++){
		if(i==j)
		    continue;
		
		if(graph[i][j]>0){
		    g_back = graph[i][j];
		    graph[i][j] = 0;

		    if(dijk.nTon(graph, i, j, graph.length-1, N)<=0){
			S_highways.add(i);
			E_highways.add(j);
		    }
		    
		    graph[i][j] = g_back;
		}
	    }
	}

	for(int i=0; i<S_highways.size(); i++){
	    if(S_highways.get(i)>N){
		System.out.printf("C%d ", S_highways.get(i) - N);
	    }
	    else{
		System.out.printf("%d ", S_highways.get(i));
	    }

	    if(E_highways.get(i)>N){
		System.out.printf("C%d\n", E_highways.get(i) - N);
	    }
	    else{
		System.out.printf("%d\n", E_highways.get(i));
	    }
	}
    }
}
	
	
