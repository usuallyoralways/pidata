package ac.origination.trade_table;

import ac.common.BlockId;

import java.util.List;

public class TradeBlockIdTno extends BlockId<Integer> {


    public int blockIdFunction(Integer data) {
        try {

            return (int) ((data - Parameters.TNO_MIN) / ((Parameters.TNO_MAX - Parameters.TNO_MIN) /  Parameters.TNO_N));
        }catch (NumberFormatException e){
            System.out.println(e.toString());
            return -1;
        }
    }

    public List<Integer> blockIdGetRange(Integer left, Integer right){return null;};

    @Override
    public int getNextBlockId(int blockId) {
        return (blockId+1)%(Parameters.TNO_LASTID+1);
    }

    @Override
    public int getLastBlockId(int blockId) {
        return (blockId-1+Parameters.TNO_LASTID+1)%(Parameters.TNO_LASTID+1);
    }
    public Integer getFistValueInId(int blockId){
        return 10*blockId;
    }

}