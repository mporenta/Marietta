package com.omadi.entities;

public class ErrorReport extends Base {
    private int id;
    private String type;
    private String missingFields;

    public ErrorReport() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMissingFields() {
        return missingFields;
    }

    public void setMissingFields(String missingFields) {
        this.missingFields = missingFields;
    }

    @Override
    public String[] toArray() {
        String[] values = new String[3];

        values[0] = String.valueOf(getId());
        values[1] = getType();
        values[2] = getMissingFields();

        return values;
    }
}
