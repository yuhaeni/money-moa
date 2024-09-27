package com.common.exception.customs;

/* 참고용 java 파일
import static java.util.stream.Collectors.toList;

import co.kr.deleo.common.client.KafkaSender;
import co.kr.deleo.common.code.LogType;
import co.kr.deleo.common.dto.common.ErrorField;
import co.kr.deleo.common.dto.common.ResponseDto;
import co.kr.deleo.common.dto.common.SystemErrorLogDto;
import co.kr.deleo.common.service.notify.NotifyMessage;
import co.kr.deleo.common.utils.ApplicationContextUtil;
import co.kr.deleo.common.utils.MessageUtil;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Path.Node;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;*/
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/*@Slf4j
@ControllerAdvice*/
public class CommonExceptionHandler_org extends ResponseEntityExceptionHandler {
/*
  private final ApplicationContext context;
  private final Optional<KafkaSender> kafkaSender;
  private final Tracer tracer;
  private final Optional<NotifyMessage> notifyMessage;
  private final Optional<MessageUtil> messageUtil;

  @Autowired
  public CommonExceptionHandler(
      ApplicationContext context,
      Optional<KafkaSender> kafkaSender,
      Tracer tracer,
      Optional<NotifyMessage> notifyMessage,
      Optional<MessageUtil> messageUtil) {
    this.context = context;
    this.kafkaSender = kafkaSender;
    this.tracer = tracer;
    this.notifyMessage = notifyMessage;
    this.messageUtil = messageUtil;
  }

  @ExceptionHandler
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ResponseDto<Object> exception(Exception e) {
    log.error(e.getMessage(), e);

    Map<String, Object> tracerMap = new HashMap<>();
    try {
      tracerMap.put("traceId", tracer.currentSpan().context().traceId());
      tracerMap.put("spanId", tracer.currentSpan().context().spanId());
    } catch (Exception te) {
      log.error(te.getMessage(), te);
    }

    notifyMessage.ifPresent(service -> service.sendErrorMessage(e, tracerMap));

    sendErrorMessage(e);

    return new ResponseDto<>(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        null);
  }

  @ExceptionHandler
  @ResponseStatus(value = HttpStatus.FORBIDDEN)
  public void accessDeniedException(AccessDeniedException e) {
    log.info(e.getMessage(), e);
  }

  @ExceptionHandler
  @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
  @ResponseBody
  public ResponseDto<Object> bizException(BizException e) {
    int code = e.getCode() == 0 ? HttpStatus.UNPROCESSABLE_ENTITY.value() : e.getCode();
    return new ResponseDto<>(code, null, "Invalid Request", getMessage(e.getMessage(), e.getData()));
  }

  @ExceptionHandler
  @ResponseStatus(value = HttpStatus.LOCKED)
  @ResponseBody
  public ResponseDto<Object> processLockException(ProcessLockException e) {
    return new ResponseDto<>(e.getCode(), e.getMessage(), "Invalid Request", getMessage(e.getMessage(), e.getKey()));
  }

  @ExceptionHandler
  @ResponseBody
  public ResponseEntity<?> dataIntegrityViolationException(DataIntegrityViolationException e) {
    Throwable throwable = e.getRootCause();

    if (throwable.getClass().getName().equals("org.postgresql.util.PSQLException") &&
        ((SQLException) throwable).getSQLState().equals("23505")) { // postgresql Duplicate key value violates unique constraint
      ResponseDto<Object> responseDto = bizException(new DuplicationException());

      return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    } else {
      exception(e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @ExceptionHandler(SessionExpireException.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public ResponseDto<?> sessionExpireException(SessionExpireException e) {
    return new ResponseDto<>(HttpStatus.UNAUTHORIZED.value(), null, getMessage(e.getMessage()));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validException(ex));
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    return handleExceptionInternal(ex, validException(ex), headers, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    return handleExceptionInternal(ex, validException(ex), headers, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    return handleExceptionInternal(ex, validException(ex), headers, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    return handleExceptionInternal(ex, validException(ex), headers, HttpStatus.BAD_REQUEST, request);
  }

  private ResponseDto<List<ErrorField>> validException(Exception e) {
    Function<List<FieldError>, List<ErrorField>> replaceError =
        errorList ->
            errorList.stream()
                .map(
                    error -> {
                      String defaultMessage = error.getDefaultMessage();
                      String field = error.getField();
                      if (messageUtil.isPresent()) {
                        defaultMessage = messageUtil.get().getMessage(defaultMessage);
                        if (StringUtils.isNotBlank(field)) {
                          field = messageUtil.get().getFieldWord(field);
                        }
                      }
                      return new ErrorField(field, defaultMessage);
                    })
                .collect(toList());

    List<FieldError> errors = null;

    if (e instanceof MethodArgumentNotValidException) {
      errors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
    } else if (e instanceof ConstraintViolationException) {
      errors = ((ConstraintViolationException) e).getConstraintViolations()
          .stream()
          .map(constraintViolation -> {
            Stream<Node> stream = StreamSupport.stream(constraintViolation.getPropertyPath().spliterator(), false);
            List<Path.Node> list = stream.collect(Collectors.toList());
            Node lastNode = list.stream().filter(node -> StringUtils.isNotBlank(node.getName()))
                .reduce((first, second) -> second).get();
            return new FieldError(
                constraintViolation.getRootBeanClass().getName(),
                lastNode.getName(),
                constraintViolation.getMessage());
          })
          .collect(Collectors.toList());
    } else if (e instanceof BindException) {
      errors = ((BindException) e).getFieldErrors();
    } else if (e instanceof HttpMessageNotReadableException) {
      if (e.getCause() instanceof JsonMappingException) {
        JsonMappingException je = (JsonMappingException) e.getCause();
        errors = List.of(
            new FieldError(
                "",
                je.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.joining(".")),
                je.getOriginalMessage()
            )
        );
      }
    } else if (e instanceof MissingServletRequestParameterException) {
      String parameter = ((MissingServletRequestParameterException) e).getParameterName();
      errors = List.of(
          new FieldError("", parameter, "Parameter " + parameter + " is missing")
      );
    }

    return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), null, "Invalid Request", replaceError.apply(errors));
  }

  @ExceptionHandler
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  private ResponseDto<List<ErrorField>> carrierValidException(CarrierValidException e) {
    List<ErrorField> errorFieldList = e.getErrorFieldList();
    errorFieldList.forEach(errorField -> errorField.setField(getFieldWord(errorField.getField())));
    return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), null, "Invalid Request", errorFieldList);
  }

  private String getFieldWord(String code) {
    if (messageUtil.isPresent()) {
      MessageUtil messageUtil = this.messageUtil.get();
      return messageUtil.getFieldWord(code);
    }

    return code;
  }

  private String getMessage(String code, Object... data) {
    if (messageUtil.isPresent()) {
      MessageUtil messageUtil = this.messageUtil.get();

      if (data != null) {
        return messageUtil.getMessage(
            code,
            Arrays.stream(data)
                .map(d -> {
                  if (d instanceof String) {
                    return messageUtil.getFieldWord((String) d);
                  }
                  return d;
                })
                .toArray());
      }

      return messageUtil.getMessage(code);
    }
    return code;
  }

  private void sendErrorMessage(Exception exception) {
    if (ApplicationContextUtil.isNotDefaultProfile()) {
      kafkaSender.ifPresent(
          kafkaSender -> {
            try {
              kafkaSender.systemError(
                  SystemErrorLogDto.builder()
                      .logType(LogType.SYSTEM)
                      .traceId(Objects.requireNonNull(tracer.currentSpan()).context().traceId())
                      .spanId(Objects.requireNonNull(tracer.currentSpan()).context().spanId())
                      .service(context.getId())
                      .createdOn(LocalDateTime.now())
                      .message(
                          String.join(
                              "\n",
                              exception.getMessage(),
                              Arrays.stream(exception.getStackTrace())
                                  .map(StackTraceElement::toString)
                                  .collect(Collectors.joining("\n"))
                          )
                      )
                      .build());
            } catch (Exception e) {
              log.error("CommonExceptionHandler sendErrorMessage Error: {}", e.getMessage());
              notifyMessage.ifPresent(notifyMessage -> notifyMessage.sendErrorMessage(e));
            }
          }
      );
    }
  }*/
}
