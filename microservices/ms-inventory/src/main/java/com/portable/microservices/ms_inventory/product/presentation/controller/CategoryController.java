package com.portable.microservices.ms_inventory.product.presentation.controller;

import com.portable.microservices.ms_inventory.product.application.usercases.CreateCategoryUseCase;
import com.portable.microservices.ms_inventory.product.application.usercases.DeleteCategoryUseCase;
import com.portable.microservices.ms_inventory.product.application.usercases.FindCategoryUseCase;
import com.portable.microservices.ms_inventory.product.application.usercases.UpdateCategoryUseCase;
import com.portable.microservices.ms_inventory.product.domain.model.Category;
import com.portable.microservices.ms_inventory.product.domain.ports.in.UpdateCategoryPortIn;
import com.portable.microservices.ms_inventory.product.presentation.dto.CategoryRequest;
import com.portable.microservices.ms_inventory.product.presentation.dto.CategoryResponse;
import com.portable.microservices.ms_inventory.product.presentation.mapper.CategoryPresentationMapper;
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
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final FindCategoryUseCase findCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final CategoryPresentationMapper presentationMapper;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        Category category = createCategoryUseCase.execute(request.name(), request.description());
        return ResponseEntity.ok(presentationMapper.toResponse(category));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        List<CategoryResponse> categories = findCategoryUseCase.findAll().stream()
                .map(presentationMapper::toResponse)
                .toList();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        return findCategoryUseCase.findById(id)
                .map(c -> ResponseEntity.ok(presentationMapper.toResponse(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id,
                                                   @RequestBody UpdateCategoryPortIn.UpdateCategoryCommand command) {
        Category updated = updateCategoryUseCase.execute(id, command);
        return ResponseEntity.ok(presentationMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteCategoryUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(findCategoryUseCase.count());
    }
}