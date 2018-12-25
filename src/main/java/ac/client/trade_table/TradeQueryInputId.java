package ac.client.trade_table;

import ac.client.QueryInput;

public class TradeQueryInputId extends QueryInput<Integer>{


    @Override
    public void setFirstHash() {
        this.firstHash=this.hv.getHashValue(String.valueOf(leftValue));
    }


    public void init(){
        setRange();
        setIds();
        setFirstHash();
        setFirstHashBS();
        setFirstId();
        setLastId();
        setLastHash();
        setLastHashBS();
    }

//    @Override
//    public void setFirstHashBS() {
//        this.firstHashBS=this.bd.beiShu(leftValue);
//
//    }

    @Override
    public void setLastHash() {
        this.lastHash=this.hv.getHashValue(String.valueOf(rightValue));
    }

    @Override
    public void setLastHashBS() {
        this.lastHashBS=this.bd.beiShu(rightValue);
    }

    public TradeQueryInputId() {
    }

    public TradeQueryInputId(Integer leftValue, Integer rightValue) {
        super(leftValue, rightValue);
    }
}
