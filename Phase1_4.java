import java.util.Scanner;
import java.util.ArrayList;

class Phase1_4{
    static int N,M,P,Q;
    static Intersection it = new Intersection();
    static Sort sort = new Sort();
    static Distance dist = new Distance();
    static Dijkstra dijk = new Dijkstra();
    static double EPS = 0.00000001;

    // input data
    static Point p[];
    static Line  l[];
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
    }

    public static void InputData(){
	Scanner sc = new Scanner(System.in);
	
	N=sc.nextInt();
       	M=sc.nextInt();
	P=sc.nextInt();
	Q=sc.nextInt();
	
	p = new Point[N+1];  //(x,y)
	l = new Line[M+1];   //(start,end)
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
}
