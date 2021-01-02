package com.rugoo.cloud.storage.exception;

/**
 * Description
 *
 * @author boxin
 * @date 2020-12-29
 * @see com.rugoo.cloud.storage.exception
 */
public class CloudStorageException extends RuntimeException {

    private CloudStorageException() {}

    public CloudStorageException(String message) {
        this(message, (Throwable) null);
    }

    public CloudStorageException(String message, Throwable cause) {
        super(message, cause);
    }

}
