package dreamteam.tp2_grupo5.models;

import java.io.Serializable;

public class RankingItem implements Serializable {
    private String country;
    private Integer totalCases;

    public RankingItem(String country, Integer totalCases) {
        this.country = country;
        this.totalCases = totalCases;
    }

    public String getCountry() {
        return country;
    }

    public String elementString(int position){
        return position + " - " + country + " - " + totalCases.toString();
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(Integer totalCases) {
        this.totalCases = totalCases;
    }
}
