package com.fooddelivery.food_delivery.controller;

import com.fooddelivery.food_delivery.model.IngredientCategory;
import com.fooddelivery.food_delivery.model.IngredientsItem;
import com.fooddelivery.food_delivery.request.IngredientCategoryRequest;
import com.fooddelivery.food_delivery.request.IngredientRequest;
import com.fooddelivery.food_delivery.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ingredients")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @PostMapping("/category")
    public ResponseEntity<IngredientCategory> createIngredientCategory(
            @RequestBody IngredientCategoryRequest req
            )throws Exception{
        IngredientCategory item=ingredientService.createIngredientCategory(req.getName(), req.getRestaurantId());
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PostMapping()
    public ResponseEntity<IngredientsItem> createItem(
            @RequestBody IngredientRequest req
    )throws Exception{
        IngredientsItem item=ingredientService.createIngredientItem(req.getName(), req.getRestaurantId(), req.getCategoryId());
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/stoke")
    public ResponseEntity<IngredientsItem> updateIngredientStoke(
            @RequestBody Long id
    )throws Exception{
        IngredientsItem item=ingredientService.updateStock(id);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<IngredientsItem>> getRestaurantIngredient(
            @RequestBody Long id
    )throws Exception{
        List<IngredientsItem> items= ingredientService.findRestaurantsIngredients(id);
        return new ResponseEntity<>(items, HttpStatus.CREATED);
    }

    @GetMapping("/restaurant/{id}/category")
    public ResponseEntity<List<IngredientCategory>> getRestaurantIngredientCategory(
            @RequestBody Long id
    )throws Exception{
        List<IngredientCategory> items= ingredientService.findIngredientCategoryByRestaurantId(id);
        return new ResponseEntity<>(items, HttpStatus.CREATED);
    }
}
