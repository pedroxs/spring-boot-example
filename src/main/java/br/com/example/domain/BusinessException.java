package br.com.example.domain;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 18/10/13
 * Time: 18:35
 * To change this template use File | Settings | File Templates.
 */
public class BusinessException extends Exception {
    private static final long serialVersionUID = 4241983730552434746L;

    public BusinessException(String message) {
        super(message);
    }
}
