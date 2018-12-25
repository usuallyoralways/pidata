package ac.origination.trade_table.DAO.Model;

import java.util.Date;

//生成数据
public class Trade {
    int tno;
    double cost;
    String date;

    int point;

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

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
