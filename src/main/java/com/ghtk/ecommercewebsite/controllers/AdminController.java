package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.*;
import com.ghtk.ecommercewebsite.models.entities.Orders;
import com.ghtk.ecommercewebsite.models.entities.Seller;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.admin.AdminService;
import com.ghtk.ecommercewebsite.services.seller.SellerService;
import com.ghtk.ecommercewebsite.services.user.UserService;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {

    private final UserService userService;
    private final SellerService sellerService;
    private final AdminService adminService;

    @PostMapping("/login")
    public CommonResult<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws AccessDeniedException {
        return CommonResult.success(adminService.authenticateAdminAndGetLoginResponse(loginUserDto));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<User> authenticatedAdmin() {
        return CommonResult.success(adminService.getAuthenticatedAdmin());
    }

    @GetMapping("/sellers")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Page<User>> allSellers (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return CommonResult.success(userService.allSellers(pageable));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Page<User>> allUsers (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return CommonResult.success(userService.allUsers(pageable));
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<User> viewUserDetails(@PathVariable Long id) throws DataNotFoundException {
        return CommonResult.success(userService.viewDetailsOfAnUser(id));
    }

    @GetMapping("/sellers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<User> viewSellerDetails(@PathVariable Long id) throws DataNotFoundException {
        return CommonResult.success(sellerService.viewDetailsOfAnSeller(id));
    }

    @PostMapping("/users/add")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<User> addNewUser(@RequestBody RegisterUserDto registerUserDto) {
        return CommonResult.success(userService.signUp(registerUserDto));
    }

//    @PostMapping("/sellers/add")
//    @PreAuthorize("hasRole('ADMIN')")
//    public CommonResult<User> addNewSeller(@RequestBody RegisterUserDto registerUserDto) {
//        return CommonResult.success(sellerService.signUpSeller(registerUserDto));
//    }

    @PostMapping("/users/update")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<User> updateUserInfo(@RequestBody UserDTO userDTO) throws DataNotFoundException {
        return CommonResult.success(userService.updateUserInfo(userDTO));
    }

    @PostMapping("/sellers/update")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Seller> updateSellerInfo(@RequestBody SellerDTO sellerDTO) throws DataNotFoundException {
        return CommonResult.success(sellerService.updateSellerInfo(sellerDTO));
    }

// Normally return to List
//    @GetMapping("/sellers")
//    @PreAuthorize("hasRole('ADMIN')")
//    public CommonResult<List<User>> allSellers() {
//        List<User> sellers = userService.findAllSellers();
//        return CommonResult.success(sellers);
//    }

// Manually convert to Page, without using PageRequest
//    @GetMapping("/sellers")
//    @PreAuthorize("hasRole('ADMIN')")
//    public CommonResult<Page<User>> allSellers(@PageableDefault(size = 10) Pageable pageable) {
//        List<User> sellers = userService.findAllSellers();
//        Page<User> pagedSellers = convertListToPage(sellers, pageable);
//        return CommonResult.success(pagedSellers);
//    }
//
//    private Page<User> convertListToPage(List<User> sellers, Pageable pageable) {
//        int pageSize = pageable.getPageSize();
//        int currentPage = pageable.getPageNumber();
//        int startItem = currentPage * pageSize;
//
//        List<User> pagedSellers;
//
//        if (sellers.size() < startItem) {
//            pagedSellers = Collections.emptyList();
//        } else {
//            int toIndex = Math.min(startItem + pageSize, sellers.size());
//            pagedSellers = sellers.subList(startItem, toIndex);
//        }
//
//        return new PageImpl<>(pagedSellers, pageable, sellers.size());
//    }

}
