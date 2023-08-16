package dev.mkuwan.adminstock.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DisplayNameTest {

    @Test
    void NameLengthIsZero_ThrowException() {
        // arrange
        String name = "";

        // act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
           new DisplayName(name);
        });

        // assertion
        String expectedMessage = "販売用商品名は1文字以上、20文字以内にしてください";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void NameLengthThan20_ThrowException(){
        // arrange
        String name = "123456789012345678901";

        // act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new DisplayName(name);
        });

        // assertion
        String expectedMessage = "販売用商品名は1文字以上、20文字以内にしてください";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void NameLengthEqual20_AssertionSucceeds(){
        // arrange
        String name = "12345678901234567890";

        // act
        var displayName = new DisplayName(name);

        // assertion
        assertEquals(name, displayName.name());
    }

    @Test
    void NameLengthEqual1_AssertionSucceeds(){
        // arrange
        String name = "1";

        // act
        var displayName = new DisplayName(name);

        // assertion
        assertEquals(name, displayName.name());
    }

    @Test
    void NameLengthEqualJapanese20_AssertionSucceeds(){
        // arrange
        String name = "あいうえおかきくけこさしすせそたちつてと";

        // act
        var displayName = new DisplayName(name);

        // assertion
        assertEquals(name, displayName.name());
    }
}