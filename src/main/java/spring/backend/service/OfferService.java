package spring.backend.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import spring.backend.dto.OfferRequest;
import spring.backend.dto.OfferResponse;
import spring.backend.entity.Menu;
import spring.backend.entity.SpecialOffer;
import spring.backend.exception.AppRuntimeException;
import spring.backend.repository.jpa.OfferRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OfferService {

    OfferRepository offerRepository;

    CoffeeshopService coffeeshopService;

    MenuService menuService;

    public SpecialOffer findByID(Long id) {
        return offerRepository.findById(id).orElseThrow(() ->
                new AppRuntimeException("Offer not found",HttpStatus.NOT_FOUND));
    }
    public void delete(Long id) {
        offerRepository.deleteById(id);
    }
     public List<OfferRequest> getAllbyCoffeeshop(Long id) {
         List<SpecialOffer> offerList = offerRepository.findAllByCoffeeshopId(id);
         return offerList.stream()
                 .map(offer -> {
                     Map<Menu, Integer> itemsList = offer.getItemsList();
                     Map<String, Integer> itemsWithQuantity = itemsList.entrySet().stream()
                             .collect(Collectors.toMap(entry -> entry.getKey().getItems(), Map.Entry::getValue));

                     return OfferRequest.builder()
                             .id(offer.getId())
                             .coffeeshopId(offer.getCoffeeshop().getId())
                             .price(offer.getPrice())
                             .name(offer.getOfferName())
                             .itemsWithQuantity(itemsWithQuantity)
                             .build();
                 })
                 .collect(Collectors.toList());
     }

    public double calculateTotalPrice(Map<Long, Integer> itemsWithQuantity) {
        double totalPrice = 0.0;
        for (Map.Entry<Long, Integer> entry : itemsWithQuantity.entrySet()) {
            Long menuId = entry.getKey();
            int quantity = entry.getValue();
            Menu menu = menuService.menuRepository.findById(menuId)
                    .orElseThrow(() -> new AppRuntimeException("Menu not found with id: " + menuId,HttpStatus.NOT_FOUND));
            totalPrice += menu.getPrice() * quantity;
        }
        return totalPrice - (totalPrice * 0.1);
    }

    public SpecialOffer save(OfferResponse offerDTO){
        if(offerDTO.getCoffeeshopId() == null) {
            throw new AppRuntimeException("Parameter coffeeshop_id is not found in request..!!", HttpStatus.NO_CONTENT);
        }
        if(offerDTO.getItemsWithQuanity() == null) {
            throw new AppRuntimeException("Parameter items is not found in request..!!", HttpStatus.NO_CONTENT);
        }
        SpecialOffer specialOffer = new SpecialOffer(offerDTO.getId(),offerDTO.getName(),
                coffeeshopService.getCoffeeShopById(offerDTO.getCoffeeshopId()),
                offerDTO.getItemsWithQuanity().entrySet().stream()
                .collect(Collectors.toMap(entry -> menuService.menuRepository.findById(entry.getKey()).orElse(null),
                        Map.Entry::getValue)),
                calculateTotalPrice(offerDTO.getItemsWithQuanity()));

        if (offerDTO.getId() != null) {
            SpecialOffer oldOffer = offerRepository.findById(offerDTO.getId()).orElseThrow(() ->
                    new AppRuntimeException("Menu not found", HttpStatus.NOT_FOUND));
            if (oldOffer != null) {
                BeanUtils.copyProperties(specialOffer,oldOffer);
                offerRepository.save(oldOffer);
            } else {
                throw new AppRuntimeException("Can't find record with identifier: " +
                        oldOffer.getId(), HttpStatus.NOT_FOUND);
            }
        }
        return offerRepository.save(specialOffer);
    }
}