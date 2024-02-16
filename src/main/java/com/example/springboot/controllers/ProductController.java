package com.example.springboot.controllers;

import com.example.springboot.domain.ProductModel;
import com.example.springboot.dtos.ProductRecordDTO;
import com.example.springboot.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping("/products/create")
    public ResponseEntity<ProductModel> createProduct(@Valid @RequestBody ProductRecordDTO productRecordDTO) {
        ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDTO, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveProduct(productModel));
    }

    @GetMapping("/products/all")
    public ResponseEntity<List<ProductModel>> getAllProductsModels() {
        List<ProductModel> list = service.findAll();
        if (!list.isEmpty()) {
            for (ProductModel productModel : list) {
                UUID id = productModel.getId();
                productModel.add(linkTo(methodOn(ProductController.class).getOneProductModel(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProductModel(@PathVariable UUID id) {
        Optional<ProductModel> productModelOptional = service.findById(id);
        if (productModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        productModelOptional.get().add(linkTo(methodOn(ProductController.class).getAllProductsModels()).withRel("Products list"));
        return ResponseEntity.status(HttpStatus.OK).body(productModelOptional.get());
    }

    @PutMapping("/products/update/{id}")
    public ResponseEntity<Object> updateProductModel(@PathVariable UUID id, @Valid @RequestBody ProductRecordDTO productRecordDTO) {
        Optional<ProductModel> productModelOptional = service.findById(id);
        if (productModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        ProductModel productModel = productModelOptional.get();
        BeanUtils.copyProperties(productRecordDTO, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(service.saveProduct(productModel));
    }

    @DeleteMapping("/products/delete/{id}")
    public ResponseEntity<Object> deleteProductModel(@PathVariable UUID id) {
        Optional<ProductModel> productModelOptional = service.findById(id);
        if (productModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        service.delete(productModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product id={" + id + "} deleted successfully.");
    }
}
