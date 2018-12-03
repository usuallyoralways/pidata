import ac.origination.trade_table.EnTradeDataSubmit;
import ac.origination.trade_table.HMacMD5;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Test {


    public static void main(String args[]) {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/tpch",
                            "admin", "admin");
            Statement stmt = c.createStatement();
            String sql = "\n" +
                    "select b_hashvalue from t_te_entrypt where b!='ThisIsBFalseData' order by b_blockid,b_hashvalue;";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                int dd = rs.getInt("b_hashvalue");
                System.out.print(dd+",");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

};
