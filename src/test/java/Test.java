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
        int beishu=126;
        Double firstValue=82.677;
        double acc=0.001;
        Double shu= (beishu*100+firstValue*100*((int)(1/acc)))/(((int)(1/(acc)))*100);

        System.out.println(shu);
        System.out.println((((int)(1/(acc)))*100));
        System.out.println(beishu*100+firstValue*100*((int)(1/acc)));
        System.out.println(firstValue*100*((int)(1/acc)));


    }

};
