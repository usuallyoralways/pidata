package ac.origination.trade_table;

import ac.common.BlockId;

import java.util.List;

public class TradeBlockIdTno extends BlockId<Integer> {

    public static int[] sum=new int[2000];
    public static void suminit(){
        for (int i = 0; i < 2000; i++) {
            sum[i]=0;
        }
    }


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

        int ret;
        int i=blockId;
        do {
            i++;
            ret=(i)%(Parameters.TNO_LASTID+1);
        }
        while (sum[ret]==0);
        return ret;
    }

    @Override
    public int getLastBlockId(int blockId) {
        int ret;
        int i=blockId;
        do {
            i--;
            ret=(i+Parameters.TNO_LASTID+1)%(Parameters.TNO_LASTID+1);
        }
        while (sum[ret]==0);
        return ret;
    }
    public Integer getFistValueInId(int blockId){
        return 10*blockId;
    }

}