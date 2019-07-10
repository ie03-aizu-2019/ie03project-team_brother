import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Set;
class AdjacencyList{
    /*adlist has Map(Index,distance)*/
    private Map<Integer,Double> adlist;
    AdjacencyList(){
	adlist = new HashMap<Integer,Double>();
    }
    public void put(int key,double d){
	adlist.put((Integer)key,(Double)d);
    }
    public double get(int key){
	double i=adlist.get((Integer)key);
	if((Double)i!=null){
	    return (double)i;
	}else{
	    return -1.0;
	}
    }
    public boolean remove(int key){
	double i=adlist.remove((Integer)key);
	if((Double)i!=null){
	    return true;
	}else{
	    return false;
	}
    }
    public boolean containsKey(int key){
	return adlist.containsKey((Integer)key);
    }
    public HashMap deepcopy(){
	return new HashMap<Integer,Double>(adlist);
    }
    public Set<Integer> keySet(){
	return adlist.keySet();
    }
    public Set<Entry<Integer,Double>> entrySet(){
	return adlist.entrySet();
    }//usage(blog): https://qiita.com/kei2100/items/0ce97733c92fdcb9c5a9
 
}
