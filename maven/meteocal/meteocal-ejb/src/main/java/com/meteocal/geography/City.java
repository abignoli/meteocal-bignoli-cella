package com.meteocal.geography;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class City {

    private String name;
    private String countryId;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return The countryId
     */
    public String getCountryId() {
        return countryId;
    }

    /**
     *
     * @param countryId The countryId
     */
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

}
