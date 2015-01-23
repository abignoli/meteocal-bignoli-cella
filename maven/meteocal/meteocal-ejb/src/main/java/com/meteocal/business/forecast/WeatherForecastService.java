/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.forecast;

import com.meteocal.business.entities.WeatherForecast;
import com.meteocal.business.entities.WeatherForecastBase;
import com.meteocal.business.entities.comparators.WeatherForecastBaseComparator;
import com.meteocal.business.entities.shared.WeatherCondition;
import com.meteocal.business.exceptions.InvalidInputException;
import com.meteocal.business.shared.scheduling.ScheduleActivator;
import com.meteocal.business.shared.utils.LocalDateTimeUtils;
import com.meteocal.shared.ApplicationVariables;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import org.openweathermap.api.long_range.LongRangeForecast;
import org.openweathermap.api.short_range.ShortRangeForecast;
import org.openweathermap.api.short_range.Weather;

/**
 *
 * @author Andrea Bignoli
 */
@Singleton
public class WeatherForecastService {

    private static final Logger logger = Logger.getLogger(WeatherForecastService.class.getName());

    private Client client;

    private final String OPENWEATHERMAP_API_SHORT_RANGE_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?q={0},{1}&mode=json&APPID={2}";
    private final String OPENWEATHERMAP_API_LONG_RANGE_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q={0},{1}&cnt={3}&mode=json&APPID={2}";

    private final String EXCEPTION_REQUEST_NO_CITY_OR_COUNTRY = "[WEATHER FORECAST SERVICE] Weather forecast request: null city or country has been provided.";
    private final String EXCEPTION_ASK_FORECAST_NO_START_END = "[WEATHER FORECAST SERVICE] Weather forecast request: null start, end.";

    // This represents the quantity of time the system is allowed to add to the forecasts to create a perfect sequence, in general this shouldn't happena anyways
    private final int FORECAST_ALLOWED_TIME_INCREASE_HOURS = 1;

    // Fixed
    public final int SHORT_RANGE_DAYS = 5;

    // Fixed
    private final int SHORT_RANGE_FORECAST_HOURS_DURATION = 3;

    // Maximum 16
    public final int LONG_RANGE_DAYS = 16;

    // Fixed
    private final int LONG_RANGE_FORECAST_HOURS_DURATION = 24;

    @PostConstruct
    private void constructed() {
        logger.log(Level.INFO, "Weather Forecast Updater created");
        client = ClientBuilder.newClient();
    }

    @Schedule(second = "*/10", minute = "*", hour = "*", persistent = false)
    public void testTask() {
        if (ScheduleActivator.WEATHER_FORECAST_UPDATER_TEST_TASK) {
            logger.log(Level.INFO, "{0}: scheduled task is getting fired", LocalDateTime.now());
//            logger.log(Level.INFO, "{0}: checking the weather", new Date());
//            Forecast forecast = client.target("http://localhost:8080/weather/rest/forecast")
//                    .request(MediaType.APPLICATION_JSON)
//                    .get(Forecast.class);
//            logger.log(Level.INFO, "Oracle says: {0}", forecast.getResult());
        }
    }

    public void testShortRangeRequest() {
        logger.log(Level.INFO, "{0}: asking short range forecast", LocalDateTime.now());
        ShortRangeForecast shortRange = getShortRangeForecast("Monza", "IT");
        logger.log(Level.INFO, "End of forecast request");
    }

    public void testLongRangeRequest() {
        logger.log(Level.INFO, "{0}: asking long range forecast", LocalDateTime.now());
        LongRangeForecast longRange = getLongRangeForecast("Monza", "IT");
        logger.log(Level.INFO, "End of forecast request");
    }

    /**
     * If start before now but the end is not, the weather forecast sequence
     * will start with the closest available forecast to the current time.
     *
     * @param start
     * @param end
     * @param city
     * @param countryID
     * @return
     * @throws InvalidInputException
     */
    public List<WeatherForecastBase> askForecast(LocalDateTime start, LocalDateTime end, String city, String countryID) throws InvalidInputException {
        ShortRangeForecast shortRangeForecast = null;
        LongRangeForecast longRangeForecast = null;

        List<WeatherForecastBase> forecasts;

        if (start == null || end == null) {
            throw new NullPointerException(EXCEPTION_ASK_FORECAST_NO_START_END);
        }

        if (start.isAfter(end)) {
            throw new InvalidInputException(InvalidInputException.WEATHER_FORECAST_SERVICE_ASK_FORECAST_START_AFTER_END);
        }

        forecasts = askForecast(city, countryID);

        forecasts = truncateAndCover(forecasts, start, end);

        print(start, end, city, countryID, forecasts);

        return forecasts;
    }

    public List<WeatherForecastBase> askForecast(String city, String countryID) {
        return mergeForecasts(getShortRangeForecast(city, countryID), getLongRangeForecast(city, countryID));
    }

    public List<WeatherForecastBase> askClosestMatch(LocalDateTime scheduledStart, LocalDateTime scheduledEnd, String city, String countryID, EnumSet<WeatherCondition> adverseConditions) throws InvalidInputException {
        List<WeatherForecastBase> forecastRange = askForecast(city, countryID);

        if (adverseConditions == null) {
            adverseConditions = EnumSet.noneOf(WeatherCondition.class);
        }

        adverseConditions.add(WeatherCondition.NOT_AVAILABLE);

        return lookForCompatibility(scheduledStart, scheduledEnd, forecastRange, adverseConditions);
    }

    private List<WeatherForecastBase> lookForCompatibility(LocalDateTime scheduledStart, LocalDateTime scheduledEnd, List<WeatherForecastBase> forecastRange, EnumSet<WeatherCondition> adverseConditions) throws InvalidInputException {
        if (scheduledStart == null || scheduledEnd == null) {
            throw new NullPointerException(EXCEPTION_ASK_FORECAST_NO_START_END);
        }

        if (scheduledStart.isAfter(scheduledEnd)) {
            throw new InvalidInputException(InvalidInputException.WEATHER_FORECAST_SERVICE_ASK_FORECAST_START_AFTER_END);
        }

        long duration = LocalDateTimeUtils.distance(scheduledStart, scheduledEnd);

        int currentMatchStartIndex = 0;
        Integer bestMatchStartIndex = null;
        Integer bestMatchEndIndex = null;

        long currentMatchDuration = 0;

        Long bestMatchDistance = null;

        for (int i = 0; i < forecastRange.size() - 1; i++) {
            WeatherForecastBase current = forecastRange.get(i);

            if (current != null) {
                if (adverseConditions.contains(current.getWeatherCondition())) {
                    currentMatchStartIndex = i + 1;
                    currentMatchDuration = 0;
                } else {
                    currentMatchDuration += current.getDuration();

                    if (currentMatchDuration >= duration) {
                        long currentMatchDistance = calculateDistance(forecastRange.get(currentMatchStartIndex).getForecastStart(), current.getForecastEnd(), scheduledStart, scheduledEnd);

                        // A match has been found, since we covered the duration range with acceptable conditions
                        if (bestMatchStartIndex != null) {
                            if (currentMatchDistance < bestMatchDistance) {
                                bestMatchStartIndex = currentMatchStartIndex;
                                bestMatchEndIndex = i;
                                bestMatchDistance = currentMatchDistance;
                            }
                        } else {
                            bestMatchStartIndex = currentMatchStartIndex;
                            bestMatchEndIndex = i;
                            bestMatchDistance = currentMatchDistance;
                        }

                        currentMatchDuration -= forecastRange.get(currentMatchStartIndex).getDuration();
                        currentMatchStartIndex++;
                    }
                }
            } else {
                currentMatchStartIndex = i + 1;
                currentMatchDuration = 0;
            }
        }

        List<WeatherForecastBase> result = new ArrayList<WeatherForecastBase>();

        if (bestMatchStartIndex != null) {
            for (int i = bestMatchStartIndex; i <= bestMatchEndIndex; i++) {
                result.add(forecastRange.get(i));
            }

            result = truncateDurationToClosestMatch(result, scheduledStart, scheduledEnd);
        } else {
            result = null;
        }
        
        return result;
    }

    private String getShortRangeRequestURL(String city, String countryID) {
        if (city == null || countryID == null) {
            throw new NullPointerException(EXCEPTION_REQUEST_NO_CITY_OR_COUNTRY);
        }

        return MessageFormat.format(OPENWEATHERMAP_API_SHORT_RANGE_BASE_URL, city, countryID, ApplicationVariables.OPENWEATHERMAP_API_KEY);
    }

    private String getLongRangeRequestURL(String city, String countryID) {
        if (city == null || countryID == null) {
            throw new NullPointerException(EXCEPTION_REQUEST_NO_CITY_OR_COUNTRY);
        }

        return MessageFormat.format(OPENWEATHERMAP_API_LONG_RANGE_BASE_URL, city, countryID, ApplicationVariables.OPENWEATHERMAP_API_KEY, LONG_RANGE_DAYS);
    }

    private ShortRangeForecast getShortRangeForecast(String city, String countryID) {
        try {
            return client.target(getShortRangeRequestURL(city, countryID)).request(MediaType.APPLICATION_JSON).get(ShortRangeForecast.class);
        } catch (NullPointerException npe) {
            throw npe;
        } catch (Exception e) {
            return null;
        }
    }

    private LongRangeForecast getLongRangeForecast(String city, String countryID) {
        try {
            return client.target(getLongRangeRequestURL(city, countryID)).request(MediaType.APPLICATION_JSON).get(LongRangeForecast.class);
        } catch (NullPointerException npe) {
            throw npe;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isShortRangeForecastNeeded(LocalDateTime start, LocalDateTime end) {
        return !(end.isBefore(LocalDateTime.now()) || start.isAfter(LocalDateTime.now().plusDays(SHORT_RANGE_DAYS + 1)));
    }

    private boolean isLongRangeForecastNeeded(LocalDateTime start, LocalDateTime end) {
        return !(end.isBefore(LocalDateTime.now().plusDays(SHORT_RANGE_DAYS - 1)) || start.isAfter(LocalDateTime.now().plusDays(LONG_RANGE_DAYS + 1)));
    }

    private List<WeatherForecastBase> convert(ShortRangeForecast shortRangeForecast) {
        List<WeatherForecastBase> result = new ArrayList<WeatherForecastBase>();

        WeatherForecastBase convertedForecast;
        WeatherForecastBase previousForecast = null;

        LocalDateTime start;
        LocalDateTime previousEnd;

        if (shortRangeForecast == null) {
            return result;
        }

        List<org.openweathermap.api.short_range.List> rawForecasts = shortRangeForecast.getList();
        Collections.sort(rawForecasts, new ShortForecastComparator());

        for (org.openweathermap.api.short_range.List rawForecast : rawForecasts) {
            if (rawForecast != null) {
                Integer unixTime = rawForecast.getDt();
                if (unixTime != null) {
                    convertedForecast = new WeatherForecastBase();

                    start = LocalDateTimeUtils.convertUnixToLocalDateTime(unixTime);
                    previousEnd = LocalDateTimeUtils.convertUnixToLocalDateTime(unixTime);

                    convertedForecast.setForecastStart(start);
                    if (previousForecast != null) {
                        previousForecast.setForecastEnd(previousEnd);
                    }

                    if (rawForecast.getWeather() != null && rawForecast.getWeather().size() > 0) {
                        org.openweathermap.api.short_range.Weather rawWeather = rawForecast.getWeather().get(0);

                        if (rawWeather != null) {
                            convertedForecast.setWeatherCondition(convertServiceCodeToWeatherCondition(rawWeather.getId()));
                        } else {
                            convertedForecast.setWeatherCondition(WeatherCondition.NOT_AVAILABLE);
                        }
                    } else {
                        convertedForecast.setWeatherCondition(WeatherCondition.NOT_AVAILABLE);
                    }

                    result.add(convertedForecast);
                    previousForecast = convertedForecast;
                }
            }
        }

        result.get(result.size() - 1).setForecastEnd(result.get(result.size() - 1).getForecastStart().plusHours(SHORT_RANGE_FORECAST_HOURS_DURATION));

        cleanFromInvalidWeatherForecasts(result);

        return result;
    }

    private List<WeatherForecastBase> convert(LongRangeForecast longRangeForecast) {
        List<WeatherForecastBase> result = new ArrayList<WeatherForecastBase>();

        WeatherForecastBase convertedForecast;
        WeatherForecastBase previousForecast = null;

        LocalDateTime start;
        LocalDateTime previousEnd;

        if (longRangeForecast == null) {
            return result;
        }

        List<org.openweathermap.api.long_range.List> rawForecasts = longRangeForecast.getList();
        Collections.sort(rawForecasts, new LongForecastComparator());

        for (org.openweathermap.api.long_range.List rawForecast : rawForecasts) {
            if (rawForecast != null) {
                Integer unixTime = rawForecast.getDt();
                if (unixTime != null) {
                    convertedForecast = new WeatherForecastBase();

                    start = LocalDateTimeUtils.convertUnixToLocalDateTime(unixTime);
                    previousEnd = LocalDateTimeUtils.convertUnixToLocalDateTime(unixTime);

                    convertedForecast.setForecastStart(start);
                    if (previousForecast != null) {
                        previousForecast.setForecastEnd(previousEnd);
                    }

                    if (rawForecast.getWeather() != null && rawForecast.getWeather().size() > 0) {
                        org.openweathermap.api.long_range.Weather rawWeather = rawForecast.getWeather().get(0);

                        if (rawWeather != null) {
                            convertedForecast.setWeatherCondition(convertServiceCodeToWeatherCondition(rawWeather.getId()));
                        } else {
                            convertedForecast.setWeatherCondition(WeatherCondition.NOT_AVAILABLE);
                        }
                    } else {
                        convertedForecast.setWeatherCondition(WeatherCondition.NOT_AVAILABLE);
                    }

                    result.add(convertedForecast);
                    previousForecast = convertedForecast;
                }
            }
        }

        result.get(result.size() - 1).setForecastEnd(result.get(result.size() - 1).getForecastStart().plusHours(LONG_RANGE_FORECAST_HOURS_DURATION));

        cleanFromInvalidWeatherForecasts(result);

        return result;
    }

    private WeatherCondition convertServiceCodeToWeatherCondition(Integer id) {
        WeatherCondition result;

        switch (id) {
            case 200:
                result = WeatherCondition.RAIN;
                break;
            case 201:
                result = WeatherCondition.RAIN;
                break;
            case 202:
                result = WeatherCondition.RAIN;
                break;
            case 210:
                result = WeatherCondition.RAIN;
                break;
            case 211:
                result = WeatherCondition.RAIN;
                break;
            case 212:
                result = WeatherCondition.RAIN;
                break;
            case 221:
                result = WeatherCondition.RAIN;
                break;
            case 231:
                result = WeatherCondition.RAIN;
                break;
            case 232:
                result = WeatherCondition.RAIN;
                break;
            case 300:
                result = WeatherCondition.RAIN;
                break;
            case 301:
                result = WeatherCondition.RAIN;
                break;
            case 302:
                result = WeatherCondition.RAIN;
                break;
            case 310:
                result = WeatherCondition.RAIN;
                break;
            case 311:
                result = WeatherCondition.RAIN;
                break;
            case 312:
                result = WeatherCondition.RAIN;
                break;
            case 313:
                result = WeatherCondition.RAIN;
                break;
            case 314:
                result = WeatherCondition.RAIN;
                break;
            case 321:
                result = WeatherCondition.RAIN;
                break;
            case 500:
                result = WeatherCondition.RAIN;
                break;
            case 501:
                result = WeatherCondition.RAIN;
                break;
            case 502:
                result = WeatherCondition.RAIN;
                break;
            case 503:
                result = WeatherCondition.RAIN;
                break;
            case 504:
                result = WeatherCondition.RAIN;
                break;
            case 511:
                result = WeatherCondition.RAIN;
                break;
            case 520:
                result = WeatherCondition.RAIN;
                break;
            case 521:
                result = WeatherCondition.RAIN;
                break;
            case 222:
                result = WeatherCondition.RAIN;
                break;
            case 531:
                result = WeatherCondition.RAIN;
                break;
            case 600:
                result = WeatherCondition.SNOW;
                break;
            case 601:
                result = WeatherCondition.SNOW;
                break;
            case 602:
                result = WeatherCondition.SNOW;
                break;
            case 611:
                result = WeatherCondition.SNOW;
                break;
            case 612:
                result = WeatherCondition.SNOW;
                break;
            case 615:
                result = WeatherCondition.SNOW;
                break;
            case 616:
                result = WeatherCondition.SNOW;
                break;
            case 620:
                result = WeatherCondition.SNOW;
                break;
            case 621:
                result = WeatherCondition.SNOW;
                break;
            case 622:
                result = WeatherCondition.SNOW;
                break;
            case 701:
                result = WeatherCondition.CLOUDS;
                break;
            case 711:
                result = WeatherCondition.CLOUDS;
                break;
            case 721:
                result = WeatherCondition.CLOUDS;
                break;
            case 731:
                result = WeatherCondition.CLOUDS;
                break;
            case 741:
                result = WeatherCondition.CLOUDS;
                break;
            case 751:
                result = WeatherCondition.CLOUDS;
                break;
            case 761:
                result = WeatherCondition.CLOUDS;
                break;
            case 762:
                result = WeatherCondition.CLOUDS;
                break;
            case 771:
                result = WeatherCondition.CLOUDS;
                break;
            case 781:
                result = WeatherCondition.CLOUDS;
                break;
            case 800:
                result = WeatherCondition.SUN;
                break;
            case 801:
                result = WeatherCondition.SUN;
                break;
            case 802:
                result = WeatherCondition.CLOUDS;
                break;
            case 803:
                result = WeatherCondition.CLOUDS;
                break;
            case 804:
                result = WeatherCondition.CLOUDS;
                break;
            case 900:
                result = WeatherCondition.RAIN;
                break;
            case 901:
                result = WeatherCondition.RAIN;
                break;
            case 902:
                result = WeatherCondition.RAIN;
                break;
            case 903:
                result = WeatherCondition.SNOW;
                break;
            case 904:
                result = WeatherCondition.SUN;
                break;
            case 905:
                result = WeatherCondition.SUN;
                break;
            case 906:
                result = WeatherCondition.SNOW;
                break;
            case 951:
                result = WeatherCondition.SNOW;
                break;
            case 952:
                result = WeatherCondition.SNOW;
                break;
            case 953:
                result = WeatherCondition.SNOW;
                break;
            case 954:
                result = WeatherCondition.SNOW;
                break;
            case 955:
                result = WeatherCondition.SNOW;
                break;
            case 956:
                result = WeatherCondition.CLOUDS;
                break;
            case 957:
                result = WeatherCondition.CLOUDS;
                break;
            case 958:
                result = WeatherCondition.CLOUDS;
                break;
            case 959:
                result = WeatherCondition.CLOUDS;
                break;
            case 960:
                result = WeatherCondition.CLOUDS;
                break;
            case 961:
                result = WeatherCondition.CLOUDS;
                break;
            case 962:
                result = WeatherCondition.CLOUDS;
                break;
            default:
                result = WeatherCondition.NOT_AVAILABLE;
                break;
        }

        return result;
    }

    private void cleanFromInvalidWeatherForecasts(List<WeatherForecastBase> convertedForecasts) {
        for (WeatherForecastBase forecast : convertedForecasts) {
            if (forecast == null || !forecast.isValid()) {
                convertedForecasts.remove(forecast);
            }
        }
    }

    private List<WeatherForecastBase> mergeForecasts(ShortRangeForecast shortRangeForecast, LongRangeForecast longRangeForecast) {
        List<WeatherForecastBase> convertedShortRangeForecasts = convert(shortRangeForecast);
        List<WeatherForecastBase> convertedLongRangeForecasts = convert(longRangeForecast);

        return mergeConvertedForecasts(convertedShortRangeForecasts, convertedLongRangeForecasts);
    }

    private List<WeatherForecastBase> mergeConvertedForecasts(List<WeatherForecastBase> convertedShortRangeForecasts, List<WeatherForecastBase> convertedLongRangeForecasts) {
        List<WeatherForecastBase> result = new ArrayList<WeatherForecastBase>();

        if (convertedShortRangeForecasts != null && convertedShortRangeForecasts.size() > 0) {
            Collections.sort(convertedShortRangeForecasts, new WeatherForecastBaseComparator());

            for (int i = 0; i < convertedShortRangeForecasts.size(); i++) {
                result.add(convertedShortRangeForecasts.get(i));
            }

            LocalDateTime shortRangeEnd = convertedShortRangeForecasts.get(convertedShortRangeForecasts.size() - 1).getForecastEnd();

            if (convertedLongRangeForecasts != null) {
                Collections.sort(convertedLongRangeForecasts, new WeatherForecastBaseComparator());

                for (int i = 0; i < convertedLongRangeForecasts.size(); i++) {
                    WeatherForecastBase wf = convertedLongRangeForecasts.get(i);

                    if (wf.getForecastStart().isBefore(shortRangeEnd) && wf.getForecastEnd().isAfter(shortRangeEnd)) {
                        wf.setForecastStart(shortRangeEnd);
                        result.add(wf);
                    }

                    if (wf.getForecastStart().isAfter(shortRangeEnd)) {
                        result.add(wf);
                    }
                }
            }
        } else {
            if (convertedLongRangeForecasts != null) {
                Collections.sort(convertedLongRangeForecasts, new WeatherForecastBaseComparator());

                for (int i = 0; i < convertedLongRangeForecasts.size(); i++) {
                    result.add(convertedLongRangeForecasts.get(i));
                }
            }
        }

        return result;
    }

    private List<WeatherForecastBase> truncateAndCover(List<WeatherForecastBase> forecasts, LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new NullPointerException();
        }

        if (forecasts == null) {
            forecasts = new ArrayList<WeatherForecastBase>();
        }

        // Truncate start,end, remove out of boundary, and leave untouched the rest
        List<WeatherForecastBase> toRemove = new ArrayList<WeatherForecastBase>();

        for (WeatherForecastBase wf : forecasts) {
            if (wf.getForecastEnd().isBefore(start)) {
                toRemove.add(wf);
            } else if (wf.getForecastStart().isAfter(end)) {
                toRemove.add(wf);
            } else {
                if (wf.getForecastStart().isBefore(start)) {
                    wf.setForecastStart(start);
                }
                if (wf.getForecastEnd().isAfter(end)) {
                    wf.setForecastEnd(end);
                }
            }
        }

        for (WeatherForecastBase wf : toRemove) {
            forecasts.remove(wf);
        }

        if (forecasts.size() == 0) {
            // Produce a single not avaible notification to cover the whole range of the forecast
            WeatherForecastBase wf = new WeatherForecastBase();
            wf.setWeatherCondition(WeatherCondition.NOT_AVAILABLE);
            wf.setForecastStart(start);
            wf.setForecastEnd(end);
            forecasts.add(wf);
        } else {
            Collections.sort(forecasts, new WeatherForecastBaseComparator());

            LocalDateTime resultStart = forecasts.get(0).getForecastStart();
            LocalDateTime resultEnd = forecasts.get(forecasts.size() - 1).getForecastEnd();

            // Ensures the start of the forecasts is the given start
            if (resultStart.isAfter(start)) {
                if (resultStart.isBefore(start.plusHours(FORECAST_ALLOWED_TIME_INCREASE_HOURS))) {
                    // We can change the first forecast start time to adapt it to our needs without losing too much accuracy
                    // Relies on the list being sorted!
                    forecasts.get(0).setForecastStart(start);
                } else {
                    WeatherForecastBase wf = new WeatherForecastBase();
                    wf.setWeatherCondition(WeatherCondition.NOT_AVAILABLE);
                    wf.setForecastStart(start);
                    wf.setForecastEnd(resultStart);
                    forecasts.add(wf);
                    Collections.sort(forecasts, new WeatherForecastBaseComparator());
                }
            }

            // Ensures the end of the forecasts is the given end
            if (resultEnd.isBefore(end)) {
                if (resultEnd.isAfter(end.minusHours(FORECAST_ALLOWED_TIME_INCREASE_HOURS))) {
                    // We can change the last forecast end time to adapt it to our needs without losing too much accuracy
                    // Relies on the list being sorted!
                    forecasts.get(forecasts.size() - 1).setForecastEnd(end);
                } else {
                    WeatherForecastBase wf = new WeatherForecastBase();
                    wf.setWeatherCondition(WeatherCondition.NOT_AVAILABLE);
                    wf.setForecastStart(resultEnd);
                    wf.setForecastEnd(end);
                    forecasts.add(wf);
                }
            }

            // Ensures there are no gaps between forecasts
            List<WeatherForecastBase> toAdd = new ArrayList<WeatherForecastBase>();

            WeatherForecastBase previous = forecasts.get(0);
            WeatherForecastBase current;

            for (int i = 1; i < forecasts.size() - 1; i++) {
                current = forecasts.get(i);

                if (!current.getForecastStart().equals(previous.getForecastEnd())) {
                    if (current.getForecastStart().isBefore(previous.getForecastEnd().plusHours(FORECAST_ALLOWED_TIME_INCREASE_HOURS))) {
                        // We can change the previous forecast end time to adapt it to our needs without losing too much accuracy
                        previous.setForecastEnd(current.getForecastStart());
                    } else {
                        WeatherForecastBase wf = new WeatherForecastBase();
                        wf.setWeatherCondition(WeatherCondition.NOT_AVAILABLE);
                        wf.setForecastStart(previous.getForecastEnd());
                        wf.setForecastEnd(current.getForecastStart());
                        toAdd.add(wf);
                    }

                }

                previous = current;
            }

            for (WeatherForecastBase wf : toAdd) {
                forecasts.add(wf);
            }

            Collections.sort(forecasts, new WeatherForecastBaseComparator());
        }

        return forecasts;
    }

    /**
     * Gives the minimum distance in seconds between the forecast interval and
     * the scheduled interval.
     *
     * @param forecastStart
     * @param forecastEnd
     * @param scheduledStart
     * @param scheduledEnd
     * @return
     */
    private long calculateDistance(LocalDateTime forecastStart, LocalDateTime forecastEnd, LocalDateTime scheduledStart, LocalDateTime scheduledEnd) throws InvalidInputException {
        long distance;
        if (LocalDateTimeUtils.distance(forecastEnd, scheduledEnd) >= 0) {
            // FE <= SS
            distance = LocalDateTimeUtils.distance(forecastEnd, scheduledEnd);
        } else if (LocalDateTimeUtils.distance(forecastStart, scheduledStart) >= 0 && LocalDateTimeUtils.distance(scheduledEnd, forecastEnd) >= 0) {
            // FS <= SS && FE >= SE
            distance = 0;
        } else if (LocalDateTimeUtils.distance(scheduledStart, forecastStart) >= 0) {
            // FS >= SS
            distance = LocalDateTimeUtils.distance(scheduledStart, forecastStart);
        } else {
            throw new InvalidInputException(InvalidInputException.WEATHER_FORECAST_SERVICE_INVALID_BEST_MATCH_CALCULATION);
        }

        return distance;
    }

    private List<WeatherForecastBase> truncateDurationToClosestMatch(List<WeatherForecastBase> result, LocalDateTime scheduledStart, LocalDateTime scheduledEnd) throws InvalidInputException {
        long eventDuration = LocalDateTimeUtils.distance(scheduledStart, scheduledEnd);
        
        if(eventDuration <= 0) 
            throw new InvalidInputException(InvalidInputException.WEATHER_FORECAST_SERVICE_ASK_FORECAST_START_AFTER_END);

        if (result != null && result.size() > 0) {
            LocalDateTime forecastStart = result.get(0).getForecastStart();
            LocalDateTime forecastEnd = result.get(result.size() - 1).getForecastEnd();

            if (LocalDateTimeUtils.distance(forecastEnd, scheduledEnd) >= 0) {
                // FE <= SS
                result = truncateAndCover(result, LocalDateTimeUtils.subtract(forecastEnd, eventDuration), forecastEnd);
            } else if (LocalDateTimeUtils.distance(forecastStart, scheduledStart) >= 0 && LocalDateTimeUtils.distance(scheduledEnd, forecastEnd) >= 0) {
                // FS <= SS && FE >= SE
                result = truncateAndCover(result, scheduledStart, scheduledEnd);
            } else if (LocalDateTimeUtils.distance(scheduledStart, forecastStart) >= 0) {
                // FS >= SS
                result = truncateAndCover(result, forecastStart, LocalDateTimeUtils.add(forecastStart, eventDuration));
            } else {
                throw new InvalidInputException(InvalidInputException.WEATHER_FORECAST_SERVICE_INVALID_BEST_MATCH_CALCULATION);
            }
        } else {
            result = null;
        }
        
        return result;
    }

    private class ShortForecastComparator implements Comparator<org.openweathermap.api.short_range.List> {

        @Override
        public int compare(org.openweathermap.api.short_range.List o1, org.openweathermap.api.short_range.List o2) {
            if (o1 == null || o2 == null) {
                return 0;
            }

            return o1.getDt() - o2.getDt();
        }
    }

    private class LongForecastComparator implements Comparator<org.openweathermap.api.long_range.List> {

        @Override
        public int compare(org.openweathermap.api.long_range.List o1, org.openweathermap.api.long_range.List o2) {
            if (o1 == null || o2 == null) {
                return 0;
            }

            return o1.getDt() - o2.getDt();
        }
    }

    private void print(LocalDateTime start, LocalDateTime end, String city, String countryID, List<WeatherForecastBase> result) {
        logger.log(Level.INFO, "City: " + city + ", Country: " + countryID + ",Start: " + start.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)) + ", End: " + end.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
        for (WeatherForecastBase wf : result) {
            logger.log(Level.INFO, wf.getForecastStart().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)) + "            " + wf.getForecastEnd().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)) + "           " + wf.getWeatherCondition());
        }

    }
}
