/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.game.forex;

import java.util.Date;
import javax.persistence.*;

/**
 * Holds data to be used in the game.
 *
 * @author Edhendil
 */
@Entity
@Table(name = "ForexHistory")
public class ForexHistoricalData {

    @Id
    @Column(name = "f_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.ORDINAL)
    private ForexCurrencyPair type;
    @Temporal(TemporalType.TIMESTAMP)
    private Date tickTime;
    private double openValue;
    private double closeValue;
    private double highValue;
    private double lowValue;

    public double getCloseValue() {
        return closeValue;
    }

    public void setCloseValue(double closeValue) {
        this.closeValue = closeValue;
    }

    public double getHighValue() {
        return highValue;
    }

    public void setHighValue(double highValue) {
        this.highValue = highValue;
    }

    public double getLowValue() {
        return lowValue;
    }

    public void setLowValue(double lowValue) {
        this.lowValue = lowValue;
    }

    public double getOpenValue() {
        return openValue;
    }

    public void setOpenValue(double openValue) {
        this.openValue = openValue;
    }

    public Date getTickTime() {
        return tickTime;
    }

    public void setTickTime(Date tickTime) {
        this.tickTime = tickTime;
    }

    public ForexCurrencyPair getType() {
        return type;
    }

    public void setType(ForexCurrencyPair type) {
        this.type = type;
    }
}
