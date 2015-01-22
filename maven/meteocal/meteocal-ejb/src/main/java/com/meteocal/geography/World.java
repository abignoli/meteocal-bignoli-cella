
package com.meteocal.geography;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class World {

    private List<Country> country = new ArrayList<Country>();

    /**
     * 
     * @return
     *     The country
     */
    public List<Country> getCountry() {
        return country;
    }

    /**
     * 
     * @param country
     *     The country
     */
    public void setCountry(List<Country> country) {
        this.country = country;
    }

}
