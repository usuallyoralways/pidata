package ac.origination.project.Dao;

import ac.common.DAO.Dao;
import ac.origination.project.Models.MPRawData;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MPdataDao extends Dao<MPRawData> {
    public void initDatabase() {

        String sql=
                "create TABLE if not exists public.t_mpdata\n" +
                "(\n" +
                "  id serial primary key,\n" +
                "  series_id varchar(20),\n" +
                "  year int,\n" +
                "  period varchar(10)," +
                "  value double precision," +
                "  footnote_codes varchar(5) \n" +
                ");";
        getJdbcTemplate().execute(sql);
        //初始化数据库
    }

    public void dropandCreate(){
        //series_id	year	period	value	footnote_codes
        String sql="drop table t_mpdata;\n" +
                "create TABLE public.t_mpdata\n" +
                "(\n" +
                "  id serial primary key,\n" +
                "  series_id varchar(20),\n" +
                "  year int,\n" +
                "  period varchar(10)," +
                "  value double precision," +
                "  footnote_codes varchar(5) \n" +
                ");";
        getJdbcTemplate().execute(sql);
    }



    public void insertItems(final List<MPRawData> MPRawDataList) {
        String sql = "insert into t_mpdata (series_id,year,period,value,footnote_codes) values(?,?,?,?,?) " +
                "ON CONFLICT(id) DO UPDATE" +
                " SET series_id=EXCLUDED.series_id, year=EXCLUDED.year,period=EXCLUDED.period," +
                "   value=EXCLUDED.value, footnote_codes=EXCLUDED.footnote_codes;";
        try {
            getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, MPRawDataList.get(i).getSeries_id());
                    ps.setInt(2, MPRawDataList.get(i).getYear());
                    ps.setString(3, MPRawDataList.get(i).getPeriod());
                    ps.setDouble(4, MPRawDataList.get(i).getValue());
                    ps.setString(5, MPRawDataList.get(i).getFootnote_codes());
                }

                public int getBatchSize() {
                    return MPRawDataList.size();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertItem(MPRawData MPRawData){
        String sql = " String sql = \"insert into t_mpdata (series_id,year,periof,value,footnote_codes) values(?,?,?,?,?) \" +\n" +
                "                \"ON CONFLICT(id) DO UPDATE\" +\n" +
                "                \" SET series_id=EXCLUDED.series_id, year=EXCLUDED.year,periof=EXCLUDED.periof,\" +\n" +
                "                \"   value=EXCLUDED.value, footnote_codes=EXCLUDED.footnote_codes;\"; ";
        Object[] params = {MPRawData.getSeries_id(), MPRawData.getYear(), MPRawData.getPeriod(), MPRawData.getValue(), MPRawData.getFootnote_codes()};
        getJdbcTemplate().update(sql, params);
    }

    public List<MPRawData> getListById(String  id, int value){
        String sql="select * from t_mpdata where "+id+"=?";
        Object []params=new Object[]{value};
        final List<MPRawData> mpRawDataList = new ArrayList<>();
        getJdbcTemplate().query(sql,params,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                MPRawData mpRawData = new MPRawData();
                mpRawData.setId(rs.getInt("id"));
                mpRawData.setSeries_id(rs.getString("series_id"));
                mpRawData.setYear(rs.getInt("year"));
                mpRawData.setPeriod(rs.getString("period"));
                mpRawData.setValue(rs.getDouble("value"));
                mpRawData.setFootnote_codes(rs.getString("footnote_codes"));
                mpRawDataList.add(mpRawData);
            }
        });
        return mpRawDataList;
    }

    public List<MPRawData> getListByIdOPValue(String  id, int value, String op){
        String sql="select * from t_trade where "+id+op+"?";
        Object []params=new Object[]{value};
        final List<MPRawData> trades = new ArrayList<>();
        getJdbcTemplate().query(sql,params,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                MPRawData trade = new MPRawData();

                trades.add(trade);
            }
        });
        return trades;
    }

    public List<MPRawData> getList(){
        String sql = "select * from t_mpdata order by value;";
        final List<MPRawData> mpRawDataArrayList = new ArrayList<>();
        getJdbcTemplate().query(sql,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                MPRawData mpRawData = new MPRawData();
                mpRawData.setValue(rs.getDouble("value"));
                mpRawDataArrayList.add(mpRawData);
            }
        });
        return mpRawDataArrayList;
    }

}
