package com.trainapp.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Document(value = "reservation_details")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservationDetails {
    private String timetableId;
    private String origin;
    private String destination;
    private List<String> passengerName;
}