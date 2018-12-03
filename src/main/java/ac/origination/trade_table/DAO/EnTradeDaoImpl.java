package ac.origination.trade_table.DAO;

import ac.common.DAO.Dao;
import ac.origination.EnItem;
import ac.origination.trade_table.DAO.Model.EnTrade;
import javafx.application.Application;
import org.apache.commons.logging.Log;
import org.postgresql.util.PSQLException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnTradeDaoImpl extends Dao {

    Logger logger = Logger.getLogger(EnTradeDaoImpl.class.getName());

//    public void cleartheDatabase(){
//        String sql="truncate t_te_entrypt;";
//        getJdbcTemplate().execute(sql);
//    }

    public void initDatabase() {
        String sql = "     create table if not exists t_te_entrypt " +
                "(a varchar(50), " +
                "a_blockid int, " +
                "a_hashvalue int, " +
                "b varchar(100)," +
                "b_blockid int," +
                "b_hashvalue int," +
                "c date," +
                "primary key(a_blockid,a_hashvalue));";

        getJdbcTemplate().execute(sql);
        //初始化数据库
    }

    public void insertEnItem(Object enTrade) {
        String sql = "insert into t_te_entrypt (a,a_blockid,a_hashvalue,  b,b_blockid,b_hashvalue,c,a_flag,a_hashpoint,b_hashpoint ) values(?,?,?,?,?,?,?,?,?,?)" +
                " ON CONFLICT (a_blockid,a_hashvalue) DO UPDATE " +
                " SET a=EXCLUDED.a, b = EXCLUDED.b," +
                " b_blockid = EXCLUDED.b_blockid, b_hashvalue = EXCLUDED.b_hashvalue," +
                " c = EXCLUDED.c,a_flag=EXCLUDED.a_flag," +
                " a_hashpoint=EXCLUDED.a_hashpoint,b_hashpoint=EXCLUDED.b_hashpoint;";
        Object[] params = {((EnTrade) (enTrade)).getA(), ((EnTrade) (enTrade)).getA_blockid(), ((EnTrade) (enTrade)).getA_hashvalue(), ((EnTrade) (enTrade)).getB(),
                ((EnTrade) (enTrade)).getB_blockid(), ((EnTrade) (enTrade)).getB_hashvalue(), ((EnTrade) (enTrade)).getC(),((EnTrade) (enTrade)).isA_flag(),
                ((EnTrade) (enTrade)).getA_hashpoint(),((EnTrade) (enTrade)).getB_hashpoint()
        };
        try {
            getJdbcTemplate().update(sql, params);
        } catch (DuplicateKeyException e) {
            logger.log(Level.WARNING, e.toString());
        }
    }

    public void insertEnItems(final List<EnTrade> enTrades) {
        String sql = "insert into t_te_entrypt (a,a_blockid,a_hashvalue,  b,b_blockid,b_hashvalue,c,a_flag,a_hashpoint,b_hashpoint ) values(?,?,?,?,?,?,?,?,?,?)" +
                " ON CONFLICT (a_blockid,a_hashvalue) DO UPDATE " +
                " SET a=EXCLUDED.a, b = EXCLUDED.b," +
                " b_blockid = EXCLUDED.b_blockid, b_hashvalue = EXCLUDED.b_hashvalue," +
                " c = EXCLUDED.c,a_flag=EXCLUDED.a_flag," +
                " a_hashpoint=EXCLUDED.a_hashpoint,b_hashpoint=EXCLUDED.b_hashpoint;";
        try {
            getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, ((EnTrade) (enTrades.get(i))).getA());
                    ps.setInt(2, ((EnTrade) (enTrades.get(i))).getA_blockid());
                    ps.setInt(3, ((EnTrade) (enTrades.get(i))).getA_hashvalue());
                    ps.setString(4, ((EnTrade) (enTrades.get(i))).getB());
                    ps.setInt(5, ((EnTrade) (enTrades.get(i))).getB_blockid());
                    ps.setInt(6, ((EnTrade) (enTrades.get(i))).getB_hashvalue());
                    ps.setString(7, ((enTrades.get(i).getC())));
                    ps.setBoolean(8, (enTrades.get(i).isA_flag()));
                    ps.setInt(9,(enTrades.get(i).getA_hashpoint()));
                    ps.setInt(10,(enTrades.get(i).getB_hashpoint()));
                }

                public int getBatchSize() {
                    return enTrades.size();
                }
            });
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString());
        }
    }


    public int getFlagById(String id, int value, final String flag) {
        String sql = "select "+flag+" from t_te_entrypt where " + id + "=? limit 1";
        Object[] params = new Object[]{value};
        final List<Integer> flagbl = new ArrayList<>();
        flagbl.add(-1);
        getJdbcTemplate().query(sql, params, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                boolean bflag = rs.getBoolean(flag);
                if (bflag)
                    flagbl.add(1);
                else
                    flagbl.add(0);

            }
        });
        if (flagbl.size() == 1)
            return -1;
        else
            return flagbl.get(1);
    }

    public List<EnTrade> getListById(String id, int value) {

        String sql = "select *  from t_te_entrypt where " + id + "=?";
        if (id.equals("a_blockid")){
            sql+=" order by a_hashvalue asc;";
        }else if(id.equals("b_blockid")){
            sql+=" order by b_hashvalue asc;";
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

    public List<EnTrade> getList(){
        String sql = "select * from t_te_entrypt;";
        final List<EnTrade> enTrades = new ArrayList<>();
        getJdbcTemplate().query(sql, new RowCallbackHandler() {
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
                enTrades.add(enTrade);
            }
        });
        return enTrades;
    }

    public void deleteByIdAndFlag(String id, int value, boolean flag,String aorbFlag) {
        String sql = "delete from t_te_entrypt where " + id + "=? and "+aorbFlag+" =?";

        Object[] params = new Object[]{value, flag};

        try {
            getJdbcTemplate().update(sql, params);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.toString());
        }
    }


    public void deleteEnItem(String[] id, int[] value) {
        String sql = "delete from t_te_entrypt where ";
        if (id.length == 1) {
            sql += id[0] + "=?";
            Object[] params = new Object[]{value[0]};
            try {
                getJdbcTemplate().update(sql, params);
            } catch (Exception e) {
                logger.log(Level.WARNING, e.toString());
            }
        } else if (id.length > 1) {
            sql += id[0] + "=? ";
            for (int i = 1; i < id.length; i++) {
                sql += "and " + id[i] + "=? ";
            }
            Object[] params = new Object[id.length];
            for (int i = 0; i < id.length; i++) {
                params[i] = value[i];
            }
            try {
                getJdbcTemplate().update(sql, params);
            } catch (Exception e) {
                logger.log(Level.WARNING, e.toString());
            }
        }

    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        EnTradeDaoImpl entradeDao = (EnTradeDaoImpl) applicationContext.getBean("entradeDao");
        EnTrade enTrade = new EnTrade();
        enTrade.setA("a");
        enTrade.setA_blockid(1);
        enTrade.setA_hashvalue(234);
        enTrade.setB("abcd");
        enTrade.setB_blockid(2);
        enTrade.setB_hashvalue(345);
        enTrade.setC("fsd");


        EnTrade enTrade1 = new EnTrade();
        enTrade1.setA("a");
        enTrade1.setA_blockid(2);
        enTrade1.setA_hashvalue(234);
        enTrade1.setB("efsg");
        enTrade1.setB_blockid(2);
        enTrade1.setB_hashvalue(345);
        enTrade1.setC("dsf");

        entradeDao.initDatabase();
        List<EnTrade> ens = new ArrayList<>();
        ens.add(enTrade1);
        ens.add(enTrade);

        entradeDao.insertEnItems(ens);
//        entradeDao.insertEnItem(enTrade);
        String[] id = {"a_blockid", "a_hashvalue"};
        int[] value = {1, 234};
        //entradeDao.deleteEnItem(id,value);
    }

}
