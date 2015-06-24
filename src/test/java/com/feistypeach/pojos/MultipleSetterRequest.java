package com.feistypeach.pojos;

import com.feistypeach.plista4j.PListObject;
import com.feistypeach.plista4j.PListValue;

import java.util.Base64;

/**
 * Created by SiJohn on 11/18/2014.
 */
@PListObject
public class MultipleSetterRequest {

    private static final String TEST_DATA = "testData";
    private static final String KEY1 = "key1";
    private static final String KEY2 = "key2";
    private static final String KEY3 = "key3";


    private byte[] testData;

    @PListValue(key = KEY1)
    private String key1;

    @PListValue(key = KEY3)
    private String key3;

    @PListValue(key = KEY2)
    private String key2;

    @PListValue(key = TEST_DATA)
    public void setTestData(byte[] testData) {
        this.testData = testData;
    }

    /**
     * In some cases, the input key tag is "String" instead of "Data", hence this is required to convert the value from string to byte array.
     * @param testData
     */
    @PListValue(key = TEST_DATA)
    public void setTestDataForStringInput(String testData) {
        this.testData = Base64.getDecoder().decode(testData);
    }

    public byte[] getTestData() {
        return testData;
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey3() {
        return key3;
    }

    public void setKey3(String key3) {
        this.key3 = key3;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }
}
