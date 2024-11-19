memberSearchIndex = [{"p":"com.g1.mychess.admin.model","c":"Admin","l":"Admin()","u":"%3Cinit%3E()"},{"p":"com.g1.mychess.admin.controller","c":"AdminController","l":"AdminController(AdminService)","u":"%3Cinit%3E(com.g1.mychess.admin.service.AdminService)"},{"p":"com.g1.mychess.admin.mapper","c":"AdminMapper","l":"AdminMapper()","u":"%3Cinit%3E()"},{"p":"com.g1.mychess.admin.exception","c":"AdminNotFoundException","l":"AdminNotFoundException(String)","u":"%3Cinit%3E(java.lang.String)"},{"p":"com.g1.mychess.admin","c":"AdminServiceApplication","l":"AdminServiceApplication()","u":"%3Cinit%3E()"},{"p":"com.g1.mychess.admin.service.impl","c":"AdminServiceImpl","l":"AdminServiceImpl(AdminRepository, BlacklistRepository, PlayerServiceClient, EmailServiceClient, AuthenticationService)","u":"%3Cinit%3E(com.g1.mychess.admin.repository.AdminRepository,com.g1.mychess.admin.repository.BlacklistRepository,com.g1.mychess.admin.client.PlayerServiceClient,com.g1.mychess.admin.client.EmailServiceClient,com.g1.mychess.admin.service.AuthenticationService)"},{"p":"com.g1.mychess.admin.service.impl","c":"AdminServiceImplTest","l":"AdminServiceImplTest()","u":"%3Cinit%3E()"},{"p":"com.g1.mychess.admin.service","c":"AuthenticationService","l":"AuthenticationService(JwtUtil)","u":"%3Cinit%3E(com.g1.mychess.admin.util.JwtUtil)"},{"p":"com.g1.mychess.admin.service","c":"AdminService","l":"autoWhitelistExpiredBans()"},{"p":"com.g1.mychess.admin.service.impl","c":"AdminServiceImpl","l":"autoWhitelistExpiredBans()"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"Blacklist()","u":"%3Cinit%3E()"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistDTO","l":"BlacklistDTO()","u":"%3Cinit%3E()"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistEmailDTO","l":"BlacklistEmailDTO()","u":"%3Cinit%3E()"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistEmailDTO","l":"BlacklistEmailDTO(String, String, String, Long)","u":"%3Cinit%3E(java.lang.String,java.lang.String,java.lang.String,java.lang.Long)"},{"p":"com.g1.mychess.admin.controller","c":"AdminController","l":"blacklistPlayer(BlacklistDTO, HttpServletRequest)","u":"blacklistPlayer(com.g1.mychess.admin.dto.BlacklistDTO,jakarta.servlet.http.HttpServletRequest)"},{"p":"com.g1.mychess.admin.service","c":"AdminService","l":"blacklistPlayer(BlacklistDTO, HttpServletRequest)","u":"blacklistPlayer(com.g1.mychess.admin.dto.BlacklistDTO,jakarta.servlet.http.HttpServletRequest)"},{"p":"com.g1.mychess.admin.service.impl","c":"AdminServiceImpl","l":"blacklistPlayer(BlacklistDTO, HttpServletRequest)","u":"blacklistPlayer(com.g1.mychess.admin.dto.BlacklistDTO,jakarta.servlet.http.HttpServletRequest)"},{"p":"com.g1.mychess.admin.config","c":"SecurityConfig","l":"corsConfigurationSource()"},{"p":"com.g1.mychess.admin.initializer","c":"DataInitializer","l":"DataInitializer()","u":"%3Cinit%3E()"},{"p":"com.g1.mychess.admin.filter","c":"JwtRequestFilter","l":"doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)","u":"doFilterInternal(jakarta.servlet.http.HttpServletRequest,jakarta.servlet.http.HttpServletResponse,jakarta.servlet.FilterChain)"},{"p":"com.g1.mychess.admin.client","c":"EmailServiceClient","l":"EmailServiceClient(String, WebClient.Builder)","u":"%3Cinit%3E(java.lang.String,org.springframework.web.reactive.function.client.WebClient.Builder)"},{"p":"com.g1.mychess.admin.util","c":"JwtUtil","l":"extractClaim(String, Function<Claims, T>)","u":"extractClaim(java.lang.String,java.util.function.Function)"},{"p":"com.g1.mychess.admin.util","c":"JwtUtil","l":"extractExpiration(String)","u":"extractExpiration(java.lang.String)"},{"p":"com.g1.mychess.admin.service","c":"AuthenticationService","l":"extractJwtToken(HttpServletRequest)","u":"extractJwtToken(jakarta.servlet.http.HttpServletRequest)"},{"p":"com.g1.mychess.admin.util","c":"JwtUtil","l":"extractRoles(String)","u":"extractRoles(java.lang.String)"},{"p":"com.g1.mychess.admin.util","c":"JwtUtil","l":"extractUserId(String)","u":"extractUserId(java.lang.String)"},{"p":"com.g1.mychess.admin.util","c":"JwtUtil","l":"extractUsername(String)","u":"extractUsername(java.lang.String)"},{"p":"com.g1.mychess.admin.service","c":"AdminService","l":"findAdminByUsername(String)","u":"findAdminByUsername(java.lang.String)"},{"p":"com.g1.mychess.admin.service.impl","c":"AdminServiceImpl","l":"findAdminByUsername(String)","u":"findAdminByUsername(java.lang.String)"},{"p":"com.g1.mychess.admin.repository","c":"BlacklistRepository","l":"findAllByIsActiveTrue()"},{"p":"com.g1.mychess.admin.repository","c":"BlacklistRepository","l":"findByPlayerId(Long)","u":"findByPlayerId(java.lang.Long)"},{"p":"com.g1.mychess.admin.repository","c":"AdminRepository","l":"findByUsername(String)","u":"findByUsername(java.lang.String)"},{"p":"com.g1.mychess.admin.util","c":"JwtUtil","l":"generateRefreshToken(UserDetails)","u":"generateRefreshToken(org.springframework.security.core.userdetails.UserDetails)"},{"p":"com.g1.mychess.admin.util","c":"JwtUtil","l":"generateToken(UserDetails, Long)","u":"generateToken(org.springframework.security.core.userdetails.UserDetails,java.lang.Long)"},{"p":"com.g1.mychess.admin.controller","c":"AdminController","l":"getAdminByUsername(String)","u":"getAdminByUsername(java.lang.String)"},{"p":"com.g1.mychess.admin.model","c":"Admin","l":"getAdminId()"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"getAdminId()"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"getAge()"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistDTO","l":"getBanDuration()"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistEmailDTO","l":"getBanDuration()"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"getBanDuration()"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"getBlacklistedAt()"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistDTO","l":"getEmail()"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"getEmail()"},{"p":"com.g1.mychess.admin.dto","c":"UserDTO","l":"getEmail()"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistDTO","l":"getEmail()"},{"p":"com.g1.mychess.admin.model","c":"Admin","l":"getEmail()"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"getGender()"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"getGlickoRating()"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"getId()"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"getId()"},{"p":"com.g1.mychess.admin.dto","c":"UserDTO","l":"getPassword()"},{"p":"com.g1.mychess.admin.model","c":"Admin","l":"getPassword()"},{"p":"com.g1.mychess.admin.client","c":"PlayerServiceClient","l":"getPlayerDetails(Long)","u":"getPlayerDetails(java.lang.Long)"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistDTO","l":"getPlayerId()"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistDTO","l":"getPlayerId()"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"getPlayerId()"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"getRatingDeviation()"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistDTO","l":"getReason()"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistEmailDTO","l":"getReason()"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistDTO","l":"getReason()"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistEmailDTO","l":"getReason()"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"getReason()"},{"p":"com.g1.mychess.admin.dto","c":"UserDTO","l":"getRole()"},{"p":"com.g1.mychess.admin.model","c":"Admin","l":"getRole()"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistEmailDTO","l":"getTo()"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistEmailDTO","l":"getTo()"},{"p":"com.g1.mychess.admin.dto","c":"UserDTO","l":"getUserId()"},{"p":"com.g1.mychess.admin.service","c":"AuthenticationService","l":"getUserIdFromRequest(HttpServletRequest)","u":"getUserIdFromRequest(jakarta.servlet.http.HttpServletRequest)"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistDTO","l":"getUsername()"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistEmailDTO","l":"getUsername()"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"getUsername()"},{"p":"com.g1.mychess.admin.dto","c":"UserDTO","l":"getUsername()"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistDTO","l":"getUsername()"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistEmailDTO","l":"getUsername()"},{"p":"com.g1.mychess.admin.model","c":"Admin","l":"getUsername()"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"getVolatility()"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"getWhitelistedAt()"},{"p":"com.g1.mychess.admin.global","c":"GlobalExceptionHandler","l":"GlobalExceptionHandler()","u":"%3Cinit%3E()"},{"p":"com.g1.mychess.admin.global","c":"GlobalExceptionHandler","l":"handleAdminNotFoundException(AdminNotFoundException)","u":"handleAdminNotFoundException(com.g1.mychess.admin.exception.AdminNotFoundException)"},{"p":"com.g1.mychess.admin.global","c":"GlobalExceptionHandler","l":"handleIllegalArgumentException(IllegalArgumentException)","u":"handleIllegalArgumentException(java.lang.IllegalArgumentException)"},{"p":"com.g1.mychess.admin.global","c":"GlobalExceptionHandler","l":"handleInvalidBlacklistOperationException(InvalidBlacklistOperationException)","u":"handleInvalidBlacklistOperationException(com.g1.mychess.admin.exception.InvalidBlacklistOperationException)"},{"p":"com.g1.mychess.admin.controller","c":"AdminController","l":"healthCheck()"},{"p":"com.g1.mychess.admin.exception","c":"InvalidBlacklistOperationException","l":"InvalidBlacklistOperationException(String)","u":"%3Cinit%3E(java.lang.String)"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"isActive()"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"isBlacklisted()"},{"p":"com.g1.mychess.admin.filter","c":"JwtRequestFilter","l":"JwtRequestFilter(JwtUtil)","u":"%3Cinit%3E(com.g1.mychess.admin.util.JwtUtil)"},{"p":"com.g1.mychess.admin.util","c":"JwtUtil","l":"JwtUtil()","u":"%3Cinit%3E()"},{"p":"com.g1.mychess.admin","c":"AdminServiceApplication","l":"main(String[])","u":"main(java.lang.String[])"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"PlayerDTO(Long, boolean, String, String, int, String, double, double, double)","u":"%3Cinit%3E(java.lang.Long,boolean,java.lang.String,java.lang.String,int,java.lang.String,double,double,double)"},{"p":"com.g1.mychess.admin.client","c":"PlayerServiceClient","l":"PlayerServiceClient(String, WebClient.Builder)","u":"%3Cinit%3E(java.lang.String,org.springframework.web.reactive.function.client.WebClient.Builder)"},{"p":"com.g1.mychess.admin.initializer","c":"DataInitializer","l":"run(String...)","u":"run(java.lang.String...)"},{"p":"com.g1.mychess.admin.config","c":"SecurityConfig","l":"SecurityConfig(JwtRequestFilter)","u":"%3Cinit%3E(com.g1.mychess.admin.filter.JwtRequestFilter)"},{"p":"com.g1.mychess.admin.config","c":"SecurityConfig","l":"securityFilterChain(HttpSecurity)","u":"securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity)"},{"p":"com.g1.mychess.admin.client","c":"EmailServiceClient","l":"sendBlacklistNotificationEmail(BlacklistEmailDTO)","u":"sendBlacklistNotificationEmail(com.g1.mychess.admin.dto.BlacklistEmailDTO)"},{"p":"com.g1.mychess.admin.client","c":"EmailServiceClient","l":"sendWhitelistNotificationEmail(WhitelistEmailDTO)","u":"sendWhitelistNotificationEmail(com.g1.mychess.admin.dto.WhitelistEmailDTO)"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"setActive(boolean)"},{"p":"com.g1.mychess.admin.model","c":"Admin","l":"setAdminId(Long)","u":"setAdminId(java.lang.Long)"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"setAdminId(Long)","u":"setAdminId(java.lang.Long)"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"setAge(int)"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistDTO","l":"setBanDuration(Long)","u":"setBanDuration(java.lang.Long)"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistEmailDTO","l":"setBanDuration(Long)","u":"setBanDuration(java.lang.Long)"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"setBanDuration(Long)","u":"setBanDuration(java.lang.Long)"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"setBlacklisted(boolean)"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"setBlacklistedAt(LocalDateTime)","u":"setBlacklistedAt(java.time.LocalDateTime)"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistDTO","l":"setEmail(String)","u":"setEmail(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"setEmail(String)","u":"setEmail(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"UserDTO","l":"setEmail(String)","u":"setEmail(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistDTO","l":"setEmail(String)","u":"setEmail(java.lang.String)"},{"p":"com.g1.mychess.admin.model","c":"Admin","l":"setEmail(String)","u":"setEmail(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"setGender(String)","u":"setGender(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"setGlickoRating(double)"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"setId(Long)","u":"setId(java.lang.Long)"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"setId(Long)","u":"setId(java.lang.Long)"},{"p":"com.g1.mychess.admin.dto","c":"UserDTO","l":"setPassword(String)","u":"setPassword(java.lang.String)"},{"p":"com.g1.mychess.admin.model","c":"Admin","l":"setPassword(String)","u":"setPassword(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistDTO","l":"setPlayerId(Long)","u":"setPlayerId(java.lang.Long)"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistDTO","l":"setPlayerId(Long)","u":"setPlayerId(java.lang.Long)"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"setPlayerId(Long)","u":"setPlayerId(java.lang.Long)"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"setRatingDeviation(double)"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistDTO","l":"setReason(String)","u":"setReason(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistEmailDTO","l":"setReason(String)","u":"setReason(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistDTO","l":"setReason(String)","u":"setReason(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistEmailDTO","l":"setReason(String)","u":"setReason(java.lang.String)"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"setReason(String)","u":"setReason(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"UserDTO","l":"setRole(String)","u":"setRole(java.lang.String)"},{"p":"com.g1.mychess.admin.model","c":"Admin","l":"setRole(String)","u":"setRole(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistEmailDTO","l":"setTo(String)","u":"setTo(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistEmailDTO","l":"setTo(String)","u":"setTo(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"UserDTO","l":"setUserId(Long)","u":"setUserId(java.lang.Long)"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistDTO","l":"setUsername(String)","u":"setUsername(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"BlacklistEmailDTO","l":"setUsername(String)","u":"setUsername(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"setUsername(String)","u":"setUsername(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"UserDTO","l":"setUsername(String)","u":"setUsername(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistDTO","l":"setUsername(String)","u":"setUsername(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistEmailDTO","l":"setUsername(String)","u":"setUsername(java.lang.String)"},{"p":"com.g1.mychess.admin.model","c":"Admin","l":"setUsername(String)","u":"setUsername(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"PlayerDTO","l":"setVolatility(double)"},{"p":"com.g1.mychess.admin.model","c":"Blacklist","l":"setWhitelistedAt(LocalDateTime)","u":"setWhitelistedAt(java.time.LocalDateTime)"},{"p":"com.g1.mychess.admin.mapper","c":"AdminMapper","l":"toUserDTO(Admin)","u":"toUserDTO(com.g1.mychess.admin.model.Admin)"},{"p":"com.g1.mychess.admin.exception","c":"UnauthorizedActionException","l":"UnauthorizedActionException(String)","u":"%3Cinit%3E(java.lang.String)"},{"p":"com.g1.mychess.admin.client","c":"PlayerServiceClient","l":"updatePlayerBlacklistStatus(Long)","u":"updatePlayerBlacklistStatus(java.lang.Long)"},{"p":"com.g1.mychess.admin.client","c":"PlayerServiceClient","l":"updatePlayerWhitelistStatus(Long)","u":"updatePlayerWhitelistStatus(java.lang.Long)"},{"p":"com.g1.mychess.admin.dto","c":"UserDTO","l":"UserDTO()","u":"%3Cinit%3E()"},{"p":"com.g1.mychess.admin.dto","c":"UserDTO","l":"UserDTO(Long, String, String, String, String)","u":"%3Cinit%3E(java.lang.Long,java.lang.String,java.lang.String,java.lang.String,java.lang.String)"},{"p":"com.g1.mychess.admin.util","c":"JwtUtil","l":"validateToken(String)","u":"validateToken(java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistDTO","l":"WhitelistDTO(Long, String, String, String)","u":"%3Cinit%3E(java.lang.Long,java.lang.String,java.lang.String,java.lang.String)"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistEmailDTO","l":"WhitelistEmailDTO()","u":"%3Cinit%3E()"},{"p":"com.g1.mychess.admin.dto","c":"WhitelistEmailDTO","l":"WhitelistEmailDTO(String, String, String)","u":"%3Cinit%3E(java.lang.String,java.lang.String,java.lang.String)"},{"p":"com.g1.mychess.admin.controller","c":"AdminController","l":"whitelistPlayer(WhitelistDTO, HttpServletRequest)","u":"whitelistPlayer(com.g1.mychess.admin.dto.WhitelistDTO,jakarta.servlet.http.HttpServletRequest)"},{"p":"com.g1.mychess.admin.service","c":"AdminService","l":"whitelistPlayer(WhitelistDTO, HttpServletRequest)","u":"whitelistPlayer(com.g1.mychess.admin.dto.WhitelistDTO,jakarta.servlet.http.HttpServletRequest)"},{"p":"com.g1.mychess.admin.service.impl","c":"AdminServiceImpl","l":"whitelistPlayer(WhitelistDTO, HttpServletRequest)","u":"whitelistPlayer(com.g1.mychess.admin.dto.WhitelistDTO,jakarta.servlet.http.HttpServletRequest)"}];updateSearchResults();