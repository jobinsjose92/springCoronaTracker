package com.jobins.coronatracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jobins.coronatracker.models.LocationStats;
import com.jobins.coronatracker.services.CoronaVirusDataService;
import java.util.List;

@Controller
public class HomeController {
	@Autowired
	
	CoronaVirusDataService coronadataservice;
	@GetMapping("/")
public String home(Model model )
{
		
		List<LocationStats> allStats = coronadataservice.getAllStats();
		int totCases = allStats.stream().mapToInt(stat->stat.getLatestTotalCases()).sum();
		model.addAttribute("locationStats",allStats);
		model.addAttribute("totalcases",totCases);
	return "home";
}
}
