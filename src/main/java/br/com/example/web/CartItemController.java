package br.com.example.web;

import br.com.example.domain.BusinessException;
import br.com.example.domain.Cart;
import br.com.example.domain.CartItem;
import br.com.example.domain.Product;
import br.com.example.repository.CartItemRepository;
import br.com.example.repository.CartRepository;
import br.com.example.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 17/10/13
 * Time: 17:20
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("items")
public class CartItemController {

    public static final String CART = "cart";

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @RequestMapping
    public ModelAndView list(ModelMap map) {
        List<CartItem> items = cartItemRepository.findAll();
        map.put("items", items);

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem item : items) {
            totalPrice = totalPrice.add(item.getPrice());
        }
        map.put("totalPrice", totalPrice);

        return new ModelAndView("items/list", map);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Object createOrUpdate(@Valid CartItem viewCartItem, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            ObjectError error = result.getAllErrors().get(0);
            if (error instanceof FieldError) {
                return ((FieldError) error).getField() + " " + error.getDefaultMessage();
            }
            return error.getDefaultMessage();
        }
        try {
            return updateSession(session, viewCartItem);
        } catch (BusinessException e) {
            return e.getMessage();
        }
    }

    @Transactional
    private String updateSession(HttpSession session, CartItem viewCartItem) throws BusinessException {

        Product product = productRepository.findOne(viewCartItem.getProduct().getId());
        product.updateStock(viewCartItem.getQuantity());
        CartItem item = cartItemRepository.findByProduct(product);
        if (item != null) {
            BeanUtils.copyProperties(item, viewCartItem, new String[]{"quantity"});
            viewCartItem.setQuantity(viewCartItem.getQuantity() + item.getQuantity());
        } else {
            viewCartItem.setProduct(product);
        }

        viewCartItem.setPrice(calculatePrice(viewCartItem));
        viewCartItem = cartItemRepository.save(viewCartItem);

        Long cartId = (Long) session.getAttribute(CART);
        Cart cart;
        if (cartId == null) {
            cart = new Cart();
            cart.setItems(new HashSet<CartItem>());
        } else {
            cart = cartRepository.findOne(cartId);
        }

        updateCart(cart, viewCartItem);

        cart = cartRepository.save(cart);
        session.setAttribute(CART, cart.getId());

        return "Item added to cart!";
    }

    private void updateCart(Cart cart, CartItem cartItem) {
        HashMap<Long, CartItem> cartItemsMap = cart.getCartItemsMap();

        if (cartItemsMap.containsKey(cartItem.getId())) {
            cartItemsMap.remove(cartItem.getId());
            cartItemsMap.put(cartItem.getId(), cartItem);
            cart.getItems().clear();
            cart.getItems().addAll(cartItemsMap.values());
        } else {
            cart.getItems().add(cartItem);
        }
    }

    private BigDecimal calculatePrice(CartItem cartItem) {
        BigDecimal result = BigDecimal.ZERO;
        return result.add(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
    }
}
