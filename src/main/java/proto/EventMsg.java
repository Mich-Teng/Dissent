package proto;

import util.Utilities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-21 10:01.
 * Package: proto
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class EventMsg implements Serializable {
    // message type
    private int eventType = 0;
    // message origin
    private String origin = null;
    // key-value map
    Map<String, Object> map = new HashMap<String, Object>();

    /**
     * * deserialize a byte array into a event msg 
     * @param arr input byte array
     */
    public EventMsg(byte[] arr) {
        EventMsg eventMsg = (EventMsg) Utilities.deserialize(arr);
        this.eventType = eventMsg.getEventType();
        this.origin = eventMsg.getOrigin();
        this.map = eventMsg.getMap();
    }

    /**
     * * general constructor 
     * @param eventType event type
     * @param origin message origin
     * @param map key -value map
     */
    public EventMsg(int eventType, String origin, Map<String, Object> map) {
        this.eventType = eventType;
        this.origin = origin;
        this.map = map;
    }

    /**
     * * get event type which is defined in EventType 
     * @return
     */
    public int getEventType() {
        return eventType;
    }

    /**
     * * get data whose key is fieldName 
     * @param fieldName key 
     * @return if doesn't exist, return null. Otherwise return the value
     */
    public Object getField(String fieldName) {
        return map.get(fieldName);
    }

    /**
     * * get key value map 
     * @return
     */
    public Map<String, Object> getMap() {
        return map;
    }

    /**
     * * add a key-value pair into map 
     * @param key key
     * @param val value
     */
    public void add(String key, Object val) {
        map.put(key, val);
    }

    /**
     * * remove a specific key in map 
     * @param key key
     */
    public void remove(String key) {
        map.remove(key);
    }

    /**
     * * get origin
     * @return origin
     */
    public String getOrigin() {
        return origin;
    }
}
