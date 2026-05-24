package com.portable.microservices.ms_inventory.product.presentation.controller;

import com.portable.microservices.ms_inventory.product.application.usercases.CreateBrandUseCase;
import com.portable.microservices.ms_inventory.product.application.usercases.DeleteBrandUseCase;
import com.portable.microservices.ms_inventory.product.application.usercases.FindBrandUseCase;
import com.portable.microservices.ms_inventory.product.application.usercases.UpdateBrandUseCase;
import com.portable.microservices.ms_inventory.product.domain.model.Brand;
import com.portable.microservices.ms_inventory.product.domain.ports.in.UpdateBrandPortIn;
import com.portable.microservices.ms_inventory.product.presentation.dto.BrandRequest;
import com.portable.microservices.ms_inventory.product.presentation.dto.BrandResponse;
import com.portable.microservices.ms_inventory.product.presentation.mapper.BrandPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

    private final CreateBrandUseCase createBrandUseCase;
    private final FindBrandUseCase findBrandUseCase;
    private final UpdateBrandUseCase updateBrandUseCase;
    private final DeleteBrandUseCase deleteBrandUseCase;
    private final BrandPresentationMapper presentationMapper;

    @PostMapping
    public ResponseEntity<BrandResponse> create(@Valid @RequestBody BrandRequest request) {
        Brand brand = createBrandUseCase.execute(request.name());
        return ResponseEntity.ok(presentationMapper.toResponse(brand));
    }

    @GetMapping
    public ResponseEntity<List<BrandResponse>> findAll() {
        List<BrandResponse> brands = findBrandUseCase.findAll().stream()
                .map(presentationMapper::toResponse)
                .toList();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> findById(@PathVariable Long id) {
        return findBrandUseCase.findById(id)
                .map(b -> ResponseEntity.ok(presentationMapper.toResponse(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BrandResponse> update(@PathVariable Long id,
                                                @RequestBody UpdateBrandPortIn.UpdateBrandCommand command) {
        Brand updated = updateBrandUseCase.execute(id, command);
        return ResponseEntity.ok(presentationMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteBrandUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(findBrandUseCase.count());
    }
}