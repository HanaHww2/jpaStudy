package me.study.smallshop.exception;

public class NotEnoughStockException extends RuntimeException{
    public NotEnoughStockException(String message) {
        super(message);
    }
}
