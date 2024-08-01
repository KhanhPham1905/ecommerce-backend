package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.mapper.CartMapper;
import com.ghtk.ecommercewebsite.models.dtos.CartDTO;
import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import com.ghtk.ecommercewebsite.services.cart.ICartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/carts/items")
public class CartItemController {

    private final ICartItemService iCartItemService;

    private final CartMapper cartMapper;

    @Autowired
    public CartItemController(ICartItemService iCartItemService, CartMapper cartMapper) {
        this.iCartItemService = iCartItemService;
        this.cartMapper = cartMapper;
    }

    @GetMapping
    public List<CartItemDTO> getAllCartItems() {
        return iCartItemService.findAll().stream().
                map(cartMapper::toCartItemDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItemDTO> getCartItemById(@PathVariable Long id) {
        return iCartItemService.findById(id)
                .map(cartItem -> ResponseEntity.ok(cartMapper.toCartItemDTO(cartItem)))
                .orElse(ResponseEntity.notFound().build());

    }


    @PostMapping
    public ResponseEntity<CartItemDTO> createCartItem(@RequestBody CartItemDTO cartItemDTO) {
        CartItem cartItem = cartMapper.toCartItemEntity(cartItemDTO);
        CartItem saved = iCartItemService.save(cartItem);
        return ResponseEntity.ok(cartMapper.toCartItemDTO(saved));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<CartItemDTO> patchBrand(@PathVariable Long id, @RequestBody CartItemDTO cartItemDTO) {
        return iCartItemService.findById(id)
                .map(cartItem -> {
                    if (cartItemDTO.getProductItemId() != null) cartItem.setProductItemId(cartItem.getProductItemId());
                    if (cartItemDTO.getQuantity() > 0) cartItem.setQuantity(cartItemDTO.getQuantity());
                    CartItem updated = iCartItemService.save(cartItem);
                    return ResponseEntity.ok(cartMapper.toCartItemDTO(updated));
                }).orElse(ResponseEntity.notFound().build());

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCartItem(@PathVariable Long id) {
        return iCartItemService.findById(id)
                .map(cartItem -> {
                    iCartItemService.deleteById(id);
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
