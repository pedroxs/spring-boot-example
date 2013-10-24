package br.com.example.repository;

import br.com.example.domain.CartItem;
import br.com.example.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 16/10/13
 * Time: 22:13
 * To change this template use File | Settings | File Templates.
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByProduct(Product product);
}
