package com.example.smartcampus.resource;

import com.example.smartcampus.model.Sensor;
import com.example.smartcampus.model.Room;
import com.example.smartcampus.store.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collection;
import java.util.stream.Collectors;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    // GET all sensors (with optional filter)
    @GET
    public Collection<Sensor> getSensors(@QueryParam("type") String type) {

        if (type == null) {
            return DataStore.sensors.values();
        }

        return DataStore.sensors.values()
                .stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    // POST create sensor
    @POST
    public Response createSensor(Sensor sensor) {

        Room room = DataStore.rooms.get(sensor.getRoomId());

    
        if (room == null) {
            throw new com.example.smartcampus.exception.LinkedResourceNotFoundException(
                    "Room does not exist for the given roomId"
            );
        }

        DataStore.sensors.put(sensor.getId(), sensor);

        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }

    // SUB-RESOURCE for readings
    @Path("/{id}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("id") String id) {
        return new SensorReadingResource(id);
    }
}