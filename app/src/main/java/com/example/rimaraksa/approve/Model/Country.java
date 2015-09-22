package com.example.rimaraksa.approve.Model;

import com.example.rimaraksa.approve.Global;

import java.io.Serializable;

/**
 * Created by rimaraksa on 20/9/15.
 */
public class Country implements Serializable {

    String countryCode;
    String countryName;
    String countryPhoneCode;

    public Country(String countryCode, String countryPhoneCode, String countryName) {
        this.countryCode = countryCode;
        this.countryPhoneCode = countryPhoneCode;
        this.countryName = countryName;
    }

    public String getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public void setCountryPhoneCode(String countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }




}
