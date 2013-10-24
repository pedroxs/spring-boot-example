package br.com.example.web;

import br.com.example.domain.Cart;
import br.com.example.domain.CartItem;
import br.com.example.domain.Product;
import br.com.example.repository.CartItemRepository;
import br.com.example.repository.CartRepository;
import br.com.example.repository.ProductRepository;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 16/10/13
 * Time: 19:34
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @RequestMapping
    public ModelAndView list() {
        return new ModelAndView("products/list", "products", productRepository.findAll());
    }

    @RequestMapping("{id}")
    public ModelAndView view(@PathVariable("id") Long id) {
        return new ModelAndView("products/view", "product", productRepository.findOne(id));
    }

    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(@ModelAttribute Product product) {
        return "products/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(@Valid Product product, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return new ModelAndView("products/form", "formErrors", result.getAllErrors());
        }
        product = productRepository.save(product);
        redirectAttributes.addFlashAttribute("globalMessage", "Successfully created a new product");
        return new ModelAndView("redirect:/products/{product.id}", "product.id", product.getId());
    }

    @RequestMapping("{id}/remove")
    public ModelAndView remove(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        List<Cart> carts = cartRepository.findByProductId(id);
        boolean okToErase = true;
        for (Cart cart : carts) {
            if (isCartToOld(cart.getCreated())) {
                removeCart(cart);
            } else {
                okToErase = false;
            }
        }
        if (okToErase) {
            productRepository.delete(id);
            redirectAttributes.addFlashAttribute("globalMessage", "Successfully deleted product");
        } else {
            redirectAttributes.addFlashAttribute("globalMessage", "Can't delete product, there are still carts with them.");
        }
        return new ModelAndView("redirect:/products");
    }

    private void removeCart(Cart cart) {
        for (CartItem cartItem : cart.getItems()) {
            cartItem.setProduct(null);
            cartItemRepository.saveAndFlush(cartItem);
        }
        cartRepository.delete(cart);
    }

    private boolean isCartToOld(Date created) {
        Period period = new Period().withHours(1);
        DateTime time = new DateTime(created);
        return time.plus(period).isBeforeNow();
    }
}
