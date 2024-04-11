package com.trainapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trainapp.dto.TTRequest;
import com.trainapp.dto.TTResponse;
import com.trainapp.model.TTModel;
import com.trainapp.repository.TTRepo;

@Service
public class TTService {

    @Autowired
    private TTRepo repository;

    public List<TTResponse> getAllTT() {
        return repository.findAll().stream().map(this::mapTTToResponse).toList();
    }

    public List<TTResponse> getByName(String name) {
        List<TTModel> model_obj = repository.findByTrainId(name);
        return model_obj.stream().map(this::mapTTToResponse).toList();

    }

    public TTResponse mapTTToResponse(TTModel obj) {
        return TTResponse.builder()
                .trainId(obj.getTrainId())
                .date(obj.getDate())
                .trainPathName(obj.getTrainPathName())
                .capacity(obj.getCapacity())
                .build();
    }

    public void addTT(TTModel obj) {
        repository.save(obj);
    }

    public TTModel updateTT(TTRequest request) {
        TTModel existingTTModel = repository.findByTrainIdAndDate(request.getTrainId(), request.getDate());
        if (existingTTModel != null) {
            existingTTModel.setCapacity(existingTTModel.getCapacity() + request.getCapacity());
            return repository.save(existingTTModel);
        } else {
            TTModel newTTModel = new TTModel();
            newTTModel.setTrainId(request.getTrainId());
            newTTModel.setDate(request.getDate());
            newTTModel.setTrainPathName(request.getTrainPathName());
            newTTModel.setCapacity(request.getCapacity());
            return repository.save(newTTModel);
        }
    }

    public String deleteTT(String trainId) {
        repository.deleteByTrainId(trainId);
        return "DELETED :D";
    }
}
