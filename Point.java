class Point{
    private double x, y;
    private int id;
    
    Point(double x, double y){
	this.x = x;
	this.y = y;
    }
    
    public double getX(){
	return x;
    }
    
    public double getY(){
	return y;
    }

    public void setID(int id){
	this.id = id;
    }

    public int getID(){
	return id;
    }

    public void add(Point pt){
	this.x += pt.getX();
	this.y += pt.getY();
    }

    public void sub(Point pt){
	this.x -= pt.getX();
	this.y -= pt.getY();
    }
}
