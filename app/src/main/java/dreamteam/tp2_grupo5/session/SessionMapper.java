package dreamteam.tp2_grupo5.session;

import com.google.gson.annotations.SerializedName;

public class SessionMapper {

    @SerializedName("token")
    public String token;
    @SerializedName("token_refresh")
    public String tokenRefresh;

    public SessionMapper(String token, String tokenRefresh) {
        this.token = token;
        this.tokenRefresh = tokenRefresh;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
