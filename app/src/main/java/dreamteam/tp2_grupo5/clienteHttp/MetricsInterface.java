package dreamteam.tp2_grupo5.clienteHttp;

public interface MetricsInterface {
    void writePreferences( String key, Integer value);
    Integer getPreferences(String key);
}
