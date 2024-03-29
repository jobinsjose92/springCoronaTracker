package com.jobins.coronatracker.services;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jobins.coronatracker.models.LocationStats;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
@Service
public class CoronaVirusDataService {

	
	public static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
	
	private List<LocationStats> allStats= new ArrayList<>();
	
	public List<LocationStats> getAllStats() {
		return allStats;
	}

	

	@PostConstruct
	//@Scheduled is used call a method on the daily or scheduled time basis
	// rememeber to call enable scheduling in main class

	public void fetchVirusData() throws IOException, InterruptedException
	{
		 List<LocationStats> newStats= new ArrayList<>();
		
		//sending the http request by the client and displaying the htt response from server
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();
		HttpResponse<String> httpResponse = client.send(request,HttpResponse.BodyHandlers.ofString());
		
		//converting the csv format in string and then parsing it
		
		StringReader csvBodyReader = new StringReader(httpResponse.body());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
		
		
		
		for(CSVRecord record:records) {
			LocationStats locationstat=new LocationStats();
			
			locationstat.setState(record.get("Province/State"));
			locationstat.setCountry(record.get("Country/Region"));
			locationstat.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
			System.out.println(locationstat);
			newStats.add(locationstat);
		}
		this.allStats=newStats;
	}
	
}
