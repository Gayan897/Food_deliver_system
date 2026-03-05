package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.dto.PaymentRequest;
import com.fooddelivery.food_delivery.entity.Payment;
import com.fooddelivery.food_delivery.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository repository;

    @Transactional
    public Payment processPayment(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(Payment.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()));
        payment.setStatus(Payment.PaymentStatus.PROCESSING);

        // Simulate payment processing
        boolean success = Math.random() > 0.1; // 90% success rate

        if (success) {
            payment.setStatus(Payment.PaymentStatus.PAID);
            payment.setTransactionId("TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            payment.setPaymentGateway("PAYMENT_GATEWAY_API");
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason("Insufficient funds");
        }

        return repository.save(payment);
    }

    public Payment getByOrderId(Long orderId) {
        return repository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderId));
    }

    @Transactional
    public Payment refundPayment(Long id) {
        Payment payment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        return repository.save(payment);
    }
}
