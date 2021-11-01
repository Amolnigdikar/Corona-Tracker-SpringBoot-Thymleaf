package com.spring.corona.service;

import com.spring.corona.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<LocationStats> locationStatsList=new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public List<LocationStats> fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats=new ArrayList<>();
        HttpClient httpClient= HttpClient.newHttpClient();
        HttpRequest request=HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();

        HttpResponse<String> response=httpClient.send(request,HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader= new StringReader(response.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {
            LocationStats locationStats=new LocationStats();
            locationStats.setState(record.get("Province/State").length() > 0 ? record.get("Province/State") : record.get("Country/Region"));
            locationStats.setCountry(record.get("Country/Region"));
            locationStats.setLatestReportedCases(Integer.parseInt(record.get(record.size()-1)));
            int latestCases=Integer.parseInt(record.get(record.size()-1));
            int previousDayCases=Integer.parseInt(record.get(record.size()-2));
            locationStats.setDiffFromPreviousDay(latestCases-previousDayCases);
            System.out.println(locationStats);
            newStats.add(locationStats);
        }

        this.locationStatsList=newStats;
        return newStats;
    }
}
