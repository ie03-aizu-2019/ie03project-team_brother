import java.util.ArrayList;

class Line{
    private Point start, end;
    private ArrayList<Point> point = new ArrayList<Point>();
    private int updown = 0;

    Line(Point st, Point ed){
	this.start = st;
	this.end = ed;
	
	point.add(this.start);
	point.add(this.end);

	if(this.start.getX() < this.end.getX());
	
	else if(this.start.getX() > this.end.getX()){
	    point.remove(this.start);
	    point.add(this.start);
	}

	else if(this.start.getY() > this.end.getY()){
	    updown = -1;
	}

	else if(this.start.getY() < this.end.getY()){
	    updown = 1;
	}
    }

    public Point getStart(){
	return start;
    }

    public Point getEnd(){
	return end;
    }

    public void insert(Point p){
	for(int i=0; i<point.size()-1; i++){
	    if((point.get(i).getX() < p.getX())
	       && (point.get(i+1).getX() > p.getX())){
		point.add(i+1,p);
		break;
	    }
	}
	
	for(int i=0; i<point.size()-1; i++){
	    // down
	    if(updown==-1){
		if((point.get(i).getY() > p.getY())
		   && (point.get(i+1).getY() < p.getY())){
		    point.add(i+1,p);
		    break;
		}
	    }
	    
	    // up
	    else if(updown==1){
		if((point.get(i).getY() < p.getY())
		   && (point.get(i+1).getY() > p.getY())){
		    point.add(i+1,p);
		    break;
		}
	    }
	}
    }

    public Point getList(int n){
	if(n>=0 && n<point.size())
	    return point.get(n);

	else
	    return new Point(-1, -1);
    }

    public int getSize(){
	return point.size();
    }

    public void setID(Point p){
	double EPS = 0.00000001;

	for(int i=0; i<point.size(); i++){
	    if(-EPS <= point.get(i).getX()-p.getX() &&
	       point.get(i).getX()-p.getX() <= EPS){
		if(-EPS <= point.get(i).getY()-p.getY() &&
		   point.get(i).getY()-p.getY() <= EPS){
		    point.get(i).setID(p.getID());
		    return;
		}
	    }
	}
	
    }
    
}
