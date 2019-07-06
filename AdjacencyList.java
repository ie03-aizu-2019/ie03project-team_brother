import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Set;
class AdjacencyList{
    /*adlist has Map(Index,distance)*/
    private Map<Integer,Integer> adlist;
    AdjacencyList(){
	adlist = new HashMap<Integer,Integer>();
    }
    public void put(int key,int d){
	adlist.put((Integer)key,(Integer)d);
    }
    public int get(int key){
	int i=adlist.get((Integer)key);
	if((Integer)i!=null){
	    return (int)i;
	}else{
	    return -1;
	}
    }
    public boolean remove(int key){
	int i=adlist.remove((Integer)key);
	if((Integer)i!=null){
	    return true;
	}else{
	    return false;
	}
    }
    public boolean containsKey(int key){
	return adlist.containsKey((Integer)key);
    }
    public HashMap deepcopy(){
	return new HashMap<Integer,Integer>(adlist);
    }
    public Set<Integer> keySet(){
	return adlist.keySet();
    }
    public Set<Entry<Integer,Integer>> entrySet(){
	return adlist.entrySet();
    }//usage(blog): https://qiita.com/kei2100/items/0ce97733c92fdcb9c5a9
 
}
