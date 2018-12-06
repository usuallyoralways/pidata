package ac.origination.project.Dao;

import ac.common.DAO.Dao;
import ac.origination.project.Models.JLRawData;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JLdataDao extends Dao<JLRawData> {
    public void initDatabase() {

        //初始化数据库
    }

    public void dropandCreate(){
        //series_id	year	period	value	footnote_codes
        String sql="drop table t_jldata;\n" +
                "create TABLE public.t_trade\n" +
                "(\n" +
                "  id serial,\n" +
                "  series_id varchar(20),\n" +
                "  year int,\n" +
                "  period varchar(10)," +
                "  value double precision," +
                "  footnote_codes varchar(5) \n" +
                ");";
        getJdbcTemplate().execute(sql);
    }



    public void insertItems(final List<JLRawData> jlRawDataList) {
        String sql = "insert into t_jldata (series_id,year,periof,value,footnote_codes) values(?,?,?,?,?) " +
                "ON CONFLICT(id) DO UPDATE" +
                " SET series_id=EXCLUDED.series_id, year=EXCLUDED.year,periof=EXCLUDED.periof," +
                "   value=EXCLUDED.value, footnote_codes=EXCLUDED.footnote_codes;";
        try {
            getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1,jlRawDataList.get(i).getSeries_id());
                    ps.setInt(2,jlRawDataList.get(i).getYear());
                    ps.setString(3,jlRawDataList.get(i).getPeriod());
                    ps.setDouble(4,jlRawDataList.get(i).getValue());
                    ps.setString(5,jlRawDataList.get(i).getFootnote_codes());
                }

                public int getBatchSize() {
                    return jlRawDataList.size();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertItem(JLRawData jlRawData){
        String sql = " String sql = \"insert into t_jldata (series_id,year,periof,value,footnote_codes) values(?,?,?,?,?) \" +\n" +
                "                \"ON CONFLICT(id) DO UPDATE\" +\n" +
                "                \" SET series_id=EXCLUDED.series_id, year=EXCLUDED.year,periof=EXCLUDED.periof,\" +\n" +
                "                \"   value=EXCLUDED.value, footnote_codes=EXCLUDED.footnote_codes;\"; ";
        Object[] params = {jlRawData.getSeries_id(),jlRawData.getYear(),jlRawData.getPeriod(),jlRawData.getValue(),jlRawData.getFootnote_codes()};
        getJdbcTemplate().update(sql, params);
    }

    public List<JLRawData> getListById(String  id, int value){
        String sql="select * from t_trade where "+id+"=?";
        Object []params=new Object[]{value};
        final List<JLRawData> jLdataDaoList = new ArrayList<>();
        getJdbcTemplate().query(sql,params,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                JLRawData jlRawData = new JLRawData();
                jlRawData.setSeries_id(rs.getString("series_id"));
                jlRawData.setYear(rs.getInt("year"));
                jlRawData.setPeriod(rs.getString("periof"));
                jlRawData.setValue(rs.getDouble("value"));
                jlRawData.setFootnote_codes(rs.getString("footnote_codes"));
            }
        });
        return jLdataDaoList;
    }

    public List<JLRawData> getListByIdOPValue(String  id, int value,String op){
        String sql="select * from t_trade where "+id+op+"?";
        Object []params=new Object[]{value};
        final List<JLRawData> trades = new ArrayList<>();
        getJdbcTemplate().query(sql,params,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                JLRawData trade = new JLRawData();

                trades.add(trade);
            }
        });
        return trades;
    }

    public List<JLRawData> getList(){
        String sql = "select * from t_trade;";
        final List<JLRawData> trades = new ArrayList<>();
        getJdbcTemplate().query(sql,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                JLRawData trade = new JLRawData();

            }
        });
        return trades;
    }

}
