package cz.gregetom.graphwiki.commons.web

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.persistence.EntityNotFoundException
import javax.validation.ConstraintViolationException
import javax.xml.bind.ValidationException

/**
 * Global exception handler
 */
@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ValidationException::class)
    fun exceptionHandler(e: ValidationException): ResponseEntity<Any> {
        LOGGER.info("Handle exception: ${e.message}")
        return ResponseEntity.badRequest().body(e.message)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun exceptionHandler(e: ConstraintViolationException): ResponseEntity<Any> {
        LOGGER.info("Handle exception: ${e.message}")
        return ResponseEntity.badRequest().body(e.message)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun exceptionHandler(e: EntityNotFoundException): ResponseEntity<Any> {
        LOGGER.info("Handle exception: ${e.message}")
        return ResponseEntity.notFound().build()
    }

    private companion object {
        private val LOGGER = LoggerFactory.getLogger(ExceptionHandler::class.java)
    }
}
