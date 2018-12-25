package ac.client;

import ac.client.trade_table.HMacMD5;
import ac.client.trade_table.TradeBlockIdCost;
import ac.client.trade_table.TradeQueryInputId;
import ac.common.BlockId;
import ac.common.HashValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QueryInput<T> implements Serializable  {

    private static final long serialVersionUID = 8683452581334592189L;

    protected T leftValue;
    protected T rightValue;
    protected List<Integer> ids;
    protected int fistId;
    protected int firstHash;
    protected int firstHashBS;
    protected int lastId;
    protected int lastHash;
    protected int lastHashBS;
    protected HashValue hv;
    protected BlockId bd;
    List<Integer> range;


    public List<Integer> getRange() {
        return range;
    }

    public void setRange(){
        range=this.bd.blockIdGetRange(leftValue,rightValue);
    }

    public void setRange(List<Integer> range) {
        this.range = this.bd.blockIdGetRange(String.valueOf(leftValue),String.valueOf(rightValue));
    }

    public BlockId getBd() {
        return bd;
    }

    public void setBd(BlockId bd) {
        this.bd = bd;
    }

    public HashValue getHv() {
        return hv;
    }

    public void setHv(HashValue hv) {
        this.hv = hv;
    }

    public T getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(T leftValue) {
        this.leftValue = leftValue;
    }

    public void setLeftValue() {
        ;
    }

    public T getRightValue() {
        return rightValue;
    }

    public void setRightValue(T rightValue) {
        this.rightValue = rightValue;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public int getFistId() {
        return fistId;
    }

    public void setFistId(int fistId) {
        this.fistId = fistId;
    }

    public int getFirstHash() {
        return firstHash;
    }

    public void setFirstHash(int firstHash) {
        this.firstHash = firstHash;
    }

    public int getFirstHashBS() {
        return firstHashBS;
    }

    public void setFirstHashBS(int firstHashBS) {
        this.firstHashBS = firstHashBS;
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public int getLastHash() {
        return lastHash;
    }

    public void setLastHash(int lastHash) {
        this.lastHash = lastHash;
    }

    public int getLastHashBS() {
        return lastHashBS;
    }

    public void setLastHashBS(int lastHashBS) {
        this.lastHashBS = lastHashBS;
    }



    public void setIds() {

        this.ids=new ArrayList();
        if (range.size()>1){
            for (int i = 1; i <range.size()-1 ; i++) {
                ids.add(range.get(i));
            }
        }
    }

    //在输入leftvalue 和rightvalue 和bd 和hv后得到
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

    @Override
    public String toString() {
        return "QueryInput{" +
                "leftValue=" + leftValue +
                ", rightValue=" + rightValue +
                ", fistId=" + fistId +
                ", firstHash=" + firstHash +
                ", firstHashBS=" + firstHashBS +
                ", lastId=" + lastId +
                ", lastHash=" + lastHash +
                ", lastHashBS=" + lastHashBS +
                ", \nsrange=" + range +
                ", \nids=" + ids +
                '}';
    }

    public void setFirstId() {
        this.fistId =this.range.get(0);
    }

    public void setFirstHash() {

        this.firstHash = hv.getHashValue(String.valueOf(leftValue));
    }


    public void setFirstHashBS() {

        this.firstHashBS=bd.beiShu(leftValue);
    }


    public void setLastId() {

        this.lastId = this.range.get(this.range.size()-1);
    }


    public void setLastHash() {

        this.lastHash = hv.getHashValue(String.valueOf(rightValue));

    }

    public QueryInput(){}

    public QueryInput(T leftValue, T rightValue){
        this.leftValue=leftValue;
        this.rightValue=rightValue;
    }


    public void setLastHashBS() {

        this.lastHashBS = bd.beiShu(rightValue);
    }

    public static void main(String[] args){
        QueryInput qi= new TradeQueryInputId(1,80000);
        HashValue hv= new HMacMD5();
        BlockId bd= new TradeBlockIdCost();
        qi.setHv(hv);
        qi.setBd(bd);
        qi.init();
        System.out.println(qi);

    }

}
