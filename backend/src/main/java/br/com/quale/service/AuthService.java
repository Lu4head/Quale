package br.com.quale.service;

import br.com.quale.dto.LoginRequestDTO;
import br.com.quale.dto.LoginResponseDTO;
import br.com.quale.utils.JwtUtil;
import br.com.quale.utils.UserDetailsImpl;
import br.com.quale.utils.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    // Dependências
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public LoginResponseDTO login(LoginRequestDTO request) throws BadCredentialsException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(request.getEmail());

            String username = userDetails.getUsername();
            String userType = userDetails.getUserType(); // <-- pega do enum

            String token = jwtUtil.generateToken(username, userType);

            return new LoginResponseDTO(token);

        } catch (Exception e) {
            log.warn("Falha na autenticação para o usuário: {}", request.getEmail());
            throw new BadCredentialsException("Usuário ou senha incorretos");
        }
    }

    public LoginResponseDTO login_admin(LoginRequestDTO request) throws BadCredentialsException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            UserDetailsImpl userDetails =
                    (UserDetailsImpl) userDetailsService.loadUserByUsername(request.getEmail());

            String username = userDetails.getUsername();
            String userType = userDetails.getUserType(); // enum convertido para string

            // 🔐 Permitir apenas ADMIN
            if (!"ADMIN".equalsIgnoreCase(userType)) {
                log.warn("Tentativa de login negada. Usuário não é ADMIN: {}", username);
                throw new BadCredentialsException("Acesso permitido apenas para administradores.");
            }

            String token = jwtUtil.generateToken(username, userType);

            return new LoginResponseDTO(token);

        } catch (BadCredentialsException e) {
            throw e;

        } catch (Exception e) {
            log.warn("Falha na autenticação para o usuário: {}", request.getEmail());
            throw new BadCredentialsException("Usuário ou senha incorretos.");
        }
    }
}