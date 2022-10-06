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
특히 **운이 없으면 8시간 중 1시간이 한번에 몰릴 때도 있다**.  


### 형태소 분석기

한글 처리를 위해서 Atlas Search 에서 제공하는 Analyzer 의 종류를 살펴보고 각 Analyzer
특징에 대해서 설명합니다.

#### 1.1 lucene.cjk

lucene.cjk Analyzer 는 Chinese Japanese Korean 의 약자로 BiGram 형태로 Token 을
분리합니다. 예를 들면 다음과 같습니다.

* 한글은 아름다운 언어입니다.
* [한글] [글은] [아름] [름다] [다운] [언어] [어입] [입니] [니다]

결과를 보면 2byte 단위로 문자열을 토큰화 시켜 나타낸 것을 알 수 있습니다.
다음과 같이 영어나 숫자와 같이 multibyte 가 아닌 문자열에 대해서는 BiGram 을
생성하지 않습니다.

* 한글은 very 아름다운 언어입니다.
* [한글] [글은] [very] [아름] [름다] [다운] [언어] [어입] [입니] [니다]

실제 BiGram 을 생성하는 부분은 한글에 대한 부분만 처리 되는 것을 확인하여
주십시오.
cjk 는 다음과 같은 불용어 리스트를 가지고 있습니다.

> a and are as at be but by for if in into is it no not of on or s such t that the their
> then there these they this to was will with www

이러한 불용어는 문자열 토큰에서 아예 제거 되며 그렇기 떄문에 인덱스 엔트리에도
포함되지 않습니다. 다음예는 불용어인 this 가 빠진 결과를 보여줍니다.

* this 포인터에 대한 역참조문제
* [포인] [인터] [터에] [대한] [역참] [참조] [문제]

주의하여야할 것은 띄어쓰기 없이 alphanum 가 붙게 되면 해당 토큰 역시 BiGram 을
생성하지 못한다는 것입니다.

Copyright 2022 MongoDB, Inc. 3 of 11
CONFIDENTIAL

* a번문제는 어떻게 될까요
* [a번문제는] [어떻] [떻게] [될까] [까요]

위에서 예를 든것과 같이 이 경우 Bigram 을 생성하지 못하기 때문에 [a번문제] 와 같은
문자열로 검색하면 검색 결과가 나오지 못합니다. 이런식으로 활용도가 상당히
떨어지기 때문에 cjk 를 사용하는 것은 권장하지 않습니다.

#### lucene.nori

위에서 설명한 cjk 가 단순히 토큰을 문자단위로 쪼개어 BIGRAM 형태로 나타낸
것이라면 nori analyzer 는 형태소 분석을 통한 의미를 가진 문자열로 분리하는
방식입니다. 예를 들면 다음과 같습니다.

* 한글은 아름다운 언어입니다.
* [한글] [아름답] [언어] [이]

위와 같이 형태소 분석을 통해서 품사를 분류하고, 조사, 관형사와 같이 불필요한
문자열 토큰은 삭제합니다. 형태소 단위로 토큰을 나누는 과정에서 품사를 찾을 수
있기 때문에 cjk 와 같은 불용어 리스트는 별도로 존재하지 않습니다.
영어와 같이 쓰이는 경우, 해당 단어는 띄어쓰기와 상관없이 정확하게 token 을 생성
합니다. 띄어쓰기가 안되었을때 BiGram 을 생성하지 못하는 cjk 와 비교하여 주십시오.

* 한글은 very 아름다운 언어입니다.
* [한글] [very] [아름답] [언어] [이]

* 한글은 very아름다운 언어입니다.
* [한글] [very] [아름답] [언어] [이]

nori analyzer 를 사용하는 경우 Search analyzer 또한 nori analyzer 가 되어야 합니다.
그래야 동일한 형태소 분석을 통해 매칭되는 토큰을 찾아낼 수 있습니다. “아름다운
언어" 를 검색어로 넣었을때 해당 검색어는 다음과 같이 Tokenize 됩니다.

* 아름다운 언어
* [아름답] [언어]

“아름답", “언어" 가 매칭이 되기 때문에 “한글은 아름다운 언어입니다" 를 검색 할 수
있습니다.
신조어와 같은 경우 형태소 분석이 정확하게 이루어지지 않을 수 있습니다. 예를 들어

* 이더리움 채굴 열풍
* [더리] [채굴] [열풍]

이 경우 “이더리움" 을 형태소 분석해서 정확한 토큰으로 뽑아내지는 못하지만 Search
Analyzer 에서도 동일하게 Tokenize 되기 때문에 검색은 이루어집니다. 다만 검색 품질
( score ) 쪽에서 이슈가 생길 수 있습니다.

#### 1.3 lucene.korean

lucene.korean Analyzer 는 cjk analyzer 의 또 다른 이름입니다.


## 2. 단점

### Search 단위 테스트 불가능

Search는 Atlas에서만 지원하는 기능이다보니 Docker 혹은 Embedded MongoDB를 통한 단위 테스트 코드 작성이 불가능하다.  
즉, **검색엔진 기능은 실제 Atlas 테스트 환경을 통해서 테스트를 수행**해야만 한다.  
