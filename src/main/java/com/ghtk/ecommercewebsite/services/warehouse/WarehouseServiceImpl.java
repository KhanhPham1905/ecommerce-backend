package com.ghtk.ecommercewebsite.services.warehouse;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.DetailWarehouseDTO;
import com.ghtk.ecommercewebsite.models.dtos.WarehouseDto;
import com.ghtk.ecommercewebsite.models.entities.Address;
import com.ghtk.ecommercewebsite.models.entities.Shop;
import com.ghtk.ecommercewebsite.models.entities.Warehouse;
import com.ghtk.ecommercewebsite.repositories.AddressRepository;
import com.ghtk.ecommercewebsite.repositories.ShopRepository;
import com.ghtk.ecommercewebsite.repositories.WarehouseRepository;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService{
    private final WarehouseRepository warehouseRepository;
    private final ShopRepository shopRepository;
    private final AddressRepository addressRepository;

    @Override
    public DetailWarehouseDTO getWarehouseInfo(Long id) throws Exception {
        DetailWarehouseDTO detailWarehouseDTO = warehouseRepository.findDetailByWarehouseId(id);
        if (detailWarehouseDTO == null){
            throw new DataNotFoundException("Cannot find detail warehouse information by id");
        }
        return detailWarehouseDTO;
    }

    @Override
    @Transactional
    public DetailWarehouseDTO createWarehouse(DetailWarehouseDTO detailWarehouseDTO,Long userId) throws Exception {
        Shop shop = shopRepository.findShopByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find shop by id"));

        Address address = Address.builder()
                .addressDetail(detailWarehouseDTO.getAddressDetail())
                .commune(detailWarehouseDTO.getCommune())
                .country(detailWarehouseDTO.getCountry())
                .district(detailWarehouseDTO.getDistrict())
                .province(detailWarehouseDTO.getProvince())
                .build();
        Address newAddress = addressRepository.save(address);

        Warehouse warehouse = Warehouse.builder()
                .isDelete(Boolean.FALSE)
                .name(detailWarehouseDTO.getName())
                .shopId(shop.getId())
                .addressId(newAddress.getId())
                .build();
        warehouseRepository.save(warehouse);
        return detailWarehouseDTO;
    }

    @Override
    @Transactional
    public DetailWarehouseDTO updateWarehouseById(DetailWarehouseDTO detailWarehouseDTO, Long id, Long userId) throws Exception {
        Shop shop = shopRepository.findShopByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find shop by user id"));
        Long shopId = warehouseRepository.findShopIdById(id);
        if (!shopId.equals(shop.getId())){
            throw  new AccessDeniedException("Cannot delete warehouse");
        }
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find warehouse by id"));
        Address address = addressRepository.findById(warehouse.getAddressId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find address by id"));
        warehouse.setName(detailWarehouseDTO.getName());
        warehouseRepository.save(warehouse);
        address.setDistrict(detailWarehouseDTO.getDistrict());
        address.setAddressDetail(detailWarehouseDTO.getAddressDetail());
        address.setCountry(detailWarehouseDTO.getCountry());
        address.setCommune(detailWarehouseDTO.getCommune());
        address.setProvince(detailWarehouseDTO.getProvince());
        addressRepository.save(address);
        return detailWarehouseDTO;
    }

    @Override
    @Transactional
    public void deleteWarehouseById(Long id, Long userId) throws Exception {
        Shop shop = shopRepository.findShopByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find shop by user id"));
        Long shopId = warehouseRepository.findShopIdById(id);
        if (!shopId.equals(shop.getId())){
            throw  new AccessDeniedException("Cannot delete warehouse");
        }
        Warehouse warehouse = warehouseRepository.findById(id)
                        .orElseThrow(()-> new DataNotFoundException("Cannot find warehouse by id"));
        warehouse.setIsDelete(Boolean.TRUE);
        warehouseRepository.save(warehouse);
    }

    @Override
    public List<Warehouse> getAllWarehouse(PageRequest pageRequest, Long userId, String name) throws Exception{
        Shop shop = shopRepository.findShopByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find shop by user id"));
        List<Warehouse> warehouseDtoList = warehouseRepository.findByShopId(shop.getId(),  name, pageRequest);
        return warehouseDtoList;
    }
}
