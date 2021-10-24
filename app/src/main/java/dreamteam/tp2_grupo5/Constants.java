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
    public final static String welcomeMsg = "Welcome!";
    public final static String register = "register";
    public final static String login = "login";
    public final static String correctPattern = "You're in :)";
    public final static String wrongPattern = "Wrong :(";

}
