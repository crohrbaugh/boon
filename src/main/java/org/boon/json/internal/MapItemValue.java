package org.boon.json.internal;

import java.util.Map;

import static org.boon.Exceptions.die;

public class MapItemValue  implements Map.Entry <String, Value>{

    Value name;
    Value value;

    private String key = null;

    private  static final boolean internKeys = Boolean.parseBoolean (System.getProperty ( "org.boon.json.implementation.internKeys", "true" ));



    public MapItemValue (Value name, Value value) {
        this.name = name;
        this.value = value;

    }

    @Override
    public String getKey() {
         if (key == null) {
            if (internKeys) {
                key =  name.toString ();
            } else {
                key =  name.toString ().intern ();
            }
         }
         return key;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public Value setValue( Value value ) {
        die ( "not that kind of Entry");
        return null;
    }

    public Value name() {
        return name;
    }

    public void name( Value name ) {
        this.name = name;
    }

    public void value( Value value ) {
        this.value = value;
    }
}
