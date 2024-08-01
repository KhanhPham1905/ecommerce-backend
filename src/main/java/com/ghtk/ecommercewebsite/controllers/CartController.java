package com.ghtk.ecommercewebsite.controllers;


import com.ghtk.ecommercewebsite.mapper.CartMapper;
import com.ghtk.ecommercewebsite.models.dtos.CartDTO;
import com.ghtk.ecommercewebsite.models.entities.Cart;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import com.ghtk.ecommercewebsite.services.cart.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final ICartService cartService;
    private final CartMapper cartMapper;

    @Autowired
    public CartController(ICartService cartService, CartMapper cartMapper) {
        this.cartService = cartService;
        this.cartMapper = cartMapper;
    }

    @GetMapping
    public List<CartDTO> getAllCarts() {
        return cartService.findAll().stream()
                .map(cartMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable Long id) {
        return cartService.findById(id)
                .map(cart -> ResponseEntity.ok(cartMapper.toDTO(cart)))
                .orElse(ResponseEntity.notFound().build());

    }


    @PostMapping
    public ResponseEntity<CartDTO> createCart(@RequestBody CartDTO cartDTO) {
        Cart cart = cartMapper.toEntity(cartDTO);
        Cart savedCart = cartService.save(cart);
        return ResponseEntity.ok(cartMapper.toDTO(savedCart));
    }


    @PutMapping("/{id}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable Long id, @RequestBody CartDTO cartDTO) {
        return cartService.findById(id).
                map(cart -> {
                    cart.setUserId(cartDTO.getUserId());
                    Cart updatedCart = cartService.save(cart);
                    return ResponseEntity.ok(cartMapper.toDTO(updatedCart));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        return cartService.findById(id)
                .map(cart -> {
                    cartService.deleteById(id);
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build());

    }

    @PostMapping("/add-items")
    public ResponseEntity<CartItem> addProductToCart(@RequestParam Long userId, Long productItemId, @RequestParam int quantity) {
        CartItem cartItem = cartService.addProductToCart(userId, productItemId, quantity);
        return ResponseEntity.ok(cartItem);
    }
}
