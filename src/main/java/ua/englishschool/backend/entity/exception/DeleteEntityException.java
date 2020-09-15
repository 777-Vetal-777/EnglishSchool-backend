package ua.englishschool.backend.entity.exception;

public class DeleteEntityException extends RuntimeException{

    public DeleteEntityException() {
    }

    public DeleteEntityException(String message) {
        super(message);
    }
}
