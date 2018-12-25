package ac.client.trade_table;

import ac.client.QueryInput;



public class TradeQueryInputCost extends QueryInput<Double> {

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
//    public void setFirstHash() {
//        this.firstHash=this.hv.getHashValue(String.valueOf(leftValue));
//    }



//    @Override
//    public void setFirstHashBS() {
//        this.firstHashBS=this.bd.beiShu(leftValue);
//    }

//
//    @Override
//    public void setLastHash() {
//        this.lastHash=this.hv.getHashValue(String.valueOf(rightValue));
//    }

//    @Override
//    public void setLastHashBS() {
//        this.firstHashBS=this.bd.beiShu(rightValue);
//    }

    public TradeQueryInputCost() {
    }

    public TradeQueryInputCost(Double leftValue, Double rightValue) {
        super(leftValue, rightValue);
    }
}
