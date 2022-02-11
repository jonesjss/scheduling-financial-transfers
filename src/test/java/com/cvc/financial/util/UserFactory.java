package com.cvc.financial.util;

import com.cvc.financial.domain.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserFactory {

    public static User createUserOrigemToBeSaved() {
        return new User("ORIGINATING");
    }

    public static User createUserDestinationToBeSaved() {
        return new User("DESTINATION");
    }
}
