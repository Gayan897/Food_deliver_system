package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.dto.DeliveryPersonRequest;
import com.fooddelivery.food_delivery.dto.DeliveryPersonResponse;
import com.fooddelivery.food_delivery.entity.DeliveryPerson;
import com.fooddelivery.food_delivery.repository.DeliveryPersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryPersonRepository repository;

    // ✅ ADD THIS: GET ALL DELIVERY PERSONS
    public List<DeliveryPersonResponse> getAllDeliveryPersons() {
        System.out.println("📋 Fetching all delivery persons from database...");
        return repository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeliveryPersonResponse registerDeliveryPerson(DeliveryPersonRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (repository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone already exists");
        }

        DeliveryPerson person = new DeliveryPerson();
        person.setName(request.getName());
        person.setPhone(request.getPhone());
        person.setEmail(request.getEmail());
        person.setVehicleType(DeliveryPerson.VehicleType.valueOf(request.getVehicleType().toUpperCase()));
        person.setVehicleNumber(request.getVehicleNumber());
        person.setLicenseNumber(request.getLicenseNumber());
        person.setIsAvailable(true);
        person.setIsActive(true);

        person.setRating(request.getRating() != null ? request.getRating() : 0.0);

        DeliveryPerson saved = repository.save(person);
        return convertToResponse(saved);
    }

    public DeliveryPersonResponse getById(Long id) {
        DeliveryPerson person = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery person not found"));
        return convertToResponse(person);
    }

    public List<DeliveryPersonResponse> getAvailableDeliveryPersons() {
        return repository.findByIsAvailableAndIsActive(true, true).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeliveryPersonResponse updateAvailability(Long id, Boolean isAvailable) {
        DeliveryPerson person = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery person not found"));
        person.setIsAvailable(isAvailable);
        return convertToResponse(repository.save(person));
    }

    @Transactional
    public DeliveryPersonResponse updateLocation(Long id, Double latitude, Double longitude) {
        DeliveryPerson person = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery person not found"));
        person.setCurrentLatitude(latitude);
        person.setCurrentLongitude(longitude);
        return convertToResponse(repository.save(person));
    }
    @Transactional
    public DeliveryPersonResponse updateDeliveryPerson(Long id, DeliveryPersonRequest request) {
        DeliveryPerson person = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery person not found"));

        // Check if email is being changed and if it already exists
        if (!person.getEmail().equals(request.getEmail()) &&
                repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Check if phone is being changed and if it already exists
        if (!person.getPhone().equals(request.getPhone()) &&
                repository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone already exists");
        }

        person.setName(request.getName());
        person.setPhone(request.getPhone());
        person.setEmail(request.getEmail());
        person.setVehicleType(DeliveryPerson.VehicleType.valueOf(request.getVehicleType().toUpperCase()));
        person.setVehicleNumber(request.getVehicleNumber());
        person.setLicenseNumber(request.getLicenseNumber());

        if (request.getRating() != null) {
            person.setRating(request.getRating());
        }

        DeliveryPerson updated = repository.save(person);
        return convertToResponse(updated);
    }

    // ✅ ADD THIS: DELETE DELIVERY PERSON
    @Transactional
    public void deleteDeliveryPerson(Long id) {
        DeliveryPerson person = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery person not found"));
        repository.delete(person);
    }

    private DeliveryPersonResponse convertToResponse(DeliveryPerson person) {
        DeliveryPersonResponse response = new DeliveryPersonResponse();
        response.setId(person.getId());
        response.setName(person.getName());
        response.setPhone(person.getPhone());
        response.setEmail(person.getEmail());
        response.setVehicleType(person.getVehicleType().name());
        response.setVehicleNumber(person.getVehicleNumber());
        response.setLicenseNumber(person.getLicenseNumber());
        response.setIsAvailable(person.getIsAvailable());
        response.setIsActive(person.getIsActive());
        response.setCurrentLatitude(person.getCurrentLatitude());
        response.setCurrentLongitude(person.getCurrentLongitude());
        response.setRating(person.getRating());
        response.setTotalDeliveries(person.getTotalDeliveries());
        response.setCreatedAt(person.getCreatedAt());
        response.setUpdatedAt(person.getUpdatedAt());
        return response;
    }
}