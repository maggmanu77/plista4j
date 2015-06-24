package com.feistypeach.pojos;

import com.feistypeach.plista4j.PListObject;
import com.feistypeach.plista4j.PListValue;

/**
 * Created by SiJohn on 11/18/2014.
 */
@PListObject
public class CheckRequest {
    @PListValue(key = "UDID")
    private String udid;

    @PListValue(key = "data")
    private byte[] data;

    public String getUdid() {
        return udid;
    }

    public byte[] getData() {
        return data;
    }
}
