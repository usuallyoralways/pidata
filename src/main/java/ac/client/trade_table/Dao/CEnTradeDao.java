package ac.client.trade_table.Dao;

import ac.client.QueryInput;
import ac.client.trade_table.Dao.Model.EnTrade;
import ac.client.trade_table.Query;
import ac.client.trade_table.TradeBlockIdCost;
import ac.client.trade_table.TradeQueryInputId;
import ac.common.DAO.Dao;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CEnTradeDao extends Dao {
    public void initDatabase() {
    }


    public List<EnTrade> getListById(String id, int value) {

        String sql = "select *  from t_te_entrypt where " + id + "=?";
        if (id.equals("a_blockid")) {
            sql += " order by a_hashvalue asc;";
        } else if (id.equals("b_blockid")) {
            sql += " order by b_hashvalue asc;";
        }

        Object[] params = new Object[]{value};
        final List<EnTrade> enTrades = new ArrayList<>();
        getJdbcTemplate().query(sql, params, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                EnTrade enTrade = new EnTrade();
                enTrade.setA(rs.getString("a"));
                enTrade.setA_blockid(rs.getInt("a_blockid"));
                enTrade.setA_hashvalue(rs.getInt("a_hashvalue"));
                enTrade.setB(rs.getString("b"));
                enTrade.setB_blockid(rs.getInt("b_blockid"));
                enTrade.setB_hashvalue(rs.getInt("b_hashvalue"));
                enTrade.setC(rs.getString("c"));
                enTrade.setA_flag(rs.getBoolean("a_flag"));
                enTrade.setA_hashpoint(rs.getInt("a_hashpoint"));
                enTrade.setB_hashpoint(rs.getInt("b_hashpoint"));
                enTrades.add(enTrade);
            }
        });
        return enTrades;
    }

    public List<EnTrade> getListById(String id, final List<Integer> values) {


        final List<EnTrade> enTrades = new ArrayList<>();

        String sqlsql = "select *  from t_te_entrypt where ";
        for (Integer item : values) {
            String sql = id + "=" + item + " or ";
            sqlsql += sql;
        }
        sqlsql += " false ";
        if (id.equals("a_blockid")) {
            sqlsql += " order by a_blockid,a_hashvalue asc;";
        } else if (id.equals("b_blockid")) {
            sqlsql += " order by b_blockid, b_hashvalue asc;";
        }

        getJdbcTemplate().query(sqlsql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                EnTrade enTrade = new EnTrade();
                enTrade.setA(rs.getString("a"));
                enTrade.setA_blockid(rs.getInt("a_blockid"));
                enTrade.setA_hashvalue(rs.getInt("a_hashvalue"));
                enTrade.setB(rs.getString("b"));
                enTrade.setB_blockid(rs.getInt("b_blockid"));
                enTrade.setB_hashvalue(rs.getInt("b_hashvalue"));
                enTrade.setC(rs.getString("c"));
                enTrade.setA_flag(rs.getBoolean("a_flag"));
                enTrade.setA_hashpoint(rs.getInt("a_hashpoint"));
                enTrade.setB_hashpoint(rs.getInt("b_hashpoint"));
                enTrades.add(enTrade);
            }

        });
        return enTrades;

    }

    public List<EnTrade> query(QueryInput tradeQueryInput,String id) {
        //中间部分
        int timequeryquery=0;
        int timequeryedge=0;


        long timetime1=System.currentTimeMillis();
        List<EnTrade> enTradeList = new ArrayList<>();
        List<EnTrade> temp = getListById(id, tradeQueryInput.getIds());
        enTradeList.addAll(temp);
        long timetime2=System.currentTimeMillis();
        timequeryquery+=timetime2-timetime1;


        if (tradeQueryInput.getFistId() != tradeQueryInput.getLastId()) {
            //左边
            timetime1=System.currentTimeMillis();
            List<EnTrade> zuobian = getListById(id, tradeQueryInput.getFistId());
            timetime2=System.currentTimeMillis();
            timequeryedge+=timetime2-timetime1;
            int sum = 0;
            int flag = -1;
            if (zuobian.size() > 0) {
                for (int i = 0; i < zuobian.size(); i++) {
                    int hash = zuobian.get(i).getA_hashvalue() - sum;
                    sum = zuobian.get(i).getA_hashvalue();
                    if (zuobian.get(i).getA().equals("ThisIsAFalseData")) {
                        if (hash > tradeQueryInput.getFirstHashBS()) {
                            break;
                        } else {
                            flag = i;
                        }
                    } else {
                        if (hash == tradeQueryInput.getFirstHash()) {
                            flag = i;
                            break;
                        }
                    }
                }
            }
            if (flag != -1) {
                EnTrade temp1 = zuobian.get(flag);
                while (temp1.getA().equals("ThisIsAFalseData")) {
                    flag++;
                    temp1 = zuobian.get(flag);
                }



                List<EnTrade> tempenTrades= new ArrayList<>();
                int flag2=flag;
                for (int i = flag + 1; i < zuobian.size(); i++) {
                    EnTrade temp2 = zuobian.get(i);
                    if (!temp2.getA().equals("ThisIsAFalseData")) {
                        temp2.setA_hashpoint(zuobian.get(flag2).getA_hashpoint());
                        tempenTrades.add(temp2);
                        flag2=(i+1)%(zuobian.size());
                    }
                }
                tempenTrades.get(0).setA_hashpoint(zuobian.get(flag2).getA_hashpoint());
                enTradeList.addAll(tempenTrades);
                timetime1=System.currentTimeMillis();
                timequeryedge+=timetime1-timetime2;
                System.out.println(tempenTrades.size());

            }

            //右边
            timetime1=System.currentTimeMillis();
            List<EnTrade> youbian = getListById(id, tradeQueryInput.getLastId());
            timetime2=System.currentTimeMillis();
            timequeryquery+=timetime2-timetime1;

            if (youbian.size()>0) {
                int sumy = 0;
                int flagy = 0;
                if (enTradeList.size() > 0) {
                    for (int i = 0; i < youbian.size(); i++) {
                        flagy = i;
                        int hash = youbian.get(i).getA_hashvalue() - sumy;
                        sumy = youbian.get(i).getA_hashvalue();
                        if (youbian.get(i).getA().equals("ThisIsAFalseData")) {
                            if (hash > tradeQueryInput.getFirstHashBS()) {
                                break;
                            }
                        } else {
                            if (hash == tradeQueryInput.getFirstHash()) {
                                flag = i;
                                break;
                            }
                        }
                    }
                }

                int j = flagy;
                EnTrade temp1 = youbian.get(j);
                while (temp1.getA().equals("ThisIsAFalseData")) {
                    j--;
                    temp1 = youbian.get(j);
                }

                int flag4 = 1;
                List<EnTrade> mid = new ArrayList<>();
                for (int i = 0; i <= j; i++) {
                    EnTrade temp2 = youbian.get(i);
                    if (!temp2.getA().equals("ThisIsAFalseData")) {
                        temp2.setA_hashpoint(youbian.get(flag4).getA_hashpoint());
                        flag4 = (i + 1) % youbian.size();
                    }
                }
                if (mid.size() > 0) {
                    mid.get(0).setA_hashpoint(youbian.get(flag4).getA_hashpoint());
                }
                enTradeList.addAll(mid);
                System.out.println("右边："+youbian);
                System.out.println(mid);
            }
            timetime1=System.currentTimeMillis();
            timequeryedge+=timetime1-timetime2;

        }
        else {
            timetime1=System.currentTimeMillis();
            List<EnTrade> zhong = getListById(id, tradeQueryInput.getLastId());
            timetime2=System.currentTimeMillis();
            timequeryquery+=timetime2-timetime1;

            if (zhong.size()>0) {
                int sum = 0;
                int flagl = 0;
                int flagr = 0;
                if (enTradeList.size() > 0) {
                    for (int i = 0; i < zhong.size(); i++) {
                        int hash = zhong.get(i).getA_hashvalue() - sum;
                        sum = zhong.get(i).getA_hashvalue();
                        if (zhong.get(i).getA().equals("ThisIsAFalseData")) {
                            if (hash > tradeQueryInput.getFirstHashBS()) {
                                break;
                            } else {
                                flagl = i;
                            }
                        } else {
                            if (hash == tradeQueryInput.getFirstHash()) {
                                flagl = i;
                                break;
                            }
                        }
                    }
                }

                if (enTradeList.size() > 0) {
                    for (int i = 0; i < zhong.size(); i++) {
                        flagr = i;
                        int hash = enTradeList.get(i).getA_hashvalue() - sum;
                        sum = enTradeList.get(i).getA_hashvalue();
                        if (enTradeList.get(i).getA().equals("ThisIsAFalseData")) {
                            if (hash > tradeQueryInput.getFirstHashBS()) {
                                break;
                            }
                        } else {
                            if (hash == tradeQueryInput.getFirstHash()) {
                                break;
                            }
                        }
                    }
                }

                int i = flagl;
                EnTrade temp5 = zhong.get(i);
                while (temp5.getA().equals("ThisIsAFalseData")) {
                    i++;
                    temp5 = zhong.get(i);
                }
                int j = flagr;
                EnTrade temp6 = zhong.get(j);
                while (temp5.getA().equals("ThisIsAFalseData")) {
                    j--;
                }
                while (i > j) {
                    return enTradeList;
                }
                int flag = 0;
                List<EnTrade> trades = new ArrayList<>();
                for (int k = i; k <= j; k++) {
                    EnTrade item = zhong.get(k);
                    if (!item.getA().equals("ThisIsAFalseData")) {
                        item.setA_hashpoint(zhong.get(flag).getA_hashpoint());
                        flag = (k + 1) % zhong.size();
                        trades.add(item);
                    }
                }
                if (trades.size() > 0)
                    trades.get(0).setA_hashpoint(zhong.get(flag).getA_hashpoint());
                timetime1=System.currentTimeMillis();
                timequeryedge+=System.currentTimeMillis();
                enTradeList.addAll(trades);
            }



        }

        System.out.println("query time:"+timequeryquery);
        System.out.println("process time:"+timequeryedge);
        return enTradeList;
    }

    public List<EnTrade> queryB(QueryInput tradeQueryInput,String id) {
        //????

        int timequeryquery=0;
        int timequeryedge=0;
        long timetime1=System.currentTimeMillis();
        List<EnTrade> enTradeList = new ArrayList<>();

        System.out.println(tradeQueryInput.getIds().size()+"sizizis");
        List<EnTrade> temp = getListById(id, tradeQueryInput.getIds());
        long timetime2=System.currentTimeMillis();
        timequeryquery+=timetime2-timetime1;
        if(temp.size()>0) {
            int block = temp.get(0).getB_blockid();
            int fistpoint=temp.get(0).getB_hashpoint();
            List<EnTrade> tempenTrades=new ArrayList<>();
            for (int i = 0; i < temp.size(); i++) {

                int tempblock=temp.get(i).getB_blockid();
                if (tempblock==block){
                    tempenTrades.add(temp.get(i));
                }else {
                    for (int j = 0; j < tempenTrades.size(); j++) {
                        if (!tempenTrades.get(j).getB().equals("ThisIsBFalseData")) {
                            tempenTrades.get(j).setB_hashpoint(fistpoint);
                            fistpoint = tempenTrades.get((j + 1) % tempenTrades.size()).getB_hashpoint();
                            enTradeList.add(tempenTrades.get(j));
                        }
                    }
                    block=tempblock;
                    tempenTrades.clear();
                }
            }
            System.out.println(enTradeList.size());

        }
        timetime1=System.currentTimeMillis();
        timequeryedge+=timetime1-timetime2;



        if (tradeQueryInput.getFistId() != tradeQueryInput.getLastId()) {
            //??
            timetime1=System.currentTimeMillis();
            List<EnTrade> zuobian = getListById(id, tradeQueryInput.getFistId());
            timetime2=System.currentTimeMillis();
            timequeryedge+=timetime2-timetime1;
            int sum = 0;
            int flag = -1;
            if (zuobian.size() > 0) {
                for (int i = 0; i < zuobian.size(); i++) {
                    int hash = zuobian.get(i).getA_hashvalue() - sum;
                    sum = zuobian.get(i).getA_hashvalue();
                    if (zuobian.get(i).getB().equals("ThisIsBFalseData")) {
                        if (hash > tradeQueryInput.getFirstHashBS()) {
                            break;
                        } else {
                            flag = i;
                        }
                    } else {
                        if (hash == tradeQueryInput.getFirstHash()) {
                            flag = i;
                            break;
                        }
                    }
                }
            }
            if (flag != -1) {
                EnTrade temp1 = zuobian.get(flag);
                while (temp1.getB().equals("ThisIsBFalseData")) {
                    flag++;
                    temp1 = zuobian.get(flag);
                }
                List<EnTrade> tempenTrades= new ArrayList<>();
                int flag2=flag;
                for (int i = flag + 1; i < zuobian.size(); i++) {
                    EnTrade temp2 = zuobian.get(i);
                    if (!temp2.getB().equals("ThisIsBFalseData")) {
                        temp2.setB_hashpoint(zuobian.get(flag2).getB_hashpoint());
                        tempenTrades.add(temp2);
                        flag2=(i+1)%(zuobian.size());
                    }
                }
                if (tempenTrades.size()>0) {
                    tempenTrades.get(0).setB_hashpoint(zuobian.get(flag2).getB_hashpoint());
                    enTradeList.addAll(tempenTrades);
                    timetime1 = System.currentTimeMillis();
                    timequeryedge += timetime1 - timetime2;
                    System.out.println(tempenTrades.size());
                }
            }
            //??
            timetime1=System.currentTimeMillis();
            List<EnTrade> youbian = getListById(id, tradeQueryInput.getLastId());
            timetime2=System.currentTimeMillis();
            timequeryquery+=timetime2-timetime1;
            if (youbian.size()>0) {
                int sumy = 0;
                int flagy = 0;
                if (enTradeList.size() > 0) {
                    for (int i = 0; i < youbian.size(); i++) {
                        flagy = i;
                        int hash = youbian.get(i).getA_hashvalue() - sumy;
                        sumy = youbian.get(i).getA_hashvalue();
                        if (youbian.get(i).getB().equals("ThisIsBFalseData")) {
                            if (hash > tradeQueryInput.getFirstHashBS()) {
                                break;
                            }
                        } else {
                            if (hash == tradeQueryInput.getFirstHash()) {
                                flag = i;
                                break;
                            }
                        }
                    }
                }
                int j = flagy;
                EnTrade temp1 = youbian.get(j);
                while (temp1.getB().equals("ThisIsBFalseData")) {
                    j--;
                    if (j<0){
                        break;
                    }
                    temp1 = youbian.get(j);

                }
                int flag4 = 1;
                List<EnTrade> mid = new ArrayList<>();
                for (int i = 0; i <= j; i++) {
                    EnTrade temp2 = youbian.get(i);
                    if (!temp2.getB().equals("ThisIsBFalseData")) {
                        temp2.setB_hashpoint(youbian.get(flag4).getB_hashpoint());
                        flag4 = (i + 1) % youbian.size();
                    }
                }
                if (mid.size() > 0) {
                    mid.get(0).setB_hashpoint(youbian.get(flag4).getB_hashpoint());
                }
                enTradeList.addAll(mid);
                System.out.println("???"+youbian);
                System.out.println(mid);
            }
            timetime1=System.currentTimeMillis();
            timequeryedge+=timetime1-timetime2;
        }
        else {
            timetime1=System.currentTimeMillis();
            List<EnTrade> zhong = getListById(id, tradeQueryInput.getLastId());
            timetime2=System.currentTimeMillis();
            timequeryquery+=timetime2-timetime1;
            if (zhong.size()>0) {
                int sum = 0;
                int flagl = 0;
                int flagr = 0;
                if (enTradeList.size() > 0) {
                    for (int i = 0; i < zhong.size(); i++) {
                        int hash = zhong.get(i).getA_hashvalue() - sum;
                        sum = zhong.get(i).getA_hashvalue();
                        if (zhong.get(i).getB().equals("ThisIsBFalseData")) {
                            if (hash > tradeQueryInput.getFirstHashBS()) {
                                break;
                            } else {
                                flagl = i;
                            }
                        } else {
                            if (hash == tradeQueryInput.getFirstHash()) {
                                flagl = i;
                                break;
                            }
                        }
                    }
                }
                if (enTradeList.size() > 0) {
                    for (int i = 0; i < zhong.size(); i++) {
                        flagr = i;
                        int hash = enTradeList.get(i).getA_hashvalue() - sum;
                        sum = enTradeList.get(i).getA_hashvalue();
                        if (enTradeList.get(i).getB().equals("ThisIsBFalseData")) {
                            if (hash > tradeQueryInput.getFirstHashBS()) {
                                break;
                            }
                        } else {
                            if (hash == tradeQueryInput.getFirstHash()) {
                                break;
                            }
                        }
                    }
                }
                int i = flagl;
                EnTrade temp5 = zhong.get(i);
                while (temp5.getB().equals("ThisIsBFalseData")) {
                    i++;
                    temp5 = zhong.get(i);
                }
                int j = flagr;
                EnTrade temp6 = zhong.get(j);
                while (temp5.getB().equals("ThisIsBFalseData")) {
                    j--;
                }
                while (i > j) {
                    return enTradeList;
                }
                int flag = 0;
                List<EnTrade> trades = new ArrayList<>();
                for (int k = i; k <= j; k++) {
                    EnTrade item = zhong.get(k);
                    if (!item.getB().equals("ThisIsBFalseData")) {
                        item.setB_hashpoint(zhong.get(flag).getB_hashpoint());
                        flag = (k + 1) % zhong.size();
                        trades.add(item);
                    }
                }
                if (trades.size() > 0)
                    trades.get(0).setB_hashpoint(zhong.get(flag).getB_hashpoint());
                timetime1=System.currentTimeMillis();
                timequeryedge+=System.currentTimeMillis();
                enTradeList.addAll(trades);
            }
        }
        System.out.println("query time:"+timequeryquery);
        System.out.println("process time:"+timequeryedge);
        return enTradeList;
    }

//    public List<EnTrade> query(TradeBlockIdCost tradeBlockIdCost) {
//        return null;
//    }

    public List<EnTrade> query(TradeQueryInputId tradeQueryInput, TradeBlockIdCost tradeBlockIdCost) {
        return null;
    }

}
