package com.spring.corona.controller;


import com.spring.corona.models.LocationStats;
import com.spring.corona.service.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {


    @Autowired
    private CoronaVirusDataService coronaVirusDataService;


    @GetMapping("/")
    public String home(Model model) throws IOException, InterruptedException {
        List<LocationStats> locationStatsList=coronaVirusDataService.fetchVirusData();
        int total=locationStatsList.stream().mapToInt(stat->stat.getLatestReportedCases()).sum();
        LocationStats india = locationStatsList.stream()
                .filter(stat -> "India".equals(stat.getCountry()))
                .findAny()
                .orElse(null);
        model.addAttribute("locationStats",coronaVirusDataService.fetchVirusData());
        model.addAttribute("totalReportedCases",total);
        model.addAttribute("totalCasesInIndia",india.getLatestReportedCases());
        return "home";
    }
}
