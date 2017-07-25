package com.omadi.controller;

import com.omadi.entities.ErrorReport;
import com.omadi.entities.Output;
import com.omadi.services.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MainService mainService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody String homePage() {
        logger.info("Home Page");

        return "home page";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/start")
    public @ResponseBody String startScraping() {
        logger.info("Start Main Scraping Service");
        if (mainService.isStarted()) {
            return "The service has already been started! You can't start it multiple times.";
        }
        if (mainService.isStopped()) {
            mainService.run();
        } else {
            mainService.start();
        }

        return "The service has been successfully started!";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stop")
    public @ResponseBody String stopScraping() {
        logger.info("Stop Main Scraping Service");
        if (!mainService.isStarted()) {
            return "The service has not been started yet!";
        }
        mainService.stopProcess();

        return "The service has been successfully stopped!";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/status")
    public @ResponseBody String checkStatus() {
        logger.info("Checking the Status");

        List<Output> outputList = mainService.getOutputList();
//        List<ErrorReport> errorReportList = mainService.getErrorReportList();

        return String.format("Scraped %s records", outputList.size());
    }
}
