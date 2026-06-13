package com.moviebooking.service;

import com.moviebooking.dto.request.CreateDiscountCodeRequest;
import com.moviebooking.dto.response.DiscountValidationResponse;
import com.moviebooking.entity.DiscountCode;
import com.moviebooking.exception.InvalidDiscountException;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.repository.DiscountCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiscountService {

    private final DiscountCodeRepository discountCodeRepository;

    public DiscountService(DiscountCodeRepository discountCodeRepository) {
        this.discountCodeRepository = discountCodeRepository;
    }

    @Transactional
    public DiscountCode createDiscountCode(CreateDiscountCodeRequest request) {
        DiscountCode discountCode = new DiscountCode(
                request.getCode(),
                request.getPercentageOff(),
                request.getValidFrom(),
                request.getValidUntil(),
                true
        );
        return discountCodeRepository.save(discountCode);
    }

    public List<DiscountCode> getAllDiscountCodes() {
        return discountCodeRepository.findAll();
    }

    @Transactional
    public void deactivateDiscountCode(Long id) {
        DiscountCode discountCode = discountCodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount code not found with id: " + id));
        discountCode.setActive(false);
        discountCodeRepository.save(discountCode);
    }

    public DiscountValidationResponse validateDiscountCode(String code) {
        DiscountCode discountCode = discountCodeRepository.findByCode(code).orElse(null);

        if (discountCode == null) {
            return new DiscountValidationResponse(false, null, null);
        }

        LocalDateTime now = LocalDateTime.now();
        boolean isValid = discountCode.getActive()
                && now.isAfter(discountCode.getValidFrom())
                && now.isBefore(discountCode.getValidUntil());

        if (isValid) {
            return new DiscountValidationResponse(true, discountCode.getPercentageOff(), discountCode.getValidUntil());
        } else {
            return new DiscountValidationResponse(false, null, null);
        }
    }

    public DiscountCode getValidDiscountCode(String code) {
        DiscountCode discountCode = discountCodeRepository.findByCode(code)
                .orElseThrow(() -> new InvalidDiscountException("Discount code not found: " + code));

        LocalDateTime now = LocalDateTime.now();
        boolean isValid = discountCode.getActive()
                && now.isAfter(discountCode.getValidFrom())
                && now.isBefore(discountCode.getValidUntil());

        if (!isValid) {
            throw new InvalidDiscountException("Discount code is not valid: " + code);
        }

        return discountCode;
    }
}
