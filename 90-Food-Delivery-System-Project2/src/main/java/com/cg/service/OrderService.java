package com.cg.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.dto.OrderDto;
import com.cg.entity.DeliveryAgent;
import com.cg.entity.Order;
import com.cg.entity.Payment;
import com.cg.enumeration.OrderStatus;
import com.cg.iservice.IOrderService;
import com.cg.mapper.OrderMapper;
import com.cg.repository.CustomerRepository;
import com.cg.repository.DeliveryAgentRepository;
import com.cg.repository.MenuItemRepository;
import com.cg.repository.OrderRepository;
import com.cg.entity.MenuItem;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DeliveryAgentRepository deliveryAgentRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    @Transactional
    public OrderDto place(OrderDto dto) {    	System.out.println("=== ORDER SERVICE: place() called ===");
    	System.out.println("ItemIds received: " + dto.getItemIds());
    	System.out.println("ItemIds size: " + (dto.getItemIds() != null ? dto.getItemIds().size() : "NULL"));

    	Order entity = new Order();
    	entity.setOrderStatus(dto.getOrderStatus() != null ? dto.getOrderStatus() : OrderStatus.PLACED);
    	entity.setOrderDate(LocalDateTime.now());
    	entity.setTotalAmount(dto.getTotalAmount());

    	// Set customer
    	if (dto.getCustomerId() != null) {
    	    customerRepository.findById(dto.getCustomerId())
    	    .ifPresent(entity::setCustomer);
    	}

    	// IMPORTANT: Add items from itemIds (which should have duplicates for quantity)
    	if (dto.getItemIds() != null && !dto.getItemIds().isEmpty()) {
    	    List<MenuItem> itemsToAdd = new ArrayList<>();
    	    List<MenuItem> allItems = menuItemRepository.findAllById(dto.getItemIds());
    	    System.out.println("All items fetched from DB: " + allItems.size());
    	
    	// The itemIds list already contains duplicates for quantity
    	Map<Long, MenuItem> itemMap = allItems.stream().collect(Collectors.toMap(MenuItem::getItemId, item -> item));
    	
    	for (Long itemId : dto.getItemIds()) {
    	     MenuItem item = itemMap.get(itemId);
    	
    	if (item != null) {
    	    itemsToAdd.add(item);
    	    System.out.println("Adding item: " + item.getItemName() + " (ID: " + item.getItemId() + ")");
    	}
    	}

    	System.out.println("Total items being added to order: " + itemsToAdd.size());
    	entity.setItems(itemsToAdd);
    	} else {
    	   System.out.println("NO ITEM IDS PROVIDED!");
    	}

    	Order saved = orderRepository.save(entity);
    	System.out.println("Order saved with ID: " + saved.getOrderId());
    	System.out.println("Items in saved order: " + (saved.getItems() != null ? saved.getItems().size() : "NULL"));
    	return OrderMapper.toDto(saved);
    }

    @Override
    @Transactional
    public OrderDto updateStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
 
        switch (order.getOrderStatus()) {

            case PLACED -> orderRepository.updateStatusOnly(orderId, OrderStatus.PREPARING);
            case PREPARING -> {
                DeliveryAgent agent = deliveryAgentRepository.findFirstByAvailabilityTrue()
                        .orElseThrow(() -> new RuntimeException("No agents available"));
                agent.setAvailability(false);
                deliveryAgentRepository.save(agent);
                orderRepository.updateStatusAndAgent(orderId, OrderStatus.PICKED_UP, agent);
            }

            case PICKED_UP -> orderRepository.updateStatusOnly(orderId, OrderStatus.OUT_FOR_DELIVERY);
            case OUT_FOR_DELIVERY -> {
                // Explicitly set the new status
                order.setOrderStatus(OrderStatus.DELIVERED);

                // Handle Cash on Delivery Payment
                if (order.getPayment() != null
&& order.getPayment().getPaymentMethod() == com.cg.enumeration.PaymentMethod.CASH_ON_DELIVERY) {
                    order.setTransactionStatus(com.cg.enumeration.TransactionStatus.SUCCESS);
                }
                // Release the Agent
                if (order.getDeliveryAgent() != null) {
                    DeliveryAgent agent = order.getDeliveryAgent();
                    agent.setAvailability(true);
                    deliveryAgentRepository.save(agent);
                }
                // Save and force immediate database update
                orderRepository.saveAndFlush(order);
            }
            default -> throw new IllegalStateException("Invalid status transition from: " + order.getOrderStatus());
        }
 
        // Fetch fresh data for the DTO
        Order updated = orderRepository.findById(orderId).orElse(null);
        return OrderMapper.toDto(updated);
    }
 
    @Override
    @Transactional(readOnly = true)
    public OrderDto getById(Long id) throws NotFoundException {
        Order order = orderRepository.findByIdWithPaymentAndAgent(id)
            .orElseThrow(NotFoundException::new);
        
        OrderDto dto = OrderMapper.toDto(order);
        
     // Populate item details with proper quantity counting
     if (order.getItems() != null && !order.getItems().isEmpty()) {
         Map<String, OrderDto.OrderItemDetail> itemDetailsMap = new LinkedHashMap<>();
         Map<String, Integer> itemQuantities = new HashMap<>();
         Map<String, Double> itemPrices = new HashMap<>();

     // Count all items by name
     for (MenuItem item : order.getItems()) {
          String itemName = item.getItemName();
          itemQuantities.put(itemName, itemQuantities.getOrDefault(itemName, 0) + 1);

     if (!itemPrices.containsKey(itemName)) {
         itemPrices.put(itemName, item.getPrice());
     }
     }
     
     // Create detail map
     itemQuantities.forEach((itemName, qty) -> {
         double price = itemPrices.get(itemName);
         double subtotal = qty * price;
         itemDetailsMap.put(itemName, new OrderDto.OrderItemDetail(qty, price, subtotal));
     });

     dto.setItemDetails(itemDetailsMap);
     }

     return dto;
    }

    @Override
    public List<OrderDto> getByCustomer(Long customerId) {
        return orderRepository.findByCustomerCustomerId(customerId)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByCustomerEmail(String email) {
        List<Order> orders = orderRepository.findAllByCustomer_EmailOrderByOrderDateDesc(email);
        List<Order> safe = (orders != null) ? orders : new ArrayList<>();
        return safe.stream().map(OrderMapper::toDto).collect(Collectors.toList());
    }

	@Override
	public void cancel(Long orderId) {
		Order orderDto = orderRepository.findById(orderId).orElseThrow();
		orderDto.setOrderStatus(OrderStatus.CANCELLED);
		orderRepository.save(orderDto);
	}

	@Override
	@Transactional
	public OrderDto createOrder(OrderDto newOrderDto) {
	    Order order = new Order();
	    order.setTotalAmount(newOrderDto.getTotalAmount());
	    order.setOrderDate(LocalDateTime.now());
	    order.setOrderStatus(OrderStatus.PENDING);
	    
	    // CRITICAL: If you have a customerId in the DTO, link it here
	    if (newOrderDto.getCustomerId() != null) {
	        customerRepository.findById(newOrderDto.getCustomerId())
	            .ifPresent(order::setCustomer);
	    }
	    
	    Order savedOrder = orderRepository.save(order);
	    
	    // Map back to DTO
	    OrderDto savedDto = OrderMapper.toDto(savedOrder);
	    return savedDto;
	}
	
	@Override
	public List<DeliveryAgent> getAvailableAgents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderDto map(Order order) {
	    OrderDto dto = new OrderDto();
	    dto.setOrderId(order.getOrderId());
	    dto.setOrderDate(order.getOrderDate());
	    dto.setOrderStatus(order.getOrderStatus());
	    dto.setTotalAmount(order.getTotalAmount());
	    dto.setTransactionStatus(order.getTransactionStatus());
	    if (order.getCustomer() != null) {
	        dto.setCustomerId(order.getCustomer().getCustomerId());
	    }
 
	    if (order.getItems() != null) {
	        dto.setItemIds(order.getItems().stream()
	            .map(mi -> mi.getItemId())
	            .toList());
	    }
 
	    Payment payment = order.getPayment();

	    if (payment != null) {
	        dto.setPaymentId(payment.getPaymentId());
	        dto.setPaymentMethod(payment.getPaymentMethod()); // enum

	        if (payment.getTransactionStatus() != null) {
	            dto.setTransactionStatus(payment.getTransactionStatus());
	        }
	    }
 
	    DeliveryAgent agent = order.getDeliveryAgent();

	    if (agent != null) {
	        dto.setDeliveryAgentId(agent.getAgentId());
	        dto.setDeliveryAgentName(agent.getAgentName());
	        dto.setDeliveryAgentPhone(agent.getContact());
	    }
 
	    return dto;
	}
}