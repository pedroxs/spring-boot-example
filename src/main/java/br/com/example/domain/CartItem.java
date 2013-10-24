package br.com.example.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 16/10/13
 * Time: 21:23
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class CartItem implements Serializable {

    private static final long serialVersionUID = -6106907890057166462L;

    @Id
    @GeneratedValue
    private Long id;

    @Min(1)
    @NotNull
    @Column
    private Integer quantity;

    @Column
    private BigDecimal price;

    @OneToOne(optional = true, orphanRemoval = true)
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
