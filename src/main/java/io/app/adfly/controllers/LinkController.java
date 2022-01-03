package io.app.adfly.controllers;

import io.app.adfly.domain.exceptions.ValidationException;
import io.app.adfly.repositories.ProductAdvertiserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("")
@RequiredArgsConstructor
public class LinkController {

    private final ProductAdvertiserRepository productAdvertiserRepository;

    @GetMapping("/s/{linkCode}")
    public ResponseEntity<?> GetLink(@PathVariable String linkCode, HttpServletResponse httpServletResponse)
    {
        var productAdvertiser = productAdvertiserRepository.getByRedirectCode(linkCode).orElseThrow(()->new ValidationException("Invalid code"));
        if(productAdvertiser.getProduct().getSite() == null){
            throw new ValidationException("Invalid url");
        }

        var website = productAdvertiser.getProduct().getProductUrl() + "?code=" + productAdvertiser.getExternalCode();

        httpServletResponse.setHeader("Location", website);
        return ResponseEntity.status(302).build();
    }

}
