package ac.origination.trade_table.DAO;


import ac.common.DAO.Dao;
import ac.origination.trade_table.DAO.Model.EnTrade;
import ac.origination.trade_table.DAO.Model.Trade;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TradeDaoImpl extends Dao<Trade> {
    public void initDatabase() {

        //初始化数据库
    }

    public void dropandCreate(){
        String sql="drop table t_trade;\n" +
                "create TABLE public.t_trade\n" +
                "(\n" +
                "  tno serial,\n" +
                "  cost double precision,\n" +
                "  date date,\n" +
                "  primary key(tno)\n" +
                ");";
        getJdbcTemplate().execute(sql);
    }
    public void insertItems(final List<Trade> trades) {
        String sql = "insert into t_trade (cost,date) values(?,?) " +
                "ON CONFLICT(tno) DO UPDATE" +
                " SET tno=EXCLUDED.tno, cost=EXCLUDED.cost,date=EXCLUDED.date;";
        try {
            getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setDouble(1,trades.get(i).getCost());
                    ps.setString(2,(trades.get(i).getDate()));
                }

                public int getBatchSize() {
                    return trades.size();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertItem(Trade trade){
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
                trade.setDate(rs.getString("date"));
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
                trade.setDate(rs.getString("date"));
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
                trade.setDate(rs.getString("date"));
                trades.add(trade);
            }
        });
        return trades;
    }


}
