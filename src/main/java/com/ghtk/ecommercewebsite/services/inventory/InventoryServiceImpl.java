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
    public List<DetailInventoryDTO> getAllInventory(Long userId) throws Exception {
        Long shopId = sellerRepository.findShopIdByUserId(userId);
        if (shopId == null){
            throw  new DataNotFoundException("Cannot find shopId by userId");
        }
        List<DetailInventoryDTO> detailInventoryDTOList = inventoryRepository.getAllInventory(shopId);
        if(detailInventoryDTOList.isEmpty()){
            throw  new DataNotFoundException("Cannot find Inventory by Shop id");
        }
        return detailInventoryDTOList;
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
                .unitPrice(detailInventoryDTO.getUnitPrice())
                .location(detailInventoryDTO.getLocation())
                .status(Boolean.TRUE)
                .build();
        supplyRepository.save(supply);

        Inventory inventory = inventoryRepository.findByProductItemIdAndWarehouseId(productItem.getId(), detailInventoryDTO.getWarehouseId());

        inventory.setQuantity(detailInventoryDTO.getQuantity() + inventory.getQuantity());

        inventoryRepository.save(inventory);

        return detailInventoryDTO;
    }

    @Override
    public List<DetailInventoryDTO> getListImport(Long userId) throws Exception {
        Long shopId = sellerRepository.findShopIdByUserId(userId);
            List<DetailInventoryDTO> detailInventoryDTOList = supplyRepository.getAllImport(shopId);
            if(detailInventoryDTOList.isEmpty()){
                throw new DataNotFoundException("Cannot not found import");
            }
        return detailInventoryDTOList;
    }

    @Override
    public List<DetailInventoryDTO> getListExport(Long userId) throws Exception {
        Long shopId = sellerRepository.findShopIdByUserId(userId);
            List<DetailInventoryDTO> detailInventoryDTOList = supplyRepository.getAllExport(shopId);
//            if(detailInventoryDTOList.isEmpty()){
//                throw new DataNotFoundException("Cannot not found Export");
//            }
        return detailInventoryDTOList;
    }
}
