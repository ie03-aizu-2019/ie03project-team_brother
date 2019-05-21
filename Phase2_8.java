import java.util.Scanner;
import java.util.ArrayList;
import java.util.Stack;

class Phase2_8{
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
	//System.out.printf("%.5f %.5f\t%.5f %.5f\n"
	//,IpArray[i].getX(),IpArray[i].getY());

	for(int i=1; i<=P; i++)
	    Projection(ad[i]);

	// make a weighted graph
	makeGraph();
	
	// input data
	for(int i=1; i<=Q; i++){
	    int sint = 0;
	    int dint = 0;
	    double dijk_ans = 0;
	    
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
		dijk_ans = dijk.nTon(graph, sint, dint, graph.length-1, N);

		if(dijk_ans>0){
		    System.out.printf("%.5f\n",dijk_ans);
		    System.out.println(dijk.retRoute());
		}
		
		else
		    System.out.println("NA");  
	    }
	}
	
	// and more...
	getHighways();
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
	for(int i=1; i<=M-1; i++){
	    for(int j=i+1; j<=M; j++){
		
		// calc the intersection
		ans = it.calc(l[i],l[j]);
		
		// output
		if(ans.getX()==-1&&ans.getY()==-1){
		    //System.out.println("NA");
		}
		
		else{
		    //System.out.printf("%.5f %.5f\n",ans.getX(),ans.getY());
		    l[i].insert(ans);
		    l[j].insert(ans);
		    Ip.add(ans);
		}
	    }
	}
    }

    public static void makeGraph(){
	
	graph = new double[N + IpArray.length][N + IpArray.length];
	
	// graph initialize(all weights set 0)
	for(int i=1; i<l.length; i++){
	    for(int j=0; j<l[i].getSize()-1; j++){
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

	System.out.println((float)s_orth.getX()+" "+(float)s_orth.getY());
    }

    public static void getHighways(){
	ArrayList<Area> areas = new ArrayList<Area>();
	Stack<Integer> stack;
	int start, end;
	int had;
	int connect[] = new int[graph.length];
	int connect_backup[] = new int[graph.length];
	int lastArea=0;
	int visited[] = new int[graph.length];
	
	// add Area(which has only 1 connection)
	for(int i=1; i<graph.length; i++){
	    connect[i] = 0;
	    for(int j=0; j<graph[i].length; j++){
		if(graph[i][j]>0){
		    connect[i]++;
		}

		connect_backup[i] = connect[i];
	    }
	    
	    System.out.println(i +" "+connect[i]);
	    
	    if(connect[i]==1){
		areas.add(new Area());
		areas.get(lastArea).set(i);
		lastArea++;
		continue;
	    }
	}
	
	// search Area(which has many connections)
	for(start=1; start<graph.length; start++){
	    had = 0;

	    // Is value of start included in any areas?
	    for(int i=0; i<areas.size(); i++){
		if(areas.get(i).contains(start)){
		    had = 1;
		    break;
		}
	    }

	    // If included, then skip
	    if(had == 1)
		continue;

	    // Initialize visited condition
	    for(int i=0; i<visited.length; i++){
		visited[i] = 0;
	    }

	    for(int i=0; i<connect.length; i++){
		connect[i] = connect_backup[i];
	    }

	    // Advanced of depth first search
	    stack = new Stack<Integer>();
	    stack.push(start);
	    //visited[start] = 1;
	    end = start;

	    while(!(stack.peek()==start && stack.size()!=1)){
		for(int j=1; j<graph[end].length; j++){
		    // connection
		    if(graph[end][j]>0){
			
			// has not visited
			if(visited[j-1] == 0){
			    visited[j-1] = 1;
			    stack.push(j);
			    end = j;
			    System.out.println(stack.toString());
			    break;
			}

			// has visited
			else{
			    connect[end]--;
			}
		    }

		    // no destination
		    if(connect[end]==0){
			stack.pop();
			System.out.println(stack.toString());
			break;
		    }
		    
		}

		if(stack.empty())
		    break;
	    }
	}
	
	// print(for debug)
	for(int i=0; i<areas.size(); i++){
	    areas.get(i).print();
	}
	
	for(int i=1; i<graph.length; i++){
	    for(int j=1; j<graph[i].length; j++){
		System.out.printf("%.2f ",graph[i][j]);
	    }
	    System.out.println();
	}
    }
}
	
	
