# API 설계

검증 실패와 할 일이 없을 때의 오류 응답에는 `code`와 `message`가 포함됩니다. 프론트엔드는 `code`로 오류 처리 동작을 정하고 `message`를 화면에 표시할 수 있습니다.

### API명: 1. 할 일 등록
- HTTP 메서드와 경로: `POST /todos`
- 요청 파라미터 / 본문: 
  - 요청 본문: title(필수), description(선택)
  ```json
  {
    "title": "자바 공부",
    "description": "컬렉션 복습하기"
  }
  ```
- 성공 상태 코드와 응답: 
  - 201
    - 생성된 할 일 반환:
    ```json
    {
      "id": 1,
      "title": "자바 공부",
      "description": "컬렉션 복습하기",
      "completed": false
    }
    ```
- 실패 상태 코드와 응답:
  - 400: 제목을 생략했거나 `null`, 빈 문자열 또는 공백만 입력한 경우
    - 오류 메시지 반환:
    ```json
    {
      "code": "INVALID_REQUEST",
      "message": "제목은 필수입니다."
    }
    ```
  - 400: 제목이 30자를 초과한 경우
    - 오류 메시지 반환:
    ```json
    {
      "code": "INVALID_REQUEST",
      "message": "제목은 1~30자로 입력해야 합니다."
    }
    ```
  - 400: 설명이 1000자를 초과한 경우
    - 오류 메시지 반환:
    ```json
    {
      "code": "INVALID_REQUEST",
      "message": "설명은 최대 1000자까지 입력할 수 있습니다"
    }
    ```

### API명: 2. 할 일 목록 전체 조회
- HTTP 메서드와 경로: `GET /todos`
- 요청 파라미터 / 본문: X
- 성공 상태 코드와 응답:
  - 200
    - 전체 할 일 목록을 ID 오름차순으로 반환:
    ```json
    [
      {
        "id": 1,
        "title": "자바 공부",
        "description": "컬렉션 복습하기",
        "completed": false
      },
      {
        "id": 2,
        "title": "운동",
        "description": "",
        "completed": true
      }
    ]
    ```
- 실패 상태 코드와 응답: X

### API명: 3. 할 일 개별 조회
- HTTP 메서드와 경로: `GET /todos/{id}`
- 요청 파라미터 / 본문:
  - 경로 파라미터: id(필수)
- 성공 상태 코드와 응답:
  - 200
    - 해당 ID의 할 일 반환:
    ```json
    {
      "id": 1,
      "title": "자바 공부",
      "description": "컬렉션 복습하기",
      "completed": false
    }
    ```
- 실패 상태 코드와 응답:
  - 404: 해당 ID가 없는 경우
    - 오류 메시지 반환:
    ```json
    {
      "code": "TODO_NOT_FOUND",
      "message": "할 일을 찾을 수 없습니다."
    }
    ```

### API명: 4. 할 일 수정
- HTTP 메서드와 경로: `PUT /todos/{id}`
- 요청 파라미터 / 본문:
  - 경로 파라미터: id(필수)
  - 요청 본문: title(필수), description(선택)
  ```json
  {
    "title": "하네스 엔지니어링 공부",
    "description": "Codex 공식 문서 정리하기"
  }
  ```
- 성공 상태 코드와 응답:
  - 200
    - 해당 ID의 할 일 반환:
    ```json
    {
      "id": 1,
      "title": "하네스 엔지니어링 공부",
      "description": "Codex 공식 문서 정리하기",
      "completed": false
    }
    ```
- 실패 상태 코드와 응답:
  - 404: 해당 ID가 없는 경우
    - 오류 메시지 반환:
    ```json
    {
      "code": "TODO_NOT_FOUND",
      "message": "할 일을 찾을 수 없습니다."
    }
    ```
  - 400: 제목을 생략했거나 `null`, 빈 문자열 또는 공백만 입력한 경우
    - 오류 메시지 반환:
    ```json
    {
      "code": "INVALID_REQUEST",
      "message": "제목은 필수입니다."
    }
    ```
  - 400: 제목이 30자를 초과한 경우
    - 오류 메시지 반환:
    ```json
    {
      "code": "INVALID_REQUEST",
      "message": "제목은 1~30자로 입력해야 합니다."
    }
    ```
  - 400: 설명이 1000자를 초과한 경우
    - 오류 메시지 반환:
    ```json
    {
      "code": "INVALID_REQUEST",
      "message": "설명은 최대 1000자까지 입력할 수 있습니다"
    }
    ```

### API명: 5. 할 일 완료 상태 변경
- HTTP 메서드와 경로: `POST /todos/{id}/completed`
- 요청 파라미터 / 본문:
  - 경로 파라미터: id(필수)
- 성공 상태 코드와 응답:
  - 200
    - 해당 ID의 할 일 반환:
    ```json
    {
      "id": 1,
      "title": "하네스 엔지니어링 공부",
      "description": "Codex 공식 문서 정리하기",
      "completed": true
    }
    ```
- 실패 상태 코드와 응답:
  - 404: 해당 ID가 없는 경우
    - 오류 메시지 반환:
    ```json
    {
      "code": "TODO_NOT_FOUND",
      "message": "할 일을 찾을 수 없습니다."
    }
    ```

### API명: 6. 할 일 삭제
- HTTP 메서드와 경로: `DELETE /todos/{id}`
- 요청 파라미터 / 본문:
  - 경로 파라미터: id(필수)
- 성공 상태 코드와 응답:
  - 200
    - 삭제 성공 메시지 반환:
    ```json
    {
      "message": "할 일이 삭제되었습니다."
    }
    ```
- 실패 상태 코드와 응답:
  - 404: 해당 ID가 없는 경우
    - 오류 메시지 반환:
    ```json
    {
      "code": "TODO_NOT_FOUND",
      "message": "할 일을 찾을 수 없습니다."
    }
    ```


