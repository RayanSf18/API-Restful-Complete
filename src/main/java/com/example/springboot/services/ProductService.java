package com.example.springboot.services;

import com.example.springboot.domain.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public ProductModel saveProduct(ProductModel productModel) {
        return repository.save(productModel);
    }

    public List<ProductModel> findAll() {
        return repository.findAll();
    }

    public Optional<ProductModel> findById(UUID id) {
        return repository.findById(id);
    }

    public void delete(ProductModel productModel) {
        repository.delete(productModel);
    }
}
