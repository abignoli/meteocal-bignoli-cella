/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.entities.comparators;

import com.meteocal.business.entities.Event;
import java.util.Comparator;

/**
 *
 * @author AB
 */
public class EventComparator implements Comparator<Event> {

    @Override
    public int compare(Event o1, Event o2) {
        if (o1 == null || o2 == null) {
            return 0;
        }
        if (o1.getStart().isBefore(o2.getStart())) {
            return -1;
        }
        if (o2.getStart().isBefore(o1.getStart())) {
            return 1;
        }

        return 0;
    }
}
