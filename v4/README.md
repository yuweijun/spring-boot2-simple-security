# core classes

AnyRequestMatcher
CompositeLogoutHandler
LogoutFilter
LogoutHandler
LogoutSuccessHandler
SecurityContextLogoutHandler
SimpleUrlLogoutSuccessHandler
DefaultLoginPageGeneratingFilter
DefaultLogoutPageGeneratingFilter
SimpleUrlAuthenticationSuccessHandler
SavedRequestAwareAuthenticationSuccessHandler
AuthenticationSuccessHandler
DefaultLoginPageGeneratingFilter
DefaultLogoutPageGeneratingFilter
UsernamePasswordAuthenticationFilter
HttpSessionSecurityContextRepository
SecurityContextRepository
AuthenticationProvider
DaoAuthenticationProvider
 
# request mapping to "POST /login" config

When spring MVC config request mapping to `POST /login`, should set `continueChainBeforeSuccessfulAuthentication = true`

```
usernamePasswordAuthenticationFilter.setContinueChainBeforeSuccessfulAuthentication(true);
```

====================================================================================================
