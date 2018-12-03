package ac.client.trade_table;

import ac.client.Query;
import ac.client.QueryInput;
import ac.common.HashValue;

import java.util.ArrayList;
import java.util.List;

public class TradeQueryInput extends QueryInput{


    public void setIds() {
        List<Integer> range=this.bd.blockIdGetRange(String.valueOf(leftValue),String.valueOf(rightValue));
        this.ids=new ArrayList();
        if (range.size()>1){
            for (int i = 1; i <range.size()-1 ; i++) {
                ids.add(range.get(i));
            }
        }
    }

    public TradeQueryInput() {
    }

    public TradeQueryInput(Object leftValue, Object rightValue) {
        super(leftValue, rightValue);
    }
}
