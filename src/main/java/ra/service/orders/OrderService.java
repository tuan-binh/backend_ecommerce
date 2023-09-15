package ra.service.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.exception.OrderException;
import ra.mapper.orders.OrderMapper;
import ra.model.domain.EDelivered;
import ra.model.domain.Orders;
import ra.model.dto.request.OrderRequest;
import ra.model.dto.response.OrderResponse;
import ra.repository.IOrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
	
	@Autowired
	private IOrderRepository orderRepository;
	@Autowired
	private OrderMapper orderMapper;
	
	@Override
	public Page<OrderResponse> findAll(Pageable pageable, Optional<String> phone) {
		List<OrderResponse> list = phone.map(s -> orderRepository.findAllByPhone(pageable, s).stream()
				  .map(item -> orderMapper.toResponse(item))
				  .collect(Collectors.toList())).orElseGet(() -> orderRepository.findAll(pageable).stream()
				  .map(item -> orderMapper.toResponse(item))
				  .collect(Collectors.toList()));
		return new PageImpl<>(list, pageable, list.size());
	}
	
	@Override
	public OrderResponse findById(Long id) throws OrderException {
		Optional<Orders> optionalOrders = orderRepository.findById(id);
		return optionalOrders.map(item -> orderMapper.toResponse(item)).orElseThrow(() -> new OrderException("order not found"));
	}
	
	@Override
	public OrderResponse save(OrderRequest orderRequest) {
		return orderMapper.toResponse(orderRepository.save(orderMapper.toEntity(orderRequest)));
	}
	
	@Override
	public OrderResponse update(OrderRequest orderRequest, Long id) {
		Orders orders = orderMapper.toEntity(orderRequest);
		orders.setId(id);
		return orderMapper.toResponse(orderRepository.save(orders));
	}
	
	@Override
	public OrderResponse changeStatus(Long id) throws OrderException {
		Orders orders = findOrderById(id);
		orders.setStatus(!orders.isStatus());
		return orderMapper.toResponse(orderRepository.save(orders));
	}
	
	@Override
	public OrderResponse changeDelivery(String typeDelivery, Long id) throws OrderException {
		EDelivered type = findDeliveryByInput(typeDelivery);
		Orders orders = findOrderById(id);
		if (orders.getEDelivered().toString().equals("PENDING")) {
			if (type.toString().equals("DELIVERY") || type.toString().equals("SUCCESS")) {
				throw new OrderException("You are in the status of waiting for confirmation");
			}
			orders.setEDelivered(type);
		} else if (orders.getEDelivered().toString().equals("PREPARE")) {
			if (type.toString().equals("PENDING") || type.toString().equals("SUCCESS")) {
				throw new OrderException("You are in preparation mode");
			}
			orders.setEDelivered(type);
		} else if (orders.getEDelivered().toString().equals("DELIVERY")) {
			if (type.toString().equals("PENDING") || type.toString().equals("PREPARE")) {
				throw new OrderException("You are in the delivery status");
			}
			orders.setEDelivered(type);
		} else if (orders.getEDelivered().toString().equals("SUCCESS")) {
			if (type.toString().equals("PENDING") || type.toString().equals("PREPARE") || type.toString().equals("DELIVERY")) {
				throw new OrderException("You are in the successful delivery status");
			}
			orders.setEDelivered(type);
		} else {
			if (type.toString().equals("PENDING") || type.toString().equals("PREPARE") || type.toString().equals("DELIVERY") || type.toString().equals("SUCCESS")) {
				throw new OrderException("You are in the order cancellation status");
			}
			orders.setEDelivered(type);
		}
		return orderMapper.toResponse(orderRepository.save(orders));
	}
	
	public Orders findOrderById(Long id) throws OrderException {
		Optional<Orders> optionalOrders = orderRepository.findById(id);
		return optionalOrders.orElseThrow(() -> new OrderException("order not found"));
	}
	
	public EDelivered findDeliveryByInput(String typeDelivery) throws OrderException {
		switch (typeDelivery) {
			case "pending":
				return EDelivered.PENDING;
			case "prepare":
				return EDelivered.PREPARE;
			case "delivery":
				return EDelivered.DELIVERY;
			case "success":
				return EDelivered.SUCCESS;
			case "cancel":
				return EDelivered.CANCEL;
			default:
				throw new OrderException("delivery not found");
		}
	}
	
}
