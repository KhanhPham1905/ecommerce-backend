package com.ghtk.ecommercewebsite.services.inventory;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.mapper.InventoryMapper;
import com.ghtk.ecommercewebsite.models.dtos.DetailInventoryDTO;
import com.ghtk.ecommercewebsite.models.dtos.InventoryDTO;
import com.ghtk.ecommercewebsite.models.entities.Inventory;
import com.ghtk.ecommercewebsite.models.entities.ProductItem;
import com.ghtk.ecommercewebsite.models.entities.Supply;
import com.ghtk.ecommercewebsite.repositories.InventoryRepository;
import com.ghtk.ecommercewebsite.repositories.ProductItemRepository;
import com.ghtk.ecommercewebsite.repositories.SellerRepository;
import com.ghtk.ecommercewebsite.repositories.SupplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{
    private final InventoryRepository inventoryRepository;
    private final SellerRepository sellerRepository;
    private final InventoryMapper inventoryMapper;
    private final ProductItemRepository productItemRepository;
    private final SupplyRepository supplyRepository;


    @Override
    public InventoryDTO getInventoryById(Long id) throws Exception {
        Inventory inventory =  inventoryRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Cannot find inventory by id"));
        return inventoryMapper.toDTO(inventory);
    }

    @Override
    public Page<DetailInventoryDTO> getAllInventory(String warehouse,String skuCode,String name,Long userId, Pageable pageable) throws Exception {
        Long shopId = sellerRepository.findShopIdByUserId(userId);
        if (shopId == null){
            throw  new DataNotFoundException("Cannot find shopId by userId");
        }
        List<DetailInventoryDTO> detailInventoryDTOList = inventoryRepository.getAllInventory(warehouse, skuCode, name, shopId);
        if(detailInventoryDTOList.isEmpty()){
            throw  new DataNotFoundException("Cannot find Inventory by Shop id");
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), detailInventoryDTOList.size());

        List<DetailInventoryDTO> pagedList = detailInventoryDTOList.subList(start, end);
        return new PageImpl<>(pagedList, pageable, detailInventoryDTOList.size());
    }

    @Override
    @Transactional
    public DetailInventoryDTO importWarehouse(DetailInventoryDTO detailInventoryDTO, Long userId) throws Exception {
        ProductItem productItem = productItemRepository.findBySkuCode(detailInventoryDTO.getSkuCode());

        Supply supply = Supply.builder()
                .quantity(detailInventoryDTO.getQuantity())
                .warehouseId(detailInventoryDTO.getWarehouseId())
                .productItemId(productItem.getId())
                .supplier(detailInventoryDTO.getSupplier())
                .location(detailInventoryDTO.getLocation())
                .status(Boolean.TRUE)
                .build();
        supplyRepository.save(supply);

        Inventory inventory = inventoryRepository.findByProductItemIdAndWarehouseId(productItem.getId(), detailInventoryDTO.getWarehouseId());
        if(inventory == null){
            Inventory newInventory = Inventory.builder()
                    .warehouseId(detailInventoryDTO.getWarehouseId())
                    .quantity(detailInventoryDTO.getQuantity())
                    .productItemId(productItem.getId())
                    .build();
            inventoryRepository.save(newInventory);

        }else {
            inventory.setQuantity(detailInventoryDTO.getQuantity() + inventory.getQuantity());
            inventoryRepository.save(inventory);
        }

        return detailInventoryDTO;
    }

    @Override
    public Page<DetailInventoryDTO> getListImport(String warehouse,String supplier,String location,String skuCode,String name ,String createdAt, Long userId, Pageable pageable) throws Exception {
        Long shopId = sellerRepository.findShopIdByUserId(userId);
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * limit;
        List<DetailInventoryDTO> detailInventoryDTOList = supplyRepository.getAllImport(warehouse,supplier,location,skuCode, name , createdAt,shopId);
        if(detailInventoryDTOList.isEmpty()){
            throw new DataNotFoundException("Cannot not found import");
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), detailInventoryDTOList.size());

        List<DetailInventoryDTO> pagedList = detailInventoryDTOList.subList(start, end);
        return new PageImpl<>(pagedList, pageable, detailInventoryDTOList.size());
    }

    @Override
    public Page<DetailInventoryDTO> getListExport(String warehouse, String  supplier,String location,String skuCode,String  name ,String createdAt, Long userId, Pageable pageable) throws Exception {
        Long shopId = sellerRepository.findShopIdByUserId(userId);
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * limit;
        List<DetailInventoryDTO> detailInventoryDTOList = supplyRepository.getAllExport(warehouse,supplier,location,skuCode, name , createdAt,shopId);
        if(detailInventoryDTOList.isEmpty()){
            throw new DataNotFoundException("Cannot not found Export");
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), detailInventoryDTOList.size());

        List<DetailInventoryDTO> pagedList = detailInventoryDTOList.subList(start, end);
        return new PageImpl<>(pagedList, pageable, detailInventoryDTOList.size());
    }
}
