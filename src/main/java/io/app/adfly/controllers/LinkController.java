package io.app.adfly.controllers;

import io.app.adfly.domain.exceptions.ValidationException;
import io.app.adfly.entities.ProductEvent;
import io.app.adfly.repositories.ProductAdvertiserRepository;
import io.app.adfly.repositories.ProductEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Date;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("")
@RequiredArgsConstructor
public class LinkController {

    private final ProductAdvertiserRepository productAdvertiserRepository;
    private final ProductEventRepository productEventRepository;

    @GetMapping("/s/{linkCode}")
    public ResponseEntity<?> GetLink(@PathVariable String linkCode, HttpServletResponse httpServletResponse)
    {
        var productAdvertiser = productAdvertiserRepository.getByRedirectCode(linkCode).orElseThrow(()->new ValidationException("Invalid code"));
        if(productAdvertiser.getProduct().getSite() == null){
            throw new ValidationException("Invalid url");
        }

        var website = productAdvertiser.getProduct().getProductUrl() + "?code=" + productAdvertiser.getExternalCode();
        //ProductEvent
        var productEvent = new ProductEvent();
        productEvent.setEvent(ProductEvent.Event.Click);
        productEvent.setProductAdvertiser(productAdvertiser);
        productEvent.setQuantity(1.0);
        productEvent.setTotalPrice(1.0);
        productEvent.setCreatedDateTime(Date.from(Instant.now()));
        productEventRepository.save(productEvent);

        httpServletResponse.setHeader("Location", website);
        return ResponseEntity.status(302).build();
    }

}
