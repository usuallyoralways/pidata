package ac.origination;


import ac.common.BlockId;
import ac.common.HashValue;

/**
 * Created by lijun on 2018/10/6.
 */
public class EnItem extends EnItemString{
    int blockId;
    int hashValue;

    BlockId bd;
    HashValue hv;



    public void setEnItem(String data){
        super.setEnItem(data);
        blockId=bd.blockIdFunction(data);
//        hashValue= hv.getHashValue(data);
    }

    public void setBd(BlockId bd) {
        this.bd = bd;
    }

    public void setHv(HashValue hv) {
        this.hv = hv;
    }

    public int getBlockId() {
        blockId=bd.blockIdFunction(data);
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getHashValue() {
        return hashValue;
    }

    public void setHashValue(int hashValue) {
        this.hashValue = hashValue;
    }

    @Override
    public String toString() {
        return super.toString()+" BlockId:"+String.valueOf(blockId)+" HashValue:"+String.valueOf(hashValue);
    }

}
