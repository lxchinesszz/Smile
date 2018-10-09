import static ch.qos.logback.classic.Level.ERROR
import static ch.qos.logback.classic.Level.INFO
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.classic.encoder.PatternLayoutEncoder

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd'T'HH:mm:ss.SSS} %p [%thread] %logger{50} as:%L : %msg  %n"
    }
}
root(INFO, ["CONSOLE"])
