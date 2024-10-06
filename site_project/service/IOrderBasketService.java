package com.ecommerce.site_project.service;

import java.util.List;

import com.ecommerce.site_project.entity.OrderBasket;
import com.ecommerce.site_project.entity.User;

public interface IOrderBasketService {
    List<OrderBasket> getAllOrderBaskets();

    public List<OrderBasket> listOrderBasket(User user);

    public Integer addProduct(Integer productId, Integer quantity, User user);

    public float updateQuantity(Integer productId, Integer quantity, User user);

    public void removeProduct(Integer productId, User user);
}
