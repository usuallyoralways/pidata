package ac.common;

import java.io.Serializable;
import java.util.List;

public class BlockId<T> implements Serializable{
    T value;
    private static final long serialVersionUID = 8683452581334592189L;
    public int blockIdFunction(T data){return -1;}
    public List<Integer> blockIdGetRange(T left, T right){return null;}

    public int getNextBlockId(int blockId){return -1;}
    public int getLastBlockId(int blockId){return -1;}
    public T getFistValueInId(int blockId){return null;}
    public T neasrFlash(T data){return null;}
    public int beiShu(T object){return 0;}

    public Object neasrFlash(){return null;}

    public T getAcc(int value){
        return null;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getLeftLimit(int blockId){
        return null;
    }
    public T getRightLimit(int blockId){
        return null;
    }
    public int beiShu(T object,int id){
        return 0;
    }
}
