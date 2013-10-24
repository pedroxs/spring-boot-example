package br.com.example.web;

import br.com.example.domain.BusinessException;
import br.com.example.domain.Cart;
import br.com.example.domain.CartItem;
import br.com.example.domain.Product;
import br.com.example.repository.CartItemRepository;
import br.com.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 17/10/13
 * Time: 20:51
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public String update(@RequestBody Cart viewCart, HttpSession session) {

        Long cartId = (Long) session.getAttribute(CartItemController.CART);

        if (cartId != null) {
            try {
                Cart cart = cartRepository.findOne(cartId);
                HashMap<Long, CartItem> cartItems = cart.getCartItemsMap();
                for (CartItem viewItem : viewCart.getItems()) {
                    if (cartItems.containsKey(viewItem.getId())) {
                        updateItem(cartItems, viewItem);
                    }
                }
                cart.getItems().clear();
                cart.getItems().addAll(cartItems.values());
                cartRepository.save(cart);
                session.setAttribute(CartItemController.CART, cart.getId());

                return "Cart updated.";
            } catch (BusinessException e) {
                return e.getMessage();
            }
        }

        return "No cart.";
    }

    private void updateItem(HashMap<Long, CartItem> cartItems, CartItem viewItem) throws BusinessException {
        CartItem cartItem = cartItems.get(viewItem.getId());
        Product product = cartItem.getProduct();
        if (viewItem.getQuantity() > cartItem.getQuantity()) {
            product.updateStock(viewItem.getQuantity() - cartItem.getQuantity());
        } else {
            product.updateStock(cartItem.getQuantity() - viewItem.getQuantity(), true);
        }
        if (viewItem.getQuantity() == 0) {
            cartItems.remove(cartItem.getId());
            cartItem.setProduct(null);
        } else {
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(viewItem.getQuantity())));
            cartItem.setQuantity(viewItem.getQuantity());
        }
        cartItemRepository.saveAndFlush(cartItem);
    }
}
