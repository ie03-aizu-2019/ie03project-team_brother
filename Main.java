import java.util.Scanner;
import java.util.ArrayList;


class Main{
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

    // Add new added point on projection method(task 7)
    static ArrayList<Point> NewAdd = new ArrayList<Point>();
    static ArrayList<Point> NewConnect = new ArrayList<Point>();
    static ArrayList<Line>  NewLine = new ArrayList<Line>();

    //   static double[][] graph; // graph
    static ArrayList<AdjacencyList> list = new ArrayList<AdjacencyList>();  // graph by list

    static int allPoint;  // the number of all point
    
    public static void main(String argv[]){
	
	// input data
	InputData();

	// register intersection point
	RegistIntersection();

	// connect added points to existing points or lines
	for(int i=1; i<=P; i++){
	    Projection(ad[i]);
	}

	// set ID(NewAdd)
	for(int i=0; i<NewAdd.size(); i++){
	    NewAdd.get(i).setID(N + IpArray.length + i);
	}

	// set ID(NewConnect)
	for(int i=0; i<NewConnect.size(); i++){
	    NewConnect.get(i).setID(N + IpArray.length + NewAdd.size() + i);
	}
	
	// make a weighted graph
	allPoint = N + IpArray.length + NewAdd.size() + NewConnect.size();
	makeList();
	
	// shortest path
	Kth_ShortestPath();
	
	// highways
	getHighways_ByList();
    }

    public static void InputData(){
	Scanner sc = new Scanner(System.in);
	
	System.err.println("Please input N, M, P and Q.");
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

	if(N>0)
	    System.err.println("Please input Point info (x and y coordinate)");
	for(int i=1; i<=N; i++){
	    p[i]=new Point(sc.nextInt(),sc.nextInt());
	    
	    if((p[i].getX() < 0 || p[i].getX() > 1000000) ||
	       (p[i].getY() < 0 || p[i].getY() > 1000000)){
		System.err.println("X and Y should be between 0 and 1,000,000.");
		System.err.println("Please input again.");
		i--;
		continue;
	    }

	    boolean hasflag = false;

	    for(int j=1; j<i; j++){
		if(hasflag = HasAlready(p[i], p[j])){
		    break;
		}
	    }

	    if(hasflag){
		System.err.println("Point shouldn't duplicate.");
		System.err.println("Please input again.");
		i--;
		continue;
	    }

	    p[i].setID(i);
	}

	// M info
	if(M>0)
	    System.err.println("Please input Line info (start and end point)");
	for(int i=1; i<=M; i++){
	    int st, ed;

	    st = sc.nextInt();
	    ed = sc.nextInt();

	    if((st < 0 || st > p.length) ||
	       (ed < 0 || ed > p.length)){
		System.err.println("The start and end point should be between 0 and "+p.length+".");
		System.err.println("Please input again.");
		i--;
		continue;
	    }
	    
	    l[i]=new Line(p[st],p[ed]);

	    boolean hasflag = false;

	    for(int j=1; j<i; j++){
		if(hasflag = (HasAlready(l[i].getStart(), l[j].getStart())
			      && HasAlready(l[i].getEnd(), l[j].getEnd()))){
		    break;
		}
	    }

	    if(hasflag){
		System.err.println("Line shouldn't duplicate.");
		System.err.println("Please input again.");
		i--;
		continue;
	    }
	}

	// P info
	if(P>0)
	    System.err.println("Please input Additional Point info (x and y coordinate)");
	for(int i=1; i<=P; i++){
	    ad[i]=new Point(sc.nextInt(),sc.nextInt());
	    
	    if((ad[i].getX() < 0 || ad[i].getX() > 1000000) ||
	       (ad[i].getY() < 0 || ad[i].getY() > 1000000)){
		System.err.println("X and Y should be between 0 and 1,000,000.");
		System.err.println("Please input again.");
		i--;
		continue;
	    }

	    boolean hasflag = false;

	    for(int j=1; j<i; j++){
		if(hasflag = HasAlready(ad[i], ad[j])){
		    break;
		}
	    }

	    if(hasflag){
		System.err.println("Point shouldn't duplicate.");
		System.err.println("Please input again.");
		i--;
		continue;
	    }
	    
	    ad[i].setID(i);
	}

	// Q info
	if(Q>0)
	    System.err.println("Please input Route info (start, end, and k-th)");
	for(int i=1; i<=Q; i++){
	    s[i] = sc.next();
	    d[i] = sc.next();
	    k[i] = sc.nextInt();

	    if(k[i]<=0){
		System.err.println("k should be at least 1.");
		System.err.println("Please input again.");
		i--;
		continue;
	    }
	}
    }

    // task 1
    public static void calcIntersection(){
	Point ans;
	double isin;
	int inflag;
	int hasflag;

	System.out.println("Intersection points:");
	
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
			if(HasAlready(Ip.get(k), ans)){
			    hasflag = 1;
			}
		    }
		    
		    l[i].insert(ans);
		    l[j].insert(ans);
		    
		    // If Ip doesn't have same point, insert this point
		    if(hasflag!=1){
			Ip.add(ans);
			System.out.println("C"+(Ip.size()-1)+
					   ": ("+Ip.get(Ip.size()-1).getX()+
					   ", "+Ip.get(Ip.size()-1).getY()+")");
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

    // if point a and point b are equal, it returns true.
    // otherwise, it returns false.
    public static boolean HasAlready(Point a, Point b){
	if(-EPS<=a.getX()-b.getX() && a.getX()-b.getX()<=EPS
	   && -EPS<=a.getY()-b.getY() && a.getY()-b.getY()<=EPS)
	    return true;

	return false;
    }

    // task 2
    public static void RegistIntersection(){
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

    }
    
    public static void makeList(){
	// graph initialize
	for(int i=0; i<allPoint; i++)
	    list.add(new AdjacencyList());
	
	// graph input
	for(int i=1; i<l.length; i++){
	    for(int j=0; j<l[i].getSize()-1; j++){
		list.get(l[i].getList(j).getID())
		    .put(l[i].getList(j+1).getID()
			 ,dist.pTop(l[i].getList(j), l[i].getList(j+1)));

		list.get(l[i].getList(j+1).getID())
		    .put(l[i].getList(j).getID()
			 ,dist.pTop(l[i].getList(j+1), l[i].getList(j)));
	    }
	}

	for(int i=0; i<NewLine.size(); i++){
	    list.get(NewLine.get(i).getStart().getID())
		.put(NewLine.get(i).getEnd().getID()
		     ,dist.pTop(NewLine.get(i).getStart(), NewLine.get(i).getEnd()));

	    list.get(NewLine.get(i).getEnd().getID())
		.put(NewLine.get(i).getStart().getID()
		     ,dist.pTop(NewLine.get(i).getEnd(), NewLine.get(i).getStart()));
	}
    }

    // task 3,4,5,6
    public static void Kth_ShortestPath(){
 	System.out.println("\nK-th shortest paths in this graph:");
	for(int i=1; i<=Q; i++){
	    int sint = 0;
	    int dint = 0;
	    double[] dijk_ans;
	    String route;

	    // convert C point to integer(start)
	    if(s[i].startsWith("C")){
		s[i] = s[i].substring(1);
		sint += N;
	    }
	    
	    // convert C point to integer(end)
	    if(d[i].startsWith("C")){
		d[i] = d[i].substring(1);
		dint += N;
	    }
	    	    
	    sint += Integer.parseInt(s[i]);
	    dint += Integer.parseInt(d[i]);
	    System.out.println("\nQuery:"+i);
	    
	    if(sint>N+IpArray.length+NewAdd.size()+NewConnect.size()
	       || dint>N+IpArray.length+NewAdd.size()+NewConnect.size())
		System.out.println("NA");
	    
	    else{
		//Using Adjancency list
		dijk_ans = dijk.nTimesnTon(list, sint, dint, list.size()-1, N, k[i]);
  
		for(int j=1; j<dijk_ans.length; j++){
		    if(dijk_ans[j]>0){
			System.out.printf("%.5f\n",dijk_ans[j]);
			System.out.println(dijk.retRouteS(j,NewAdd.size(), NewConnect.size()));
		    }

		    else if(j==1){
			System.out.println("NA");
		    }
		    
		    else
			break;  
		}
	    }
	}
    }

    // task 7
    public static void Projection(Point ad_0){
	Point orth;  // orthogonal projection vector
	Point st;
	Point st_backup;
	Point ed;
	Point ad;
	double d;
	double isin;
	
	Point s_orth = new Point(0, 0);
	double shortest = 100000000;
	int shortest_ind = -1;
	
	for(int i=1; i<=N; i++){
	    if(HasAlready(p[i],ad_0))
		return;
	}
	
	NewAdd.add(ad_0);
	
	for(int i=1; i<=M; i++){
	    isin = dist.pTop(l[i].getStart(),ad_0)+dist.pTop(ad_0,l[i].getEnd())
		- dist.pTop(l[i].getStart(),l[i].getEnd());
	    if(-EPS<=isin && isin<=EPS){
		shortest_ind = i;
		l[shortest_ind].insert(ad_0);
		return;
	    }
	}

	for(int i=1; i<=N; i++){
	    d = dist.pTop(ad_0, p[i]);
	    
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
	    
	    if(shortest > d){
		shortest = d;
		shortest_ind = i;
		s_orth = orth;
	    }
	}

	// shortest != 0
	if(!(-EPS <= shortest && shortest <= EPS)){
	    
	    // connect point == one of graph points
	    for(int i=1; i<=N; i++){
		if(HasAlready(p[i],s_orth)){
		    NewLine.add(new Line(ad_0, s_orth));
		    return;
		}
	    }

	    // connect point is new point in graph
	    NewConnect.add(s_orth);
	    
	    if(shortest_ind != -1){
		// System.out.println("line: "+shortest_ind);
		l[shortest_ind].insert(s_orth);
		NewLine.add(new Line(ad_0, s_orth));
	    }
	}
	//System.out.println((float)s_orth.getX()+" "+(float)s_orth.getY());
    }
    
    // task 8(by using adjacency list)
    public static void getHighways_ByList(){
	double g_back;
	int opo_back;
	ArrayList<Integer> S_highways = new ArrayList<Integer>();
	ArrayList<Integer> E_highways = new ArrayList<Integer>();
	AdjacencyList[] adl;

	for(int i=1; i<list.size(); i++){
	    for(int j=i; j<allPoint; j++){
		
		if(list.get(i).containsKey(j)){
		    g_back = list.get(i).get(j);
		    list.get(i).remove(j);
		    adl=list.toArray(new AdjacencyList[list.size()]);
		    
		    if(dijk.nTon(adl, i, j, list.size()-1, N)<=0){
			S_highways.add(i);
			E_highways.add(j);
		    }
		    
		    list.get(i).put(j,g_back);
		}
	    }
	}
	
        System.out.println("\nHighways in this graph:");
	
	// outputs
	for(int i=0; i<S_highways.size(); i++){
	    
	    // start point
	    if(S_highways.get(i)>N && S_highways.get(i)<N + Ip.size()){
	    	System.out.printf("C%d ", S_highways.get(i) - N);
	    }
	    else{
		System.out.printf("%d ", S_highways.get(i));
	    }

	    // end point
	    if(E_highways.get(i)>N && E_highways.get(i)<N + Ip.size()){
		System.out.printf("C%d\n", E_highways.get(i) - N);
	    }
	    else{
		System.out.printf("%d\n", E_highways.get(i));
	    }
	}
    }
}
	
	
