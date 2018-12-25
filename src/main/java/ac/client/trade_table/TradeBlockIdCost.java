package ac.client.trade_table;

import ac.common.BlockId;
import ac.origination.trade_table.Parameters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TradeBlockIdCost extends BlockId<Double> implements Serializable{
    private static final long serialVersionUID = 8683452581334592189L;
    @Override
    public int blockIdFunction(Double data) {
        try {
            return (int) ((data - Parameters.COST_MIN) / ((Parameters.COST_MAX - Parameters.COST_MIN) / (double) Parameters.COST_N));
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

    public List<Integer> blockIdGetRange(Double left, Double right){
        List<Integer> results= new ArrayList<>();
        int lid= blockIdFunction(left);
        int rlid=blockIdFunction(right);
        for (int i = lid; i <=rlid ; i++) {
            results.add(i);
        }
        return results;
    }

    public int beiShu(Double object){
        Double fistValue = getFistValueInId(blockIdFunction(object));
        if ((object).equals(fistValue))
            return 0;

        return (int) (((object) - fistValue) / Parameters.COST_ACC);
    }

    @Override
    public Double getFistValueInId(int blockId){
        return blockId*10000.0;
    }

    public Double neasrFlash(Double data){
        return ((int)(data/Parameters.COST_ACC))*Parameters.COST_ACC;
    }

    public static void main(String[] args){
        System.out.println(new TradeBlockIdCost().neasrFlash(34.567));
    }
}
