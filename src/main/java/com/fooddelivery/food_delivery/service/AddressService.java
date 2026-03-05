package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.dto.AddressRequest;
import com.fooddelivery.food_delivery.dto.AddressResponse;
import com.fooddelivery.food_delivery.entity.Address;
import com.fooddelivery.food_delivery.entity.User;
import com.fooddelivery.food_delivery.repository.AddressRepository;
import com.fooddelivery.food_delivery.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    public List<AddressResponse> getUserAddresses(Long userId) {
        List<Address> addresses = addressRepository.findByUserId(userId);
        return addresses.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressResponse addAddress(Long userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // If this is the first address or marked as default, set it as default
        List<Address> existingAddresses = addressRepository.findByUserId(userId);
        if (existingAddresses.isEmpty() || request.getIsDefault()) {
            // Remove default from other addresses
            existingAddresses.forEach(addr -> {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            });
        }

        Address address = new Address();
        address.setUser(user);
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setPostalCode(request.getPostalCode());
        address.setState(request.getState());
        address.setLandmark(request.getLandmark());
        address.setIsDefault(existingAddresses.isEmpty() || request.getIsDefault());
        address.setType(Address.AddressType.valueOf(request.getType().toUpperCase()));

        Address savedAddress = addressRepository.save(address);
        return convertToResponse(savedAddress);
    }

    @Transactional
    public AddressResponse updateAddress(Long addressId, AddressRequest request) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setPostalCode(request.getPostalCode());
        address.setState(request.getState());
        address.setLandmark(request.getLandmark());

        if (request.getIsDefault()) {
            // Remove default from other addresses
            List<Address> otherAddresses = addressRepository.findByUserId(address.getUser().getId());
            otherAddresses.forEach(addr -> {
                if (!addr.getId().equals(addressId)) {
                    addr.setIsDefault(false);
                    addressRepository.save(addr);
                }
            });
            address.setIsDefault(true);
        }

        address.setType(Address.AddressType.valueOf(request.getType().toUpperCase()));

        Address updatedAddress = addressRepository.save(address);
        return convertToResponse(updatedAddress);
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        addressRepository.delete(address);
    }

    private AddressResponse convertToResponse(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getAddressLine(),
                address.getCity(),
                address.getPostalCode(),
                address.getState(),
                address.getLandmark(),
                address.getIsDefault(),
                address.getType().name(),
                address.getCreatedAt()
        );
    }
}
