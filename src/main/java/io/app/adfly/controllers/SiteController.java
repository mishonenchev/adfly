package io.app.adfly.controllers;

import io.app.adfly.domain.dto.*;
import io.app.adfly.domain.exceptions.RecordNotFoundException;
import io.app.adfly.domain.exceptions.ValidationException;
import io.app.adfly.domain.mapper.Mapper;
import io.app.adfly.entities.Role;
import io.app.adfly.entities.Site;
import io.app.adfly.repositories.SiteRepository;
import io.app.adfly.services.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/secure/company/sites")
@RequiredArgsConstructor
@RolesAllowed({Role.USER_COMPANY})
public class SiteController {

    private final UserService userService;
    private final SiteRepository siteRepository;

    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PaginatedResponse.class)
    }
    )
    public ResponseEntity<?> GetAllSites(@RequestParam(defaultValue = "0", required = false) int startAt,
                                         @RequestParam(defaultValue = "10", required = false) int count)
    {
        var request = new PaginatedRequest(startAt, count);
        Pageable pageable = Mapper.map(request, Pageable.class);
        var user = userService.GetCurrentUser();

        if(!user.isPresent())
            return ResponseEntity.status(401).build();
        var sites = siteRepository.findAllByCompany(user.get().getCompany(), pageable);

        var mappedProducts =  Mapper.mapList(sites.toList(), SiteDto.class);
        PaginatedResponse<SiteDto> paginatedResponse = Mapper.mapPaginatedResponse(mappedProducts, request, (int)sites.getTotalElements());

        return ResponseEntity.ok(paginatedResponse);
    }

    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = SiteDto.class)
    }
    )
    public ResponseEntity<?> AddSite(@RequestBody CreateSiteRequest request){
        var user = userService.GetCurrentUser();
        if(!user.isPresent()) return ResponseEntity.status(401).build();

        var site = Mapper.map(request, Site.class);
        site.setCompany(user.get().getCompany());
        siteRepository.save(site);

        var siteDto = Mapper.map(site, SiteDto.class);
        return ResponseEntity.ok(siteDto);
    }

    @PutMapping("{siteId}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = SiteDto.class)
    }
    )
    public ResponseEntity<?> UpdateSite(@RequestBody CreateSiteRequest request, @PathVariable Long siteId){
        var user = userService.GetCurrentUser();
        if(!user.isPresent()) return ResponseEntity.status(401).build();

        var site = siteRepository.findById(siteId).orElseThrow(()-> new RecordNotFoundException("Site id is not found"));
        if(!site.getCompany().equals(user.get().getCompany())) throw new ValidationException("This site is not managed by this user");

        Mapper.map(request, site);
        siteRepository.save(site);

        var siteDto = Mapper.map(site, SiteDto.class);
        return ResponseEntity.ok(siteDto);
    }

    @DeleteMapping("{siteId}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    }
    )
    public ResponseEntity<?> RemoveSite(@PathVariable Long siteId){
        var user = userService.GetCurrentUser();
        if(!user.isPresent()) return ResponseEntity.status(401).build();

        var site = siteRepository.findById(siteId).orElseThrow(()-> new RecordNotFoundException("Site id is not found"));
        if(!site.getCompany().equals(user.get().getCompany())) throw new ValidationException("This site is not managed by this user");


        siteRepository.delete(site);
        return ResponseEntity.ok().build();
    }

}
