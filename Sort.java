public class Sort{
    Sort(){};
    Point[] shellSort(Point[] p,int cnt){
	int i,j,sub;
	Point hzp;
	sub=cnt/2;// 距離を保存
	while(sub!=0){
	    for(i=1;i <= cnt;i++){
		j=i;
		hzp=p[i];
		while(j >= sub+1){
		    if(p[j-sub].getX()<hzp.getX() ||
		       p[j-sub].getX()==hzp.getX() &&
		       p[j-sub].getY()<hzp.getY()) break;//交換する条件(p[j-sub]と対象hzpを比べる)
		    p[j]=p[j-sub];
		    j-=sub;
		}
		p[j]=hzp;
	    }
	    if(sub/2!=0){
		sub/=2;//距離を縮める
	    }else if(sub==1){
		sub = 0;
	    }else{
		sub = 1;
	    }
	}
	return p;
    }
}
