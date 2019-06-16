import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;

class Range{
    private int value;
    private int max, min;

    Range(int value){
	this.value = value;
    }

    public int getValue(){
	return value;
    }

    public void setRange(int min, int max){
	this.min = min;
	this.max = max;
    }

    public int getMax(){
	return max;
    }

    public boolean isValid(){
	return min <= value && value <= max;
    }
}

class Pair{
    private int d1, d2;

    Pair(int d1, int d2){
	this.d1 = d1;
	this.d2 = d2;
    }

    public int get1(){
	return d1;
    }

    public int get2(){
	return d2;
    }

    public boolean isEqualN(Pair p){
	return this.d1 == p.get1() && d2 == p.get2();
    }

    public boolean isEqualM(Pair p){
	return (this.d1 == p.get1() && d2 == p.get2())
	    || (this.d1 == p.get2() && d2 == p.get1());
    }
}

class Trio{
    private int d1, d2, d3;

    Trio(int d1, int d2, int d3){
	this.d1 = d1;
	this.d2 = d2;
	this.d3 = d3;
    }

    public int get1(){
	return d1;
    }
    
    public int get2(){
	return d2;
    }
    
    public int get3(){
	return d3;
    }

    public boolean isEqual(Trio t){
	return d1==t.get1() && d2==t.get2() && d3==t.get3();
    }
}

class LimitRandom{
    int size;
    ArrayList<Integer> array = new ArrayList<Integer>();

    public LimitRandom(int size){
	this.size = size;
	
	if(size > 1){
	    for(int i=0; i<size; i++){
		array.add(i+1);
	    }
	}
    }

    public int getRandom(){
	Random rnd = new Random();
	int randomValue = rnd.nextInt(array.size());
	int returnValue = array.remove(randomValue);

	if(array.size()==0){
	    reset();
	}
	
	return returnValue;
    }

    private void reset(){
	if(size > 1){
	    for(int i=0; i<size; i++){
		array.add(i+1);
	    }
	}
    }
}

class Generator{
    static Random rnd =new Random();
    static ArrayList<Pair> pt = new ArrayList<Pair>();
    static ArrayList<Pair> ln = new ArrayList<Pair>();
    static ArrayList<Pair> pin = new ArrayList<Pair>();
    static ArrayList<Trio> qin = new ArrayList<Trio>();
    static Range N, M, P, Q;
    static int XYmax;

    private static void init(int task){
	Scanner sc = new Scanner(System.in);
	
	N = new Range(sc.nextInt());
	M = new Range(sc.nextInt());
	P = new Range(sc.nextInt());
	Q = new Range(sc.nextInt());

	ranges(task);
    }
    
    private static void ranges(int task){
	switch(task){
	case 1:
	    N.setRange(3,4);
	    M.setRange(2,2);
	    P.setRange(0,0);
	    Q.setRange(0,0);
	    XYmax = 1000;
	    break;
	case 2:
	    N.setRange(2,200);
	    M.setRange(1,100);
	    P.setRange(0,0);
	    Q.setRange(0,0);
	    XYmax = 1000;
	    break;
	case 3:
	case 4:
	    N.setRange(2,1000);
	    M.setRange(1,500);
	    P.setRange(0,0);
	    Q.setRange(0,100);
	    XYmax = 10000;
	    break;
	case 5:
	case 6:
	    N.setRange(2,200);
	    M.setRange(1,N.getValue());
	    P.setRange(0,0);
	    Q.setRange(0,100);
	    XYmax = 10000;
	    break;
	case 7:
	    N.setRange(2,1000);
	    M.setRange(1,N.getValue());
	    P.setRange(0,100);
	    Q.setRange(0,0);
	    XYmax = 10000;
	    break;
	case 8:
	    N.setRange(2,2000);
	    M.setRange(1,N.getValue());
	    P.setRange(0,0);
	    Q.setRange(0,0);
	    XYmax = 100000;
	    break;
	default:
	    break;
	}
    }

    private static boolean Condition(int task){
	boolean cond1 = 0<task && task<=8;
	boolean cond2 = N.isValid() && M.isValid() && P.isValid() && Q.isValid();
	boolean cond3 = M.getValue() <= Combination(N.getValue(),2);

	return cond1 && cond2 && cond3;
    }

    private static int Combination(int prev, int next){
	int C=1, dev=1;
	
	for(int i=next; i>0; i--){
	    C *= prev--;
	    dev *= i;
	}

	return C/dev;
    }

    private static int Intersection(ArrayList<Pair> pts, ArrayList<Pair> lns){
	int count=0;
	Point ans;
	Intersection it = new Intersection();
	
	for(int i=0; i<lns.size(); i++){
	    for(int j=i; j<lns.size(); j++){
		ans = it.calc(PairToLine(ln.get(i), pts), PairToLine(ln.get(j), pts));

		if(ans.getX()>=0 || ans.getY()>=0){
		    count++;
		}
	    }
	}
	
	return count;
    }

    public static Line PairToLine(Pair l, ArrayList<Pair> p){
	Point start = new Point(p.get(l.get1()-1).get1(), p.get(l.get1()-1).get2());
	Point end = new Point(p.get(l.get2()-1).get1(), p.get(l.get2()-1).get2());
	
	return new Line(start, end);
    }

    private static void setRandom(int task){
	// N
	for(int i=0; i<N.getValue(); i++){
	    pt.add(new Pair(rnd.nextInt(XYmax+1), rnd.nextInt(XYmax+1)));

	    for(int j=0; j<pt.size()-1; j++){
		if(pt.get(i).isEqualN(pt.get(j))){
		    pt.remove(i--);
		    break;
		}
	    }
	}

	// M
	LimitRandom lrnd = new LimitRandom(M.getValue()*2);

	for(int i=0; i<M.getValue(); i++){
	    if(task==1){
		ln.add(new Pair(lrnd.getRandom(), lrnd.getRandom()));
	    }

	    else{
		ln.add(new Pair(rnd.nextInt(N.getValue())+1, rnd.nextInt(N.getValue())+1));
		
		if(ln.get(i).get1() == ln.get(i).get2()){
		    ln.remove(i--);
		    continue;
		}
		
		for(int j=0; j<ln.size()-1; j++){
		    if(ln.get(i).isEqualM(ln.get(j))
		       || ln.get(i).get1()==ln.get(i).get2()){
			ln.remove(i--);
			break;
		    }
		}
	    }
	}

	// P
	for(int i=0; i<P.getValue(); i++){
	    pin.add(new Pair(rnd.nextInt(XYmax+1), rnd.nextInt(XYmax+1)));

	    if(pin.get(i).get1() == pin.get(i).get2()){
		pin.remove(i--);
		continue;
	    }

	    for(int j=0; j<pin.size()-1; j++){
		if(pin.get(i).isEqualN(pin.get(j))){
		    pin.remove(i--);
		    break;
		}
	    }
	}

	// Q
	for(int i=0; i<Q.getValue(); i++){
	    int k=1;

	    if(!(task == 3 || task == 4))
		k = rnd.nextInt(10)+1;
	    
	    qin.add(new Trio(rnd.nextInt(N.getValue() + Intersection(pt, ln) + 2) + 1,
			     rnd.nextInt(N.getValue() + Intersection(pt, ln) + 2) + 1,
			     k));

	    for(int j=0; j<pin.size()-1; j++){
		if(qin.get(i).isEqual(qin.get(j))){
		    qin.remove(i--);
		    break;
		}
	    }
	}
    }
    
    private  static void output(){
	System.out.println(N.getValue() + " " +
			   M.getValue() + " " +
			   P.getValue() + " " +
			   Q.getValue() + " ");
	
	// N
	for(int i=0; i<pt.size(); i++){
	    System.out.println(pt.get(i).get1()+" "+pt.get(i).get2());
	}

	// M
	for(int i=0; i<ln.size(); i++){
	    System.out.println(ln.get(i).get1()+" "+ln.get(i).get2());
	}

	// P
	for(int i=0; i<pin.size(); i++){
	    System.out.println(pin.get(i).get1()+" "+pin.get(i).get2());
	}

	// Q
	for(int i=0; i<qin.size(); i++){
	    if(qin.get(i).get1()>N.getValue())
		System.out.printf("C%d ",qin.get(i).get1() - N.getValue());
	    else
		System.out.printf("%d ",qin.get(i).get1());
	    
	    if(qin.get(i).get2()>N.getValue())
		System.out.printf("C%d ",qin.get(i).get2() - N.getValue());
	    else
		System.out.printf("%d ",qin.get(i).get2());

	    System.out.printf("%d\n",qin.get(i).get3());
	}
    }

    public static void main(String args[]){
	// input task number
	if(args.length<1)
	    return;
	
	// sellect range
	// input N, M, P, Q
	init(Integer.parseInt(args[0]));

	if(!Condition(Integer.parseInt(args[0])))
	    return;
	
	// generate data
	// check data is correct
	setRandom(Integer.parseInt(args[0]));
	// if not, generate once more(loop)

	// if correct, output data
	output();
    }
}
