class Intersection{
    private double EPS = 0.00000001;
    
    public Point calc(Line l1, Line l2){
	double det;
	Point p1, p2, q1 ,q2;
	double s, t;
	double x=-1, y=-1;

	p1 = l1.getStart();
	q1 = l1.getEnd();
	p2 = l2.getStart();
	q2 = l2.getEnd();

	// determination
	det = (q1.getX() - p1.getX())*(p2.getY() - q2.getY())
	    + (q2.getX() - p2.getX())*(q1.getY() - p1.getY());

	// step1
	if(-EPS<=det && det<=EPS);

	else{
	    // step2
	    s = (  (p2.getY() - q2.getY())*(p2.getX() - p1.getX())
		  +(q2.getX() - p2.getX())*(p2.getY() - p1.getY()) )/det;
	    
	    t = (  (p1.getY() - q1.getY())*(p2.getX() - p1.getX())
		  +(q1.getX() - p1.getX())*(p2.getY() - p1.getY()) )/det;
	    
	    // step3
	    if((0<s && s<1) && (0<t && t<1)){
		
		
		// step4
		x = p1.getX() + s*(q1.getX() - p1.getX());
		y = p1.getY() + s*(q1.getY() - p1.getY());
	    }
	}
	
	return new Point(x, y);
    }
}
