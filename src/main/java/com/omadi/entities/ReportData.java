package com.omadi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportData implements Serializable {
    private int id;

    @JsonProperty("form_part")
    private Integer formPart;

    public ReportData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getFormPart() {
        return formPart;
    }

    public void setFormPart(Integer formPart) {
        this.formPart = formPart;
    }
}
