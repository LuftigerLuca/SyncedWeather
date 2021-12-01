package eu.luftiger.syncedweather.model;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import eu.luftiger.syncedweather.SyncedWeather;
import eu.luftiger.syncedweather.utils.ConfigService;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Weather {

    private final SyncedWeather plugin;
    private final ConfigService configService;

    private String locationName;
    private String weatherName;
    private double tempF;
    private double tempC;
    private double windSpeed;
    private double windDegree;

    public Weather(SyncedWeather plugin){
        this.plugin = plugin;
        this.configService = plugin.getConfigService();
    }

    public void update(){
        Map<String, Object > respMap = new HashMap<>();
        String API_KEY = configService.getConfig().getString("API_KEY");
        String LOCATION = configService.getConfig().getString("Location");
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + LOCATION + "&appid=" + API_KEY + "&units=imperial";

        try{
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null){
                result.append(line);
            }
            rd.close();;
            respMap = jsonToMap (result.toString());

        }catch (IOException e){
            Bukkit.getLogger().warning("[SyncedWeather] There is an error with the request for the data:   §4" + e.getMessage().replace(" ", ""));
            return;
        }

        this.locationName = respMap.get("name").toString() + ", " + jsonToMap(respMap.get("sys").toString()).get("country").toString();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        this.tempF = round(Double.parseDouble(jsonToMap(respMap.get("main").toString()).get("temp").toString()), 2);
        this.tempC = round(Math.rint((tempF - 32) * 5/9), 2);

        String rawInfo = respMap.get("weather").toString()
                .replace("[", "")
                .replace("]", "")
                .replace("{", "")
                .replace("}", "")
                .replace(" ", "");

        List<String> weather = new ArrayList<>();

        for (String s : rawInfo.split(",")) {
            weather.add(s.split("=")[1]);
        }

        if(weather.size() < 1){
            Bukkit.getLogger().warning("[SyncedWeather] The weather station you have selected is not compatible!");
        }

        weatherName = weather.get(1);

        this.windSpeed = round(Math.rint(Double.parseDouble(jsonToMap(respMap.get("wind").toString()).get("speed").toString())) * 3.6, 2);
        this.windDegree = round(Double.parseDouble(jsonToMap(respMap.get("wind").toString()).get("deg").toString()), 2);

    }

    private static Map<String,Object> jsonToMap(String str){
        return new Gson().fromJson(str, new TypeToken<HashMap<String,Object>>() {}.getType());
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getWeatherName() {
        return weatherName;
    }

    public void setWeatherName(String weatherName) {
        this.weatherName = weatherName;
    }

    public double getTempF() {
        return tempF;
    }

    public void setTempF(double tempF) {
        this.tempF = tempF;
    }

    public double getTempC() {
        return tempC;
    }

    public void setTempC(double tempC) {
        this.tempC = tempC;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(double windDegree) {
        this.windDegree = windDegree;
    }
}
