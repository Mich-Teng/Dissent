package proto;

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
    private int eventType = 0;
    private String origin = null;
    Map<String, Object> map = new HashMap<String, Object>();

    // deserialize a byte array into a event msg
    public EventMsg(byte[] arr) {
        // todo
    }

    public EventMsg(int eventType, String origin, Map<String, Object> map) {
        this.eventType = eventType;
        this.origin = origin;
        this.map = map;
    }

    public int getEventType() {
        return eventType;
    }

    public Object getField(String fieldName) {
        return map.get(fieldName);
    }

    public Map<String, Object> getMap() {
        return map;
    }


}
