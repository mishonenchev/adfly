package io.app.adfly.controllers;

import io.app.adfly.domain.dto.ProductEventRequest;
import io.app.adfly.domain.exceptions.ValidationException;
import io.app.adfly.entities.ProductAdvertiser;
import io.app.adfly.entities.ProductEvent;
import io.app.adfly.repositories.ProductAdvertiserRepository;
import io.app.adfly.repositories.ProductEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/public-api")
@RequiredArgsConstructor
public class PublicApiController {

    private final ProductAdvertiserRepository productAdvertiserRepository;
    private final ProductEventRepository productEventRepository;

    @PostMapping("{publicApiKey}/order")
    public ResponseEntity<?> Order(@RequestBody ProductEventRequest request,
                                   @PathVariable String publicApiKey,
                                   @RequestParam String code){
        var productAdvertiser = productAdvertiserRepository.getByExternalCode(code).orElseThrow(()-> new ValidationException("Invalid external code"));
        var product = productAdvertiser.getProduct();
        var apiKey = product.getSite().getPublicApiKey();
        if (!Objects.equals(publicApiKey, apiKey))
            throw new ValidationException("Wrong public api key");

        var productEvent = new ProductEvent();
        productEvent.setEvent(ProductEvent.Event.Order);
        productEvent.setCreatedDateTime(Date.from(Instant.now()));
        productEvent.setQuantity(request.getQuantity());
        productEvent.setTotalPrice(request.getTotalPrice());
        productEvent.setProductAdvertiser(productAdvertiser);
        productEventRepository.save(productEvent);

        return ResponseEntity.ok().build();
    }
}
