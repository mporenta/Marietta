package com.omadi.controller;

import com.omadi.config.AppConfig;
import com.omadi.entities.Output;
import com.omadi.entities.Response;
import com.omadi.entities.Status;
import com.omadi.services.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://usve254453.serverprofi24.com")
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private MainService mainService;

    public String homePage() {
        logger.info("Home Page");

        return "home page";
    }

    @RequestMapping(value = "/start")
    public Response startScraping() {
        logger.info("Start Main Scraping Service");
        Response response = new Response();
        if (mainService != null) {
            response.setMessage("You can't start the service, because it has already been started!");
        } else {
            ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
            mainService = ctx.getBean(MainService.class);
            mainService.start();
            response.setMessage("");
        }

        return response;
    }

    @RequestMapping(value = "/stop")
    public Response stopScraping() {
        logger.info("Stop Main Scraping Service");
        Response response = new Response();
        if (mainService == null) {
            response.setMessage("You can't stop the process, because the service has not been started yet!");
        } else {
            mainService.stopProcess();
            response.setMessage("");
            while (!mainService.isFinished()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mainService = null;
        }

        return response;
    }

    @RequestMapping("/status")
    public Status checkStatus() {
        logger.info("Checking the Status");
        Status status = new Status();

        if (mainService == null) {
            status.setStopped(true);
        } else {
            status.setStopped(mainService.isStopped());
            status.setCurrentType(mainService.getObjectType());
            List<Output> outputList = mainService.getOutputList();
            status.setOutputSize(outputList.size());
        }

        return status;
    }
}
