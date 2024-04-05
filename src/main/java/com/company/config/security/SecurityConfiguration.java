package com.company.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.company.config.exception.AuthExceptionHandler;
import com.company.model.entity.Account.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	private JWTAuthorizationFilter jwtAuthorizationFilter;
	
	@Autowired
	private AuthExceptionHandler authExceptionHandler;
	
	@Bean
	public AuthenticationManager authenticationManager (AuthenticationConfiguration authConfig) throws Exception{
		return authConfig.getAuthenticationManager();
	}
	
	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain configureSecurity(HttpSecurity http) throws Exception {
		http
			.cors(withDefaults())
			.csrf((csrf) -> csrf.disable())
			.authorizeHttpRequests((requests) -> requests
					// check exists
					.requestMatchers(RegexRequestMatcher.regexMatcher(".+/exists.+")).anonymous()
					
					// authen
					.requestMatchers("/api/v1/auth/password/change").authenticated()
					.requestMatchers("/api/v1/auth/**").anonymous()
					
				// department
					//get department by id
					.requestMatchers(HttpMethod.GET, "/api/v1/departments/{id}/**")
						.hasAnyAuthority(Role.ADMIN.toString(), Role.MANAGER.toString())
					
					//get all list department	
					.requestMatchers(HttpMethod.GET, "/api/v1/departments/**")
						.hasAnyAuthority(Role.ADMIN.toString())
//						.anonymous()
		
					// group
					.requestMatchers("/api/v1/groups/**")
						.hasAnyAuthority(Role.ADMIN.toString(), Role.MANAGER.toString())
					
					// account	
					.requestMatchers("/api/v1/accounts/**")
						.hasAnyAuthority(Role.ADMIN.toString())
						
					.anyRequest().authenticated())
			
			.httpBasic(withDefaults())
			.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling()
			.authenticationEntryPoint(authExceptionHandler)
            .accessDeniedHandler(authExceptionHandler);
		
		return http.build();	
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.applyPermitDefaultValues();
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	// ma hoa theo BCrypt (base 64)
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
