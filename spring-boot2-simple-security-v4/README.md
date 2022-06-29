# core classes

LogoutFilter
AuthenticationProvider
DefaultLoginPageGeneratingFilter
DefaultLogoutPageGeneratingFilter
SimpleUrlAuthenticationSuccessHandler
SavedRequestAwareAuthenticationSuccessHandler
AuthenticationSuccessHandler
DefaultLoginPageGeneratingFilter
DefaultLogoutPageGeneratingFilter
UsernamePasswordAuthenticationFilter
 
# request mapping to "POST /login" config

When spring MVC config request mapping to `POST /login`, should set `continueChainBeforeSuccessfulAuthentication = true`

```
usernamePasswordAuthenticationFilter.setContinueChainBeforeSuccessfulAuthentication(true);
```

====================================================================================================
