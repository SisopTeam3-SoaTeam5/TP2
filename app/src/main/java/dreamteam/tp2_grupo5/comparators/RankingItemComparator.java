package dreamteam.tp2_grupo5.comparators;

import java.util.Comparator;

import dreamteam.tp2_grupo5.models.RankingItem;

public class RankingItemComparator implements Comparator<RankingItem> {

    @Override
    public int compare(RankingItem item1, RankingItem item2) {
       return Integer.compare(item1.getTotalCases(),item2.getTotalCases());
    }

    @Override
    public Comparator<RankingItem> reversed() {
        return (item1, item2) -> Integer.compare(item2.getTotalCases(),item1.getTotalCases());
    }
}
