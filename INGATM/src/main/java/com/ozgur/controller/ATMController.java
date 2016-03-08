package com.ozgur.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozgur.model.ATM;

@RestController
public class ATMController {

	private List<ATM> atmList;

	public ATMController() {
		try {
			RestTemplate restTemplate = new RestTemplate();
			// first, broken json data is get from remote
			String str = restTemplate.getForObject("https://www.ing.nl/api/locator/atms/", String.class);
			// broken json fixed
			str = str.substring(6);
			// object mapper is used to unmarshall json to object
			ObjectMapper mapper = new ObjectMapper();

			atmList = Arrays.asList(mapper.readValue(str, ATM[].class));

			List<ATM> findATM = new ArrayList<ATM>();

			// gather whole ING atms
			for (ATM atm : atmList) {
				if (atm.getType().equals("ING")) {
					findATM.add(atm);
				}
			}
			atmList = findATM;

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception

		}
	}

	// test webservis http://localhost:8080/INGATM/atm/
	@RequestMapping(value = "/atm", method = RequestMethod.GET)
	public @ResponseBody List<ATM> getATM() {
		return atmList;
	}

	// test webservis http://localhost:8080/INGATM/atm/AMSTERDAM
	@RequestMapping(value = "/atm/{cityName}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<ATM> getRemoteATM(@PathVariable String cityName) {
		List<ATM> findATM = new ArrayList<ATM>();
		try {

			for (ATM atm : atmList) {
				if (atm.getAddress().getCity().equals(cityName)) {
					findATM.add(atm);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception

		}
		return findATM;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView welcome() {
		// When open the web page It will redirect to login page for authorization
		ModelAndView model = new ModelAndView("login");

		return model;

	}

	@RequestMapping(value = { "/index**" }, method = RequestMethod.GET)
	public ModelAndView getATMs() {

		ModelAndView model = new ModelAndView();
        // Un-authorized users redirect the access denied page. otherwise redirect to index welcome page
		if (getPrincipal().equals("anonymousUser"))
			model.setViewName("accessDenied");
		else
			model.setViewName("index");

		model.addObject("atms", atmList);
		return model;

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, headers = "Content-Type=application/x-www-form-urlencoded")
	@ResponseBody
	public ModelAndView searchCity(@RequestParam(value = "cityName", required = true) String cityName) {
		ModelAndView model = new ModelAndView();
		List<ATM> findATM = new ArrayList<ATM>();
		// this function find the filtered criteria in the atmslist
		for (ATM atm : atmList) {
			if (!cityName.isEmpty()) {
				if (atm.getAddress().getCity().equals(cityName.toUpperCase()))
					findATM.add(atm);
			} else
				findATM = atmList;
		}

		model.setViewName("index");
		model.addObject("atms", findATM);

		return model;
	}

	// Spring Security :
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {

		ModelAndView model = new ModelAndView();
		String page = "index";
		if (error != null) {
			model.addObject("error", "Invalid username and password!");
			page = "login";
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
			page = "login";
		}
		model.setViewName(page);

		return model;

	}

	@RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
		model.addAttribute("user", getPrincipal());
		return "accessDenied";
	}

	private String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

}