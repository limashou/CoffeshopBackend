package spring.backend.service;

import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import spring.backend.dto.order.ItemsDTOResponse;
import spring.backend.entity.Menu;
import spring.backend.entity.Order;
import spring.backend.entity.OrderItems;
import spring.backend.entity.SpecialOffer;
import spring.backend.exception.AppRuntimeException;
import spring.backend.dto.order.ItemsDto;
import spring.backend.repository.jpa.ItemsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemsService {
    ItemsRepository itemsRepository;
    MenuService menuService;
    OfferService offerService;
    ModelMapper modelMapper;

    public OrderItems addItems(ItemsDto itemsDto, Order order){
        if (order == null || itemsDto.getItem() == null) {
            throw new AppRuntimeException("Order or Item is missing in the request", HttpStatus.BAD_REQUEST);
        }

        OrderItems items = modelMapper.map(itemsDto,OrderItems.class);
        items.setOrder(order);

        if (itemsDto.getId() != null) {
            OrderItems oldItems = itemsRepository.findById(itemsDto.getId()).orElseThrow(() ->
                    new AppRuntimeException("DopInfo not found", HttpStatus.NOT_FOUND));
            if (oldItems != null) {
                BeanUtils.copyProperties(items,oldItems);
                itemsRepository.save(oldItems);
            } else {
                throw new AppRuntimeException("Can't find record with identifier: " +
                        itemsDto.getId(), HttpStatus.NOT_FOUND);
            }
        }
        return itemsRepository.saveAndFlush(items);
    }

    public double getTotalPrice(List<ItemsDto> itemList) {
        if (itemList.isEmpty()) {
            throw new AppRuntimeException("Body is empty", HttpStatus.NO_CONTENT);
        } else {
            double total_price = 0;

            for (ItemsDto itemsDto : itemList) {
                OrderItems.ItemType itemType = itemsDto.getItemType();

                if (itemType == OrderItems.ItemType.MENU) {
                    Optional<Menu> menuOptional = menuService.menuRepository.findById(itemsDto.getItem());
                    if (menuOptional.isPresent()) {
                        Menu menu = menuOptional.get();
                        total_price += menu.getPrice() * itemsDto.getQuantity();
                    }
                } else {
                    Optional<SpecialOffer> offerOptional = offerService.offerRepository.findById(itemsDto.getItem());
                    if (offerOptional.isPresent()) {
                        SpecialOffer specialOffer = offerOptional.get();
                        total_price += specialOffer.getPrice() * itemsDto.getQuantity();
                    }
                }
            }
            return total_price;
        }
    }


    public List<ItemsDTOResponse> getItems(Long order_id){
        List<OrderItems> orderItems = itemsRepository.findByOrderId(order_id);
        if (orderItems.isEmpty()) {
            throw new AppRuntimeException("Can't find items by order id: " +
                    order_id, HttpStatus.NOT_FOUND);
        } else {
            List<ItemsDTOResponse>  response = new ArrayList<>();
            for(OrderItems orderItem : orderItems) {
                OrderItems.ItemType itemType = orderItem.getItemType();
                if(itemType == OrderItems.ItemType.MENU) {
                   Optional<Menu> menuOptional = menuService.menuRepository.findById(orderItem.getItem());
                   menuOptional.ifPresent(menu -> response.add(
                           ItemsDTOResponse.builder()
                                   .name(menu.getItems())
                                   .price(menu.getPrice() * orderItem.getQuantity())
                                   .quantity(orderItem.getQuantity())
                                   .build()
                    ));
                } else {
                   Optional<SpecialOffer> offer = offerService.offerRepository.findById(orderItem.getItem());
                   offer.ifPresent(specialOffer -> response.add(
                           ItemsDTOResponse.builder()
                                   .name(specialOffer.getOfferName())
                                   .price(specialOffer.getPrice() * orderItem.getQuantity())
                                   .quantity(orderItem.getQuantity())
                                   .build()
                   ));
                }
            }
            return response;
        }
    }
}
