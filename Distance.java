import java.lang.Math;
class Distance{
    Distance(){
    }
    double pTop(Point a,Point b){
        double dis1,dis2;
	dis1=a.getX()-b.getX();
	dis1*=dis1;           //
	dis2=a.getY()-b.getY();
	dis2*=dis2;
	return Math.sqrt(dis1+dis2);
    }
}
