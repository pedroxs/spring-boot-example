package br.com.example.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 16/10/13
 * Time: 19:17
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Cart implements Serializable {

    private static final long serialVersionUID = 1400573379344159243L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Date created;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items;

    public Cart() {
        created = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<CartItem> getItems() {
        return items;
    }

    public void setItems(Set<CartItem> items) {
        this.items = items;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public HashMap<Long, CartItem> getCartItemsMap() {

        HashMap<Long, CartItem> cartItems = new HashMap<Long, CartItem>();

        for (CartItem item : items) {
            cartItems.put(item.getId(), item);
        }

        return cartItems;
    }
}
