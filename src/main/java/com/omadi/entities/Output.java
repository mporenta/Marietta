package com.omadi.entities;

public class Output extends Base {
    private String billTo;
    private long jobAcceptedTime;
    private long jobCompleteTime;
    private int newInvoice;
    private int hookAndMilesSubtotal;
    private double workServiceHours;
    private String revenueByHour;
    private String truck;

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public long getJobAcceptedTime() {
        return jobAcceptedTime;
    }

    public void setJobAcceptedTime(long jobAcceptedTime) {
        this.jobAcceptedTime = jobAcceptedTime;
    }

    public long getJobCompleteTime() {
        return jobCompleteTime;
    }

    public void setJobCompleteTime(long jobCompleteTime) {
        this.jobCompleteTime = jobCompleteTime;
    }

    public int getNewInvoice() {
        return newInvoice;
    }

    public void setNewInvoice(int newInvoice) {
        this.newInvoice = newInvoice;
    }

    public int getHookAndMilesSubtotal() {
        return hookAndMilesSubtotal;
    }

    public void setHookAndMilesSubtotal(int hookAndMilesSubtotal) {
        this.hookAndMilesSubtotal = hookAndMilesSubtotal;
    }

    public double getWorkServiceHours() {
        return workServiceHours;
    }

    public void setWorkServiceHours(double workServiceHours) {
        this.workServiceHours = workServiceHours;
    }

    public String getRevenueByHour() {
        return revenueByHour;
    }

    public void setRevenueByHour(String revenueByHour) {
        this.revenueByHour = revenueByHour;
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public String[] toArray() {
        String[] values = new String[8];

        values[0] = getBillTo();
        values[1] = String.valueOf(getJobAcceptedTime());
        values[2] = String.valueOf(getJobCompleteTime());
        values[3] = String.valueOf(getNewInvoice());
        values[4] = String.valueOf(getHookAndMilesSubtotal());
        values[5] = String.valueOf(getWorkServiceHours());
        values[6] = getRevenueByHour();
        values[7] = getTruck();

        return values;
    }
}
