package br.com.example.repository;

import br.com.example.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 16/10/13
 * Time: 19:34
 * To change this template use File | Settings | File Templates.
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c join c.items i join i.product p where p.id = ?1")
    List<Cart> findByProductId(Long productId);
}
