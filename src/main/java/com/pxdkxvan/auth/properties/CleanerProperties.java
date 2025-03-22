package com.pxdkxvan.auth.properties;

import com.pxdkxvan.auth.util.OptionalHelper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.cleaner")
public record CleanerProperties(Verification verification, Account account,
                                @NestedConfigurationProperty VerificationProperties verificationProperties) {

    private static final String DEFAULT_VERIFICATION_CRON = "0 0 0,8,16 * * ?";
    private static final String DEFAULT_ACCOUNT_CRON = "0 0 4 * * ?";

    public CleanerProperties {
        verification = OptionalHelper.getOrCreate(verification, () -> new Verification(null));
        account = OptionalHelper.getOrCreate(account, () -> new Account(null, verificationProperties.lifetime()));
    }

    public record Verification(String cron) {
        public Verification {
            cron = OptionalHelper.getOrDefault(cron, DEFAULT_VERIFICATION_CRON);
        }
    }

    public record Account(String cron, Duration verificationGracePeriod) {
        public Account {
            cron = OptionalHelper.getOrDefault(cron, DEFAULT_ACCOUNT_CRON);
        }
    }

}
