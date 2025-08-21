package com.apirest.users.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class PhoneDto {

    @JsonProperty("numero")
    @JsonAlias("number")
    @NotBlank
    private String number;

    @JsonProperty("codigoCiudad")
    @JsonAlias({"cityCode", "citycode"})
    @NotBlank
    private String cityCode;

    @JsonProperty("codigoPais")
    @JsonAlias({"countryCode", "contrycode"})
    @NotBlank
    private String countryCode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCitycode() {
        return cityCode;
    }

    public String getContrycode() {
        return countryCode;
    }
}