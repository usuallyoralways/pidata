package ac.origination.project.Dao;

import ac.common.DAO.Dao;
import ac.origination.project.Models.EnJLData;
import ac.origination.project.Models.JLRawData;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnJLdataDao  extends Dao<EnJLData>{
    public void initDatabase() {
        String sql="create TABLE if not exists public.t_te_jldata\n" +
                "(\n" +
                "  en_id varchar(50),\n" +
                "  en_series_id varchar(50),\n" +
                "  en_year varchar(50),\n" +
                "  en_period varchar(20)," +
                "  en_value varchar(50)," +
                "  footnote_codes varchar(20)," +
                "   a_blockid integer," +
                "   a_hashvalue integer," +
                "   a_hashpoint \n" +
                ");";
        getJdbcTemplate().execute(sql);
        //初始化数据库
    }

    public void dropandCreate(){
        //series_id	year	period	value	footnote_codes
        String sql="drop table t_te_jldata;\n" +
                "create TABLE public.t_te_jldata\n" +
                "(\n" +
                "  en_id varchar(50),\n" +
                "  en_series_id varchar(50),\n" +
                "  en_year varchar(50),\n" +
                "  en_period varchar(20)," +
                "  en_value varchar(50)," +
                "  footnote_codes varchar(20)," +
                "   a_blockid integer," +
                "   a_hashvalue integer," +
                "   a_hashpoint \n" +
                ");";
        getJdbcTemplate().execute(sql);
    }



    public void insertItems(final List<EnJLData> jlRawDataList) {
        String sql = "insert into t_jldata (series_id,year,periof,value,footnote_codes) values(?,?,?,?,?) " +
                "ON CONFLICT(id) DO UPDATE" +
                " SET series_id=EXCLUDED.series_id, year=EXCLUDED.year,periof=EXCLUDED.periof," +
                "   value=EXCLUDED.value, footnote_codes=EXCLUDED.footnote_codes;";
        try {
            getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {


                    //
                }

                public int getBatchSize() {
                    return jlRawDataList.size();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertItem(EnJLData enJ){
        String sql = " String sql = \"insert into t_jldata (series_id,year,periof,value,footnote_codes) values(?,?,?,?,?) \" +\n" +
                "                \"ON CONFLICT(id) DO UPDATE\" +\n" +
                "                \" SET series_id=EXCLUDED.series_id, year=EXCLUDED.year,periof=EXCLUDED.periof,\" +\n" +
                "                \"   value=EXCLUDED.value, footnote_codes=EXCLUDED.footnote_codes;\"; ";
        Object[] params = {};
        getJdbcTemplate().update(sql, params);
    }

    public List<EnJLData> getListById(String  id, int value){
        String sql="select * from t_trade where "+id+"=?";
        Object []params=new Object[]{value};
        final List<EnJLData> jLdataDaoList = new ArrayList<>();
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

    public List<EnJLData> getListByIdOPValue(String  id, int value,String op){
        String sql="select * from t_trade where "+id+op+"?";
        Object []params=new Object[]{value};
        final List<EnJLData> trades = new ArrayList<>();
        getJdbcTemplate().query(sql,params,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {

            }
        });
        return trades;
    }

    public List<EnJLData> getList(){
        String sql = "select * from t_trade;";
        final List<EnJLData> trades = new ArrayList<>();
        getJdbcTemplate().query(sql,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                JLRawData trade = new JLRawData();

            }
        });
        return trades;
    }
}
