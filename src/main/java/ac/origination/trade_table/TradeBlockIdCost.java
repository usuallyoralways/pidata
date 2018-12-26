package ac.origination.trade_table;

import ac.common.BlockId;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

public class TradeBlockIdCost extends BlockId <Double>{
    static int sum[]=new int[2000];
    static void initSum(){
        for (int i=0;i<2000;i++) {
            sum[i]=0;
        }
    }

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
        int i=blockId;
        int ret;
        do {
            i++;
            ret= (i)%(Parameters.COST_LASTID+1);
        }
        while (sum[ret]==0);
        return ret;
    }

    @Override
    public int beiShu(Double value) {
        int bid=blockIdFunction(value);
        Double fistValue = getFistValueInId(bid);
        if (value.equals(fistValue))
            return 0;
        return (int) ((value - fistValue) / Parameters.COST_ACC);
    }

    @Override
    public int getLastBlockId(int blockId) {
        int ret;
        int i=blockId;
        do {
            i--;
            ret= (i-1+2*Parameters.COST_LASTID+2)%(Parameters.COST_LASTID+1);
        }while (sum[ret]==0);

        return ret;
    }

    @Override
    public Double getFistValueInId(int blockId){
        
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
