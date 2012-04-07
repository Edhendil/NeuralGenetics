/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.game.forex;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * To parse data from
 *
 * @author Edhendil
 */
public class ForexDataParser {

    public List<ForexHistoricalData> parseData(InputStream input) throws IOException, ParseException {
        List<ForexHistoricalData> result = new ArrayList<ForexHistoricalData>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        // read header line and leave it
        reader.readLine();
        String line;
        String[] lineContent;
        while ((line = reader.readLine()) != null) {
            ForexHistoricalData data = new ForexHistoricalData();
            lineContent = line.split(",");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hhmmss");
            Date date = dateFormat.parse(lineContent[1]);
            Date time = timeFormat.parse(lineContent[2]);
            date.setTime(date.getTime() + time.getTime());
            double open = Double.valueOf(lineContent[3]);
            double high = Double.valueOf(lineContent[4]);
            double low = Double.valueOf(lineContent[5]);
            double close = Double.valueOf(lineContent[6]);

            data.setCloseValue(close);
            data.setOpenValue(open);
            data.setHighValue(high);
            data.setLowValue(low);
            data.setTickTime(date);
            data.setType(ForexCurrencyPair.EURUSD);
            result.add(data);
        }

        return result;
    }
}
