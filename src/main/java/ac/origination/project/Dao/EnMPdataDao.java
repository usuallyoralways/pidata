package ac.origination.project.Dao;

import ac.common.DAO.Dao;
import ac.origination.project.Models.EnMPData;
import ac.origination.project.Models.MPRawData;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnMPdataDao extends Dao<EnMPData>{
    public void initDatabase() {
        String sql="create TABLE if not exists public.t_te_mpdata\n" +
                "(\n" +
                "  en_id varchar(50) primary key,\n" +
                "  en_series_id varchar(50),\n" +
                "  en_year varchar(50),\n" +
                "  en_period varchar(20)," +
                "  en_value varchar(50)," +
                "  footnote_codes varchar(20)," +
                "   a_blockid integer," +
                "   a_hashvalue integer," +
                "   a_hashpoint integer\n" +
                ");";
        getJdbcTemplate().execute(sql);
        //初始化数据库
    }

    public void dropandCreate(){
        //series_id	year	period	value	footnote_codes
        String sql="drop table t_te_mpdata;\n" +
                "create TABLE public.t_te_mpdata\n" +
                "(\n" +
                "  en_id varchar(50) primary key,\n" +
                "  en_series_id varchar(50),\n" +
                "  en_year varchar(50),\n" +
                "  en_period varchar(20)," +
                "  en_value varchar(50)," +
                "  footnote_codes varchar(20)," +
                "   a_blockid integer," +
                "   a_hashvalue integer," +
                "   a_hashpoint integer\n" +
                ");";
        getJdbcTemplate().execute(sql);
    }



    public void insertItems(final List<EnMPData> jlRawDataList) {
        String sql = "insert into t_te_jldata (en_id,en_series_id,en_year,en_periof,en_value,en_footnote_codes,a_blockid,a_hashvalue,a_hashpoint) values(?,?,?,?,?,?,?,?) " +
                "ON CONFLICT(en_id) DO UPDATE" +
                " SET en_id=EXCLUDED.series_id, en_series_id=EXCLUDED.en_series_id, en_year=EXCLUDED.en_year,en_period=EXCLUDED.en_period," +
                "   en_value=EXCLUDED.en_value, en_footnote_codes=EXCLUDED.en_footnote_codes,a_blockid=EXCLUDED.a_blockid," +
                "   a_hashvalue=EXCLUDED.a_hashvalue,a_hashpoint=EXCLUEDEB.a_hashpoint;";
        try {
            getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {

                    ps.setString(1,jlRawDataList.get(i).getEn_id());
                    ps.setString(2, jlRawDataList.get(i).getEn_series_id());
                    ps.setString(3, jlRawDataList.get(i).getEn_year());
                    ps.setString(4, jlRawDataList.get(i).getEn_period());
                    ps.setString(5, jlRawDataList.get(i).getEn_value());
                    ps.setString(6, jlRawDataList.get(i).getEn_footnote_codes());
                    ps.setInt(7,jlRawDataList.get(i).getA_blockId());
                    ps.setInt(8,jlRawDataList.get(i).getA_hashvalue());
                    ps.setInt(9,jlRawDataList.get(i).getA_hashpoint());

                }

                public int getBatchSize() {
                    return jlRawDataList.size();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertItem(EnMPData enJ){
        String sql = "insert into t_te_jldata (en_id,en_series_id,en_year,en_period,en_value,en_footnote_codes,a_blockid,a_hashvalue,a_hashpoint) values(?,?,?,?,?,?,?,?) " +
                "ON CONFLICT(en_id) DO UPDATE" +
                " SET en_id=EXCLUDED.series_id, en_series_id=EXCLUDED.en_series_id, en_year=EXCLUDED.en_year,en_period=EXCLUDED.en_period," +
                "   en_value=EXCLUDED.en_value, en_footnote_codes=EXCLUDED.en_footnote_codes,a_blockid=EXCLUDED.a_blockid," +
                "   a_hashvalue=EXCLUDED.a_hashvalue,a_hashpoint=EXCLUEDEB.a_hashpoint;";
        Object[] params = {enJ.getEn_id(),enJ.getEn_series_id(),enJ.getEn_year(),enJ.getEn_period(),enJ.getEn_value(),enJ.getEn_footnote_codes(),
                enJ.getA_blockId(),enJ.getA_hashvalue(),enJ.getA_hashpoint()};
        getJdbcTemplate().update(sql, params);
    }

    public List<EnMPData> getListById(String  id, int value){
        String sql="select * from t_te_jldata where "+id+"=?";
        Object []params=new Object[]{value};
        final List<EnMPData> jLdataDaoList = new ArrayList<>();
        getJdbcTemplate().query(sql,params,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                EnMPData enMPData = new EnMPData();
                enMPData.setEn_id(rs.getString("en_id"));
                enMPData.setEn_series_id(rs.getString("en_series_id"));
                enMPData.setEn_year(rs.getString("en_year"));
                enMPData.setEn_period(rs.getString("en_period"));
                enMPData.setEn_value(rs.getString(",en_value"));
                enMPData.setEn_footnote_codes(rs.getString("en_footnote_codes"));
                enMPData.setA_hashvalue(rs.getInt("a_hashvalue"));
                enMPData.setA_blockId(rs.getInt("a_blockid"));
                enMPData.setA_hashpoint(rs.getInt("a_hashpoint"));
                jLdataDaoList.add(enMPData);
            }
        });
        return jLdataDaoList;
    }

    public List<EnMPData> getListByIdOPValue(String  id, int value, String op){
        String sql="select * from t_trade where "+id+op+"?";
        Object []params=new Object[]{value};
        final List<EnMPData> trades = new ArrayList<>();
        getJdbcTemplate().query(sql,params,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {

            }
        });
        return trades;
    }

    public List<EnMPData> getList(){
        String sql = "select * from t_trade;";
        final List<EnMPData> trades = new ArrayList<>();
        getJdbcTemplate().query(sql,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                MPRawData trade = new MPRawData();

            }
        });
        return trades;
    }
}
