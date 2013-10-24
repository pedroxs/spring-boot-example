package br.com.example.domain;

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 16/10/13
 * Time: 18:48
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Product implements Serializable {

    private static final long serialVersionUID = -5121420520220661789L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Product.class);

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(nullable = false)
    private Integer stock;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @Lob
    @Column(nullable = true)
    private byte[] photo;

    @Transient
    private MultipartFile multipartFile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
        if (multipartFile != null) {
            try {
                this.photo = multipartFile.getBytes();
            } catch (IOException e) {
                LOGGER.error("Failed trying to retrieve bytes from file.", e);
            }
        }
    }

    public String getPhotoBase64() {
        return Base64.encodeBase64String(photo);
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    /**
     * Default is to subtract from stock
     * @param newQuantity
     */
    public void updateStock(int newQuantity) throws BusinessException {
        updateStock(newQuantity, false);
    }

    public void updateStock(int newQuantity, boolean add) throws BusinessException {

        if (stock == 0) {
            throw new BusinessException("No more products available!");
        }

        int newStock;
        if (add) {
            newStock = stock + newQuantity;
        } else {
            newStock = stock - newQuantity;
        }

        if (newStock < 0) {
            throw new BusinessException("Not enough products to satisfy purchase amount!");
        }

        stock = newStock;
    }
}
