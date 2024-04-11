package com.trainapp.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.trainapp.dto.ReservationRequest;
import com.trainapp.dto.TTRequest;
import com.trainapp.dto.TTResponse;
import com.trainapp.model.TTModel;
import com.trainapp.service.TTService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/timetable")
public class TTController {
    @Autowired
    private TTService service;

    @GetMapping("/{name}")
    public List<TTResponse> getTrainTimeTable(@PathVariable String name) {
        return service.getByName(name);
    }

    @PostMapping
    public String postObj(@RequestBody TTModel obj) {
        service.addTT(obj);
        return "DONE :D";
    }

    @PutMapping("/")
    public ResponseEntity<TTModel> updateTT(@RequestBody TTRequest request) {
        System.out.println(request);
        TTModel updatedTT = service.updateTT(request);
        // send to reservation after this
        // we will send the tt id , source , destination , passenger list
        sendToReservationService(request.getTrainId(), request.getTrainPathName(), request.getOrigin(),
                request.getDestination(),
                request.getPassengerName());
        return ResponseEntity.ok(updatedTT);
    }

    private void sendToReservationService(String trainId, String trainPathName, String origin, String destination,
            List<String> passengerNames) {
        // Create a ReservationRequest object with the necessary data
        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setTrainId(trainId);
        reservationRequest.setTrainPathName(trainPathName);
        reservationRequest.setOrigin(origin);
        reservationRequest.setDestination(destination);
        reservationRequest.setPassengerName(passengerNames);

        // Send the ReservationRequest as a JSON request to the reservation service
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity("http://localhost:8082/api/reservation/", reservationRequest, Void.class);
    }

    @GetMapping
    public List<TTResponse> getAllTT() {
        return service.getAllTT();
    }

    @DeleteMapping("/{id}")
    public String deleteTT(@PathVariable String id) {
        return service.deleteTT(id);

    }

}
