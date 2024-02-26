package spring.backend.service;

import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import spring.backend.dto.order.ItemsDto;
import spring.backend.dto.order.OrderDTO;
import spring.backend.dto.order.OrderDTOResponse;
import spring.backend.entity.Order;
import spring.backend.entity.OrderItems;
import spring.backend.exception.AppRuntimeException;
import spring.backend.repository.jpa.OrderRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    OrderRepository orderRepository;
    ModelMapper modelMapper;
    DopInfoService dopInfoService;
    UserService userService;
    ItemsService itemsService;

    public void addNew(OrderDTO orderDTO) {
        Order order = new Order();
        order.setUser(userService.getUser());
        order.setDopInfoCoffeeshop(dopInfoService.findByID(orderDTO.getDopInfoCoffeeshop_id()));
        updateOrderDetails(order, orderDTO);
        List<OrderItems> items = processOrderItems(orderDTO.getItemsList(), order);
        order.setItems(items);
    }

    public OrderDTOResponse getOrder(Long order_id) {
        Optional<Order> orderOptional = orderRepository.findWithItems(order_id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            OrderDTOResponse response = modelMapper.map(order, OrderDTOResponse.class);
            response.setDopInfoCoffeeshop_id(order.getDopInfoCoffeeshop().getId());
            response.setItemsList(itemsService.getItems(order_id));

            return response;
        } else {
            throw  new AppRuntimeException("By this id " + order_id + " empty ",HttpStatus.NOT_FOUND);
        }
    }

    public String cancelOrder(Long order_id){
        Optional<Order> orderOptional = orderRepository.findWithItems(order_id);
        if(orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(Order.Status.Canceled);
            if (order.getPayment_method() == Order.PaymentMethod.Card && order.getPayment_status() == Order.PaymentStatus.Paid) {
                order.setPayment_status(Order.PaymentStatus.Refunded);
                orderRepository.save(order);
                return "You will be refunded because u canceled order";
            }else {
                orderRepository.save(order);
                return "You canceled order";
            }
//            OrderDTOResponse response = modelMapper.map(order, OrderDTOResponse.class);
//            response.setItemsList(itemsService.getItems(order_id));
//            response.setDopInfoCoffeeshop_id(order.getDopInfoCoffeeshop().getId());
        }else {
            throw new AppRuntimeException("by this id not found ",HttpStatus.NOT_FOUND);
        }
    }

    public OrderDTOResponse changeOrder(OrderDTO orderDTO) {
        Optional<Order> orderOptional = orderRepository.findById(orderDTO.getId());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.getStatus() != Order.Status.Canceled) {
                if (order.getPayment_method() == Order.PaymentMethod.Card && order.getPayment_status() == Order.PaymentStatus.Paid) {
                    System.out.println("You will be refunded and charged again since you changed the order");
                }
                updateOrderDetails(order, orderDTO);
                List<OrderItems> items = processOrderItems(orderDTO.getItemsList(), order);
                order.setItems(items);
                return modelMapper.map(order, OrderDTOResponse.class);
            } else {
                throw new AppRuntimeException("You cannot change canceled order", HttpStatus.METHOD_NOT_ALLOWED);
            }
        } else {
            throw new AppRuntimeException("Body or order id is missing ", HttpStatus.NOT_FOUND);
        }
    }

    public List<OrderDTOResponse> getListOrders() {
        List<Order> orderList = orderRepository.findByUserId(userService.getUser().getId());
        return orderList.stream()
                .map(order -> OrderDTOResponse.builder()
                        .id(order.getId())
                        .dopInfoCoffeeshop_id(order.getDopInfoCoffeeshop().getId())
                        .order_date(order.getOrder_date())
                        .possibly_ready_time(order.getPossibly_ready_time())
                        .paymentMethod(order.getPayment_method())
                        .paymentStatus(order.getPayment_status())
                        .status(order.getStatus())
                        .order_number(order.getOrder_number())
                        .itemsList(itemsService.getItems(order.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    private void updateOrderDetails(Order order, OrderDTO orderDTO) {
        order.setOrder_date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        order.setPossibly_ready_time(LocalDateTime.now().plusMinutes(15).truncatedTo(ChronoUnit.SECONDS));
        order.setPayment_method(orderDTO.getPaymentMethod());
        order.setPayment_status(orderDTO.getPaymentStatus());
        order.setStatus(Order.Status.Not_ready);
        order.setTotal_price(itemsService.getTotalPrice(orderDTO.getItemsList()));
        orderRepository.save(order);
    }

    private List<OrderItems> processOrderItems(List<ItemsDto> itemsDtoList, Order order) {
        List<OrderItems> items = new ArrayList<>();
        for (ItemsDto itemsDto : itemsDtoList) {
            items.add(itemsService.addItems(itemsDto, order));
        }
        return items;
    }
}
