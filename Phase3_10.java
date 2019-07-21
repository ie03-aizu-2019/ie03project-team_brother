import java.util.Scanner;
import java.util.ArrayList;


class Phase3_10{
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
	
	// result
	//for(int i=1; i<IpArray.length; i++)
	//  System.out.printf("%.5f %.5f\n"
	//	      ,IpArray[i].getX(),IpArray[i].getY());
	
	for(int i=1; i<=P; i++){
	    Projection(ad[i]);
	}

	// set ID(NewAdd)
	for(int i=0; i<NewAdd.size(); i++){
	    NewAdd.get(i).setID(N + IpArray.length + i);
	    // System.out.println(NewAdd.get(i).getID());
	}

	// set ID(NewConnect)
	for(int i=0; i<NewConnect.size(); i++){
	    NewConnect.get(i).setID(N + IpArray.length + NewAdd.size() + i);
	    // System.out.println(NewConnect.get(i).getID());
	}

	// output(for debug)
	/*
	for(int i=0; i<NewAdd.size(); i++)
	    System.out.printf("add[%d]%d  %.2f %.2f\n"
			      ,i, NewAdd.get(i).getID(), NewAdd.get(i).getX(), NewAdd.get(i).getY());
	for(int i=0; i<NewConnect.size(); i++)
	    System.out.printf("con[%d]%d  %.2f %.2f\n"
			      ,i, NewConnect.get(i).getID(), NewConnect.get(i).getX(), NewConnect.get(i).getY());
	for(int i=0; i<NewLine.size(); i++)
	    System.out.printf("line[%d] (%.2f %.2f) -> (%.2f %.2f)\n"
			      , i
			      , NewLine.get(i).getStart().getX(), NewLine.get(i).getStart().getY()
			      , NewLine.get(i).getEnd().getX(), NewLine.get(i).getEnd().getY());
	*/
	
	// make a weighted graph
	allPoint = N + IpArray.length + NewAdd.size() + NewConnect.size();
	//makeGraph();
	makeList();

	// graph output(for debug)
	/*
	// graph(adjacency array)
	for(int i=1; i<graph.length; i++){
	    for(int j=1; j<graph[i].length; j++){
		if(graph[i][j]==0)
		    System.out.printf("---- ");

		else
		    System.out.printf("%.2f ",graph[i][j]);
	    }
	    System.out.println();
	}
	*/
	// graph(adjacency list)
	/*for(int i=1; i<list.size(); i++){
	    System.out.printf("%2d: ",i);
	    for(int j=1; j<allPoint; j++){
		if(list.get(i).containsKey(j)){
		    System.out.printf("[%2d, %.2f] ",j, list.get(i).get(j));
		}
	    }
	    System.out.println();
	    }*/
	
	
	// shortest path
	Kth_ShortestPath();
	
	// highways
	// getHighways();
	getHighways_ByList();
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

    // task 1
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
			if(HasAlready(Ip.get(k), ans)){
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

    // make weighted graph
    /*public static void makeGraph(){
	
	graph = new double[allPoint][allPoint];
	
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

	for(int i=0; i<NewLine.size(); i++){
	    graph[NewLine.get(i).getStart().getID()][NewLine.get(i).getEnd().getID()]
		= dist.pTop(NewLine.get(i).getStart(), NewLine.get(i).getEnd());

	    graph[NewLine.get(i).getEnd().getID()][NewLine.get(i).getStart().getID()]
		= dist.pTop(NewLine.get(i).getStart(), NewLine.get(i).getEnd());
	}
    }
    */
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
		//Using Adjancency matrix
		//dijk_ans = dijk.nTimesnTon(graph, sint, dint, graph.length-1, N, k[i]);
      
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
		// System.out.println(shortest+"  "+d);
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

    // task 8
    /*public static void getHighways(){
	double g_back;
	ArrayList<Integer> S_highways = new ArrayList<Integer>();
	ArrayList<Integer> E_highways = new ArrayList<Integer>();
	AdjacencyList[] adl;

	for(int i=1; i<graph.length; i++){
	    for(int j=i; j<graph[i].length; j++){
		if(i==j)
		    continue;
		
		if(graph[i][j]>0){
		    g_back = graph[i][j];
		    graph[i][j] = 0;
		    if(dijk.nTon(graph, i, j,graph.length-1, N)<=0){
			S_highways.add(i);
			E_highways.add(j);
		    }
		    
		    graph[i][j] = g_back;
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
    */
    
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
		    /*g_back = graph[i][j];
		      graph[i][j] = 0;*/
		    g_back = list.get(i).get(j);
		    list.get(i).remove(j);
		    adl=list.toArray(new AdjacencyList[list.size()]);
		    
		    /*test adjacencyList*/
		    /* for(int a =1;a<adl.length;a++){
	            System.out.printf("%2d: ",a);
	                for(int b=1; b<allPoint; b++){
		         if(adl[a].containsKey(b)){
		          System.out.printf("[%2d, %.2f] ",b, adl[a].get(b));
	              	 }
			}
	                System.out.println();
			}*/

		    
		    if(dijk.nTon(/*graph*/adl, i, j, list.size()-1, N)<=0){
			S_highways.add(i);
			E_highways.add(j);
		    }
		    
		    //graph[i][j] = g_back;
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
	
	
