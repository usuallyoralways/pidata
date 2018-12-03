package ac.origination.trade_table.DAO;


import ac.common.DAO.Dao;
import ac.origination.trade_table.DAO.Model.EnTrade;
import ac.origination.trade_table.DAO.Model.Trade;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TradeDaoImpl extends Dao {
    public void initDatabase() {

        //初始化数据库
    }

    public void addTrade(Trade trade){
        String sql = "insert into t_trade (cost,date) values(?,?) ";
        Object[] params = {trade.getCost(),trade.getDate()};
        getJdbcTemplate().update(sql, params);
    }

    public List<Trade> getListById(String  id, int value){
        String sql="select * from t_trade where "+id+"=?";
        Object []params=new Object[]{value};
        final List<Trade> trades = new ArrayList<>();
        getJdbcTemplate().query(sql,params,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Trade trade = new Trade();
                trade.setTno(rs.getInt("tno"));
                trade.setCost(rs.getDouble("cost"));
                trade.setDate(rs.getDate("date"));
                trades.add(trade);
            }
        });
        return trades;
    }

    public List<Trade> getListByIdLessValue(String  id, int value){
        String sql="select * from t_trade where "+id+"<=?";
        Object []params=new Object[]{value};
        final List<Trade> trades = new ArrayList<>();
        getJdbcTemplate().query(sql,params,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Trade trade = new Trade();
                trade.setTno(rs.getInt("tno"));
                trade.setCost(rs.getDouble("cost"));
                trade.setDate(rs.getDate("date"));
                trades.add(trade);
            }
        });
        return trades;
    }

    public List<Trade> getList(){
        String sql = "select * from t_trade;";
        final List<Trade> trades = new ArrayList<>();
        getJdbcTemplate().query(sql,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Trade trade = new Trade();
                trade.setTno(rs.getInt("tno"));
                trade.setCost(rs.getDouble("cost"));
                trade.setDate(rs.getDate("date"));
                trades.add(trade);
            }
        });
        return trades;
    }


}
