package com.pxkdxvan.auth.local.properties;

import com.pxkdxvan.auth.shared.utils.OptionalHelper;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mail.properties.mail")
public record MailOptionsProperties(SMTP smtp) {

    private static final Boolean DEFAULT_SMTP_AUTH = Boolean.FALSE;

    private static final Boolean DEFAULT_STARTTLS_ENABLE = Boolean.FALSE;
    private static final Boolean DEFAULT_STARTTLS_REQUIRED = Boolean.FALSE;

    public MailOptionsProperties {
        smtp = OptionalHelper.getOrCreate(smtp, () -> new SMTP(null, null));
    }

    public record SMTP(Boolean auth, StartTls startTls) {

        public SMTP {
            auth = OptionalHelper.getOrDefault(auth, DEFAULT_SMTP_AUTH);
            startTls = OptionalHelper.getOrCreate(startTls, () -> new StartTls(null, null));
        }

        public record StartTls(Boolean enable, Boolean required) {
            public StartTls {
                enable = OptionalHelper.getOrDefault(enable, DEFAULT_STARTTLS_ENABLE);
                required = OptionalHelper.getOrDefault(required, DEFAULT_STARTTLS_REQUIRED);
            }
        }

    }

}

