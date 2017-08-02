package com.omadi.controller;

import com.omadi.entities.Output;
import com.omadi.entities.Response;
import com.omadi.entities.Status;
import com.omadi.services.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:63344", "http://usve254453.serverprofi24.com"})
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private MainService mainService;

    @Autowired
    private ApplicationContext context;

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

            /*String[] beanDefinitionNames = context.getBeanDefinitionNames();
            for (int i = 0; i < beanDefinitionNames.length; i++) {
                String beanDefinitionName = beanDefinitionNames[i];
                logger.info(beanDefinitionName);
            }*/
            mainService = context.getBean(MainService.class);

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
            mainService.interrupt();
            response.setMessage("");
            try {
                mainService.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
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
            status.setStopped(mainService.isInterrupted());
            status.setCurrentType(mainService.getObjectType());
            List<Output> outputList = mainService.getOutputList();
            status.setOutputSize(outputList.size());
        }

        return status;
    }
}
