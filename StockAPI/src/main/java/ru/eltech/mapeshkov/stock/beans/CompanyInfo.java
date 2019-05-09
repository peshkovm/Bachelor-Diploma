package ru.eltech.mapeshkov.stock.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Objects;

/**
 * POJO class for information about company
 */
public class CompanyInfo {
    @JsonProperty("1. symbol")
    private String symbol;
    @JsonProperty("2. name")
    private String name;
    @JsonProperty("3. type")
    private String type;
    @JsonProperty("4. region")
    private String region;
    @JsonProperty("5. marketOpen")
    private LocalTime marketOpen;
    @JsonProperty("6. marketClose")
    private LocalTime marketClose;
    @JsonProperty("7. timezone")
    private ZoneId timezone;
    @JsonProperty("8. currency")
    private String currency;
    @JsonProperty("9. matchScore")
    private double matchScore;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyInfo that = (CompanyInfo) o;
        return Double.compare(that.getMatchScore(), getMatchScore()) == 0 &&
                getSymbol().equals(that.getSymbol()) &&
                getName().equals(that.getName()) &&
                getRegion().equals(that.getRegion()) &&
                getMarketOpen().equals(that.getMarketOpen()) &&
                getMarketClose().equals(that.getMarketClose()) &&
                getTimezone().equals(that.getTimezone()) &&
                getCurrency().equals(that.getCurrency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSymbol(), getName(), getRegion(), getMarketOpen(), getMarketClose(), getTimezone(), getCurrency(), getMatchScore());
    }

    @Override
    public String toString() {
        return "CompanyInfo{" + '\n' +
                "symbol: " + symbol + '\n' +
                "name: " + name + '\n' +
                "region: " + region + '\n' +
                "marketOpen: " + marketOpen + '\n' +
                "marketClose: " + marketClose + '\n' +
                "timezone: " + timezone + '\n' +
                "currency: " + currency + '\n' +
                "matchScore: " + matchScore + '\n' +
                '}';
    }

    /**
     * Returns company's symbol
     * @return
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns company's name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Returns company's type
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Returns company's region
     * @return
     */
    public String getRegion() {
        return region;
    }

    /**
     * Returns company's market open
     * @return
     */
    public LocalTime getMarketOpen() {
        return marketOpen;
    }

    /**
     * Returns company's market close
     * @return
     */
    public LocalTime getMarketClose() {
        return marketClose;
    }

    /**
     * Returns company's timezone
     * @return
     */
    public ZoneId getTimezone() {
        return timezone;
    }

    /**
     * Returns company's currency
     * @return
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Returns match score between symbol and company's name
     * @return
     */
    public double getMatchScore() {
        return matchScore;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setMarketOpen(String marketOpen) {
        this.marketOpen = LocalTime.parse(marketOpen);
    }

    public void setMarketClose(String marketClose) {
        this.marketClose = LocalTime.parse(marketClose);
    }

    public void setTimezone(String timezone) {
        this.timezone = ZoneId.of(timezone);
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }
}