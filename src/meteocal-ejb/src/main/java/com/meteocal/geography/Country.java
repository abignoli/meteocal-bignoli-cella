
package com.meteocal.geography;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Country {

    private String name;
    private String id;
    private List<City> city = new ArrayList<City>();

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The city
     */
    public List<City> getCity() {
        return city;
    }

    /**
     * 
     * @param city
     *     The city
     */
    public void setCity(List<City> city) {
        this.city = city;
    }


}
