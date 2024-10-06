//package com.khomsi.site_project.controller;
//
//
//
//import com.khomsi.site_project.entity.Purchase;
//import com.khomsi.site_project.service.PurchaseService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class PurchaseController {
//
//    @Autowired
//    private PurchaseService purchaseService;
//
//    @PostMapping("/purchase/save")
//    public String savePurchase(@RequestParam("userId") Long userId,
//                               @RequestParam("productId") Long productId,
//                               @RequestParam("quantity") int quantity) {
//        // Create and save the purchase
//        Purchase purchase = new Purchase();
//        purchase.setUserId(userId);
//        purchase.setProductId(productId);
//        purchase.setQuantity(quantity);
//
//        purchaseService.savePurchase(purchase);
//        return "Purchase saved successfully!";
//    }
//}
//
//package com;


