package com.hackerrank.sample.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.dto.FilteredProducts;
import com.hackerrank.sample.dto.SortedProducts;

@RestController
public class SampleController {

	final String uri = "https://jsonmock.hackerrank.com/api/inventory";
	RestTemplate restTemplate = new RestTemplate();
	String result = restTemplate.getForObject(uri, String.class);
	JSONObject root = new JSONObject(result);

	JSONArray data = root.getJSONArray("data");

	@CrossOrigin
	@GetMapping("/filter/price/{initial_price}/{final_price}")
	private ResponseEntity<ArrayList<FilteredProducts>> filtered_books(@PathVariable("initial_price") int init_price,
			@PathVariable("final_price") int final_price) {

		try {

			ArrayList<FilteredProducts> books = new ArrayList<FilteredProducts>();
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = data.toString();
			ArrayList<FilteredProducts> products = objectMapper.readValue(jsonString,
					new TypeReference<ArrayList<FilteredProducts>>() {
					});

			books = (ArrayList<FilteredProducts>) products.stream()
					.filter(p -> p.Price >= init_price && p.Price <= final_price)
					.collect(Collectors.toList());

			books = (ArrayList<FilteredProducts>) books.stream().sorted((p1, p2) -> Integer.compare(p1.Price, p2.Price))
					.collect(Collectors.toList());
			return new ResponseEntity<ArrayList<FilteredProducts>>(books, HttpStatus.OK);

		} catch (Exception E) {
			System.out.println("Error encountered : " + E.getMessage());
			return new ResponseEntity<ArrayList<FilteredProducts>>(HttpStatus.NOT_FOUND);
		}

	}

	@CrossOrigin
	@GetMapping("/sort/price")
	private ResponseEntity<ArrayList<SortedProducts>> sorted_books() {

		try {

			SortedProducts[] ans = new SortedProducts[data.length()];

			ObjectMapper mapper = new ObjectMapper();
			String jsonString = data.toString();
			ArrayList<SortedProducts> sorted = mapper.readValue(jsonString,
					new TypeReference<ArrayList<SortedProducts>>() {
					});

			ArrayList<SortedProducts> prod = (ArrayList<SortedProducts>) sorted.stream()
					.sorted((p1, p2) -> Integer.compare(p1.Price, p2.Price)).collect(Collectors.toList());

			return new ResponseEntity<ArrayList<SortedProducts>>(prod, HttpStatus.OK);

		} catch (Exception E) {
			System.out.println("Error encountered : " + E.getMessage());
			return new ResponseEntity<ArrayList<SortedProducts>>(HttpStatus.NOT_FOUND);
		}

	}

	@CrossOrigin
	@GetMapping("/sort/pricearray")
	private ResponseEntity<SortedProducts[]> sorted_books_array() {

		try {

			// SortedProducts[] ans = new SortedProducts[data.length()];

			ObjectMapper mapper = new ObjectMapper();
			String jsonString = data.toString();
			ArrayList<SortedProducts> sorteds = mapper.readValue(jsonString,
					new TypeReference<ArrayList<SortedProducts>>() {
					});

			SortedProducts[] prod = sorteds.stream()
					.sorted((p1, p2) -> Integer.compare(p1.Price, p2.Price)).map(sorted -> sorted.Price)
					.toArray(SortedProducts[]::new);

			return new ResponseEntity<SortedProducts[]>(prod, HttpStatus.OK);

		} catch (Exception E) {
			System.out.println("Error encountered : " + E.getMessage());
			return new ResponseEntity<SortedProducts[]>(HttpStatus.NOT_FOUND);
		}

	}

}
