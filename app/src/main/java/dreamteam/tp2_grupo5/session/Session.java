package dreamteam.tp2_grupo5.session;


import com.google.gson.annotations.SerializedName;

public class Session {
    @SerializedName("token")
    String token;
    @SerializedName("token_refresh")
    String tokenRefresh;

    public Session(String token, String refreshToken) {
        this.token = token;
        this.tokenRefresh = refreshToken;
    }

    //hacer el metodo cron aca
}
