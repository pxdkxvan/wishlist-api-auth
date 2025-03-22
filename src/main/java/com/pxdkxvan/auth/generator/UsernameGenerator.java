package com.pxdkxvan.auth.generator;

import com.github.javafaker.Faker;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class UsernameGenerator {

    private static final Faker faker = new Faker();

    public String random() {
        String FIRST_WORD = faker.options().option(
                faker.superhero().prefix(),
                faker.name().firstName(),
                faker.ancient().god(),
                faker.medical().diseaseName()
        );

        String SECOND_WORD = faker.options().option(
                faker.superhero().suffix(),
                faker.name().lastName(),
                faker.ancient().titan(),
                faker.gameOfThrones().dragon()
        );

        String DIGIT_PART = faker.options().option(
                faker.number().digit(),
                faker.number().digits(2),
                faker.number().digits(3)
        );

        return (FIRST_WORD + SECOND_WORD + DIGIT_PART).replaceAll("\\s", "");
    }

}
