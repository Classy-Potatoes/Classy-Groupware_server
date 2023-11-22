//package com.potatoes.cg.common.exception;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import static org.springframework.http.HttpStatus.CONFLICT;
//import static org.springframework.http.HttpStatus.NOT_FOUND;
//
//
//// RestControllerAdvice -> Exception 처리를 이런식으로 하겠다!
//@RestControllerAdvice
//public class ExceptionHandlingController {
//
//    /* 1xx 그룹은 클라이언트의 요청이 처리되고 있음을 나타낸다. */
//    /* 2xx 그룹은 클라이언트의 요청이 성공적으로 처리되었음을 나타낸다. */
//    /* 3xx 그룹은 클라이언트가 다른 위치로 이동해야 함을 나타낸다 */
//
//    /* 4xx 그룹은 클라이언트의 요청에 오류가 있음을 나타낸다. */
//    /* 400 : Bad Request */
//    @ExceptionHandler(BadRequestException.class)
//    public ResponseEntity<ExceptionResponse> badRequestException( BadRequestException e ) {
//
//        final ExceptionResponse exceptionResponse
//                = ExceptionResponse.of( e.getCode(), e.getMessage() );
//
//        return ResponseEntity.badRequest().body( exceptionResponse );
//    }
//
//    /* 401 : UnAuthorized 인증 오류 => 이미 처리 되어 있음 */
//
//    /* 403 : Forbidden 인가 오류 => 이미 처리 되어 있음 */
//
//    /* 404 : Not Found */
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<ExceptionResponse> notFoundException( NotFoundException e ) {
//
//        final ExceptionResponse exceptionResponse
//                = ExceptionResponse.of( e.getCode(), e.getMessage() );
//
//        return ResponseEntity.status( NOT_FOUND ).body( exceptionResponse );
//    }
//
//
//    /* 409 :  (Conflict) : 요청이 서버의 현재 상태와 충돌함을 나타낸다.
//    *       => 충돌, 논리적으로 해당 기능을 수정할 수 없는 경우 처리 */
//    @ExceptionHandler(ConflictException.class)
//    public ResponseEntity<ExceptionResponse> conflictException( ConflictException e ) {
//
//        final ExceptionResponse exceptionResponse
//                = ExceptionResponse.of( e.getCode(), e.getMessage() );
//
//        return ResponseEntity.status( CONFLICT ).body( exceptionResponse );
//    }
//
//
//    /* 5xx 그룹은 서버에서 오류가 발생했음을 나타낸다. */
//    /* 500 : Internal Server Error, 서버 내부 오류로 인해 요청을 처리할 수 없음을 나타낸다. */
//    @ExceptionHandler(ServerInternalException.class)
//    public ResponseEntity<ExceptionResponse> serverInternalException( ServerInternalException e ) {
//
//        final ExceptionResponse exceptionResponse
//                = ExceptionResponse.of( e.getCode(), e.getMessage() );
//
//        return ResponseEntity.internalServerError().body( exceptionResponse );
//    }
//
//
//    /* Validation Exception
//    * 어노테이션 Vaild를 했을 경우 디폴트가 올바르지 않게 했을경우 에러처리
//    * */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ExceptionResponse> methodValidException(MethodArgumentNotValidException e) {
//
//        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
//
//        final ExceptionResponse exceptionResponse
//                = ExceptionResponse.of(9000, defaultMessage);
//
//        // 400번 코드로 응답
//        return ResponseEntity.badRequest().body( exceptionResponse );
//    }
//
//
//}
