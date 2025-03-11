package com.pxkdxvan.auth.shared.logger;

import com.pxkdxvan.auth.shared.config.ConstantsConfig;
import com.pxkdxvan.auth.shared.utils.ColorOutput;

import org.fusesource.jansi.Ansi;
import org.slf4j.MDC;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class LoggerBuilder {

    private static final Ansi.Color DEFAULT_PREFIX_COLOR = Ansi.Color.MAGENTA;
    private static final Ansi.Color WARNING_COLOR = Ansi.Color.YELLOW;
    private static final Ansi.Color ERROR_COLOR = Ansi.Color.RED;
    private static final Ansi.Color CLEANER_COLOR = Ansi.Color.CYAN;

    private static final Ansi.Color STATUS_STARTED_COLOR = Ansi.Color.CYAN;
    private static final Ansi.Color STATUS_ONGOING_COLOR = Ansi.Color.BLUE;
    private static final Ansi.Color STATUS_THROWN_COLOR = Ansi.Color.RED;
    private static final Ansi.Color STATUS_COMPLITED_COLOR = Ansi.Color.GREEN;

    private static final String SEGMENT_DELIMITER = " | ";
    private static final String HEADER_DELIMITER = " ";
    private static final String BODY_DELIMITER = " / ";
    private static final String ENTRY_DELIMITER = " -> ";

    private static final String PREFIX_TEMPLATE = "[%s]";

    private static final String ID_KEY = "ID";
    private static final String CLASS_KEY = "Class";
    private static final String METHOD_KEY = "Method";
    private static final String CAUSE_KEY = "Cause";
    private static final String MESSAGE_KEY = "Message";
    private static final String EXECUTION_KEY = "Execution";

    public static HeaderStep builder() {
        return new Builder();
    }

    public enum Prefix {
        REQUEST,
        RESPONSE,
        CONTROLLER,
        SERVICE,
        BUILDER,
        ADAPTER,
        PROVIDER,
        CLEANER,
        WARNING,
        ERROR;

        @Override
        public String toString() {
            return ColorOutput.colorize(PREFIX_TEMPLATE.formatted(name()), switch (this) {
                case WARNING -> WARNING_COLOR;
                case ERROR -> ERROR_COLOR;
                case CLEANER -> CLEANER_COLOR;
                default -> DEFAULT_PREFIX_COLOR;
            });
        }
    }

    private enum Status {
        STARTED,
        ONGOING,
        THROWN,
        COMPLETED;

        @Override
        public String toString() {
            return ColorOutput.colorize(name(), switch (this) {
                case STARTED -> STATUS_STARTED_COLOR;
                case ONGOING -> STATUS_ONGOING_COLOR;
                case THROWN -> STATUS_THROWN_COLOR;
                case COMPLETED -> STATUS_COMPLITED_COLOR;
            });
        }
    }

    public interface HeaderStep {
        PrefixedHeaderStep withPrefix(Prefix prefix);
    }

    public interface PrefixedHeaderStep {
        PrefixedHeaderStep withItem(Object item);
        PrefixedHeaderStep withItems(Collection<Object> items);
        StartingBodyStep asStarted();
        BodyStep asOngoing();
        ThrowingBodyStep asThrown();
        CompletingBodyStep asCompleted();
    }

    public interface BodyStep {
        <T extends BodyStep> T withEntry(String key, Object value);
        <T extends BodyStep> T withEntries(Map<String, Object> entries);
        <T extends BodyStep> T withId();
        <T extends BodyStep> T withCaption(String caption);
        <T extends BodyStep> T merge(T other);
        String build();
    }

    public interface StartingBodyStep extends BodyStep {
        StartingBodyStep withHandler(String className, String methodName);
    }

    public interface ThrowingBodyStep extends BodyStep {
        ThrowingBodyStep withException(String cause, String message);
    }

    public interface CompletingBodyStep extends BodyStep {
        CompletingBodyStep withExecution(Long execution);
    }

    private static class Builder implements HeaderStep, PrefixedHeaderStep,
            BodyStep, StartingBodyStep, ThrowingBodyStep, CompletingBodyStep {

        private final Set<Object> headerItems = new LinkedHashSet<>();
        private final Map<String, Object> bodyEntries = new LinkedHashMap<>();

        @SuppressWarnings("unchecked")
        private <T extends BodyStep> T self() {
            return (T) this;
        }

        @Override
        public PrefixedHeaderStep withPrefix(Prefix prefix) {
            return withItem(prefix);
        }

        @Override
        public PrefixedHeaderStep withItem(Object item) {
            headerItems.add(item);
            return this;
        }

        @Override
        public PrefixedHeaderStep withItems(Collection<Object> items) {
            if (items != null) headerItems.addAll(items);
            return this;
        }

        @Override
        public StartingBodyStep asStarted() {
            withItem(Status.STARTED);
            return this;
        }

        @Override
        public BodyStep asOngoing() {
            withItem(Status.ONGOING);
            return this;
        }

        @Override
        public ThrowingBodyStep asThrown() {
            withItem(Status.THROWN);
            return this;
        }

        @Override
        public CompletingBodyStep asCompleted() {
            withItem(Status.COMPLETED);
            return this;
        }

        @Override
        public <T extends BodyStep> T withEntry(String key, Object value) {
            bodyEntries.put(key, value);
            return self();
        }

        @Override
        public <T extends BodyStep> T withEntries(Map<String, Object> entries) {
            if (entries != null) bodyEntries.putAll(entries);
            return self();
        }

        @Override
        public <T extends BodyStep> T withId() {
            return withEntry(ID_KEY, MDC.get(ConstantsConfig.CORRELATION_HEADER));
        }

        @Override
        public <T extends BodyStep> T withCaption(String caption) {
            return withEntry(MESSAGE_KEY, caption);
        }

        @Override
        public <T extends BodyStep> T merge(T other) {
            Builder otherBuilder = (Builder) other;
            withItems(otherBuilder.headerItems);
            withEntries(otherBuilder.bodyEntries);
            return self();
        }

        @Override
        public String build() {
            String header = Stream
                    .of(headerItems
                                .stream()
                                .filter(item -> item instanceof Prefix),
                        headerItems
                                .stream()
                                .filter(item -> item instanceof Status),
                        headerItems
                                .stream()
                                .filter(item -> !(item instanceof Prefix || item instanceof Status)))
                    .flatMap(Function.identity())
                    .map(Object::toString)
                    .collect(Collectors.joining(HEADER_DELIMITER));

            String body = Stream
                    .concat(bodyEntries
                                    .entrySet()
                                    .stream()
                                    .filter(entry -> entry.getKey().equals(ID_KEY)),
                            bodyEntries
                                    .entrySet()
                                    .stream()
                                    .filter(entry -> !entry.getKey().equals(ID_KEY)))
                    .map(entry -> entry.getKey() + ENTRY_DELIMITER + entry.getValue())
                    .collect(Collectors.joining(BODY_DELIMITER));

            return header + SEGMENT_DELIMITER + body;
        }

        @Override
        public StartingBodyStep withHandler(String className, String methodName) {
            return this
                    .withEntry(CLASS_KEY, className)
                    .withEntry(METHOD_KEY, methodName);
        }

        @Override
        public ThrowingBodyStep withException(String cause, String message) {
            return this
                    .withEntry(CAUSE_KEY, cause)
                    .withEntry(MESSAGE_KEY, message);
        }

        @Override
        public CompletingBodyStep withExecution(Long execution) {
            return withEntry(EXECUTION_KEY, execution);
        }

    }

}