package Models;

public class Coinsignature {

    private String name;
    private String symbol;
    private String rank;
    private String price_usd;
    private String percent_change_1h;

    public Coinsignature(String name, String symbol,
                         String rank, String price_usd, String percent_change_1h) {

        this.name = name;
        this.symbol = symbol;
        this.rank = rank;
        this.price_usd = price_usd;
        this.percent_change_1h = percent_change_1h;

    }

    public String getName() {
        return name;
    }



    public String getSymbol() {
        return symbol;
    }



    public String getRank() {
        return rank;
    }



    public String getPrice_usd() {
        return price_usd;
    }



    public String getPercent_change_1h() {
        return percent_change_1h;
    }


}
