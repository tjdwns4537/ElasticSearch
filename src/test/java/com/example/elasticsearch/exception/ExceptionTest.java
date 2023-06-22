package com.example.elasticsearch.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExceptionTest {

    @Test
    @DisplayName("인덱스 범위 테스트")
    public void IndexOutOfBoundsExceptionTest() {

        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            CustomRuntimeException.throwException("occur runtime error");
        });

        Assertions.assertThrows(IndexOutException.class, () -> {
            IndexOutException.throwException("occur index error");
        });
    }
}
