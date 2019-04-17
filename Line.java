class Line{
    private Point start, end;

    Line(Point st, Point ed){
	this.start = st;
	this.end = ed;
    }

    public Point getStart(){
	return start;
    }

    public Point getEnd(){
	return end;
    }
}
