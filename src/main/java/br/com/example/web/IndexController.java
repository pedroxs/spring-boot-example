package br.com.example.web;

import br.com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 17/10/13
 * Time: 18:53
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping
    public ModelAndView list() {
        return new ModelAndView("index", "products", productRepository.findAll());
    }
}
