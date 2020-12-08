package message.utils;
import java.util.LinkedList;
import java.util.Properties;

public class LinkedProperities extends Properties {

    private LinkedList<String> linkedList = new LinkedList<String>();



    @Override
    public synchronized Object put(Object key, Object value) {
        linkedList.add((String) key);
        return super.put(key, value);
    }


    public LinkedList<String> orderStringPropertyNames() {
        return linkedList;
    }

}