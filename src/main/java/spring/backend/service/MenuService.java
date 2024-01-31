package spring.backend.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import spring.backend.dto.DopInfoDTO;
import spring.backend.dto.MenuDTO;
import spring.backend.entity.Menu;
import spring.backend.exception.AppRuntimeException;
import spring.backend.repository.jpa.MenuRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    CoffeeshopService coffeeshopService;
    ModelMapper modelMapper = new ModelMapper();
    public Menu saveMenu(MenuDTO menuDTO) {
        if(menuDTO.getCoffeshop_id() == null) {
            throw new AppRuntimeException("Parameter coffeeshop_id is not found in request..!!", HttpStatus.NO_CONTENT);
        }
        Menu menu = new Menu(menuDTO.getId(),menuDTO.getItems(),menuDTO.getPrice(),
                coffeeshopService.getCoffeeShopById(menuDTO.getCoffeshop_id()),menuDTO.getType());

        if (menuDTO.getId() != null) {
            Menu oldMenu = menuRepository.findById(menuDTO.getId()).orElseThrow(() ->
                    new AppRuntimeException("Menu not found", HttpStatus.NOT_FOUND));
            if (oldMenu != null) {
                BeanUtils.copyProperties(menu,oldMenu);
                menuRepository.save(oldMenu);
            } else {
                throw new AppRuntimeException("Can't find record with identifier: " +
                        menuDTO.getId(), HttpStatus.NOT_FOUND);
            }
         }
        return menuRepository.save(menu);
    }

    public List<MenuDTO> newestItems(Long coffeeshopId){
        List<Menu> menuList = menuRepository.findTop4ByCoffeeshopIdOrderByIdDesc(coffeeshopId);
        return menuList.stream()
                .map(menu -> MenuDTO.builder()
                        .id(menu.getId())
                        .items(menu.getItems())
                        .price(menu.getPrice())
                        .coffeshop_id(menu.getCoffeeshop().getId())
                        .type(menu.getItemsType())
                        .build())
                .collect(Collectors.toList());
    }

//    public List<MenuDTO> popularItems(Long coffeeshopId, int topN) {
//        List<Menu> allItems = menuRepository.findAll(); // Получите все предметы из базы данных
//        List<MenuDTO> popularItems = new ArrayList<>();
//
//        for (Menu item : allItems) {
//            int orderCount = menuRepository.countByItemsContainsAndCoffeeshopId(item, coffeeshopId);
//            item.setOrderCount(orderCount);
//        }
//
//        allItems.sort(Comparator.comparingInt(Menu::getOrderCount).reversed());
//
//        int topItemsCount = Math.min(allItems.size(), topN);
//        List<Item> topItems = allItems.subList(0, topItemsCount);
//
//        popularItems = topItems.stream()
//                .map(item -> ItemDTO.builder()
//                        .id(item.getId())
//                        .name(item.getName())
//                        .price(item.getPrice())
//                        .coffeshopId(coffeeshopId)
//                        .orderCount(item.getOrderCount())
//                        .build())
//                .collect(Collectors.toList());
//
//        return popularItems;
//    }

    public void deleteItem(Long id){ menuRepository.deleteById(id); }
}
