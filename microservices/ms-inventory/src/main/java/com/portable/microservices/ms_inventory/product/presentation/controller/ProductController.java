package com.portable.microservices.ms_inventory.product.presentation.controller;

import com.portable.microservices.ms_inventory.product.application.usercases.CreateProductUseCase;
import com.portable.microservices.ms_inventory.product.application.usercases.FindProductUseCase;
import com.portable.microservices.ms_inventory.product.application.usercases.UpdateProductUseCase;
import com.portable.microservices.ms_inventory.product.application.usercases.DeleteProductUseCase;
import com.portable.microservices.ms_inventory.product.domain.model.Product;
import com.portable.microservices.ms_inventory.product.presentation.dto.CreateProductRequest;
import com.portable.microservices.ms_inventory.product.presentation.dto.ProductResponse;
import com.portable.microservices.ms_inventory.product.presentation.mapper.ProductPresentationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final FindProductUseCase findProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final ProductPresentationMapper presentationMapper;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        Product product = createProductUseCase.execute(
                new CreateProductUseCase.CreateProductCommand(
                        request.categoryId(),
                        request.brandId(),
                        request.codProd(),
                        request.codAnexo(),
                        request.descripcion(),
                        request.modelosCompatibles(),
                        request.preCom(),
                        request.preVen()
                )
        );
        return ResponseEntity.ok(presentationMapper.toResponse(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        List<ProductResponse> products = findProductUseCase.findAll().stream()
                .map(presentationMapper::toResponse)
                .toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable UUID id) {
        return findProductUseCase.findById(id)
                .map(p -> ResponseEntity.ok(presentationMapper.toResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable UUID id,
                                                  @RequestBody UpdateProductUseCase.UpdateProductCommand command) {
        Product updated = updateProductUseCase.execute(id, command);
        return ResponseEntity.ok(presentationMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteProductUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(findProductUseCase.count());
    }
}
