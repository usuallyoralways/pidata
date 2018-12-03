package test.origination.trade.TestEnDao;

import ac.origination.trade_table.DAO.Model.Trade;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import test.origination.trade.Models.TestEnTrade;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestTheDao extends JdbcDaoSupport {
    public void initDatabase() {
        String sql = " create table if not exists t_test_entrypt (\n" +
                "    a   varchar(50) primary key, \n" +
                "    b   varchar(50),\n" +
                "    c   varchar(50),\n" +
                "    a_hashvalue int,\n" +
                "    b_hashvalue float\n" +
                ");\n";

        getJdbcTemplate().execute(sql);
        //初始化数据库
    }

    public void insert(TestEnTrade testEnTrade){
        String sql = "insert into t_test_entrypt (a,b,c,a_hashvalue,b_hashvalue) values(?,?,?,?,?)" +
                " ON CONFLICT (a) DO UPDATE " +
                " SET a=EXCLUDED.a, b = EXCLUDED.b," +
                " c = EXCLUDED.c, b_hashvalue = EXCLUDED.b_hashvalue," +
                " a_hashvalue=EXCLUDED.a_hashvalue";
        Object[] params = {testEnTrade.getA(), testEnTrade.getB(), testEnTrade.getC(), testEnTrade.getA_hashvalue(),
                testEnTrade.getB_hashvalue(),
        };
        try {
            getJdbcTemplate().update(sql, params);
        } catch (DuplicateKeyException e) {
            System.out.println("INSERT ERROR"+e.toString());
        }
    }
    public List<TestEnTrade> getListById(){
        String sql="select * from t_test_entrypt order by b_hashvalue;";
        final List<TestEnTrade> testEnTrades = new ArrayList<>();
        getJdbcTemplate().query(sql,new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                TestEnTrade testEnTrade= new TestEnTrade();
                testEnTrade.setA(rs.getString("a"));
                testEnTrade.setA_hashvalue(rs.getInt("a_hashvalue"));
                testEnTrade.setB(rs.getString("b"));
                testEnTrade.setB_hashvalue(rs.getDouble("b_hashvalue"));
                testEnTrades.add(testEnTrade);
            }
        });
        return testEnTrades;
    }

}
