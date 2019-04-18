import java.util.Scanner;
class phase1_2{
    public static void main(String argv[]){
	int N,M,P,Q;
	Scanner sc = new Scanner(System.in);
	Intersection it = new Intersection();
	Sort sort = new Sort();
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

	IArray = sort.shellSort(IArray,IArray.length-1);

	for(int i=1; i<IArray.length; i++)
	    System.out.printf("%.5f %.5f\n",IArray[i].getX(),IArray[i].getY());

	// and more...
    }
}
