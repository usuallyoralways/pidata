package ac.origination.trade_table;

import ac.common.BlockId;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

public class TradeBlockIdCost extends BlockId {

    @Override
    public int blockIdFunction(String data) {
        try {
            return (int) ((Double.valueOf(data) - Parameters.COST_MIN) / ((Parameters.COST_MAX - Parameters.COST_MIN) / (double) Parameters.COST_N));
        }catch (NumberFormatException e){
            System.out.println(e.toString());
            return -1;
        }
    }

    @Override
    public int getNextBlockId(int blockId) {
        if (blockId==Parameters.COST_LASTID)
            return super.getNextBlockId(blockId);
        else
            return blockId+1;
    }

    @Override
    public int getLastBlockId(int blockId) {
        if (blockId==Parameters.COST_FIRSTID)
            return super.getNextBlockId(blockId);
        else
            return blockId-1;
    }

    @Override
    public Object getFistValueInId(int blockId){
        
        return (blockId*Parameters.COST_MAX/(Parameters.COST_N)*1.0);
    }

    public Double neasrFlash(Double data){
        return ((int)(data/Parameters.COST_ACC))*Parameters.COST_ACC;
    }

    public static void main(String[] args){
        System.out.println(new TradeBlockIdCost().neasrFlash(34.567));
        System.out.println(new TradeBlockIdCost().getFistValueInId(3));
        System.out.println(new TradeBlockIdCost().getFistValueInId(4));
        int a=0;
        int b=1,c=3;
        a+=b-c;
        System.out.println(a);

    }
}
