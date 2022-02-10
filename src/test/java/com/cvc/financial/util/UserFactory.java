package com.cvc.financial.util;

import com.cvc.financial.domain.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserFactory {

    public static User createUserToBeSaved() {
        return new User("TEST");
    }
}
