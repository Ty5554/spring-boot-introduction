package com.example.todo.controller.accounting;

import com.example.todo.service.accounting.ItemEntity;
import com.example.todo.service.accounting.ItemService;
import com.example.todo.service.accounting.OrderItemEntity;
import com.example.todo.service.accounting.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/accounting")
@RequiredArgsConstructor
public class AccountingController {

    private final ItemService itemService;
    private final OrderService orderService;

    @GetMapping
    public String checkout(Model model) {
        var items = itemService.findAll().stream().map(ItemDTO::fromEntity).toList();
        model.addAttribute("items", items);
        return "accounting/checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(@RequestParam Map<String, String> params, Model model) {
        List<ItemEntity> allItems = itemService.findAll();
        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (ItemEntity item : allItems) {
            String qtyStr = params.get("quantity_" + item.id());
            if (qtyStr != null) {
                int qty;
                try {
                    qty = Integer.parseInt(qtyStr);
                } catch (NumberFormatException e) {
                    qty = 0;
                }
                if (qty > 0) {
                    int subtotal = item.price() * qty;
                    orderItems.add(new OrderItemEntity(null, 0, item.name(), item.price(), qty, subtotal));
                }
            }
        }

        if (orderItems.isEmpty()) {
            model.addAttribute("error", "商品を1つ以上選択してください。");
            var items = allItems.stream().map(ItemDTO::fromEntity).toList();
            model.addAttribute("items", items);
            return "accounting/checkout";
        }

        String paymentStr = params.get("payment");
        int payment;
        try {
            payment = Integer.parseInt(paymentStr);
        } catch (NumberFormatException e) {
            model.addAttribute("error", "お支払い金額を入力してください。");
            var items = allItems.stream().map(ItemDTO::fromEntity).toList();
            model.addAttribute("items", items);
            return "accounting/checkout";
        }

        int subtotal = orderItems.stream().mapToInt(OrderItemEntity::subtotal).sum();
        int tax = (int) Math.floor(subtotal * 0.1);
        int totalAmount = subtotal + tax;

        if (payment < totalAmount) {
            model.addAttribute("error", "お支払い金額が不足しています。合計: " + String.format("%,d", totalAmount) + "円");
            var items = allItems.stream().map(ItemDTO::fromEntity).toList();
            model.addAttribute("items", items);
            return "accounting/checkout";
        }

        var order = orderService.checkout(orderItems, payment);
        return "redirect:/accounting/orders/" + order.id();
    }

    @GetMapping("/orders")
    public String orderList(Model model) {
        var orders = orderService.findAll().stream().map(OrderDTO::fromEntity).toList();
        model.addAttribute("orders", orders);
        return "accounting/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable long id, Model model) {
        var order = orderService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var orderItems = orderService.findOrderItems(id).stream()
                .map(OrderItemDTO::fromEntity).toList();
        model.addAttribute("order", OrderDTO.fromEntity(order));
        model.addAttribute("orderItems", orderItems);
        return "accounting/order_detail";
    }
}
