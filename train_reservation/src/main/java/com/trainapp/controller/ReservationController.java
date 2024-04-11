package com.trainapp.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.trainapp.dto.ReservationRequest;
import com.trainapp.dto.ReservationResponse;
import com.trainapp.service.ReservationService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @SuppressWarnings({ "null", "unchecked" })
    @PostMapping("/")
    public ReservationResponse reservationBooking(@RequestBody ReservationRequest reservationRequest) {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(reservationRequest);
        Map<String, Object> locationResponse = restTemplate.getForObject(
                "http://localhost:8084/api/location/" + reservationRequest.getTrainPathName(), Map.class);
        List<String> stationList = (List<String>) locationResponse.get("stationList");
        int stationA = stationList.indexOf(reservationRequest.getOrigin());
        int stationB = stationList.indexOf(reservationRequest.getDestination());
        Map<String, Object> trainResponse = restTemplate.getForObject(
                "http://localhost:8081/api/train/" + reservationRequest.getTrainId(), Map.class);
        int trainFare = (Integer) trainResponse.get("trainFare");
        float totalFare = Math.abs(stationA - stationB) * reservationRequest.getPassengerName().size() * trainFare;

        ReservationResponse reservationResponse = new ReservationResponse();
        reservationResponse.setTrainId(reservationRequest.getTrainId());
        reservationResponse.setTrainRouteName(reservationRequest.getTrainPathName());
        reservationResponse.setOrigin(reservationRequest.getOrigin());
        reservationResponse.setDestination(reservationRequest.getDestination());
        reservationResponse.setPassengerName(reservationRequest.getPassengerName());
        reservationResponse.setQuantity(reservationRequest.getPassengerName().size());
        reservationResponse.setFare(totalFare);
        return reservationResponse;
    }

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.CREATED)
    public String getReservations(@RequestBody ReservationRequest reservationRequest) {
        reservationService.getAllReservations();
        return "Sent all reservations";
    }

}
