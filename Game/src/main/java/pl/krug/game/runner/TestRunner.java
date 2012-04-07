package pl.krug.game.runner;

import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import pl.krug.game.forex.ForexDAO;
import pl.krug.game.forex.ForexHistoricalData;

public class TestRunner {

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {

        ForexDAO dao = new ForexDAO();

        GregorianCalendar todayCal = new GregorianCalendar();
        GregorianCalendar someAgo = new GregorianCalendar(2012, 1, 1);
        
        Date today = todayCal.getTime();
        Date weekAgo = someAgo.getTime();
        List<ForexHistoricalData> data = dao.getData(weekAgo, today);
        System.out.println(data.size());

    }
}
