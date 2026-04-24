package com.example.smartcampus.resource;

import com.example.smartcampus.model.Sensor;
import com.example.smartcampus.model.Sensor;
import com.example.smartcampus.model.SensorReading;
import com.example.smartcampus.model.SensorReading;
import com.example.smartcampus.store.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadings() {
        List<SensorReading> sensorReadings = DataStore.readings.get(sensorId);

        if (sensorReadings == null) {
            sensorReadings = new ArrayList<>();
        }

        return Response.ok(sensorReadings).build();
    }

    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found")
                    .build();
        }

       
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new com.example.smartcampus.exception.SensorUnavailableException(
                    "Sensor is under maintenance and cannot accept new readings"
            );
        }

        List<SensorReading> sensorReadings = DataStore.readings.get(sensorId);

        if (sensorReadings == null) {
            sensorReadings = new ArrayList<>();
            DataStore.readings.put(sensorId, sensorReadings);
        }

        sensorReadings.add(reading);

        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}