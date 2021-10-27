package dreamteam.tp2_grupo5.clienteHttp;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import dreamteam.tp2_grupo5.CovidRanking;
import dreamteam.tp2_grupo5.models.LocationStats;
import dreamteam.tp2_grupo5.models.RankingItem;

public class CoronavirusDataService extends AsyncTask<String, String, HashMap<String, Integer>> {


    HashMap<String, Integer> covidStats = new HashMap<String, Integer>();
    private final AsyncInterface caller;
    Integer integer;

    public CoronavirusDataService(Context caller) {
        this.caller = (AsyncInterface) caller;
    }

    public HashMap<String, Integer> fetchVirusData(String uri){
        HttpURLConnection urlConnection = null;
        try {
            String result = null;
            HashMap<String, Integer> stats = new HashMap<String, Integer>();
            URL url = new URL(uri);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-length", "0");
            urlConnection.setUseCaches(false);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.setConnectTimeout(100000);
            urlConnection.setReadTimeout(100000);

            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                result = convertInputStreamToString(new InputStreamReader(urlConnection.getInputStream())).toString();
                StringReader csvBodyReader = new StringReader(result);
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(csvBodyReader);
                for (CSVRecord record : records) {

                    LocationStats locationStats = new LocationStats();

                    locationStats.setCountry(record.get("Country/Region"));
                    locationStats.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));

                    Integer prev = stats.get(locationStats.getCountry());
                    Integer newValue = locationStats.getLatestTotalCases();
                    if(prev != null)
                        newValue += prev.intValue();

                    stats.put(locationStats.getCountry(), newValue);

                }
            }
            else { //Analizar que hacer aca
                return null;
            }

            return stats;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected HashMap<String, Integer> doInBackground(String... params) {
        return fetchVirusData(params[0]);
    }

    @Override
    protected void onPostExecute(HashMap<String, Integer> result) {
        super.onPostExecute(result);
        HashMap<Integer, RankingItem> sortedStats = sortStats(result);
        System.out.println("sortedStats: " + sortedStats);
        caller.activityToWithPayload(CovidRanking.class, sortedStats);
    }

    private StringBuilder convertInputStreamToString(InputStreamReader inputStream) throws IOException, IOException {
        BufferedReader br = new BufferedReader(inputStream);
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            result.append(line + "\n");
        }
        br.close();
        return result;
    }

    private HashMap<Integer, RankingItem> sortStats(HashMap<String, Integer> unSortedStats) { //Falta implemenar el ascendente
        HashMap<Integer, RankingItem> sortedMap = new HashMap<>();
        integer = 0;
        unSortedStats.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> {
            sortedMap.put(integer, new RankingItem(x.getKey(),x.getValue()));
            integer++;
        });
        return sortedMap;
    }

}
