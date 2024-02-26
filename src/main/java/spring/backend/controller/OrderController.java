package spring.backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spring.backend.dto.order.ItemsDTOResponse;
import spring.backend.dto.order.OrderDTO;
import spring.backend.dto.order.OrderDTOResponse;
import spring.backend.service.DopInfoService;
import spring.backend.service.ItemsService;
import spring.backend.service.OrderService;

import java.util.List;

@Controller
@RequestMapping("/api/coffeeshop/order")
@AllArgsConstructor
public class OrderController {
    OrderService orderService;
    DopInfoService dopInfoService;
    ItemsService itemsService;

    @PostMapping(value = "/create")
    public ResponseEntity<String> addNewOrder(@RequestBody OrderDTO itemsDto) {
        if(itemsDto != null){
            orderService.addNew(itemsDto);
            return ResponseEntity.ok("Order successfully created");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Body is empty");
        }
    }
    @GetMapping(value = "/get/{id}")
    public ResponseEntity<OrderDTOResponse> getOne(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping(value = "/history")
    public ResponseEntity<List<OrderDTOResponse>> history(){
        return ResponseEntity.ok(orderService.getListOrders());
    }

    @PostMapping(value = "/cancel/{id}")
    public ResponseEntity<String> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @PatchMapping(value = "/change")
    public ResponseEntity<OrderDTOResponse> changeOrder(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.changeOrder(orderDTO));
    }
}
