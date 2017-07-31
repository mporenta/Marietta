package com.omadi.entities;

import javax.persistence.*;

@Entity
@Table(name = "omadi_report")
public class Output extends Base {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "node_id")
    private int nodeId;

    @Column(name = "bill_to")
    private String billTo;

    @Column(name = "job_accepted_time")
    private long jobAcceptedTime;

    @Column(name = "job_complete_time")
    private long jobCompleteTime;

    @Column(name = "new_invoice")
    private int newInvoice;

    @Column(name = "hook_and_miles_subtotal")
    private double hookAndMilesSubtotal;

    @Column(name = "work_service_hours")
    private double workServiceHours;

    @Column(name = "revenue_by_hour")
    private String revenueByHour;

    @Column(name = "truck")
    private String truck;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

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

    public double getHookAndMilesSubtotal() {
        return hookAndMilesSubtotal;
    }

    public void setHookAndMilesSubtotal(double hookAndMilesSubtotal) {
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
