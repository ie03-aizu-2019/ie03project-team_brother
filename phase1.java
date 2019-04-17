import java.util.Scanner;
class phase1{
    public static void main(){
	int N,M,P,Q;
	Scanner sc = new Scanner(System.in);
	N=sc.nextInt();
       	M=sc.nextInt();
	P=sc.nextInt();
	Q=sc.nextInt();
	Point p[N],ans;//(x,y)
	Line  l[M];//(start,end)
	for(int i=1;i<=N;i++){
	    p[i]=new Point(sc.nextInt(),sc.nextInt());
	}
	for(int i=1;i<=M;i++){
	    l[i]=new Line(p[sc.nextInt()],p[sc.nextInt()]);
	}
	ans = calc(l[1],l[2]);
	if(ans.getX()==-1&&ans.getY()==-1){
	    System.out.println("NA");
	}else{
	    System.out.println(ans.getX()+" "+ans.getY());
	}
    }
}
