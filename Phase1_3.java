import java.util.Scanner;

class Phase1_3{
    public static void main(String argv[]){
	int N,M,P,Q;
	Scanner sc = new Scanner(System.in);
	Intersection it = new Intersection();
	Sort sort = new Sort();
	Distance dist = new Distance();
	Dijkstra dijk = new Dijkstra();
	double EPS = 0.00000001;
	
	Point[] PArray, IArray;   // Array of Intersection Point
	int par_num=0, iar_num=0; // The number of each Array

	// input data
	N=sc.nextInt();
       	M=sc.nextInt();
	P=sc.nextInt();
	Q=sc.nextInt();
	
	Point p[] = new Point[N+1],ans;  //(x,y)
	Line  l[] = new Line[M+1];       //(start,end)

	for(int i=0; i<M; i++)
	    par_num += i;
	
	PArray = new Point[par_num+1];
	
	for(int i=1;i<=N;i++){
	    p[i]=new Point(sc.nextInt(),sc.nextInt());
	    p[i].setID(i);
	}
	for(int i=1;i<=M;i++){
	    l[i]=new Line(p[sc.nextInt()],p[sc.nextInt()]);
	}
	
	par_num=1;     // 1 origin
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
		    iar_num++;
		}

		PArray[par_num++] = ans;
	    }
	}

	IArray = new Point[iar_num+1];
	iar_num = 1;     // 1 origin
	
	for(int i=1; i<PArray.length; i++){
	    if(PArray[i].getX()!=-1){
		IArray[iar_num++] = PArray[i];
	    }
	}

	// sort
	IArray = sort.shellSort(IArray,IArray.length-1);

	// set id
	for(int i=1; i<IArray.length; i++){
	    IArray[i].setID(p.length + i-1);
	    
	    for(int j=1; j<l.length; j++)
		l[j].setID(IArray[i]);
	}

	// result
	//for(int i=1; i<IArray.length; i++)
	//System.out.printf("%.5f %.5f\n",IArray[i].getX(),IArray[i].getY());

	// graph
	double[][] graph = new double[N + IArray.length][N + IArray.length];
	
	// graph initialize
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

	// graph output
	/*
	for(int i=1; i<graph.length; i++){
	    for(int j=1; j<graph[i].length; j++){
		System.out.printf("%.2f  ",graph[i][j]);
	    }
	    System.out.println();
	}
	*/

	// input data
	String s, d;
	int k;
	int sint, dint;
	double dijk_ans;
	for(int i=0; i<Q; i++){
	    sint = 0;
	    dint = 0;
	    
	    s = sc.next();
	    d = sc.next();
	    k = sc.nextInt();

	    // debug
	    //System.out.println(s+", "+d);

	    if(s.startsWith("C")){
		s = s.substring(1);
		sint += N;
	    }

	    if(d.startsWith("C")){
		d = d.substring(1);
		dint += N;
	    }
	    // debug
	    //System.out.println(s+", "+d);
	    
	    sint += Integer.parseInt(s);
	    dint += Integer.parseInt(d);
	    	
	    // debug
	    //System.out.println(sint+", "+dint);
	    
	    if(sint>N+IArray.length || dint>N+IArray.length)
		System.out.println("NA");
	    
	    else{
		dijk_ans = dijk.nTon(graph, sint, dint, graph.length-1);

		if(dijk_ans>0)
		    System.out.printf("%.5f\n",dijk_ans);

		else
		    System.out.println("NA");  
	    }
	}
	
	// and more...
    }
}
