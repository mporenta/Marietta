package com.omadi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omadi.entities.*;
import com.omadi.helpers.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Scope("prototype")
public class MainService extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(MainService.class);

    @Value("${omadi.api.username}")
    private String username;

    @Value("${omadi.api.password}")
    private String password;

    private ObjectMapper mapper = new ObjectMapper();

    private String objectType = "";

    private List<Output> outputList = new ArrayList<>();
    private List<ErrorReport> errorReportList = new ArrayList<>();

    @Autowired
    private OutputService outputService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private CSVWriterService csvWriterService;

    /*public void run1(String... args) throws Exception {
        BasicAuthRestTemplate restTemplate = new BasicAuthRestTemplate(username, password);
        ResponseEntity<String> entity = restTemplate.getForEntity(Url.getNodeUrl(21452), String.class);
        String body = entity.getBody();
        System.out.println(body);
    }*/

    @Override
    public void run() {
        try {
            _run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void _run() throws IOException {
        List<Type> types = typeService.getAll();
        logger.info("username: {}\npassword: {}", username, password);
        BasicAuthRestTemplate restTemplate = new BasicAuthRestTemplate(username, password);
        for (Type type : types) {
            if (this.isInterrupted()) {
                break;
            }
            objectType = type.getType();
            int page = 0;
            String url = Url.getReportUrl(type);
            while (true) {
                if (this.isInterrupted()) {
                    break;
                }
                logger.info("Requesting url {}", url);
                ResponseEntity<OmadiReport> reportResult = restTemplate.getForEntity(url, OmadiReport.class);

                if (reportResult.getStatusCode().equals(HttpStatus.OK)) {
                    OmadiReport omadiReport = reportResult.getBody();

                    logger.info("There are {} records, Current page is {}, Total Pages is {}",
                            omadiReport.getTotalCount(), omadiReport.getCurrentPage(), omadiReport.getTotalPages());

                    parseData(restTemplate, omadiReport);

                    page++;
                    if (page == omadiReport.getTotalPages()) {
                        break;
                    }
                    url = Url.getReportUrl(omadiReport.getNextPageQuery());
                } else {
                    logger.warn("Bad Request for the {} type!", type.getType());
                    break;
                }
            }

            type.setDone(1);
            typeService.save(type);
        }

        List<Output> outputList = outputService.getAll();
        logger.info("Saving... {} records", outputList.size());
        csvWriterService.start("output", Output.class);
        csvWriterService.save(outputList);

        if (errorReportList.size() > 0) {
            logger.info("Saving... {} error reports", errorReportList.size());
            csvWriterService.start("error_report", ErrorReport.class);
            csvWriterService.save(errorReportList);
        }
    }

    private void parseData(BasicAuthRestTemplate restTemplate, OmadiReport omadiReport) throws IOException {
        List<ReportData> reportDataList = omadiReport.getReportDataList();
        for (ReportData reportData : reportDataList) {
            if (this.isInterrupted()) {
                break;
            }
            if (reportData.getFormPart() >= 1) {
                int nodeId = reportData.getId();
                logger.info("Parsing id {}", nodeId);

                Output output = outputService.isExist(nodeId);
                if (output != null) {
                    continue;
                }

                ResponseEntity<String> result = restTemplate
                        .getForEntity(Url.getNodeUrl(nodeId), String.class);

                if (result.getStatusCode().equals(HttpStatus.OK)) {
                    String body = result.getBody();
                    Map<String, Object> dataMap = mapper.readValue(body, Map.class);

                    //Fields - Main Map
                    Map<String, Object> fields = (Map<String, Object>) dataMap.get("fields");
                    String type = String.valueOf(dataMap.get("type"));

                    //Bill to
                    String billTo = requestToObjectType(restTemplate, fields, "shop", "bill_to",
                            "account_name");

                    Long enforcementStartTimestamp = 0L;
                    if (fields.containsKey("enforcement_start_timestamp")) {
                        enforcementStartTimestamp = Long.parseLong(String.valueOf(fields.get("enforcement_start_timestamp")));
                    }

                    //Job accepted and completed time
                    if (!dataMap.containsKey("dispatch_fields")) {
                        addToErrorReport(nodeId, type, "dispatch_fields");
                        continue;
                    }
                    Map<String, String> dispatchFields = (Map<String, String>) dataMap.get("dispatch_fields");
                    Long jobAcceptedTime = getJobAcceptedTime(dispatchFields, enforcementStartTimestamp);

                    if (!dispatchFields.containsKey("job_complete_time")) {
                        addToErrorReport(nodeId, type, "job_complete_time");
                        continue;
                    }
                    Long jobCompleteTime = Long.parseLong(dispatchFields.get("job_complete_time"));
                    if (jobAcceptedTime.equals(jobCompleteTime)) {
                        jobAcceptedTime = enforcementStartTimestamp;
                    }

                    //New invoice
                    if (!fields.containsKey("new_invoice")) {
                        addToErrorReport(nodeId, type, "new_invoice");
                        continue;
                    }
                    Integer newInvoice = (Integer) fields.get("new_invoice");

                    //Hook and miles subtotal
                    if (!fields.containsKey("hook_and_miles_subtotal")) {
                        addToErrorReport(nodeId, type, "hook_and_miles_subtotal");
                        continue;
                    }
                    String objectValue = String.valueOf(fields.get("hook_and_miles_subtotal"));
                    Double hookAndMilesSubtotal = Double.parseDouble(objectValue);

                    //Calculate "Work service hours"
                    Double workServiceHours = (jobCompleteTime - jobAcceptedTime) / 3600.0D;
                    workServiceHours = BigDecimal.valueOf(workServiceHours)
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue();

                    //Calculate "Revenue by hour"
                    if (workServiceHours == 0) {
                        addToErrorReport(nodeId, type, "workServiceHours is 0, it is impossible divide by 0");
                        continue;
                    }
                    Double revenueByHour = hookAndMilesSubtotal / workServiceHours;
                    revenueByHour = BigDecimal.valueOf(revenueByHour)
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue();

                    //Truck
                    String truck = requestToObjectType(restTemplate, fields, "company_vehicle",
                            "truck", "vehicle_2");

                    //New Output Object
                    output = new Output();
                    output.setNodeId(nodeId);
                    output.setBillTo(billTo);
                    output.setJobAcceptedTime(jobAcceptedTime);
                    output.setJobCompleteTime(jobCompleteTime);
                    output.setNewInvoice(newInvoice);
                    output.setHookAndMilesSubtotal(hookAndMilesSubtotal);
                    output.setWorkServiceHours(workServiceHours);
                    output.setRevenueByHour(String.format("$%s", revenueByHour));
                    output.setTruck(truck);

                    outputService.save(output);
                }
            }
        }
    }

    private Long getJobAcceptedTime(Map<String, String> dispatchFields, Long enforcementStartTimestamp) {
        Long jobAcceptedTime;
        if (!dispatchFields.containsKey("job_accepted_time")) {
            if (!dispatchFields.containsKey("started_driving_time")) {
                jobAcceptedTime = enforcementStartTimestamp;
            } else {
                String startedDrivingTime = dispatchFields.get("started_driving_time");
                if (startedDrivingTime.trim().length() == 0) {
                    jobAcceptedTime = enforcementStartTimestamp;
                } else {
                    jobAcceptedTime = Long.parseLong(startedDrivingTime);
                }
            }
        } else {
            String jobAcceptedTimeStr = dispatchFields.get("job_accepted_time");
            if (jobAcceptedTimeStr.trim().length() == 0) {
                jobAcceptedTime = enforcementStartTimestamp;
            } else {
                jobAcceptedTime = Long.parseLong(jobAcceptedTimeStr);
            }
        }

        return jobAcceptedTime;
    }

    private void addToErrorReport(int id, String type, String field) {
        ErrorReport errorReport = new ErrorReport();
        errorReport.setId(id);
        errorReport.setMissingFields(field);
        errorReport.setType(type);

        errorReportList.add(errorReport);
    }

    private String requestToObjectType(BasicAuthRestTemplate restTemplate, Map<String, Object> fields,
                                       String objectType, String field1, String field2) throws IOException {
        Map<String, Integer> node = (Map<String, Integer>) fields.get(field1);
        Integer nodeId = node.get("node_id");
        ResponseEntity<String> result = restTemplate.getForEntity(Url.getObjectUrl(objectType, nodeId), String.class);
        String resultBody = result.getBody();
        Map<String, Object> resultMap = mapper.readValue(resultBody, Map.class);
        Map<String, Object> resultFields = (Map<String, Object>) resultMap.get("fields");

        return String.valueOf(resultFields.get(field2));
    }

    public List<Output> getOutputList() {
        return outputService.getAll();
    }

    public String getObjectType() {
        return objectType;
    }
}
