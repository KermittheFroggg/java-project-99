package hexlet.code.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import hexlet.code.component.RsaKeyProperties;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class EncodersConfig {

    @Autowired
    private RsaKeyProperties rsaKeys;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtEncoder jwtEncoder(RsaKeyProperties rsaKeyProperties) {
        JWK jwk = new RSAKey.Builder(rsaKeyProperties.getPublicKey())
                .privateKey(rsaKeyProperties.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    JwtDecoder jwtDecoder(RsaKeyProperties rsaKeyProperties) {
        return NimbusJwtDecoder.withPublicKey(rsaKeyProperties.getPublicKey()).build();
    }
}
