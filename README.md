
# Spring Cloud Gateway (SCG) - YML 설정 가능 값

Spring Cloud Gateway(SCG)에서 `application.yml` 파일에 설정할 수 있는 값은 매우 다양합니다. SCG는 Spring Boot 애플리케이션의 일부로 동작하며, Gateway 자체의 설정뿐만 아니라 Spring Boot 및 Spring Cloud와 관련된 다양한 설정을 yml 파일에서 정의할 수 있습니다.

## Spring Cloud Gateway 주요 설정 값

다음은 Spring Cloud Gateway의 `application.yml`에서 설정할 수 있는 주요 옵션들입니다.

### 1. 기본 설정

```yaml
spring:
  cloud:
    gateway:
      default-filters: # 모든 라우트에 공통으로 적용될 필터를 정의합니다.
        - name: AddResponseHeader
          args:
            name: X-Response-Default-Foo
            value: Default-Bar
      discovery:
        locator:
          enabled: true  # Service Discovery를 통해 자동으로 라우트를 추가할 수 있습니다.
      routes:  # 라우트 정의
        - id: my-route  # 라우트 ID
          uri: http://httpbin.org  # 목적지 URI
          predicates:  # 요청에 대한 조건을 정의합니다.
            - Path=/get
          filters:  # 라우트에 적용될 필터를 정의합니다.
            - AddRequestHeader=X-Request-Foo, Bar
            - AddResponseHeader=X-Response-Foo, Bar
```

### 2. 라우트 관련 설정

- **`id`**: 라우트의 고유 식별자.
- **`uri`**: 요청을 리디렉션할 대상 URI.
- **`order`**: 라우트의 우선순위.
- **`predicates`**: 요청 조건을 정의하는 프레디케이트. 여러 가지 유형이 있습니다:
  - **Path**: 경로에 대한 조건.
  - **Method**: HTTP 메소드(GET, POST 등)에 대한 조건.
  - **Header**: 요청 헤더에 대한 조건.
  - **Query**: 쿼리 파라미터에 대한 조건.
  - **Host**: 호스트 이름에 대한 조건.
  - **RemoteAddr**: 요청 클라이언트의 IP 주소에 대한 조건.
  - **Cookie**: 쿠키에 대한 조건.

### 3. predicates 관련 설정

요청을 특정 조건에 맞춰 필터링하고, 해당 조건을 만족하는 요청만 라우팅하는 데 사용됩니다.

- **Path**: 특정 URL 경로를 기준으로 라우팅합니다.
- **Host**: 요청의 호스트 헤더를 기준으로 라우팅합니다.
- **Method**: HTTP 메서드(GET, POST 등)를 기준으로 라우팅합니다.
- **Header**: 특정 HTTP 헤더를 기준으로 라우팅합니다.
- **Query**: 요청의 쿼리 파라미터를 기준으로 라우팅합니다.
- **Cookie**: 쿠키를 기준으로 라우팅합니다.
- **After**: 요청의 타임스탬프가 특정 시간 이후일 때 라우팅합니다.
- **Before**: 요청의 타임스탬프가 특정 시간 이전일 때 라우팅합니다.
- **Between**: 요청의 타임스탬프가 특정 시간 범위 내에 있을 때 라우팅합니다.
- **RemoteAddr**: 클라이언트의 IP 주소를 기준으로 라우팅합니다.
- **Weight**: 라우팅의 가중치를 설정하여 특정 라우트로 트래픽을 퍼센트 분산시킬 수 있습니다.
- **CloudFoundryRouteService**: Cloud Foundry에서 제공하는 서비스 경로에 대해 라우팅할 때 사용합니다.
- **ReadBodyPredicateFactory**: 요청 본문을 읽고 조건을 만족할 때 라우팅합니다.

### 4. 필터 관련 설정

필터는 요청/응답에 대해 다양한 처리를 할 수 있는 기능을 제공합니다.

- **AddRequestHeader**: 요청 헤더 추가.
- **AddResponseHeader**: 응답 헤더 추가.
- **RewritePath**: 경로 재작성.
- **RedirectTo**: 요청을 리디렉션.
- **SetPath**: 경로 변경.
- **PrefixPath**: 경로에 프리픽스 추가.
- **StripPrefix**: 경로에서 프리픽스 제거.
- **Retry**: 실패한 요청 재시도.
- **CircuitBreaker**: 서킷 브레이커 패턴 적용.
- **RateLimiter**: 요청 속도 제한.

### 5. 고급 설정

- **Global Filters**: 모든 라우트에 대해 공통적으로 적용될 필터를 정의.

```yaml
spring:
  cloud:
    gateway:
      default-filters:
        - DedupeResponseHeader=Access-Control-Allow-Origin,RETAIN_UNIQUE
```

- **Service Discovery**: 서비스 디스커버리와의 통합.

```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true  # DiscoveryClient를 통해 동적으로 라우트 설정
```

### 6. 설정 예시

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: first_route
          uri: http://example.org
          predicates:
            - Path=/foo/**
          filters:
            - RewritePath=/foo/(?<segment>.*), /$\{segment}
        - id: second_route
          uri: lb://my-service
          predicates:
            - Host=**.example.com
            - Header=X-Request-Id, \d+
          filters:
            - PrefixPath=/myservice
            - RemoveRequestHeader=X-Request-Foo
      globalcors:  # 전역 CORS 설정
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "http://allowed-origin.com"
            allowedMethods:
              - GET
              - POST
              - PUT
              - DELETE
              - PATCH
```
