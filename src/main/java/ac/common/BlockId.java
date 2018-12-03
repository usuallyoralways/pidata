package ac.common;

import java.io.Serializable;
import java.util.List;

public class BlockId implements Serializable{
    private static final long serialVersionUID = 8683452581334592189L;
    public int blockIdFunction(String data){return -1;};
    public List<Integer> blockIdGetRange(String left, String right){return null;};
    public int getNextBlockId(int blockId){return -1;};
    public int getLastBlockId(int blockId){return -1;}
    public Object getFistValueInId(int blockId){return null;}
    public Object neasrFlash(Object data){return null;}
    public int beiShu(Object object){return 0;}
}
