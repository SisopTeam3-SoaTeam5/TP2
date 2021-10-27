package dreamteam.tp2_grupo5;

import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public final static String baseUrl = "http://so-unlam.net.ar/api/api/";
    public final static int passwordMinLength = 8;
    public final static String commissionValidationError = "Commission should be 2900 or 3900";
    public final static String fieldsValidationError = "Fields should not be empty";
    public final static String emailValidationError = "Should be a valid email";
    public final static String emptyEmail = "Email should not be empty";
    public final static String passwordLengthMsg = "Password should be at least 8 char long";
    public final static String passwordMatchError = "Passwords doesn't match";
    public final static List<String> commissionValues = Arrays.asList("2900","3900");
    public final static String successMsg = "Success";
    public final static String testEnv = "TEST";
    public final static String prodEnv = "PROD";
    public final static String successRegister = "Register successfully!";
    public final static String successLoggin = "Welcome back!";
    public final static String register = "register";
    public final static String login = "login";
    public final static String homePage = "homePage";
    public final static String covidRanking = "covidRanking";
    public final static String refresh = "refresh";
    public final static String correctPattern = "You're in :)";
    public final static String wrongPattern = "Wrong :(";
    public final static String virusDataUrl = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    public final static float ACC = 15;

}
