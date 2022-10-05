# 왜 MongoDB Atlas Search를 선택했나요?

## 1. 장점

### 높은 SLA

AWS OpenSearch의 경우 [SLA가 99.9%](https://aws.amazon.com/ko/opensearch-service/faqs/) 이다.  
이를 [uptime](https://uptime.is/) 을 통해 실제 시간으로 변환해보면 다음과 같다.

* Daily: 1m 26s
* Weekly: 10m 4s
* Monthly: **43m 49s**
* Quarterly: 2h 11m 29s
* Yearly: **8h 45m 56s**

반면, MongoDB Atlas의 경우 [SLA가 99.995%](https://www.mongodb.com/ko-kr/cloud/atlas/reliability) 이다.  
이 역시 실제 시간으로 변환해보면 다음과 같다.

* Daily: 4s
* Weekly: 30s
* Monthly: 2m 11s
* Quarterly: 6m 34s
* Yearly: 26m 17s

**1년에 8시간 45분 장애날 확률**과 **1년에 26분 장애날 확률**은 생각보다 큰 차이다.  
특히 **운이 없으면 저 8시간 중 1시간이 한번에 몰릴 때도 있다**.  




## 2. 단점

### Search 단위 테스트 불가능

Search는 Atlas에서만 지원하는 기능이다보니 Docker 혹은 Embedded MongoDB를 통한 단위 테스트 코드 작성이 불가능하다.  
즉, **검색엔진 기능은 실제 Atlas 테스트 환경을 통해서 테스트를 수행**해야만 한다.  
