package today.vfu.hospitals;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * The security of the program. Forces users to log in if they use Admin
     * or Superadmin.
     * Focused on authentication.
     * Authorization is handled elsewhere.
     */

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable().antMatcher("/**").authorizeRequests()
                .antMatchers("/avaliableHospitals", "/User/index.html",
                        "/usersName").permitAll()
                .antMatchers("/*", "/Admin/*", "/Superadmin/*")
                .authenticated()
                .and()
                .oauth2Login().permitAll()
                .and().
                logout()
                .clearAuthentication(true)
                .logoutSuccessUrl("/User/index.html");
    }
}
