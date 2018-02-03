package android.balaji.tech.mylibrary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.format.Formatter;
import android.util.Patterns;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Balaji SB on 3/2/18.
 */

public class ConfigUtils {

    private static Pattern pattern;
    private static Matcher matcher;
    private static SharedPreferences preferences;
    private static ConfigUtils utils;
    private ServiceHandler handler;
    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Jymycab";
    private static final float BITMAP_SCALE = 0.6f;
    private static final float BLUR_RADIUS = 25f;

    /*
    Get Singleton instance for ConfigUtils
     */

    public static synchronized ConfigUtils getInstance() {
        if (utils == null) {
            utils = new ConfigUtils();
        }
        return utils;
    }

     /*
    Get Singleton instance for SharedPreferences
     */

    public static synchronized SharedPreferences getSharedPreferences(Context mContext) {
        if (null == preferences)
            preferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return preferences;
    }

    /**
     * To validate if given string is valid email or not
     **/

    public static boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * To validate if given string is valid Email or Phone or not
     **/

    public static boolean validateEmailorPhone(String email) {
        return Patterns.PHONE.matcher(email).matches() || Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * To validate if given string is valid phone or not
     **/

    public static boolean validatePhone(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    /**
     * To validate if given string is valid url or not
     **/

    public static boolean validateURL(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }

    /**
     * To validate if given string is valid password or not
     **/

    public static boolean validatePassword(String password) {
        String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_-]).{8,20})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * To validate if given string is valid date of birth or not(minimum 18 years old)
     **/

    public static boolean validateDOB(Calendar newDate) {
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);
        return !minAdultAge.before(newDate);
    }

    /**
     * To encrypt the given string into md5 format
     **/

    public static String encryptToMD5(String password) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.trim().getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    /**
     * To get android device unique id
     **/

    public static String getUniqueId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * To Convert unix timestamp to normal date
     **/

    public static String convertTimestampToDate(long milliSeconds) {
        Date date = new Date(milliSeconds * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    /**
     * To Convert ISO dateformat to normal date
     **/

    public static String convertISOTimeToDate(String isoTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date convertedDate = null;
        String formattedDate = null;
        try {
            convertedDate = sdf.parse(isoTime);
            formattedDate = new SimpleDateFormat("dd-MM-yyyy" + "\n" + " hh:mm:ss a").format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    /**
     * To Uppercase first letter of String
     **/

    public static String upperCaseSentence(String value) {
        char[] array = value.toCharArray();
        array[0] = Character.toUpperCase(array[0]);
        // Uppercase all letters that follow a whitespace character.
        /*for (int i = 1; i < array.length; i++) {
            if (Character.isWhitespace(array[i - 1])) {
                array[i] = Character.toUpperCase(array[i]);
            }
        }*/
        return new String(array);
    }

    /**
     * To Uppercase all letters that follow a whitespace character.
     **/

    public static String upperCaseAllFirst(String value) {
        char[] array = value.toCharArray();
        array[0] = Character.toUpperCase(array[0]);
        for (int i = 1; i < array.length; i++) {
            if (Character.isWhitespace(array[i - 1])) {
                array[i] = Character.toUpperCase(array[i]);
            }
        }
        return new String(array);
    }

    /**
     * To format decimal upto 2 digits
     **/

    public static String format2Decimal(String text) {
        DecimalFormat format = new DecimalFormat("#0.00");
        return format.format(Double.parseDouble(text.trim()));
    }

    /**
     * To format decimal upto 8 digits
     **/

    public static String format8Decimal(String text) {
        DecimalFormat format = new DecimalFormat("#0.00000000");
        return format.format(Double.parseDouble(text.trim()));
    }

    /**
     * To get IP Address from Mobiledata or WiFi
     **/

    public static String getIpAddress(Context mContext) {
        boolean WIFI = false;
        boolean MOBILE = false;
        String ipAddress = "";

        ConnectivityManager CM = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();
        for (NetworkInfo netInfo : networkInfo) {
            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
                if (netInfo.isConnected())
                    WIFI = true;
            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                if (netInfo.isConnected())
                    MOBILE = true;
        }
        if (WIFI == true) {
            ipAddress = GetDeviceipWiFiData(mContext);
        }
        if (MOBILE == true) {
            ipAddress = GetDeviceipMobileData(mContext);
        }
        return ipAddress;
    }

    /**
     * To get IP Address from Mobiledata
     **/

    private static String GetDeviceipMobileData(Context mContext) {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }


    /**
     * To get IP Address from Wifi
     **/

    private static String GetDeviceipWiFiData(Context mContext) {
        WifiManager wm = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
        @SuppressWarnings("deprecation")
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }

    /**
     * To check the internet is available or not
     **/


    public static boolean isOnline(Context con) {
        ConnectivityManager connectivityManager;
        boolean connected = false;
        try {
            connectivityManager = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return connected;
    }


    /**
     * To get all methods in Specified Class
     **/

    public static Method[] getAllFunctions(Class className) {
        return className.getDeclaredMethods();
    }

    /**
     * To get bitmap from url
     **/

    public Bitmap getBitmapFromURL(String new_url) {
        Bitmap bmp = null;
        try {
            URL url = new URL(new_url);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            System.out.print(e);
        }
        return bmp;
    }


    /**
     * To get blurred image
     **/

    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);

        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        intrinsicBlur.setRadius(BLUR_RADIUS);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    /**
     * To load image using renderscript
     **/


    public static Bitmap loadImageWithRenderScript(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

}
