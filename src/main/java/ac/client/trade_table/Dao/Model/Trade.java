package ac.client.trade_table.Dao.Model;

import java.util.Date;

//生成数据
public class Trade {
    int tno;
    double cost;
    String date;


    public Trade(){

    }

    public Trade(int tno, double cost, String date) {
        this.tno = tno;
        this.cost = cost;
        this.date = date;
    }

    public int getTno() {
        return tno;
    }

    public void setTno(int tno) {
        this.tno = tno;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "tno=" + tno +
                ", cost=" + cost +
                ", date=" + date +
                '}';
    }
}
