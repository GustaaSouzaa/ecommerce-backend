package com.ecommerce.ecommercebackend.service;

import com.ecommerce.ecommercebackend.entity.Cart;
import com.ecommerce.ecommercebackend.entity.CartItem;
import com.ecommerce.ecommercebackend.entity.Order;
import com.ecommerce.ecommercebackend.entity.OrderItem;
import com.ecommerce.ecommercebackend.entity.Product;
import com.ecommerce.ecommercebackend.entity.User;
import com.ecommerce.ecommercebackend.repository.OrderRepository;
import com.ecommerce.ecommercebackend.repository.OrderItemRepository;
import com.ecommerce.ecommercebackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.ecommerce.ecommercebackend.dto.OrderResponse;
import com.ecommerce.ecommercebackend.dto.OrderItemResponse;
import com.ecommerce.ecommercebackend.exception.BadRequestException;
import com.ecommerce.ecommercebackend.exception.ResourceNotFoundException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        ProductRepository productRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }


    @Transactional
    public OrderResponse createOrderFromCart(User user) {
        Cart cart = cartService.getOrCreateCartByUser(user);
        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.isEmpty()) {
            // Lança exceção BadRequestException em vez de RuntimeException
            throw new BadRequestException("O carrinho está vazio. Adicione produtos antes de finalizar o pedido.");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            Integer requestedQuantity = cartItem.getQuantity();

            if (product.getStockQuantity() < requestedQuantity) {
                // Lança exceção BadRequestException em vez de RuntimeException
                throw new BadRequestException("Estoque insuficiente para o produto: " + product.getName() +
                        ". Disponível: " + product.getStockQuantity() +
                        ", solicitado: " + requestedQuantity);
            }
            product.setStockQuantity(product.getStockQuantity() - requestedQuantity);
            productRepository.save(product);

            totalAmount = totalAmount.add(product.getPrice().multiply(new BigDecimal(requestedQuantity)));
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice());

            order.getOrderItems().add(orderItem);
            orderItemRepository.save(orderItem);
        }

        cartService.clearCart(user);

        return mapOrderToOrderResponse(order);
    }


    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(User user) {
        List<Order> orders = orderRepository.findByUser(user);

        // Percorrer os pedidos para garantir que os OrderItems sejam carregados
        orders.forEach(order -> {
            order.getOrderItems().size();
        });

        return orders.stream()
                .map(this::mapOrderToOrderResponse)
                .collect(Collectors.toList());
    }


    private OrderResponse mapOrderToOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemResponse(
                        orderItem.getId(),
                        orderItem.getProduct().getId(),
                        orderItem.getProduct().getName(),
                        orderItem.getPriceAtPurchase(),
                        orderItem.getQuantity(),
                        orderItem.getPriceAtPurchase().multiply(new BigDecimal(orderItem.getQuantity()))
                ))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getUser().getUsername(),
                order.getOrderDate(),
                order.getTotalAmount(),
                itemResponses
        );
    }
}