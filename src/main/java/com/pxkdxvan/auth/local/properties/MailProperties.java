package com.pxkdxvan.auth.local.properties;

import com.pxkdxvan.auth.local.exception.mail.MailUsernameNotFoundException;
import com.pxkdxvan.auth.local.exception.mail.MailPasswordNotFoundException;
import com.pxkdxvan.auth.shared.config.ConstantsConfig;
import com.pxkdxvan.auth.shared.utils.OptionalHelper;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mail")
public record MailProperties(String host, Integer port, String username, String password, String defaultEncoding, Properties props) {

    private static final String DEFAULT_HOST = "smtp.gmail.com";
    private static final Integer DEFAULT_PORT = 587;

    public MailProperties {
        host = OptionalHelper.getOrDefault(host, DEFAULT_HOST);
        port = OptionalHelper.getOrDefault(port, DEFAULT_PORT);
        username = OptionalHelper.getOrThrow(username, MailUsernameNotFoundException::new);
        password = OptionalHelper.getOrThrow(password, MailPasswordNotFoundException::new);
        defaultEncoding = OptionalHelper.getOrDefault(defaultEncoding, ConstantsConfig.DEFAULT_ENCODING);
        props = OptionalHelper.getOrCreate(props, () -> new Properties(null));
    }

    public record Properties(MailOptionsProperties mail) {
        public Properties {
            mail = OptionalHelper.getOrCreate(mail, () -> new MailOptionsProperties(null));
        }
    }

}
