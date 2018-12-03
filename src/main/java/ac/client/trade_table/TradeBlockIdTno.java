package ac.client.trade_table;

import ac.common.BlockId;
import ac.origination.trade_table.Parameters;

import java.util.ArrayList;
import java.util.List;

public class TradeBlockIdTno extends BlockId {

    public int blockIdFunction(String data) {
        try {

            return (int) ((Double.valueOf(data) - Parameters.TNO_MIN) / ((Parameters.TNO_MAX - Parameters.TNO_MIN) / (double) Parameters.TNO_N));
        }catch (NumberFormatException e){
            System.out.println(e.toString());
            return -1;
        }
    }

    public List<Integer> blockIdGetRange(String left, String right){
        List<Integer> results= new ArrayList<>();
        int lid= blockIdFunction(left);
        int rlid=blockIdFunction(right);
        for (int i = lid; i <rlid ; i++) {
            results.add(i);
        }
        return results;
    };

    @Override
    public int getNextBlockId(int blockId) {
        if (blockId==Parameters.TNO_LASTID)
            return super.getNextBlockId(blockId);
        else
            return blockId+1;
    }

    @Override
    public int getLastBlockId(int blockId) {
        if (blockId==Parameters.TNO_FIRSTID)
            return super.getNextBlockId(blockId);
        else
            return blockId-1;
    }
    public Object getFistValueInId(int blockId){
        return 10*blockId;
    }

}