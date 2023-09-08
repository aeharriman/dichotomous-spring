

### How the Filters work in Spring Security
```mermaid
graph TD

subgraph SAC[Spring Application Context / Beans]
    subgraph CSFC[Custom <b><u>SecurityFilterChain</b></u>: Order that request/response passes through is determined at runtime by spring ]
        BTAF[<u><b>BearerTokenAuthenticationFilter</u></b></br>Decodes JWT]
        subgraph CF[<u><b>CorsFilter</u></b></br>Handles</br>OPTIONS requests,</br>sets outgoing</br>headers on others]
            
        end
        subgraph FSI[<u><b>FilterSecurityInterceptor</u></b></br>Does access control checks]

        end
    end

    SMS[SecurityMetadataSource]
    subgraph SCX[SecurityContext]
    AAO[Authentication]
    end
    EP[Endpoint]
    FCP[FilterChainProxy</br>A bean that implements Filter</br>]
end
subgraph SC[Servlet container, running on a port]
    AOSF[Pre-DFP servlet filters]
    DFP[<u>DelegatingFilterProxy</u></br>Bridges servlet container's lifecycle<br>to spring's ApplicationContext]
    OF[Post-DFP servlet filters]
end

HT[HTTP Request]


DFP <--> |Delegates to|FCP
HT --> |Enters through port|AOSF
AOSF -->DFP
FCP --> |selects instance</br>of SecurityFilterChain based on endpoint coverage|CSFC
CSFC <--> EP
BTAF --> |establishes|AAO
FSI --> |references|AAO
FSI --> |references|SMS
DFP -.-> OF
OF -.-> HTTPResponse
```


### How the Factory methods in SecurityConfig set up the spring security structure
```mermaid
graph TD

subgraph SAC[Spring Application Context / Beans]

        
    subgraph SecurityFilterChain
    BTAF[BearerTokenAuthenticationFilter]
    CF[CorsFilter]
    end
        
        AM[AuthenticationManager]
        JWP[JwtAuthenticationProvider]
        JD[JwtDecoder]


    CCCS[CorsConfigurationSource]
    
end

subgraph SFCF[securityFilterChain factory method with HttpSecurity builder]
    .cors
    .o2[.oauth2ResourceServer]
end

subgraph CCS[corsConfigurationSource factory]
        
    CC[CorsConfiguration]
end

O2RSC[Oauth2ResourceServerConfigurer]
JC[jwtConfigurer </br>with auto configuration]

SFCF --> |Replaces default|SecurityFilterChain
.cors --> |replaces with CorsFilter</br>connected to custom CCS|CF
CF --> |uses|CCCS
CCS --> |registers as a bean|CCCS
CC --> |registers with|CCCS
.o2 --> |customizes|O2RSC
O2RSC --> |customizes|JC
JWP --> |Uses|JD
JC --> |sets up|JWP
JC --> |sets up|JD
JWP --> |registers with|AM
O2RSC --> |wires|BTAF
BTAF --> |wires|AM
```
---

## Backend authentication:

```mermaid
sequenceDiagram
  participant Http as Http Request
  box grey SecurityFilterChain<br/><br/>A filter can act both on the way in and the way out. These three only act on the way in.<br/><br/>Not all filters are shown.
  participant CorsF as CorsFilter
  participant FSI as FilterSecurityInterceptor
  participant BTAF as BearerTokenAuthenticationFilter
  end
  participant C as Controller
  
  Http->>CorsF: Preflight Request<br/> Could be cached or unnecessary
  Note over Http,CorsF: OPTIONS /api/keys<br/>Origin: http://localhost:6565<br/>Access-Control-Request-Method: GET<br/>Access-Control-Request-Headers: Authorization
  CorsF->>Http: Preflight Response
  Note over CorsF,Http: Status: 200 (OK)<br/>Access-Control-Allow-Origin: http://localhost:6565<br/>Access-Control-Allow-Methods: GET<br/>Access-Control-Allow-Headers: *<br/>Access-Control-Max-Age: 86400
  Http->>FSI: Send Actual Request
  Note over Http,FSI: GET /api/keys<br/>Authorization: Bearer (JWT access token)
  Note over FSI: Path Exclusion Check for /api/keys
  FSI->>BTAF: 
  Note over BTAF: Authentication Check (JWT)<br/>NimbusJwtDecoder automatically:<br/>Fetches public key from JWKS endpoint and verifies JWT signature.<br/>Checks JWT's exp claim to ensure it's not expired.<br/>If JWT has nbf claim, ensures current time is after it.<br/>Validates JWT's iss claim matches expected issuer (Auth0)<br/>Check aud is https://hello-world.example.com
  BTAF->>C: Hits endpoint
  Note over C: Calls service method<br/>that returns keys from DB
  C->>Http: Build response
  Note over C, Http: Status: 200 (OK)<br/>Body: { name: "Name1", key: "Key1" }
```



### How OPTIONS Requests are handled in the SecurityFilterChain
```mermaid
graph TD

    subgraph CSFC[SecurityFilterChain - CorsFilter responds during OPTIONS, other filters are bypassed unless configured otherwise]
        CF[CorsFilter</br>Checks for Access-Control-Request-Method,</br>determines response and sets headers.</br>On a non-OPTIONS request,</br> would set CORS headers on the way out]
    end
    subgraph CC[CorsConfiguration]
        ALO[Allowed Origins</br>Sets Access-Control-Allow-Origin]
        ALM[Allowed Methods</br>Sets Access-Control-Allow-Methods]
        ALH[Allowed Headers</br>Sets Access-Control-Allow-Headers]
    end
    CCS[CorsConfigurationSource]
    HT[External Servlet Filters]

HT <--> |In: HTTPServletRequest /OPTIONS</br>Out: HTTPServletResponse with CORS headers|CSFC
CF --> |uses|CCS
CCS --> |selects config based on url pattern|CC
```

### Non-OPTIONS Requests in the SecurityFilterChain
```mermaid
graph TD


    subgraph CSFC[Custom SecurityFilterChain]
        BTAF[BearerTokenAuthenticationFilter</br>Activates if Bearer token present</br>Responds 401 if issues with authentication]
    end
    JWP[JwtAuthenticationProvider]
        subgraph JD[JwtDecoder - Spring default unless bean defined]
            D[Default behavior - </br>gets public key from jwks_url, </br>validates signature</br>validates aud, exp, nbf, iss.</br>references issuer URI and audience</br>from application.properties.</br>Can configure custom validators</br>with DelegatingOauth2TokenValidator]
        end
    

    subgraph SCX[SecurityContext]
    AAO[JwtAuthenticationToken</br>Represents authentication state of current user.</br>Used in subsequent filters for authorization checks, etc.]
    end
    AM[AuthenticationManager: ProviderManager]
    AO[Authentication: BearerTokenAuthenticationToken]
    JAC[JwtAuthenticationConverter]
    HT[External Servlet Filters]

HT <--> |In: HTTPServletRequest with Bearer token</br>Out: Validated HTTPServletResponse CORS headers|CSFC
BTAF -.->|Creates by extracting token</br> from HttpServletRequest| AO
AO -.-> |Gets passed to|AM
AM -.-> |Selects based on token authentication type|JWP
JWP -.-> |1. uses|JD
JD -.-> |Creates 'jwt' object with info</br> from claims and headers that gets passed to|JAC
JAC -.-> |Transforms 'jwt' object into|AAO
JWP -.-> |2. Stores</br> JwtAuthenticationToken</br> in|SCX
```
