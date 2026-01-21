package com.romantic_messenger.romanticmessenger.claude.exception;

import com.anthropic.errors.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.polly.model.PollyException;
import software.amazon.awssdk.services.s3.model.S3Exception;
import com.twilio.exception.ApiException;
import com.twilio.exception.TwilioException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Validation failed: {}", errors);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                errors,
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            UnauthorizedException ex,
            HttpServletRequest request) {

        log.error("Authentication failed: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.UNAUTHORIZED.value(),
                "Authentication Failed",
                "Invalid API key. Please check your Claude API credentials.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitException(
            RateLimitException ex,
            HttpServletRequest request) {

        log.warn("Rate limit exceeded: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Rate Limit Exceeded",
                "You have exceeded the rate limit or your account is out of tokens. Please try again later.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            BadRequestException ex,
            HttpServletRequest request) {

        log.error("Bad request to Claude API: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Request",
                "Invalid request to Claude API: " + ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(
            InternalServerException ex,
            HttpServletRequest request) {

        log.error("Claude API server error: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Service Unavailable",
                "Claude API is temporarily unavailable. Please try again later.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    @ExceptionHandler(AnthropicException.class)
    public ResponseEntity<ErrorResponse> handleAnthropicException(
            AnthropicException ex,
            HttpServletRequest request) {

        log.error("Anthropic API error: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_GATEWAY.value(),
                "Claude API Error",
                "Failed to generate message: " + ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse);
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeoutException(
            SocketTimeoutException ex,
            HttpServletRequest request) {

        log.error("Request timeout: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.REQUEST_TIMEOUT.value(),
                "Request Timeout",
                "The request to Claude API timed out. Please try again.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(errorResponse);
    }

    @ExceptionHandler(AnthropicIoException.class)
    public ResponseEntity<ErrorResponse> handleAnthropicIoException(
            AnthropicIoException ex,
            HttpServletRequest request) {

        log.error("Network I/O error: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Network Error",
                "Network communication error occurred. Please check your connection and try again.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    @ExceptionHandler(SdkClientException.class)
    public ResponseEntity<ErrorResponse> handleAwsSdkClientException(
            SdkClientException ex,
            HttpServletRequest request) {

        log.error("AWS SDK client error: {}", ex.getMessage(), ex);

        String message = ex.getMessage();
        if (message.contains("Unable to load credentials")) {
            message = "AWS credentials are not configured properly. Please check your AWS configuration.";
        }

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "AWS Service Error",
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    @ExceptionHandler(PollyException.class)
    public ResponseEntity<ErrorResponse> handlePollyException(
            PollyException ex,
            HttpServletRequest request) {

        log.error("Amazon Polly error: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_GATEWAY.value(),
                "Text-to-Speech Service Error",
                "Failed to convert text to speech: " + ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse);
    }

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ErrorResponse> handleS3Exception(
            S3Exception ex,
            HttpServletRequest request) {

        log.error("S3 error: {}", ex.getMessage(), ex);

        String message;
        if (ex.statusCode() == 301) {
            message = "S3 bucket region mismatch. Please check your S3 bucket region configuration.";
        } else if (ex.statusCode() == 403) {
            message = "Access denied to S3 bucket. Please check your AWS credentials and bucket permissions.";
        } else if (ex.statusCode() == 404) {
            message = "S3 bucket not found. Please check your bucket name configuration.";
        } else {
            message = "Failed to upload file to S3: " + ex.getMessage();
        }

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_GATEWAY.value(),
                "S3 Storage Error",
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleTwilioApiException(
            ApiException ex,
            HttpServletRequest request) {
        log.error("Twilio API error: {}", ex.getMessage(), ex);

        String message;
        if (ex.getMessage().contains("unverified")) {
            message = "The recipient phone number is unverified. ";
        } else if (ex.getMessage().contains("Permission to send") && ex.getMessage().contains("region")) {
            message = "SMS permissions are not enabled for this region. .";
        } else if (ex.getMessage().contains("not a valid phone number")) {
            message = "Invalid phone number format. Please use E.164 format (e.g., +1234567890).";
        } else if (ex.getMessage().contains("authentication")) {
            message = "Twilio authentication failed. Please check your credentials.";
        } else if (ex.getMessage().contains("insufficient funds")) {
            message = "Twilio account has insufficient funds.";
        } else {
            message = "Failed to send SMS/MMS: " + ex.getMessage();
        }

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_GATEWAY.value(),
                "SMS Service Error",
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse);
    }

    @ExceptionHandler(TwilioException.class)
    public ResponseEntity<ErrorResponse> handleTwilioException(
            TwilioException ex,
            HttpServletRequest request) {

        log.error("Twilio error: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_GATEWAY.value(),
                "SMS Service Error",
                "Twilio service error: " + ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(
            IOException ex,
            HttpServletRequest request) {

        log.error("I/O error: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "I/O Error",
                "An error occurred while processing audio data.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
