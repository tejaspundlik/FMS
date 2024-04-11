package com.trainapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trainapp.model.LocationModel;
import com.trainapp.repository.LocationRepository;

import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationRepository repository;

    public List<LocationModel> getAll() {
        return repository.findAll();
    }

    public LocationModel getById(String id) {
        return repository.findById(id).get();
    }

    public LocationModel getByName(String name) {
        return repository.findByName(name);
    }

    public String addPath(LocationModel obj) {
        repository.save(obj);
        return "DONE :D";
    }

}
