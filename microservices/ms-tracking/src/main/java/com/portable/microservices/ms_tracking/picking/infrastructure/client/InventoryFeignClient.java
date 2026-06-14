package com.portable.microservices.ms_tracking.picking.infrastructure.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.portable.microservices.ms_tracking.picking.infrastructure.client.dto.LocationApiResponse;
import com.portable.microservices.ms_tracking.picking.infrastructure.client.dto.LocationResponse;

@FeignClient(name = "ms-inventory", path = "/api/v1/locations")
public interface InventoryFeignClient {

    @GetMapping
    LocationApiResponse<List<LocationResponse>> getAllLocations();
}