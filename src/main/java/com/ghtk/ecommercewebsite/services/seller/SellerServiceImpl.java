package com.ghtk.ecommercewebsite.services.seller;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
//import com.ghtk.ecommercewebsite.mapper.SellerMapper;
import com.ghtk.ecommercewebsite.mapper.SellerMapper;
import com.ghtk.ecommercewebsite.mapper.UserMapper;
import com.ghtk.ecommercewebsite.models.dtos.*;
import com.ghtk.ecommercewebsite.models.entities.*;
import com.ghtk.ecommercewebsite.models.enums.RoleEnum;
import com.ghtk.ecommercewebsite.repositories.AddressRepository;
import com.ghtk.ecommercewebsite.repositories.SellerRepository;
import com.ghtk.ecommercewebsite.services.auth.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.exceptions.SellerAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import com.ghtk.ecommercewebsite.repositories.RoleRepository;
import com.ghtk.ecommercewebsite.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService{

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationServiceImpl authenticationService;
    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;
    private final UserMapper userMapper;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public User signUpSeller(RegisterUserDto input) throws SellerAlreadyExistedException {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SELLER);
        if (optionalRole.isEmpty()) { return null; }

        Role sellerRole = optionalRole.get();

        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            Set<Role> existingRoles = existingUser.getRoles();
//            Role sellerRole = Role.builder().name(RoleEnum.SELLER).build();

            if (existingRoles.contains(sellerRole)) {
                throw new SellerAlreadyExistedException(input.getEmail());
            } else {
                existingRoles.add(sellerRole);
                existingUser.setRoles(existingRoles);
                return userRepository.save(existingUser);
            }
        } else {
            Set<Role> roles = new HashSet<>(List.of(optionalRole.get()));
            var user = User.builder()
                    .fullName(input.getFullName())
                    .email(input.getEmail())
                    .password(passwordEncoder.encode(input.getPassword()))
                    .roles(roles)
                    .build();
            return userRepository.save(user);
        }
    }

    @Override
    public LoginResponse authenticateSellerAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException {
        return authenticationService.authenticateSellerAndGetLoginResponse(loginUserDto);
    }

    @Override
    public User getAuthenticatedSeller() {
        return (User) authenticationService.getAuthentication().getPrincipal();
    }

//    @Override
//    public Map<String, Object>  getInformation(Long useId) throws  Exception {
//        Seller seller = sellerRepository.findByUserId(useId)
//                .orElseThrow(() -> new DataNotFoundException("information not found"));
//        User user = userRepository.findById(useId)
//                .orElseThrow(() -> new DataNotFoundException("information not found"));
//        List<Address> address = addressRepository.findByUserId(userId)
//                .orElseThrow(() -> new DataNotFoundException("information not found"));
//
//        SellerDTO sellerDTO = sellerMapper.toDTO(seller);
//        UserDTO userDTO = userMapper.toDTO(user);
//        Map<String, Object>  response = new HashMap<>();
//        response.put("seller", sellerDTO);
//        response.put("user", userDTO);
//        response.put("address", address);
//        return response;
//    }

    @Override
    public DetailSellerInfoDTO getSellerInfo(Long userId) throws Exception{
        DetailSellerInfoDTO detailSellerInfoDTO = sellerRepository.getDetailSellerInfo(userId)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));;
        return detailSellerInfoDTO;
    }

    @Override
    @Transactional
    public DetailSellerInfoDTO updateSellerInfo(DetailSellerInfoDTO detailSellerInfoDTO,Long userId) throws Exception{
        Seller seller = sellerRepository.findByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find seller by id"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find user by id"));

        Address address = addressRepository.findByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find user by id"));
//        List<Address> address = addressRepository.findByUserId(userId)
//                .orElseThrow(() -> new DataNotFoundException("Cannot find address by id"));
//        if(!category.getShopId().equals(shopId)) {
//            throw new AccessDeniedException("Account seller and shop not match");
//        }
//                .orElseThrow(() -> new DataNotFoundException("Category not found"));;
        ;

        seller.setTax(detailSellerInfoDTO.getTax());
        seller.setCccd(detailSellerInfoDTO.getCccd());
//        user.setEmail(detailSellerInfoDTO.getEmail());
        user.setFullName(detailSellerInfoDTO.getFullName());
        user.setPhone(detailSellerInfoDTO.getPhone());
        address.setAddressDetail(detailSellerInfoDTO.getAddressDetail());
        address.setCommune(detailSellerInfoDTO.getCommune());
        address.setCountry(detailSellerInfoDTO.getCountry());
        address.setProvince(detailSellerInfoDTO.getProvince());
        address.setDistrict(detailSellerInfoDTO.getDistrict());
        sellerRepository.save(seller);
        userRepository.save(user);
//        for (Address addressTemp : address) {
            // Giả sử bạn có thể cập nhật địa chỉ dựa trên thông tin từ DTO
            // Bạn cần phải logic điều kiện và cập nhật theo cách của bạn
            addressRepository.save(address);
//        }
        return detailSellerInfoDTO;
    }
}
